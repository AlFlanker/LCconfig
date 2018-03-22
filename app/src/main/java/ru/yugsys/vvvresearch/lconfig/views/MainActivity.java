package ru.yugsys.vvvresearch.lconfig.views;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
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
import ru.yugsys.vvvresearch.lconfig.Services.CRC16;
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
        mAdapter.disableForegroundDispatch(this);
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
            if (!this.DecodeGetSystemInfoResponse(systemInfo, currentDataDevice)) {
                return;
            }
            task = new StartReadTask();
            task.execute(new Void[0]);
        }

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
            addEditIntent.putExtra("generateDevice", Boolean.TRUE);
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
        final ProgressDialog dialog;

        private StartReadTask() {
            this.dialog = new ProgressDialog(MainActivity.this);
        }

        protected void onPreExecute() {
            this.dialog.setMessage("Чтение данных");
            this.dialog.show();
            int cpt = 0;
            currentDataDevice = Helper.DecodeGetSystemInfoResponse(systemInfo, currentDataDevice);
            if (currentDataDevice != null) {
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
                try {
                    decode(readMultipleBlockAnswer); // to this.currentDevice
                    if (this.dialog.isShowing()) {
                        this.dialog.dismiss();
                    }
                    readMultipleBlockAnswer = null;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
            task = null;
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
            currentDevice = Helper.decodeByteArrayToDevice(crc);
            ((App) getApplication()).getModel().setCurrentDevice(currentDevice);
            byte[] b = Helper.Object2ByteArray(currentDevice);
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

    public static boolean DecodeGetSystemInfoResponse(byte[] GetSystemInfoResponse, DataDevice dataDevice) {
        DataDevice ma = dataDevice;
        if (GetSystemInfoResponse[0] == 0 && GetSystemInfoResponse.length >= 12) {
            String uidToString = "";
            byte[] uid = new byte[8];

            for (int i = 1; i <= 8; ++i) {
                uid[i - 1] = GetSystemInfoResponse[10 - i];
                uidToString = uidToString + Helper.ConvertHexByteToString(uid[i - 1]);
            }

            ma.setUid(uidToString);
            if (uid[0] == -32) {
                ma.setTechno("ISO 15693");
            } else if (uid[0] == -48) {
                ma.setTechno("ISO 14443");
            } else {
                ma.setTechno("Unknown techno");
            }

            if (uid[1] == 2) {
                ma.setManufacturer("STMicroelectronics");
            } else if (uid[1] == 4) {
                ma.setManufacturer("NXP");
            } else if (uid[1] == 7) {
                ma.setManufacturer("Texas Instruments");
            } else if (uid[1] == 1) {
                ma.setManufacturer("Motorola");
            } else if (uid[1] == 3) {
                ma.setManufacturer("Hitachi");
            } else if (uid[1] == 4) {
                ma.setManufacturer("NXP");
            } else if (uid[1] == 5) {
                ma.setManufacturer("Infineon");
            } else if (uid[1] == 6) {
                ma.setManufacturer("Cylinc");
            } else if (uid[1] == 7) {
                ma.setManufacturer("Texas Instruments");
            } else if (uid[1] == 8) {
                ma.setManufacturer("Fujitsu");
            } else if (uid[1] == 9) {
                ma.setManufacturer("Matsushita");
            } else if (uid[1] == 10) {
                ma.setManufacturer("NEC");
            } else if (uid[1] == 11) {
                ma.setManufacturer("Oki");
            } else if (uid[1] == 12) {
                ma.setManufacturer("Toshiba");
            } else if (uid[1] == 13) {
                ma.setManufacturer("Mitsubishi");
            } else if (uid[1] == 14) {
                ma.setManufacturer("Samsung");
            } else if (uid[1] == 15) {
                ma.setManufacturer("Hyundai");
            } else if (uid[1] == 16) {
                ma.setManufacturer("LG");
            } else {
                ma.setManufacturer("Unknown manufacturer");
            }

            if (uid[1] == 2) {
                if (uid[2] >= 4 && uid[2] <= 7) {
                    ma.setProductName("LRI512");
                    ma.setMultipleReadSupported(false);
                    ma.setMemoryExceed2048bytesSize(false);
                } else if (uid[2] >= 20 && uid[2] <= 23) {
                    ma.setProductName("LRI64");
                    ma.setMultipleReadSupported(false);
                    ma.setMemoryExceed2048bytesSize(false);
                } else if (uid[2] >= 32 && uid[2] <= 35) {
                    ma.setProductName("LRI2K");
                    ma.setMultipleReadSupported(true);
                    ma.setMemoryExceed2048bytesSize(false);
                } else if (uid[2] >= 40 && uid[2] <= 43) {
                    ma.setProductName("LRIS2K");
                    ma.setMultipleReadSupported(false);
                    ma.setMemoryExceed2048bytesSize(false);
                } else if (uid[2] >= 44 && uid[2] <= 47) {
                    ma.setProductName("M24LR64");
                    ma.setMultipleReadSupported(true);
                    ma.setMemoryExceed2048bytesSize(true);
                } else if (uid[2] >= 64 && uid[2] <= 67) {
                    ma.setProductName("LRI1K");
                    ma.setMultipleReadSupported(true);
                    ma.setMemoryExceed2048bytesSize(false);
                } else if (uid[2] >= 68 && uid[2] <= 71) {
                    ma.setProductName("LRIS64K");
                    ma.setMultipleReadSupported(true);
                    ma.setMemoryExceed2048bytesSize(true);
                } else if (uid[2] >= 72 && uid[2] <= 75) {
                    ma.setProductName("M24LR01E");
                    ma.setMultipleReadSupported(true);
                    ma.setMemoryExceed2048bytesSize(false);
                } else if (uid[2] >= 76 && uid[2] <= 79) {
                    ma.setProductName("M24LR16E");
                    ma.setMultipleReadSupported(true);
                    ma.setMemoryExceed2048bytesSize(true);
                    if (!ma.isBasedOnTwoBytesAddress()) {
                        return false;
                    }
                } else if (uid[2] >= 80 && uid[2] <= 83) {
                    ma.setProductName("M24LR02E");
                    ma.setMultipleReadSupported(true);
                    ma.setMemoryExceed2048bytesSize(false);
                } else if (uid[2] >= 84 && uid[2] <= 87) {
                    ma.setProductName("M24LR32E");
                    ma.setMultipleReadSupported(true);
                    ma.setMemoryExceed2048bytesSize(true);
                    if (!ma.isBasedOnTwoBytesAddress()) {
                        return false;
                    }
                } else if (uid[2] >= 88 && uid[2] <= 91) {
                    ma.setProductName("M24LR04E");
                    ma.setMultipleReadSupported(true);
                    ma.setMemoryExceed2048bytesSize(true);
                } else if (uid[2] >= 92 && uid[2] <= 95) {
                    ma.setProductName("M24LR64E");
                    ma.setMultipleReadSupported(true);
                    ma.setMemoryExceed2048bytesSize(true);
                    if (!ma.isBasedOnTwoBytesAddress()) {
                        return false;
                    }
                } else if (uid[2] >= 96 && uid[2] <= 99) {
                    ma.setProductName("M24LR08E");
                    ma.setMultipleReadSupported(true);
                    ma.setMemoryExceed2048bytesSize(true);
                } else if (uid[2] >= 100 && uid[2] <= 103) {
                    ma.setProductName("M24LR128E");
                    ma.setMultipleReadSupported(true);
                    ma.setMemoryExceed2048bytesSize(true);
                    if (!ma.isBasedOnTwoBytesAddress()) {
                        return false;
                    }
                } else if (uid[2] >= 108 && uid[2] <= 111) {
                    ma.setProductName("M24LR256E");
                    ma.setMultipleReadSupported(true);
                    ma.setMemoryExceed2048bytesSize(true);
                    if (!ma.isBasedOnTwoBytesAddress()) {
                        return false;
                    }
                } else if (uid[2] >= -8 && uid[2] <= -5) {
                    ma.setProductName("detected product");
                    ma.setBasedOnTwoBytesAddress(true);
                    ma.setMultipleReadSupported(true);
                    ma.setMemoryExceed2048bytesSize(true);
                } else {
                    ma.setProductName("Unknown product");
                    ma.setBasedOnTwoBytesAddress(false);
                    ma.setMultipleReadSupported(false);
                    ma.setMemoryExceed2048bytesSize(false);
                }

                ma.setDsfid(Helper.ConvertHexByteToString(GetSystemInfoResponse[10]));
                ma.setAfi(Helper.ConvertHexByteToString(GetSystemInfoResponse[11]));
                if (ma.isBasedOnTwoBytesAddress()) {
                    String temp = new String();
                    temp = temp + Helper.ConvertHexByteToString(GetSystemInfoResponse[13]);
                    temp = temp + Helper.ConvertHexByteToString(GetSystemInfoResponse[12]);
                    ma.setMemorySize(temp);
                } else {
                    ma.setMemorySize(Helper.ConvertHexByteToString(GetSystemInfoResponse[12]));
                }

                if (ma.isBasedOnTwoBytesAddress()) {
                    ma.setBlockSize(Helper.ConvertHexByteToString(GetSystemInfoResponse[14]));
                } else {
                    ma.setBlockSize(Helper.ConvertHexByteToString(GetSystemInfoResponse[13]));
                }

                if (ma.isBasedOnTwoBytesAddress()) {
                    ma.setIcReference(Helper.ConvertHexByteToString(GetSystemInfoResponse[15]));
                } else {
                    ma.setIcReference(Helper.ConvertHexByteToString(GetSystemInfoResponse[14]));
                }
            } else {
                ma.setProductName("Unknown product");
                ma.setBasedOnTwoBytesAddress(false);
                ma.setMultipleReadSupported(false);
                ma.setMemoryExceed2048bytesSize(false);
                ma.setAfi(Helper.ConvertHexByteToString(GetSystemInfoResponse[11]));
                ma.setDsfid(Helper.ConvertHexByteToString(GetSystemInfoResponse[10]));
                ma.setMemorySize(Helper.ConvertHexByteToString(GetSystemInfoResponse[12]));
                ma.setBlockSize(Helper.ConvertHexByteToString(GetSystemInfoResponse[13]));
                ma.setIcReference(Helper.ConvertHexByteToString(GetSystemInfoResponse[14]));
            }

            return true;
        } else if (ma.getTechno() == "ISO 15693") {
            ma.setProductName("Unknown product");
            ma.setBasedOnTwoBytesAddress(false);
            ma.setMultipleReadSupported(false);
            ma.setMemoryExceed2048bytesSize(false);
            ma.setAfi("00 ");
            ma.setDsfid("00 ");
            ma.setMemorySize("3F ");
            ma.setBlockSize("03 ");
            ma.setIcReference("00 ");
            return true;
        } else {
            return false;
        }
    }
}
