package ru.yugsys.vvvresearch.lconfig.views;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.Utils;
import ru.yugsys.vvvresearch.lconfig.App;
import ru.yugsys.vvvresearch.lconfig.R;
import ru.yugsys.vvvresearch.lconfig.Services.CRC16;
import ru.yugsys.vvvresearch.lconfig.Services.GPSTracker;
import ru.yugsys.vvvresearch.lconfig.Services.Helper;
import ru.yugsys.vvvresearch.lconfig.Services.NFCCommand;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DataDevice;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.Device;
import ru.yugsys.vvvresearch.lconfig.presenters.AddEditPresentable;
import ru.yugsys.vvvresearch.lconfig.presenters.AddEditPresenter;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;

import java.nio.ByteBuffer;
import java.util.HashMap;

public class AddEditActivity extends AppCompatActivity implements AddEditViewable, View.OnClickListener {


    private ExpandableLinearLayout expandableLinearLayout;
    private EditText deveuiEdit;
    private EditText appEUIEdit;
    private EditText appKeyEdit;
    private EditText nwkIDEdit;
    private EditText devAdrEdit;
    private EditText nwkSKeyEdit;
    private EditText appSKeyEdit;
    private Switch isOTAASwitch;
    private EditText gpsEditLongitude;
    private Spinner out_typeSpinner;
    private Spinner typeSpinner;
    private Button addEditButton;
    private AddEditPresentable presenter;

    // new  fields
    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;
    private Device currentDevice;
    private View buttonLayout;
    private View triangleButton;
    private EditText gpsEditLatitude;
    private DataDevice currentDev;
    private byte[] systemInfo;
    private byte[] addressStart;
    private HashMap<Integer, byte[]> memory = new HashMap<>();
    private byte[] WriteSingleBlockAnswer = null;
    private int cpt;
    private byte[] valueBlocksWrite;
    private int blocksToWrite = 0;


    @Override
    protected void onResume() {
        super.onResume();
        this.mAdapter.enableForegroundDispatch(this, this.mPendingIntent, this.mFilters, this.mTechLists);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("NFC","AddActivity");

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_edit);

