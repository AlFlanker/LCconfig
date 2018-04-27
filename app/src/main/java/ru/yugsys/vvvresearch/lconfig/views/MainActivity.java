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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;
import ru.yugsys.vvvresearch.lconfig.App;
import ru.yugsys.vvvresearch.lconfig.Logger;
import ru.yugsys.vvvresearch.lconfig.R;
import ru.yugsys.vvvresearch.lconfig.Services.*;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DataDevice;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DeviceEntry;
import ru.yugsys.vvvresearch.lconfig.model.Interfaces.ModelListener;
import ru.yugsys.vvvresearch.lconfig.presenters.MainPresentable;
import ru.yugsys.vvvresearch.lconfig.presenters.MainPresenter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements MainViewable, View.OnClickListener, ModelListener.OnNFCConnected,AsyncTaskCallBack.ReadCallBack,MainContentAdapter.OnItemClickListener {


    public static final String ADD_NEW_DEVICE_MODE = "AddNewDeviceMode";
    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;
    private DeviceEntry currentDevice;
    private DataDevice currentDataDevice;
    private byte[] systemInfo;
    Logger log = Logger.getInstance();
    private MainContentAdapter adapter;
    private RecyclerView recyclerView;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private MainPresentable mainPresenter;
    private ProgressBar progressBar;
    private View scrollView;

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
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            tagFromIntent = (Tag) intent.getParcelableExtra("android.nfc.extra.TAG");
            currentDataDevice.setCurrentTag(tagFromIntent);
            systemInfo = NFCCommand.SendGetSystemInfoCommandCustom(tagFromIntent, currentDataDevice);
            if (!Util.DecodeSystemInfoResponse(systemInfo, currentDataDevice)) {
                return;
            }
            ReadTask readTask = new ReadTask(null,null);
            readTask.subscribe(this);
            readTask.execute(currentDataDevice);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPremissionGPS();
        MainContentAdapter.onItemClickListener = this;
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);
        recyclerView = findViewById(R.id.lc5_recycler_view);
        scrollView = findViewById(R.id.lc5_scrollViewFull);
        scrollView.setVisibility(View.GONE);
        adapter = new MainContentAdapter(recyclerView.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar = findViewById(R.id.MainProgressBar);
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
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
        recyclerView.scrollToPosition(0);
        mainPresenter.fireUpdateDataForView();
       recyclerView.getRootView().startAnimation(AnimationUtils.loadAnimation(recyclerView.getContext(),R.anim.push_elem));
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
        } else if (id == R.id.action_clearBD) {
            ((App) getApplication()).getModel().clearDataBase();
        }
        else if(id == R.id.action_CopyDB){
            new DataBaseMigrate(((App)getApplication()).getDaoSession()).migrate();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setContentForView(List<DeviceEntry> devices) {
        adapter.setDevices(devices);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void OnCardFullView(DeviceEntry deviceEntry) {
        EditText textView = findViewById(R.id.lc5_edit_deveui);
        EditText lc5_edit_gps_latitude = findViewById(R.id.lc5_edit_gps_latitude);
        EditText lc5_edit_gps_longitude = findViewById(R.id.lc5_edit_gps_longitude);
        EditText lc5_edit_appEUI = findViewById(R.id.lc5_edit_appEUI);
        EditText lc5_edit_appKey = findViewById(R.id.lc5_edit_appKey);
        EditText lc5_edit_nwkID = findViewById(R.id.lc5_edit_nwkID);
        EditText lc5_edit_devAdr = findViewById(R.id.lc5_edit_devAdr);
        EditText lc5_edit_nwkSKey = findViewById(R.id.lc5_edit_nwkSKey);
        EditText lc5_edit_comment = findViewById(R.id.lc5_edit_comment);
        EditText lc5_date = findViewById(R.id.lc5_date1);
        EditText lc5_appskey = findViewById(R.id.lc5_edit_appSKey);

        textView.setText(deviceEntry.getEui());
         lc5_edit_gps_latitude.setText(String.valueOf(deviceEntry.getLatitude()));
         lc5_edit_gps_longitude.setText(String.valueOf(deviceEntry.getLongitude()));
         lc5_edit_appEUI.setText(deviceEntry.getAppeui());
         lc5_edit_appKey.setText(deviceEntry.getAppkey());
         lc5_edit_nwkID.setText(deviceEntry.getNwkid());
         lc5_edit_devAdr.setText(deviceEntry.getDevadrMSBtoLSB());
         lc5_edit_nwkSKey.setText(deviceEntry.getNwkskey());
         lc5_edit_comment.setText(deviceEntry.getComment());
         lc5_date.setText(deviceEntry.getDateOfLastChange().toString());
        lc5_appskey.setText(deviceEntry.getAppskey());


        recyclerView.setVisibility(View.GONE);
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.ef1);

        scrollView.setVisibility(View.VISIBLE);
        scrollView.startAnimation(animation);

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
    public void OnNFCConnected(DeviceEntry dev) {
        log.d("NFC", dev.getType());
    }


    @Override
    public void OnDeviceEntry(DeviceEntry deviceEntry) {
        if(deviceEntry!=null) {
            ((App) getApplication()).getModel().setCurrentDevice(deviceEntry);
            Intent addActivity = new Intent(this, AddEditActivity.class);
            addActivity.putExtra(ADD_NEW_DEVICE_MODE, Boolean.FALSE);
            currentDataDevice = null;
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            startActivity(addActivity);
        }
        else{
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(), getString(R.string.Incorrect), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void OnItemSelected(String eui) {
        mainPresenter.loadDevByEUI(eui);
    }
}
