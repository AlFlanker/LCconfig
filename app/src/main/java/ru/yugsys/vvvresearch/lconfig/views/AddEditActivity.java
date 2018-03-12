package ru.yugsys.vvvresearch.lconfig.views;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import ru.yugsys.vvvresearch.lconfig.App;
import ru.yugsys.vvvresearch.lconfig.R;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DataDevice;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.Device;
import ru.yugsys.vvvresearch.lconfig.presenters.AddEditPresentable;
import ru.yugsys.vvvresearch.lconfig.presenters.AddEditPresenter;

public class AddEditActivity extends AppCompatActivity implements AddEditViewable, View.OnClickListener {

    private EditText deveuiEdit;
    private EditText appEUIEdit;
    private EditText appKeyEdit;
    private EditText nwkIDEdit;
    private EditText devAdrEdit;
    private EditText nwkSKeyEdit;
    private EditText appSKeyEdit;
    private EditText isOTAAEdit;
    private EditText gpsEdit;
    private EditText out_typeEdit;
    private EditText typeEdit;
    private Button addEditButton;
    private AddEditPresentable presenter;
    // new  fields
    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;
    public DataDevice dataDevice;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);
        presenter = new AddEditPresenter(((App)getApplication()).getModel());
        presenter.bind(this);
        typeEdit = findViewById(R.id.lc5_edit_type);
        appEUIEdit = findViewById(R.id.lc5_edit_appEUI);
        appKeyEdit = findViewById(R.id.lc5_edit_appKey);
        nwkIDEdit = findViewById(R.id.lc5_edit_nwkID);
        devAdrEdit = findViewById(R.id.lc5_edit_devAdr);
        nwkSKeyEdit = findViewById(R.id.lc5_edit_nwkSKey);
        appSKeyEdit = findViewById(R.id.lc5_edit_appSKey);
        isOTAAEdit = findViewById(R.id.lc5_edit_isOTAA);
        gpsEdit = findViewById(R.id.lc5_edit_gps);
        out_typeEdit = findViewById(R.id.lc5_edit_out_type);
        deveuiEdit = findViewById(R.id.lc5_edit_deveui);
        addEditButton = findViewById(R.id.action_add_edit);
        addEditButton.setOnClickListener(this);

        // new code
        mAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mAdapter != null && mAdapter.isEnabled()) {
            mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
            IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
            mFilters = new IntentFilter[]{ndef,};
            mTechLists = new String[][]{new String[]{android.nfc.tech.NfcV.class.getName()}};
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction().toString())){
            Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            dataDevice = new DataDevice();
            dataDevice.setCurrentTag(tagFromIntent);
        }
    }

    @Override
    public void setDeviceFields(Device device) {
        typeEdit.setText(device.getType());
        deveuiEdit.setText(device.getEui());
        appEUIEdit.setText(device.getAppeui());
        appKeyEdit.setText(device.getAppkey());
        nwkIDEdit.setText(device.getNwkid());
        devAdrEdit.setText(device.getDevadr());
        nwkSKeyEdit.setText(device.getNwkskey());
        appSKeyEdit.setText(device.getAppskey());
        //isOTAAEdit.setText(device.get
        gpsEdit.setText(device.getLatitude() + ", " + device.getLongitude());
        out_typeEdit.setText(device.getOutType());
    }

    private Device fieldToDevice() {
        Device device = new Device();
        device.setType(typeEdit.getText().toString());
        device.setEui(deveuiEdit.getText().toString());
        device.setAppeui(appEUIEdit.getText().toString());
        device.setAppkey(appKeyEdit.getText().toString());
        device.setNwkid(nwkIDEdit.getText().toString());
        device.setDevadr(devAdrEdit.getText().toString());
        device.setNwkskey(nwkSKeyEdit.getText().toString());
        device.setAppskey(appSKeyEdit.getText().toString());
        device.setKI("gdfg");
        device.setKV("gdfg");
        //isOTAAEdit.setText(device.get
        String gpsText = gpsEdit.getText().toString();
        String[] gpsSplit = gpsText.split(",");
        if (gpsSplit.length == 2) {
            device.setLatitude(Double.parseDouble(gpsSplit[0]));
            device.setLongitude(Double.parseDouble(gpsSplit[1]));
        }
        device.setOutType(out_typeEdit.getText().toString());
        return device;
    }

    @Override
    public void onClick(View view) {
        presenter.fireNewDevice(fieldToDevice());
        finish();
    }
}
;