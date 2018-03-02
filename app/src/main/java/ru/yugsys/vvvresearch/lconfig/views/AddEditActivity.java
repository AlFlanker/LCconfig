package ru.yugsys.vvvresearch.lconfig.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import ru.yugsys.vvvresearch.lconfig.R;
import ru.yugsys.vvvresearch.lconfig.fakemodel.BusinessModel;
import ru.yugsys.vvvresearch.lconfig.fakemodel.Device;
import ru.yugsys.vvvresearch.lconfig.presenters.AddEditPresentable;
import ru.yugsys.vvvresearch.lconfig.presenters.AddEditPresenter;

public class AddEditActivity extends AppCompatActivity implements AddEditViewable,View.OnClickListener {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);
        presenter = new AddEditPresenter(BusinessModel.getInstance());
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
        gpsEdit.setText(device.getLatitude() + ", " +device.getLongitude());
        out_typeEdit.setText(device.getOutType());
    }

    private Device fieldToDevice (){
        Device device = new Device();
        device.setType(typeEdit.getText().toString());
        device.setEui(deveuiEdit.getText().toString());
        device.setAppeui(appEUIEdit.getText().toString());
        device.setAppkey(appKeyEdit.getText().toString());
        device.setNwkid(nwkIDEdit.getText().toString());
        device.setDevadr(devAdrEdit.getText().toString());
        device.setNwkskey(nwkSKeyEdit.getText().toString());
        device.setAppskey(appSKeyEdit.getText().toString());
        //isOTAAEdit.setText(device.get
        String gpsText = gpsEdit.getText().toString();
        String[] gpsSplit = gpsText.split(",");
        if (gpsSplit.length==2) {
            device.setLatitude(Double.parseDouble(gpsSplit[0]));
            device.setLongitude(Double.parseDouble(gpsSplit[1]));
        }
        device.setOutType(out_typeEdit.getText().toString());
        return device;
    }

    @Override
    public void onClick(View view) {
        presenter.fireNewDevice(fieldToDevice());
    }
}
;