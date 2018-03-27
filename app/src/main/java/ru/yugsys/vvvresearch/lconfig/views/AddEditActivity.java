package ru.yugsys.vvvresearch.lconfig.views;

import android.Manifest;
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
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.Utils;
import ru.yugsys.vvvresearch.lconfig.App;
import ru.yugsys.vvvresearch.lconfig.Logger;
import ru.yugsys.vvvresearch.lconfig.R;
import ru.yugsys.vvvresearch.lconfig.Services.CRC16;
import ru.yugsys.vvvresearch.lconfig.Services.GPSTracker;
import ru.yugsys.vvvresearch.lconfig.Services.NFCCommand;
import ru.yugsys.vvvresearch.lconfig.Services.Util;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DataDevice;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.Device;
import ru.yugsys.vvvresearch.lconfig.presenters.AddEditPresentable;
import ru.yugsys.vvvresearch.lconfig.presenters.AddEditPresenter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

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
    private byte[] writeResult = null;
    private byte[] valueBlocksWrite;
    private boolean createNewDevice;
    private boolean readyToWriteDevice = false;
    private Logger log = Logger.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        log.d("NFC", "AddActivity");
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
        Location mLocation = new Location("");
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    100);
        } else {

            GPSTracker gpsTracker = GPSTracker.instance();
            gpsTracker.setContext(this);
            gpsTracker.OnStartGPS();
            mLocation = getLastKnownLocation();
        }
        log.d("GPS", "Activity gps start");

        presenter = new AddEditPresenter(((App) getApplication()).getModel());
        presenter.bind(this);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        createNewDevice = getIntent().getBooleanExtra(MainActivity.ADD_NEW_DEVICE_MODE, true);
        if (createNewDevice) {
            String jperf = getString(R.string.pref_JUG_SYSTEMA);
            if (mLocation.getProvider().equals("")) {
                mLocation = new Location("");
                mLocation.setLongitude(38.997585);
                mLocation.setLatitude(45.071137);
            }
            currentDevice = Util.generate(jperf + "00000000", mLocation);
        } else {
            currentDevice = ((App) getApplication()).getModel().getCurrentDevice();
        }
        if (currentDevice != null) {
            setDeviceFields(currentDevice);
        }


    }

    @Override
    protected void onNewIntent(Intent intent) {
        log.d("NFC", "newintent");
        super.onNewIntent(intent);
        String action = intent.getAction();
        if ("android.nfc.action.TECH_DISCOVERED".equals(action)) {
            Tag tagFromIntent = (Tag) intent.getParcelableExtra("android.nfc.extra.TAG");
            currentDev = new DataDevice();
            currentDev.setCurrentTag(tagFromIntent);
            systemInfo = NFCCommand.SendGetSystemInfoCommandCustom(tagFromIntent, currentDev);
            //  currentDev = Util.DecodeGetSystemInfoResponse(systemInfo,currentDev);

            Toast.makeText(getApplicationContext(), "Tag detected!", Toast.LENGTH_SHORT).show();
            log.d("NFC", "add new tag");
            if (createNewDevice) {
                currentDevice = fieldToDevice();
                String jpref = getString(R.string.pref_JUG_SYSTEMA);
                String muid = currentDev.getUid().replace(" ", "");
                muid = muid.substring(8);
                byte[] b = Util.hexToBytes(muid);
                for (int i = 0; i < b.length / 2; i++) {
                    byte temp = b[i];
                    b[i] = b[b.length - i - 1];
                    b[b.length - i - 1] = temp;
                }
                muid = Util.ConvertHexByteArrayToString(b);
                currentDevice.setDevadrMSBtoLSB(muid);
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
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                presenter.fireGetNewGPSData();
            }
        } else {

            log.d("NFC", "readNFC");
            log.d("NFC", "post readNFC");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (NfcAdapter.getDefaultAdapter(this) != null) {
            NfcAdapter.getDefaultAdapter(this).disableForegroundDispatch(this);
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
        devAdrEdit.setText(device.getDevadrMSBtoLSB());
        nwkSKeyEdit.setText(device.getNwkskey());
        appSKeyEdit.setText(device.getAppskey());
        isOTAASwitch.setChecked(false);
        gpsEditLongitude.setText(String.format(Locale.ENGLISH, "%.6f", device.getLongitude()));
        gpsEditLatitude.setText(String.format(Locale.ENGLISH, "%.6f", device.getLatitude()));
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
        device.setDevadrMSBtoLSB(devAdrEdit.getText().toString());
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

            this.dialog.setMessage(getString(R.string.dont_leave_phone));
            this.dialog.show();
            valueBlocksWrite = new byte[123];
            final DataDevice dataDevice = currentDev;


            try {
                if (currentDev != null) {

                    systemInfo = NFCCommand.SendGetSystemInfoCommandCustom(dataDevice.getCurrentTag(), dataDevice);
                    if ((Util.DecodeGetSystemInfoResponse(systemInfo, currentDev)) != null) {
                        int MemorySizeBytes = (Util.ConvertStringToInt((dataDevice.getMemorySize().replace(" ", ""))) + 1) * 4;
                        byte[] raw = Util.Object2ByteArray(currentDevice);
                        System.arraycopy(raw, 0, valueBlocksWrite, 0, raw.length);
                        //  valueBlocksWrite[0]=0x00;
                        CRC16 crc16 = new CRC16();
                        int res = crc16.CRC16ArrayGet(0, raw);

                        res &= 0x0000ffff;
                        byte[] crc = ByteBuffer.allocate(4).putInt(res).array();
                        StringBuilder sb = new StringBuilder();
                        for (Byte b : crc) {
                            sb.append(String.format("%02x ", b));
                        }
                        log.d("NFCdata", "crc byte: " + sb.toString());
                        valueBlocksWrite[121] = crc[3];
                        valueBlocksWrite[122] = crc[2];
                        sb = new StringBuilder();
                        for (Byte b : valueBlocksWrite) {
                            sb.append(String.format("%02x ", b));
                        }
                        log.d("NFCdata", "result byte: " + sb.toString());

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
            int countOfAttempt = 0;
            log.d("NFCdata", "doInBackground 1");
            byte[] block;
            DataDevice dataDevice = currentDev;
            writeResult = null;
            byte[] dataBuf;
            StringBuilder sb;
            if (currentDev != null) {
                if ((Util.DecodeGetSystemInfoResponse(systemInfo, currentDev)) != null) {
                    int numberOfBlocks = 0;
                    if (valueBlocksWrite.length % 4 != 0) {
                        log.d("NFCdata", "doInBackground init param");
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
                    log.d("NFCdata", sb.toString());
                    int ResultWriteAnswer = 0;

                    for (int startAddres = 0; startAddres < numberOfBlocks; startAddres++) {
                        byte[] addressStart = Util.ConvertIntTo2bytesHexaFormat(startAddres);
                        block = new byte[4];
                        block[0] = dataBuf[startAddres * 4];
                        block[1] = dataBuf[startAddres * 4 + 1];
                        block[2] = dataBuf[startAddres * 4 + 2];
                        block[3] = dataBuf[startAddres * 4 + 3];
                        countOfAttempt = 0;
                        writeResult = null;
                        log.d("NFCdata", "doInBackground pre write");
                        while ((writeResult == null || writeResult[0] == 1) && countOfAttempt <= 10) {
                            writeResult = NFCCommand.writeSingleBlockCommand(dataDevice.getCurrentTag(), addressStart, block, dataDevice);
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
                log.d("NFCdata", "doInBackground post");
            }

            return null;
        }

        protected void onPostExecute(final Void unused) {
            if (this.dialog.isShowing())
                this.dialog.dismiss();
            log.d("NFCdata", "onPostExecute post dialog");
            if (writeResult == null) {
                log.d("NFCdata", "onPostExecute post dialog in if");
                log.d("NFCdata", "onPostExecute post dialog \n ERROR File Transfer (No tag answer)");
                Toast.makeText(getApplicationContext(), "ERROR File Transfer (No tag answer) ", Toast.LENGTH_SHORT).show();
            } else if (writeResult[0] == (byte) 0x01) {

                log.d("NFCdata", "onPostExecute post dialog \n RROR File Transfer 1");
                Toast.makeText(getApplicationContext(), "ERROR File Transfer ", Toast.LENGTH_SHORT).show();
            } else if (writeResult[0] == (byte) 0xFF) {

                log.d("NFCdata", "onPostExecute post dialog \n ERROR File Transfer 2 ");
                Toast.makeText(getApplicationContext(), "ERROR File Transfer ", Toast.LENGTH_SHORT).show();
            } else if (writeResult[0] == (byte) 0xE1) {

                log.d("NFCdata", "onPostExecute post dialog \n ERROR File Transfer process stopped ");
                Toast.makeText(getApplicationContext(), "ERROR File Transfer process stopped", Toast.LENGTH_SHORT).show();
            } else if (writeResult[0] == (byte) 0x00) {
                log.d("NFCdata", "onPostExecute post dialog \n EWrite Sucessfull");
                Toast.makeText(getApplicationContext(), "Write Sucessfull ", Toast.LENGTH_SHORT).show();
                presenter.fireNewDevice(fieldToDevice());
                log.d("NFCdata", "onPostExecute post dialog \n field to device");
                readyToWriteDevice = false;
                if (Build.VERSION.SDK_INT == 26) {
                    log.d("NFCdata", "onPostExecute post dialog \n vibr api 26");
                    vibrator.vibrate(VibrationEffect.createOneShot(500, 100));
                } else if (Build.VERSION.SDK_INT < 26) {
                    log.d("NFCdata", "onPostExecute post dialog \n vibr api <26");
                    vibrator.vibrate(500);
                }
                log.d("NFCdata", "onPostExecute post dialog \n finish()");
                finish();


            } else {
                log.d("NFCdata", "onPostExecute post dialog \n File Transfer ERROR end TASK!");
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
            case R.id.action_gps: {
                Intent intent = new Intent(this, MapsActivity.class);
                intent.putExtra(MapsActivity.LATITUDE, Double.parseDouble(gpsEditLatitude.getText().toString()));
                intent.putExtra(MapsActivity.LOGITUDE, Double.parseDouble(gpsEditLongitude.getText().toString()));
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private Location getLastKnownLocation() {
        LocationManager mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
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
