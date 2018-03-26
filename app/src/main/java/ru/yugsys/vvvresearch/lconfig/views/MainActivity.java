package ru.yugsys.vvvresearch.lconfig.views;

import android.Manifest;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;
import ru.yugsys.vvvresearch.lconfig.App;
import ru.yugsys.vvvresearch.lconfig.R;
import ru.yugsys.vvvresearch.lconfig.Services.CRC16;
import ru.yugsys.vvvresearch.lconfig.Services.GPSTracker;
import ru.yugsys.vvvresearch.lconfig.Services.Util;
import ru.yugsys.vvvresearch.lconfig.Services.NFCCommand;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DataDevice;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.Device;
import ru.yugsys.vvvresearch.lconfig.model.Interfaces.ModelListener;
import ru.yugsys.vvvresearch.lconfig.presenters.MainPresentable;
import ru.yugsys.vvvresearch.lconfig.presenters.MainPresenter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainViewable, View.OnClickListener, ModelListener.OnNFCConnected {


    public static final String ADD_NEW_DEVICE_MODE = "AddNewDeviceMode";
    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;
    private Device currentDevice;
    private DataDevice currentDataDevice;
    private byte[] systemInfo;
    private byte[] readMultipleBlockAnswer = null;
    private byte[] numberOfBlockToRead = null;
    private byte[] addressStart;
    private StartReadTask task;
    int cpt;


    private MainContentAdapter adapter;
    private RecyclerView recyclerView;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private MainPresentable mainPresenter;

    @Override
    protected void onPause() {
        super.onPause();
        if (mAdapter != null) {
            mAdapter.disableForegroundDispatch(this);
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);
        byte[] arr;
        Tag tagFromIntent;
        currentDataDevice = new DataDevice();
        if ("android.nfc.action.TECH_DISCOVERED".equals(intent.getAction())) {
            tagFromIntent = (Tag) intent.getParcelableExtra("android.nfc.extra.TAG");
            currentDataDevice.setCurrentTag(tagFromIntent);
            systemInfo = NFCCommand.SendGetSystemInfoCommandCustom(tagFromIntent, currentDataDevice);
            if (!Util.DecodeSystemInfoResponse(systemInfo, currentDataDevice)) {
                return;
            }
            task = new StartReadTask(this);
            task.execute(new Void[0]);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPremissionGPS();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);
        recyclerView = findViewById(R.id.lc5_recycler_view);
        adapter = new MainContentAdapter(recyclerView.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Connect presenter to main view
        mainPresenter = new MainPresenter(((App) getApplication()).getModel());
        mainPresenter.bind(this);
        mainPresenter.fireUpdateDataForView();
        //getPremissionGPS();
        mAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mAdapter != null && mAdapter.isEnabled()) {
            mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
            IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
            mFilters = new IntentFilter[]{ndef,};
            mTechLists = new String[][]{new String[]{android.nfc.tech.NfcV.class.getName()}};
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        if (mAdapter != null) {
            mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists);
        }
        mainPresenter.fireUpdateDataForView();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            GPSTracker gpsTracker = GPSTracker.instance();
            gpsTracker.setContext(this);
            gpsTracker.OnStartGPS();
        }

    }

    private void getPremissionGPS() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_CODE);
        }
        Log.d("GPS", "Activity gps start");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setContentForView(List<Device> devices) {
        adapter.setDevices(devices);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab) {
            Intent addEditIntent = new Intent(this, AddEditActivity.class);
            addEditIntent.putExtra(ADD_NEW_DEVICE_MODE, true);
            startActivity(addEditIntent);
        }
    }


    @Override
    public void OnNFCConnected(Device dev) {
        Log.d("NFC", dev.type);
    }

    class StartReadTask extends AsyncTask<Void, Void, Void> {
        private MainActivity mainActivity;
        final ProgressDialog dialog;

        StartReadTask(MainActivity mainActivity) {
            this.mainActivity = mainActivity;
            this.dialog = new ProgressDialog(mainActivity);
        }

        protected void onPreExecute() {
            this.dialog.setMessage("Чтение данных");
            this.dialog.show();
            int cpt = 0;
            if ((mainActivity.currentDataDevice = Util.DecodeGetSystemInfoResponse(mainActivity.systemInfo, mainActivity.currentDataDevice)) != null) {
                mainActivity.addressStart = new byte[8];
                Arrays.fill(mainActivity.addressStart, (byte) 0x00);
                mainActivity.numberOfBlockToRead = new byte[]{(byte) 0x00, (byte) 0x20}; // 32 блока на 4 байта
            }
        }

        protected Void doInBackground(Void... params) {
            if ((mainActivity.currentDataDevice = Util.DecodeGetSystemInfoResponse(mainActivity.systemInfo, mainActivity.currentDataDevice)) != null) {
                if (mainActivity.currentDataDevice.isMultipleReadSupported() && ByteBuffer.wrap(mainActivity.numberOfBlockToRead).getShort() > 1) {
                    if (Util.Convert2bytesHexaFormatToInt(mainActivity.numberOfBlockToRead) < 32) {
                        while ((mainActivity.readMultipleBlockAnswer == null || mainActivity.readMultipleBlockAnswer[0] == 1) && mainActivity.cpt <= 10) {
                            mainActivity.readMultipleBlockAnswer = NFCCommand.SendReadMultipleBlockCommandCustom(mainActivity.currentDataDevice.getCurrentTag(), mainActivity.addressStart, mainActivity.numberOfBlockToRead[1], mainActivity.currentDataDevice);
                            mainActivity.cpt++;
                        }

                        mainActivity.cpt = 0;
                    } else {
                        while ((mainActivity.readMultipleBlockAnswer == null || mainActivity.readMultipleBlockAnswer[0] == 1) && mainActivity.cpt <= 10) {
                            mainActivity.readMultipleBlockAnswer = NFCCommand.SendReadMultipleBlockCommandCustom2(mainActivity.currentDataDevice.getCurrentTag(), mainActivity.addressStart, mainActivity.numberOfBlockToRead, mainActivity.currentDataDevice);
                            mainActivity.cpt++;
                        }

                        mainActivity.cpt = 0;
                    }

                }
                else {
                    while ((mainActivity.readMultipleBlockAnswer == null || mainActivity.readMultipleBlockAnswer[0] == 1) && mainActivity.cpt <= 10) {
                        mainActivity.readMultipleBlockAnswer = NFCCommand.Send_several_ReadSingleBlockCommands_NbBlocks(mainActivity.currentDataDevice.getCurrentTag(), mainActivity.addressStart, mainActivity.numberOfBlockToRead, mainActivity.currentDataDevice);
                        mainActivity.cpt++;
                    }

                    mainActivity.cpt = 0;
                }
            }

            return null;
        }

        protected void onPostExecute(Void unused) {

            if ((mainActivity.currentDataDevice = Util.DecodeGetSystemInfoResponse(mainActivity.systemInfo, mainActivity.currentDataDevice)) != null) {
                try {
                    mainActivity.decode(mainActivity.readMultipleBlockAnswer); // to this.currentDevice
                    if (this.dialog.isShowing()) {
                        this.dialog.dismiss();
                    }
                    mainActivity.readMultipleBlockAnswer = null;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
            mainActivity.task = null;
        }
    }


    public  void decode(byte[] raw) throws IllegalAccessException, IOException, NoSuchFieldException {
        CRC16 crc16 = new CRC16();
        StringBuilder sb;
        byte[] crc = new byte[121];
        System.arraycopy(raw, 1, crc, 0, 121);
        int res = crc16.CRC16ArrayGet(0, crc) & 0x0000FFFF;
        res = Integer.reverseBytes(res);
        res >>= 16;
        res &= 0x0000FFFF;
        sb = new StringBuilder();
        for (Byte b : ByteBuffer.allocate(4).putInt(res).array()) {
            sb.append(String.format("%02x ", b));
        }
        Log.d("NFCdata", "crc: " + sb.toString());
        sb = new StringBuilder();
        for (Byte b : crc) {
            sb.append(String.format("%02x ", b));
        }
        Log.d("NFCdata", "crc arr:" + sb.toString());
        sb = new StringBuilder();
        for (Byte b : raw) {
            sb.append(String.format("%02x ", b));
        }
        Log.d("NFCdata", sb.toString());
        if (ByteBuffer.wrap(new byte[]{0x00, 0x00, raw[123], raw[122]}).getInt() == res) {
            currentDevice = Util.decodeByteArrayToDevice(crc);
            ((App) getApplication()).getModel().setCurrentDevice(currentDevice);
            byte[] b = Util.Object2ByteArray(currentDevice);
            sb = new StringBuilder();
            for (Byte a : b) {
                sb.append(String.format("0x%02x", a) + "; ");
            }
            Log.d("fileds", sb.toString());

            Intent addActivity = new Intent(this, AddEditActivity.class);
            addActivity.putExtra(ADD_NEW_DEVICE_MODE, Boolean.FALSE);
            currentDataDevice = null;
            startActivity(addActivity);
        }
    }


}
