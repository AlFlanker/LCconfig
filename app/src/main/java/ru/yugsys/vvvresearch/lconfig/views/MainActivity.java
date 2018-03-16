package ru.yugsys.vvvresearch.lconfig.views;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import ru.yugsys.vvvresearch.lconfig.App;
import ru.yugsys.vvvresearch.lconfig.R;
import ru.yugsys.vvvresearch.lconfig.Services.GPSTracker;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.Device;
import ru.yugsys.vvvresearch.lconfig.presenters.MainPresentable;
import ru.yugsys.vvvresearch.lconfig.presenters.MainPresenter;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MainViewable, View.OnClickListener {
    private MainContentAdapter adapter;
    private RecyclerView recyclerView;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private MainPresentable mainPresenter;

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
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
            //((App) getApplication()).getModel().saveDevice(d);
        }
        GPSTracker gpsTracker = GPSTracker.instance();
        gpsTracker.setContext(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_CODE);
        }
        Log.d("GPS", "Activity gps start");
        gpsTracker.OnStartGPS();
        mainPresenter.fireUpdateDataForView();
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
            startActivity(addEditIntent);
        }
    }


}
