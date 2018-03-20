package ru.yugsys.vvvresearch.lconfig.views;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.content.pm.PackageManager;
import android.location.Location;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.Utils;
import ru.yugsys.vvvresearch.lconfig.App;
import ru.yugsys.vvvresearch.lconfig.R;
import ru.yugsys.vvvresearch.lconfig.Services.GPSTracker;
import ru.yugsys.vvvresearch.lconfig.Services.Helper;
import ru.yugsys.vvvresearch.lconfig.Services.NFCCommand;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DataDevice;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.Device;
import ru.yugsys.vvvresearch.lconfig.model.Interfaces.Model;
import ru.yugsys.vvvresearch.lconfig.model.Interfaces.ModelListener;
import ru.yugsys.vvvresearch.lconfig.presenters.AddEditPresentable;
import ru.yugsys.vvvresearch.lconfig.presenters.AddEditPresenter;

import java.io.IOException;
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
    private byte[] systemInfo;
    private DataDevice currentDev;
    private byte[] addressStart = null;
    private HashMap<Integer, byte[]> memory = new HashMap<>(40);
    private int cpt;
    private byte[] WriteSingleBlockAnswer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("NFC","AddActivity");

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_edit);
        presenter = new AddEditPresenter(((App) getApplication()).getModel());
        presenter.bind(this);
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
        currentDevice = ((App) getApplication()).getModel().getCurrentDevice();
        setDeviceFields(currentDevice);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d("NFC","newintent");
        super.onNewIntent(intent);
        Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        currentDev = ((App) getApplication()).getModel().getCurrentDev();
        currentDev.setCurrentTag(tagFromIntent);

    }
    @Override
    protected void onPostResume() {
        super.onPostResume();
        mPendingIntent = PendingIntent.getActivity(this, 0,new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists);
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
        device.setKI("991C");
        device.setKV("EC03CE03D003E103E30304040E04B9096C09CE080F087407A6060506");
        //TODO: Fill device field
        //isOTAAEdit.setText(device.get
        device.setLatitude(Double.parseDouble(gpsEditLatitude.getText().toString()));
        device.setLongitude(Double.parseDouble(gpsEditLongitude.getText().toString()));
        device.setOutType(out_typeSpinner.getSelectedItem().toString());
        return device;
    }

    @Override
    public void onClick(View view) {
        presenter.fireNewDevice(fieldToDevice());
        finish();
    }
    //**************************************************************

    private class StartWriteTask extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog;

        private StartWriteTask() {
            this.dialog = new ProgressDialog(AddEditActivity.this);
        }

        protected void onPreExecute() {
            this.dialog.setMessage("Please, place your phone near the card");
            this.dialog.show();
            byte[] valueBlocksWrite = new byte[123];
            try {
                byte[] raw = Helper.Object2ByteArray(currentDevice);
                systemInfo = NFCCommand.SendGetSystemInfoCommandCustom(currentDev.getCurrentTag(), currentDev);
                if (Helper.DecodeGetSystemInfoResponse(systemInfo, currentDev) != null) {
                    addressStart = new byte[]{0x00, 0x00, 0x00, 0x00};
                    System.arraycopy(raw, 0, valueBlocksWrite, 1, raw.length);
                    valueBlocksWrite[0] = 1;
                    byte[] valueBlockWrite = new byte[4];
                    System.arraycopy(valueBlocksWrite, 0, valueBlockWrite, 0, 4);
                    memory.put(0, valueBlockWrite);
                    for (int i = 1; i < 32; i++) {
                        valueBlockWrite = new byte[4];
                        System.arraycopy(valueBlocksWrite, i * 4, valueBlockWrite, 0, 4);
                        memory.put(0, valueBlockWrite);
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

        protected Void doInBackground(Void... params) {
            cpt = 0;
            DataDevice dataDevice = currentDev;
            WriteSingleBlockAnswer = null;
//            if (Helper.DecodeGetSystemInfoResponse(systemInfo,currentDev)!=null) {
//                while((WriteSingleBlockAnswer == null || WriteSingleBlockAnswer[0] == 1) && cpt <= 10) {
//                    WriteSingleBlockAnswer = NFCCommand.SendWriteSingleBlockCommand(dataDevice.getCurrentTag(), BasicWrite.this.addressStart, BasicWrite.this.dataToWrite, dataDevice);
//                    cpt++;
//                }
//            }

            return null;
        }

        protected void onPostExecute(Void unused) {
//            if (this.dialog.isShowing()) {
//                this.dialog.dismiss();
//            }
//
//            if (BasicWrite.this.WriteSingleBlockAnswer == null) {
//                Toast.makeText(BasicWrite.this.getApplicationContext(), "ERROR Write (No tag answer) ", 0).show();
//            } else if (BasicWrite.this.WriteSingleBlockAnswer[0] == 1) {
//                Toast.makeText(BasicWrite.this.getApplicationContext(), "ERROR Write ", 0).show();
//            } else if (BasicWrite.this.WriteSingleBlockAnswer[0] == -1) {
//                Toast.makeText(BasicWrite.this.getApplicationContext(), "ERROR Write ", 0).show();
//            } else if (BasicWrite.this.WriteSingleBlockAnswer[0] == 0) {
//                Toast.makeText(BasicWrite.this.getApplicationContext(), "Write Sucessfull ", 0).show();
//            } else {
//                Toast.makeText(BasicWrite.this.getApplicationContext(), "Write ERROR ", 0).show();
//            }

        }
    }
}
