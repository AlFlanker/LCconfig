package ru.yugsys.vvvresearch.lconfig.views;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.*;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
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
import ru.yugsys.vvvresearch.lconfig.Services.*;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DataDevice;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DeviceEntry;
import ru.yugsys.vvvresearch.lconfig.model.Interfaces.ModelListener;

import java.util.*;

public class AddEditActivity extends AppCompatActivity implements AddEditViewable, View.OnClickListener, AsyncTaskCallBack.WriteCallback, ModelListener.OnGPSdata, ModelListener.OnNFCConnected {


    private Vibrator vibrator;
    private ProgressBar progressBar;
    private ScrollView scrollView;
    private ExpandableLinearLayout expandableLinearLayout;
    private EditText commentEdit;
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
    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;
    private DeviceEntry currentDevice;
    private View buttonLayout;
    private View triangleButton;
    private EditText gpsEditLatitude;
    private DataDevice currentDev;
    private byte[] systemInfo;
    private boolean createNewDevice;
    private boolean readyToWriteDevice = false;
    private Logger log = Logger.getInstance();
    private String comment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        scrollView = findViewById(R.id.lc5_scrollView);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        commentEdit = findViewById(R.id.lc5_edit_comment);
        /*Filters*/
//        appEUIEdit.setFilters(new InputFilter[]{new LengthFilter((short) 16)});
//        appKeyEdit.setFilters(new InputFilter[]{new LengthFilter((short) 32)});
//        nwkIDEdit.setFilters(new InputFilter[]{new LengthFilter((short) 8)});
//        devAdrEdit.setFilters(new InputFilter[]{new LengthFilter((short) 8)});
//        nwkSKeyEdit.setFilters(new InputFilter[]{new LengthFilter((short) 32)});
//        appSKeyEdit.setFilters(new InputFilter[]{new LengthFilter((short) 32)});
//        commentEdit.setFilters(new InputFilter[]{new LengthFilter((short) 50)});
        gpsEditLatitude.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        gpsEditLongitude.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        gpsEditLongitude.setFilters(new InputFilter[]{new HEXfilter(2, 7)});
        gpsEditLatitude.setFilters(new InputFilter[]{new HEXfilter(2, 7)});

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
            mLocation = GPSTracker.getLastKnownLocation(this);
        }

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        createNewDevice = getIntent().getBooleanExtra(MainActivity.ADD_NEW_DEVICE_MODE, false);
        if (createNewDevice) {
            String jperf = getString(R.string.pref_JUG_SYSTEMA);
            if(mLocation!=null ) {
                if(mLocation.getProvider().equals("")) {
                    mLocation = new Location("");
                    mLocation.setLongitude(38.997585);
                    mLocation.setLatitude(45.071137);
                }
            }
            else{
                mLocation = new Location("");
                mLocation.setLongitude(38.997585);
                mLocation.setLatitude(45.071137);
            }
            currentDevice = DeviceEntry.generate(jperf + "00000000", mLocation);
        } else {
            currentDevice = ((App) getApplication()).getModel().getCurrentDevice();
        }
        if (currentDevice != null) {
            setDeviceFields(currentDevice);
        }

        scrollView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String action = intent.getAction();
        if ("android.nfc.action.TECH_DISCOVERED".equals(action)) {
            Tag tagFromIntent = (Tag) intent.getParcelableExtra("android.nfc.extra.TAG");
            currentDev = new DataDevice();
            currentDev.setCurrentTag(tagFromIntent);
            systemInfo = NFCCommand.SendGetSystemInfoCommandCustom(tagFromIntent, currentDev);
            currentDev = DataDevice.DecodeGetSystemInfoResponse(systemInfo, currentDev);


            Toast.makeText(getApplicationContext(), getString(R.string.TagDetected), Toast.LENGTH_SHORT).show();
            if (createNewDevice && currentDev.getUid() != null) {
                currentDevice = fieldToDevice();
                String jpref = getString(R.string.pref_JUG_SYSTEMA);
                String muid = currentDev.getUid().replace(" ", "");
                muid = muid.substring(8);
                currentDevice.setDevadr(muid.toUpperCase());
                currentDevice.setEui(new StringBuilder().append(jpref).append(muid).toString().toUpperCase());
                setDeviceFields(currentDevice);
                createNewDevice = false;

            }
            if (readyToWriteDevice && currentDev.getUid() != null) {
                scrollView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                currentDevice = fieldToDevice();
                String jpref = getString(R.string.pref_JUG_SYSTEMA);
                String muid = currentDev.getUid().replace(" ", "");
                muid = muid.substring(8);
                currentDevice.setDevadr(currentDev.getUid().replace(" ", "").substring(8).toUpperCase());
                currentDevice.setEui(new StringBuilder().append(jpref).append(muid).toString().toUpperCase());
                setDeviceFields(currentDevice);
                WriteTask task = new WriteTask(currentDev);
                task.subscribe(this);
                task.execute(currentDevice);
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

                ((App) getApplication()).getModel().getGPSLocation();
            }
        } else {
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
    public void setDeviceFields(DeviceEntry device) {
        setSpinnerValuePosition(device.getType(), typeSpinner);
        setSpinnerValuePosition(device.getOutType(), out_typeSpinner);
        deveuiEdit.setText(device.getEui());
        appEUIEdit.setText(device.getAppeui());
        appKeyEdit.setText(device.getAppkey());
        nwkIDEdit.setText(device.getNwkid());
        devAdrEdit.setText(Util.getDevadrMSBtoLSB(device.getDevadr()));
        nwkSKeyEdit.setText(device.getNwkskey());
        appSKeyEdit.setText(device.getAppskey());
        isOTAASwitch.setChecked(device.getIsOTTA());
        gpsEditLongitude.setText(String.format(Locale.ENGLISH, "%.6f", device.getLongitude()));
        gpsEditLatitude.setText(String.format(Locale.ENGLISH, "%.6f", device.getLatitude()));
        commentEdit.setText(device.getComment());

    }

    @Override
    public void setLocationFields(Location location) {
        gpsEditLongitude.setText(String.valueOf(location.getLongitude()));
        gpsEditLatitude.setText(String.valueOf(location.getLatitude()));
    }

    private void setSpinnerValuePosition(String value, Spinner spinner) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();
        int i = adapter.getPosition(value);
        if (spinner == typeSpinner) this.typeSpinner.setSelection(i);
        else if (spinner == out_typeSpinner) this.out_typeSpinner.setSelection(i);
    }

    private DeviceEntry fieldToDevice() {
        DeviceEntry device = new DeviceEntry();
        device.setType(typeSpinner.getSelectedItem().toString().trim().toUpperCase());
        device.setEui(deveuiEdit.getText().toString().toUpperCase());
        device.setAppeui(appEUIEdit.getText().toString().toUpperCase());
        device.setAppkey(appKeyEdit.getText().toString().toUpperCase());
        device.setNwkid(nwkIDEdit.getText().toString().toUpperCase());
        device.setDevadr(Util.getDevadrMSBtoLSB(devAdrEdit.getText().toString().toUpperCase()));
        device.setNwkskey(nwkSKeyEdit.getText().toString().toUpperCase());
        device.setAppskey(appSKeyEdit.getText().toString().toUpperCase());
        device.setKI("991C");
        device.setKV("EC03CE03D003E103E30304040E04B9096C09CE080F087407A6060506");
        device.setIsOTTA(isOTAASwitch.isChecked());
        device.setLatitude(Double.parseDouble(gpsEditLatitude.getText().toString()));
        device.setLongitude(Double.parseDouble(gpsEditLongitude.getText().toString()));
        device.setOutType(out_typeSpinner.getSelectedItem().toString().toUpperCase());
        device.setComment(commentEdit.getText().toString());
        device.setDateOfLastChange(new Date());
        device.setIsDeleted(false);
        return device;
    }

    @Override
    public void onClick(View view) {
        //  currentDevice = fieldToDevice(); // In OnNewIntent()
        readyToWriteDevice = true;
        Toast.makeText(getApplicationContext(), getString(R.string.TouchToDevice), Toast.LENGTH_SHORT).show();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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


    @Override
    public void writeComplate(Byte writeResult) {
        scrollView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        if (writeResult == null) {
        } else if (writeResult == (byte) 0x01) {
            Toast.makeText(getApplicationContext(), getString(R.string.ERRORFileTransfer), Toast.LENGTH_SHORT).show();
        } else if (writeResult== (byte) 0xFF) {
            Toast.makeText(getApplicationContext(), getString(R.string.ERRORFileTransfer), Toast.LENGTH_SHORT).show();
        } else if (writeResult == (byte) 0xE1) {
            Toast.makeText(getApplicationContext(), getString(R.string.TransferStop), Toast.LENGTH_SHORT).show();
        } else if (writeResult == (byte) 0x00) {
            Toast.makeText(getApplicationContext(), getString(R.string.WriteSucessfull), Toast.LENGTH_SHORT).show();

            ((App) getApplication()).getModel().saveDevice(currentDevice);
            readyToWriteDevice = false;
            if (Build.VERSION.SDK_INT >= 26) {
                vibrator.vibrate(VibrationEffect.createOneShot(500, 100));
            } else if (Build.VERSION.SDK_INT < 26) {
                vibrator.vibrate(500);
            }
            finish();
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.ERRORFileTransfer), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void OnGPSdata(Location location) {
        if (location != null) {
            setLocationFields(location);
        }
    }

    @Override
    public void OnNFCConnected(DeviceEntry dev) {
        setDeviceFields(dev);
    }
}
