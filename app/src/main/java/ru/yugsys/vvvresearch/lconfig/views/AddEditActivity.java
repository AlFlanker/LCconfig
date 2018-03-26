package ru.yugsys.vvvresearch.lconfig.views;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.*;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

import java.util.Arrays;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

public class AddEditActivity extends AppCompatActivity implements AddEditViewable, View.OnClickListener {
    private Vibrator vibrator;
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
    private byte[] writeResult = null;
    private int countOfAttempt;
    private byte[] valueBlocksWrite;
    private int numberOfBlocks = 0;
    private Location mLocation;
    private LocationManager mLocationManager;
    private boolean createNewDevice;
    private boolean readyToWriteDevice = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("NFC","AddActivity");
        readyToWriteDevice = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);
        Toolbar toolbar = findViewById(R.id.toolbar_add_edit);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();//
            }
        });
        FloatingActionButton fab = findViewById(R.id.fab_nfc);
        fab.setOnClickListener(this);
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
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        createNewDevice = getIntent().getBooleanExtra(MainActivity.ADD_NEW_DEVICE_MODE, true);
        if (createNewDevice) {
            String jperf = getString(R.string.pref_JUG_SYSTEMA);
            mLocation = getLastKnownLocation();
            currentDevice = Helper.generate(jperf + "00000000", mLocation);
            currentDevice.Longitude = mLocation.getLongitude();
            currentDevice.Latitude = mLocation.getLatitude();
        } else {
            currentDevice = ((App) getApplication()).getModel().getCurrentDevice();
        }
        if (currentDevice != null) {
            setDeviceFields(currentDevice);
        }


    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d("NFC", "newintent");
        super.onNewIntent(intent);
        String action = intent.getAction();
        if ("android.nfc.action.TECH_DISCOVERED".equals(action)) {
            Tag tagFromIntent = (Tag) intent.getParcelableExtra("android.nfc.extra.TAG");
            currentDev = new DataDevice();
            currentDev.setCurrentTag(tagFromIntent);
            systemInfo = NFCCommand.SendGetSystemInfoCommandCustom(tagFromIntent, currentDev);
            //  currentDev = Helper.DecodeGetSystemInfoResponse(systemInfo,currentDev);

            Toast.makeText(getApplicationContext(), "Tag detected!", Toast.LENGTH_SHORT).show();
            Log.d("NFC", "add new tag");
            if (createNewDevice) {
                currentDevice = fieldToDevice();
                String jpref = getString(R.string.pref_JUG_SYSTEMA);
                String muid = currentDev.getUid().replace(" ", "");
                muid = muid.substring(8);
                byte[] b = Helper.hexToBytes(muid);

                for (int i = 0; i < b.length / 2; i++) {
                    byte temp = b[i];
                    b[i] = b[b.length - i - 1];
                    b[b.length - i - 1] = temp;
                }
                muid = Helper.ConvertHexByteArrayToString(b);
                currentDevice.setDevadr(muid);
                currentDevice.setEui(new StringBuilder().append(jpref).append(muid).toString());
                setDeviceFields(currentDevice);
                createNewDevice = false;
            }
            if (readyToWriteDevice) {
                currentDevice = fieldToDevice();
                new StartWriteTask().execute();
                readyToWriteDevice = false;
            }

        }

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists);
        if (this.getIntent().getBooleanExtra(MainActivity.ADD_NEW_DEVICE_MODE, true)) {
            presenter.fireGetNewGPSData();
        } else {

            Log.d("NFC", "readNFC");
            Log.d("NFC", "post readNFC");
        }
    }

    @Override
    public void setDeviceFields(Device device) {
        setSpinnerValuePosition(device.getType(), typeSpinner);
        setSpinnerValuePosition(device.getOutType(), out_typeSpinner);
        deveuiEdit.setText(device.getEui());
        appEUIEdit.setText(device.getAppeui());
        appKeyEdit.setText(device.getAppkey());
        nwkIDEdit.setText(device.getNwkid());
        devAdrEdit.setText(device.getDevadr());
        nwkSKeyEdit.setText(device.getNwkskey());
        appSKeyEdit.setText(device.getAppskey());
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

        device.setIsOTTA(isOTAASwitch.isChecked());
        device.setLatitude(Double.parseDouble(gpsEditLatitude.getText().toString()));
        device.setLongitude(Double.parseDouble(gpsEditLongitude.getText().toString()));
        device.setOutType(out_typeSpinner.getSelectedItem().toString());
        return device;
    }

    @Override
    public void onClick(View view) {
        currentDevice = fieldToDevice();
        readyToWriteDevice = true;
        Toast.makeText(getApplicationContext(), "поднеси к устройству!", Toast.LENGTH_SHORT).show();


    }
    //************************************
    //**************************************************************


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unBind();
    }

    private class StartWriteTask extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(AddEditActivity.this);

        // can use UI thread here
        protected void onPreExecute() {

            this.dialog.setMessage("Не убирай телефон от устройства!");
            this.dialog.show();
            valueBlocksWrite = new byte[123];
            final DataDevice dataDevice = currentDev;


            try {
                if (currentDev != null) {

                    systemInfo = NFCCommand.SendGetSystemInfoCommandCustom(dataDevice.getCurrentTag(), dataDevice);
                    if ((Helper.DecodeGetSystemInfoResponse(systemInfo, currentDev)) != null) {
                        int MemorySizeBytes = (Helper.ConvertStringToInt((dataDevice.getMemorySize().replace(" ", ""))) + 1) * 4;
                        byte[] raw = Helper.Object2ByteArray(currentDevice);
                        System.arraycopy(raw, 0, valueBlocksWrite, 0, raw.length);
                        //  valueBlocksWrite[0]=0x00;
                        CRC16 crc16 = new CRC16();
                        int res = crc16.CRC16ArrayGet(0, raw);
                        res = Integer.reverseBytes(res);
                        res >>= 16;
                        byte[] crc = ByteBuffer.allocate(4).putInt(res).array();
                        StringBuilder sb = new StringBuilder();
                        for (Byte b : crc) {
                            sb.append(String.format("%02x ", b));
                        }
                        Log.d("NFCdata", "crc byte: " + sb.toString());
                        valueBlocksWrite[121] = crc[3];
                        valueBlocksWrite[122] = crc[2];
                        sb = new StringBuilder();
                        for (Byte b : valueBlocksWrite) {
                            sb.append(String.format("%02x ", b));
                        }
                        Log.d("NFCdata", "result byte: " + sb.toString());

                    }
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }


        }


        // automatically done on worker thread (separate from UI thread)
        @Override
        protected Void doInBackground(Void... params) {
            countOfAttempt = 0;
            Log.d("NFCdata", "doInBackground 1");
            byte[] block;
            DataDevice dataDevice = currentDev;
            writeResult = null;
            byte[] dataBuf;
            StringBuilder sb;
            if (currentDev != null) {
                if ((Helper.DecodeGetSystemInfoResponse(systemInfo, currentDev)) != null) {
                    if (valueBlocksWrite.length % 4 != 0) {
                        Log.d("NFCdata", "doInBackground init param");
                        int l = 4 - valueBlocksWrite.length % 4;
                        dataBuf = new byte[valueBlocksWrite.length + l];
                        System.arraycopy(valueBlocksWrite, 0, dataBuf, 0, valueBlocksWrite.length);
                        Arrays.fill(dataBuf, valueBlocksWrite.length, valueBlocksWrite.length + 1, (byte) 0xFF);
                        numberOfBlocks = dataBuf.length / 4;

                    } else {
                        dataBuf = new byte[valueBlocksWrite.length];
                        System.arraycopy(valueBlocksWrite, 0, dataBuf, 0, valueBlocksWrite.length);
                        numberOfBlocks = dataBuf.length / 4;
                    }
                    sb = new StringBuilder();
                    for (Byte b : dataBuf) {
                        sb.append(String.format("%02x ", b));
                    }
                    Log.d("NFCdata", sb.toString());
                    int ResultWriteAnswer = 0;

                    for (int startAddres = 0; startAddres < numberOfBlocks; startAddres++) {
                        addressStart = Helper.ConvertIntTo2bytesHexaFormat(startAddres);
                        block = new byte[4];
                        block[0] = dataBuf[startAddres * 4];
                        block[1] = dataBuf[startAddres * 4 + 1];
                        block[2] = dataBuf[startAddres * 4 + 2];
                        block[3] = dataBuf[startAddres * 4 + 3];
                        countOfAttempt = 0;
                        writeResult = null;
                        Log.d("NFCdata", "doInBackground pre write");
                        while ((writeResult == null || writeResult[0] == 1) && countOfAttempt <= 10) {
                            writeResult = NFCCommand.SendWriteSingleBlockCommand(dataDevice.getCurrentTag(), addressStart, block, dataDevice);
                            countOfAttempt++;
                        }
                        if (writeResult != null) {
                            if (writeResult[0] != (byte) 0x00) {
                                ResultWriteAnswer++;
                                writeResult[0] = (byte) 0xE1;
                                return null;
                            }
                        }
                    }
                    if (writeResult != null) {
                        if (ResultWriteAnswer > 0)
                            writeResult[0] = (byte) 0xFF;
                        else
                            writeResult[0] = (byte) 0x00;
                    }
                }
                Log.d("NFCdata", "doInBackground post");
            }

            return null;
        }

        protected void onPostExecute(final Void unused) {
            if (this.dialog.isShowing())
                this.dialog.dismiss();
            Log.d("NFCdata", "onPostExecute post dialog");
            if (writeResult == null) {
                Log.d("NFCdata", "onPostExecute post dialog in if");
                Log.d("NFCdata", "onPostExecute post dialog \n ERROR File Transfer (No tag answer)");
                Toast.makeText(getApplicationContext(), "ERROR File Transfer (No tag answer) ", Toast.LENGTH_SHORT).show();
            } else if (writeResult[0] == (byte) 0x01) {

                Log.d("NFCdata", "onPostExecute post dialog \n RROR File Transfer 1");
                Toast.makeText(getApplicationContext(), "ERROR File Transfer ", Toast.LENGTH_SHORT).show();
            } else if (writeResult[0] == (byte) 0xFF) {

                Log.d("NFCdata", "onPostExecute post dialog \n ERROR File Transfer 2 ");
                Toast.makeText(getApplicationContext(), "ERROR File Transfer ", Toast.LENGTH_SHORT).show();
            } else if (writeResult[0] == (byte) 0xE1) {

                Log.d("NFCdata", "onPostExecute post dialog \n ERROR File Transfer process stopped ");
                Toast.makeText(getApplicationContext(), "ERROR File Transfer process stopped", Toast.LENGTH_SHORT).show();
            } else if (writeResult[0] == (byte) 0x00) {
                Log.d("NFCdata", "onPostExecute post dialog \n EWrite Sucessfull");
                Toast.makeText(getApplicationContext(), "Write Sucessfull ", Toast.LENGTH_SHORT).show();
                presenter.fireNewDevice(fieldToDevice());
                Log.d("NFCdata", "onPostExecute post dialog \n field to device");
                readyToWriteDevice = false;
                if (Build.VERSION.SDK_INT == 26) {
                    Log.d("NFCdata", "onPostExecute post dialog \n vibr api 26");
                    vibrator.vibrate(VibrationEffect.createOneShot(2000, 100));
                } else if (Build.VERSION.SDK_INT < 26) {
                    Log.d("NFCdata", "onPostExecute post dialog \n vibr api <26");
                    vibrator.vibrate(2000);
                }
                Log.d("NFCdata", "onPostExecute post dialog \n finish()");
                finish();


            } else {
                Log.d("NFCdata", "onPostExecute post dialog \n File Transfer ERROR end TASK!");
                Toast.makeText(getApplicationContext(), "File Transfer ERROR ", Toast.LENGTH_SHORT).show();
            }


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_gps:{
                Intent intent = new Intent(this,MapsActivity.class);
                intent.putExtra(MapsActivity.LATITUDE,Double.parseDouble(gpsEditLatitude.getText().toString()));
                intent.putExtra(MapsActivity.LOGITUDE,Double.parseDouble(gpsEditLongitude.getText().toString()));
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private Location getLastKnownLocation() {
        mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location myLocation = null;
        for (String provider : providers) {
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (myLocation == null || l.getAccuracy() < myLocation.getAccuracy()) {
                myLocation = l;
            }
        }
        return myLocation;
    }
}
