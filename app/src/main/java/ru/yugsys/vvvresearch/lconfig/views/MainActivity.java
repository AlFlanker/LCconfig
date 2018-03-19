package ru.yugsys.vvvresearch.lconfig.views;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcV;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import ru.yugsys.vvvresearch.lconfig.App;
import ru.yugsys.vvvresearch.lconfig.R;
import ru.yugsys.vvvresearch.lconfig.Services.GPSTracker;
import ru.yugsys.vvvresearch.lconfig.Services.Helper;
import ru.yugsys.vvvresearch.lconfig.Services.NFCCommand;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DataDevice;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.Device;
import ru.yugsys.vvvresearch.lconfig.model.Interfaces.Model;
import ru.yugsys.vvvresearch.lconfig.model.Interfaces.ModelListener;
import ru.yugsys.vvvresearch.lconfig.presenters.MainPresentable;
import ru.yugsys.vvvresearch.lconfig.presenters.MainPresenter;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static ru.yugsys.vvvresearch.lconfig.Services.Helper.decodeByteList;

public class MainActivity extends AppCompatActivity implements MainViewable, View.OnClickListener,ModelListener.OnNFCConnected {


    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;
    private Device currentDevice;
    private DataDevice currentDataDevice;
    private byte[] systemInfo;
    byte[] readMultipleBlockAnswer = null;
    byte[] numberOfBlockToRead = null;
    byte[] addressStart;
    int cpt;



    private MainContentAdapter adapter;
    private RecyclerView recyclerView;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private MainPresentable mainPresenter;

    @Override
    protected void onPause() {
        super.onPause();
        mAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d("NFC","NFC");
         currentDataDevice = new DataDevice();
        super.onNewIntent(intent);
        byte[] arr;
        Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        currentDataDevice.setCurrentTag(tagFromIntent);
        Log.d("NFC","start AddEditActivity");
        systemInfo=NFCCommand.SendGetSystemInfoCommandCustom(tagFromIntent,currentDataDevice);
        Log.d("NFC",Arrays.toString(systemInfo));
        new StartReadTask().execute(new Void[0]);
        Log.d("NFC","In Main");


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        if(mAdapter.isEnabled()){
            mPendingIntent = PendingIntent.getActivity(this, 0,new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
            IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
            mFilters = new IntentFilter[] {ndef,};
            mTechLists = new String[][] { new String[] { android.nfc.tech.NfcV.class.getName() } };
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        mPendingIntent = PendingIntent.getActivity(this, 0,new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists);
        getPremissionGPS();
        mainPresenter.fireUpdateDataForView();
    }

    private void getPremissionGPS() {
        GPSTracker gpsTracker = GPSTracker.instance();
        gpsTracker.setContext(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_CODE);
        }
        Log.d("GPS", "Activity gps start");
        gpsTracker.OnStartGPS();
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
            startActivity(addEditIntent);
        }
    }


    @Override
    public void OnNFCConnected(Device dev) {
        Log.d("NFC", dev.type);
    }


    /**
     * класс чтения данных NFC
     * возвращает данные через @see EventManager#EventManager()
     */
    private class StartReadTask extends AsyncTask<Void, Void, Void> {


        private StartReadTask() {

        }

        protected void onPreExecute() {

            int cpt = 0;
            if ((currentDataDevice = Helper.DecodeGetSystemInfoResponse(systemInfo, currentDataDevice)) != null) {
                addressStart = new byte[8];
                Arrays.fill(addressStart,(byte)0x00);
                numberOfBlockToRead = new byte[]{(byte)0x00,(byte)0x20}; // 32 блока на 4 байта
            }
        }

        protected Void doInBackground(Void... params) {
            if ((currentDataDevice = Helper.DecodeGetSystemInfoResponse(systemInfo, currentDataDevice)) != null) {
                if (currentDataDevice.isMultipleReadSupported() && ByteBuffer.wrap(numberOfBlockToRead).getShort() > 1) {
                    if (Helper.Convert2bytesHexaFormatToInt(numberOfBlockToRead) < 32) {
                        while ((readMultipleBlockAnswer == null || readMultipleBlockAnswer[0] == 1) && cpt <= 10) {
                            readMultipleBlockAnswer = NFCCommand.SendReadMultipleBlockCommandCustom(currentDataDevice.getCurrentTag(), addressStart, numberOfBlockToRead[1], currentDataDevice);
                            cpt++;
                        }

                        cpt = 0;
                    } else {
                        while ((readMultipleBlockAnswer == null || readMultipleBlockAnswer[0] == 1) && cpt <= 10) {
                            readMultipleBlockAnswer = NFCCommand.SendReadMultipleBlockCommandCustom2(currentDataDevice.getCurrentTag(), addressStart, numberOfBlockToRead, currentDataDevice);
                            cpt++;
                        }

                        cpt =0;
                    }

                }
                else {
                    while ((readMultipleBlockAnswer == null || readMultipleBlockAnswer[0] == 1) && cpt <= 10) {
                        readMultipleBlockAnswer = NFCCommand.Send_several_ReadSingleBlockCommands_NbBlocks(currentDataDevice.getCurrentTag(), addressStart, numberOfBlockToRead, currentDataDevice);
                        cpt++;
                    }

                    cpt = 0;
                }
            }

            return null;
        }

        protected void onPostExecute(Void unused) {

            if ((currentDataDevice = Helper.DecodeGetSystemInfoResponse(systemInfo, currentDataDevice)) != null) {
                StringBuilder stringBuilder = new StringBuilder();
                for(Byte b: readMultipleBlockAnswer)
                    stringBuilder.append(String.format("0x%02x",b)+"; ");
                Log.d("NFC",stringBuilder.toString());
                Log.d("NFC",String.valueOf(readMultipleBlockAnswer.length));
                try {
                    decode(readMultipleBlockAnswer); // to this.currentDevice
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public  void decode(byte[] raw) throws IllegalAccessException, IOException, NoSuchFieldException {

        currentDevice =Helper.decodeByteArrayToDevice(raw);
        ((App)getApplication()).getModel().setCurrentDevice(currentDevice);
        byte[] b=Helper.Object2ByteArray(currentDevice);
        StringBuilder sb = new StringBuilder();
        for(Byte a:b){
            sb.append(String.format("0x%02x",a)+"; ");
        }
        Log.d("fileds",sb.toString());
        Intent addActivity = new Intent(this,AddEditActivity.class);
        startActivity(addActivity);
    }


}
