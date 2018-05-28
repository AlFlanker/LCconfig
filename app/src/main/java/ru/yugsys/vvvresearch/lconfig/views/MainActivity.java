package ru.yugsys.vvvresearch.lconfig.views;

import android.Manifest;
import android.app.PendingIntent;
import android.content.*;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import android.widget.Toast;
import ru.yugsys.vvvresearch.lconfig.App;
import ru.yugsys.vvvresearch.lconfig.Logger;
import ru.yugsys.vvvresearch.lconfig.R;
import ru.yugsys.vvvresearch.lconfig.Services.*;
import ru.yugsys.vvvresearch.lconfig.Services.RequestsManager.CheckRequest;
import ru.yugsys.vvvresearch.lconfig.Services.RequestsManager.ExternalRequestsReceiver;
import ru.yugsys.vvvresearch.lconfig.Services.RequestsManager.RequestMaster;
import ru.yugsys.vvvresearch.lconfig.Services.RequestsManager.Strategy.REST;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.DeviceEntryDao;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DataDevice;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DeviceEntry;
import ru.yugsys.vvvresearch.lconfig.model.Interfaces.Model;
import ru.yugsys.vvvresearch.lconfig.model.Interfaces.ModelListener;
import java.util.List;


public class MainActivity extends AppCompatActivity implements MainViewable,
        View.OnClickListener,
        ModelListener.OnNFCConnected,
        AsyncTaskCallBack.ReadCallBack,
        ModelListener.OnDataRecived,
        DetectInternetConnection.ConnectivityReceiverListener {


    public static final String ADD_NEW_DEVICE_MODE = "AddNewDeviceMode";
    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;
    private DataDevice currentDataDevice;
    private byte[] systemInfo;
    private IntentFilter intentFilter;
    private DetectInternetConnection detectInternetConnection;
    Logger log = Logger.getInstance();
    private MainContentAdapter adapter;
    private RecyclerView recyclerView;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int ERROR = 0;
    private static final int MESSAGE = 1;
    private ExternalRequestsReceiver externalRequestsReceiver;
    private ProgressBar progressBar;
    private CheckRequest checkRequest;
    private boolean iCheck;
    private BroadcastReceiver listener;
    private int numberOfUnregistered = 0;
    public static String responseFromIS = "ru.yugsys.vvvresearch.lconfig.MainActivity";

    @Override
    protected void onPause() {
        super.onPause();
        if (mAdapter != null) {
            mAdapter.disableForegroundDispatch(this);
        }
        if (externalRequestsReceiver.jobScheduler != null) {
            externalRequestsReceiver.jobScheduler.cancelAll();
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
            if (!DataDevice.DecodeSystemInfoResponse(systemInfo, currentDataDevice)) {
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
        progressBar = findViewById(R.id.MainProgressBar);
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        // WithoutPresenter
        App.getInstance().getModel().getEventManager().subscribeOnDataRecive(this);
//        App.getInstance().getModel().loadAllDeviceDataByProperties(Model.Properties.DateOfChange, Model.Direction.Reverse);
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
        getPremissionGPS();
        /*Apps targeting Android 7.0 (API level 24) and higher
         do not receive CONNECTIVITY_ACTION broadcasts if they declare the broadcast receiver in their manifest.
         Apps will still receive CONNECTIVITY_ACTION broadcasts if they register their BroadcastReceiver
          with Context.registerReceiver() and that context is still valid
          */
        intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        detectInternetConnection = new DetectInternetConnection();
        registerReceiver(detectInternetConnection, intentFilter);


        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(ExternalRequestsReceiver.ACTION);
        externalRequestsReceiver = new ExternalRequestsReceiver();
        registerReceiver(externalRequestsReceiver, iFilter);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CheckRequest.ACTION);
        checkRequest = new CheckRequest();
        registerReceiver(checkRequest, intentFilter);


        listener = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(responseFromIS)) {
                    String alias = intent.getStringExtra("alias");
                    String eui = intent.getStringExtra("eui");
                    Toast.makeText(getApplicationContext(), "Synchonize Device: " + alias + "\n" + "EUI: " + eui, Toast.LENGTH_SHORT).show();
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(responseFromIS);
        registerReceiver(listener, filter);

        /*!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/


        App.getInstance().BindConnectivityListener(this);
        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        if (mAdapter != null) {
            mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists);
        }
        recyclerView.scrollToPosition(0);
        ((App) getApplication()).getModel().loadAllDeviceDataByProperties(Model.Properties.DateOfChange, Model.Direction.Reverse);
       recyclerView.getRootView().startAnimation(AnimationUtils.loadAnimation(recyclerView.getContext(),R.anim.push_elem));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            GPSTracker gpsTracker = GPSTracker.instance();
            gpsTracker.setContext(this);
            gpsTracker.OnStartGPS();
        }


        listener = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(responseFromIS)) {
                    String alias = intent.getStringExtra("alias");
                    String eui = intent.getStringExtra("eui");
                    Toast.makeText(getApplicationContext(), "Synchonize Device: " + alias + "\n" + "EUI: " + eui, Toast.LENGTH_SHORT).show();
                }
            }
        };



    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    private void getPremissionGPS() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_CODE);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_NETWORK_STATE},
                    PERMISSION_REQUEST_CODE);
        }


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET},
                    PERMISSION_REQUEST_CODE);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.NFC) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.NFC},
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
            List<DeviceEntry> list = ((App) getApplication()).getDaoSession().getDeviceEntryDao().queryBuilder().where(DeviceEntryDao.Properties.IsSyncServer.eq(true)).build().list();
            if (list != null) {
                for (DeviceEntry dev : list) {
                    dev.setIsSyncServer(false);
                    ((App) getApplication()).getDaoSession().getDeviceEntryDao().update(dev);
                }
            }
            Toast.makeText(getApplicationContext(), "All devices are not registered! TEST!", Toast.LENGTH_SHORT).show();

//            ((App) getApplication()).getModel().clearDataBase();
        }
        else if(id == R.id.action_CopyDB){
            /*service launch point*/
            Intent intent = new Intent();
            intent.setAction(ExternalRequestsReceiver.ACTION);
            Log.d("Broad", "send: " + ExternalRequestsReceiver.ACTION);
            sendBroadcast(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setContentForView(List<DeviceEntry> devices) {
        adapter.setDevices(devices);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab) {
            Intent addEditIntent = new Intent(this, AddEditActivity.class);
            addEditIntent.putExtra(ADD_NEW_DEVICE_MODE, true);
            startActivity(addEditIntent);
//            showSnack(DetectInternetConnection.isConnected(this));
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
    public void OnDataRecived(List<DeviceEntry> devList) {
        for (DeviceEntry dev : devList) {
            if (!dev.getIsSyncServer()) numberOfUnregistered++;
        }
        this.setContentForView(devList);

    }

    @Override
    public void OnNetworkConnectionChanged(boolean isConnected) {
        if (!isConnected) {
            Toast.makeText(getApplicationContext(), "There is no network connection!\n" +
                    "Synchronization is not possible!", Toast.LENGTH_SHORT).show();
        } else if (isConnected) {
            Toast.makeText(getApplicationContext(), "\n" +
                    "There is a connection to the network!\n" +
                    "Synchronization is possible!", Toast.LENGTH_SHORT).show();
        }
    }


}