        typeSpinner = findViewById(R.id.lc5_spinner_type);
        out_typeSpinner = findViewById(R.id.lc5_spinner_out_type);
        appEUIEdit = findViewById(R.id.lc5_edit_appEUI);
        appKeyEdit = findViewById(R.id.lc5_edit_appKey);
        nwkIDEdit = findViewById(R.id.lc5_edit_nwkID);
        devAdrEdit = findViewById(R.id.lc5_edit_devAdr);
        nwkSKeyEdit = findViewById(R.id.lc5_edit_nwkSKey);
        appSKeyEdit = findViewById(R.id.lc5_edit_appSKey);
        isOTAASwitch = findViewById(R.id.lc5_switch_isOTAA);
        gpsEditLongitude = findViewById(R.id.lc5_edit_gps_longitude);
        gpsEditLatitude = findViewById(R.id.lc5_edit_gps_latitude);
        deveuiEdit = findViewById(R.id.lc5_edit_deveui);
        addEditButton = findViewById(R.id.action_add_edit);
        addEditButton.setOnClickListener(this);
        buttonLayout = findViewById(R.id.buttonExpand);
        triangleButton = findViewById(R.id.button_triangle_add_edit);
        expandableLinearLayout = findViewById(R.id.expandableLayoutAddEdit);
        expandableLinearLayout.setInterpolator(Utils.createInterpolator(Utils.DECELERATE_INTERPOLATOR));
        expandableLinearLayout.setExpanded(false);
        expandableLinearLayout.setListener(new ExpandableLayoutListenerAdapter() {
            @Override
            public void onPreOpen() {
                UtilAnimators.createRotateAnimator(triangleButton, 0f, 180f).start();
            }

            @Override
            public void onPreClose() {
                UtilAnimators.createRotateAnimator(triangleButton, 180f, 0f).start();
            }
        });
        buttonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expandableLinearLayout.toggle();
            }
        });
        // new code
        mAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mAdapter != null && mAdapter.isEnabled()) {
            mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
            IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
            mFilters = new IntentFilter[]{ndef,};
            mTechLists = new String[][]{new String[]{android.nfc.tech.NfcV.class.getName()}};
        }

        GPSTracker gpsTracker = GPSTracker.instance();
        gpsTracker.setContext(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    100);
        }
        Log.d("GPS", "Activity gps start");
        gpsTracker.OnStartGPS();
        presenter = new AddEditPresenter(((App) getApplication()).getModel());
        presenter.bind(this);
        currentDevice = ((App) getApplication()).getModel().getCurrentDevice();
        if (currentDevice != null) {
            setDeviceFields(currentDevice);
        }


    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d("NFC","newintent");
        super.onNewIntent(intent);
        String action = intent.getAction();
        if ("android.nfc.action.TECH_DISCOVERED".equals(action)) {
            Tag tagFromIntent = (Tag) intent.getParcelableExtra("android.nfc.extra.TAG");
            ((App) getApplication()).getModel().getCurrentDev().setCurrentTag(tagFromIntent);
            currentDev = ((App) getApplication()).getModel().getCurrentDev();
            currentDev.setCurrentTag(tagFromIntent);
            Log.d("NFC", "add new tag");

            currentDevice = fieldToDevice();
            new StartWriteTask().execute(new Void[0]);
        }
    }
    @Override
    protected void onPostResume() {
        super.onPostResume();
        mPendingIntent = PendingIntent.getActivity(this, 0,new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists);
        Log.d("NFC","readNFC");
        Log.d("NFC","post readNFC");

    }

    @Override
    public void setDeviceFields(Device device) {
        setSpinnerValuePosition(device.getType(), typeSpinner);
        setSpinnerValuePosition(device.getOutType(),out_typeSpinner);
        deveuiEdit.setText(device.getEui());
        appEUIEdit.setText(device.getAppeui());
        appKeyEdit.setText(device.getAppkey());
        nwkIDEdit.setText(device.getNwkid());
        devAdrEdit.setText(device.getDevadr());
        nwkSKeyEdit.setText(device.getNwkskey());
        appSKeyEdit.setText(device.getAppskey());
        //TODO: change "false" to device field
        isOTAASwitch.setChecked(false);
        gpsEditLongitude.setText(String.valueOf(device.getLongitude()));
        gpsEditLatitude.setText(String.valueOf(device.getLatitude()));
    }

    @Override
    public void setLocationFields(Location location) {
        gpsEditLongitude.setText(String.valueOf(location.getLongitude()));
        gpsEditLatitude.setText(String.valueOf(location.getLatitude()));
    }

    private void setSpinnerValuePosition(String value, Spinner spinner) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();
        int i = adapter.getPosition(value);
        this.typeSpinner.setSelection(i);
    }

    private Device fieldToDevice() {
        Device device = new Device();
        device.setType(typeSpinner.getSelectedItem().toString());
        device.setEui(deveuiEdit.getText().toString());
        device.setAppeui(appEUIEdit.getText().toString());
        device.setAppkey(appKeyEdit.getText().toString());
        device.setNwkid(nwkIDEdit.getText().toString());
        device.setDevadr(devAdrEdit.getText().toString());
        device.setNwkskey(nwkSKeyEdit.getText().toString());
        device.setAppskey(appSKeyEdit.getText().toString());
        //default constants of the LC5 firmware
//        device.setKI("7321");
//        device.setKV("1004,974,976,993,995,1028,1038,2489,2412,2254,2063,1908,1702,1541");
        //default constants of the LC5 firmware in HEX
        device.setKI("991C");
        device.setKV("EC03CE03D003E103E30304040E04B9096C09CE080F087407A6060506");
        //TODO: Fill isOTAA device field
        device.setIsOTTA(isOTAASwitch.isChecked());
        device.setLatitude(Double.parseDouble(gpsEditLatitude.getText().toString()));
        device.setLongitude(Double.parseDouble(gpsEditLongitude.getText().toString()));
        device.setOutType(out_typeSpinner.getSelectedItem().toString());
        return device;
    }

    @Override
    public void onClick(View view) {
        presenter.fireNewDevice(fieldToDevice());
        currentDevice = fieldToDevice();
        new StartWriteTask().execute(new Void[0]);
        finish();
    }
    //************************************
    //**************************************************************

    private class StartWriteTask extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog;

        private StartWriteTask() {
            this.dialog = new ProgressDialog(AddEditActivity.this);
        }

        protected void onPreExecute() {


            valueBlocksWrite = new byte[123];
            try {
                byte[] raw = Helper.Object2ByteArray(currentDevice);
                StringBuilder sb = new StringBuilder();

                Log.d("write23", sb.toString());
                systemInfo = NFCCommand.SendGetSystemInfoCommandCustom(currentDev.getCurrentTag(), currentDev);
                if (Helper.DecodeGetSystemInfoResponse(systemInfo, currentDev) != null) {
                    addressStart = new byte[]{0x00, 0x00, 0x00, 0x00};
                    System.arraycopy(raw, 0, valueBlocksWrite, 1, raw.length);
                    valueBlocksWrite[0] = 1;
                    CRC16 crc16 = new CRC16();
                    int res = crc16.CRC16ArrayGet(0, raw) & 0x0000FFFF;
                    res = Integer.reverseBytes(res);
                    res >>= 16;
                    byte[] crc = ByteBuffer.allocate(4).putInt(res).array();
                    valueBlocksWrite[121] = crc[2];
                    valueBlocksWrite[122] = crc[3];

                    this.dialog.setMessage("Please, keep your phone close to the tag");
                    this.dialog.show();

                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        protected Void doInBackground(Void... params) {
            cpt = 0;
            byte[] block = new byte[4];
            DataDevice dataDevice = currentDev;
            WriteSingleBlockAnswer = null;
            byte[] dataToWrite;


            if (DecodeGetSystemInfoResponse(systemInfo)) {
                if (valueBlocksWrite.length % 4 != 0) {
                    int l = 4 - valueBlocksWrite.length % 4;
                    dataToWrite = new byte[valueBlocksWrite.length + l];
                    System.arraycopy(valueBlocksWrite, 0, dataToWrite, 0, valueBlocksWrite.length);
                    Arrays.fill(dataToWrite, valueBlocksWrite.length, valueBlocksWrite.length + 1, (byte) 0xFF);
                    blocksToWrite = dataToWrite.length / 4;
                } else {
                    dataToWrite = new byte[valueBlocksWrite.length];
                    System.arraycopy(valueBlocksWrite, 0, dataToWrite, 0, valueBlocksWrite.length);
                    blocksToWrite = dataToWrite.length / 4;
                }
                for (int iAddressStart = 0; iAddressStart < blocksToWrite; ++iAddressStart) {
                    addressStart = Helper.ConvertIntTo2bytesHexaFormat(iAddressStart);
                    WriteSingleBlockAnswer = null;
                    block[0] = dataToWrite[iAddressStart * 4];
                    block[1] = dataToWrite[iAddressStart * 4 + 1];
                    block[2] = dataToWrite[iAddressStart * 4 + 2];
                    block[3] = dataToWrite[iAddressStart * 4 + 3];
                    while ((WriteSingleBlockAnswer == null || WriteSingleBlockAnswer[0] == 1) && cpt <= 10) {
                        WriteSingleBlockAnswer = NFCCommand.SendWriteSingleBlockCommand(dataDevice.getCurrentTag(), Helper.ConvertIntTo2bytesHexaFormat(1), block, dataDevice);
                        cpt++;
                    }
                }


            }
            return null;
        }


//        protected Void doInBackground(Void... params) {
//            cpt = 0;
//            DataDevice dataDevice = currentDev;
//            byte[] dataToWrite;
//            WriteSingleBlockAnswer = null;
//            if (DecodeGetSystemInfoResponse(systemInfo)) {
//                int ResultWriteAnswer = 0;
//                byte[] block = new byte[4];
//                if(valueBlocksWrite.length%4!=0){
//                    int l = 4 - valueBlocksWrite.length%4 ;
//                    dataToWrite = new byte[valueBlocksWrite.length+l];
//                    System.arraycopy(valueBlocksWrite,0,dataToWrite,0,valueBlocksWrite.length);
//                    Arrays.fill(dataToWrite,valueBlocksWrite.length,valueBlocksWrite.length+1,(byte)0xFF);
//                    blocksToWrite = dataToWrite.length/4;
//                    for(int iAddressStart = 0; iAddressStart < blocksToWrite; ++iAddressStart) {
//                        addressStart = Helper.ConvertIntTo2bytesHexaFormat(iAddressStart);
//                        WriteSingleBlockAnswer = null;
//                        block[0]=dataToWrite[iAddressStart*4];
//                        block[1]=dataToWrite[iAddressStart*4+1];
//                        block[2]=dataToWrite[iAddressStart*4+2];
//                        block[3]=dataToWrite[iAddressStart*4+3];
//                        while((WriteSingleBlockAnswer == null || WriteSingleBlockAnswer[0] == 1) && cpt <= 10) {
//                            WriteSingleBlockAnswer = NFCCommand.SendWriteSingleBlockCommand(dataDevice.getCurrentTag(), addressStart, block, dataDevice);
//                            cpt++;
//                        }
//                        if (WriteSingleBlockAnswer[0] != 0) {
//
//                            WriteSingleBlockAnswer[0] = -31;
//                            return null;
//                        }
//                    }
//                    if (ResultWriteAnswer > 0) {
//                    WriteSingleBlockAnswer[0] = -1;
//                    } else {
//                    WriteSingleBlockAnswer[0] = 0;
//                    }
//                }
////                if (ResultWriteAnswer > 0) {
////                    WriteSingleBlockAnswer[0] = -1;
////                } else {
////                    WriteSingleBlockAnswer[0] = 0;
////                }
//            }
//
//
//
//            return null;
//        }


        protected void onPostExecute(Void unused) {
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            Log.d("NFC", "succses");
//            if (WriteSingleBlockAnswer[0] == 1) {
//                Log.d("NFC",  "ERROR File Transfer ");
//            } else if (WriteSingleBlockAnswer[0] == -1) {
//                Log.d("NFC",   "ERROR File Transfer ");
//            } else if (WriteSingleBlockAnswer[0] == -31) {
//                Log.d("NFC",   "ERROR File Transfer process stopped");
//            } else if (WriteSingleBlockAnswer[0] == 0) {
//                Log.d("NFC",  "Write Sucessfull ");
//            } else {
//                Log.d("NFC",   "File Transfer ERROR ");
//            }

        }
    }

    public boolean DecodeGetSystemInfoResponse(byte[] GetSystemInfoResponse) {
        DataDevice ma = currentDev;
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
