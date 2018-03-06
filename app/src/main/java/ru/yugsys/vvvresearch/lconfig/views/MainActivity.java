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
import android.view.*;
import android.widget.TextView;
import ru.yugsys.vvvresearch.lconfig.App;
import ru.yugsys.vvvresearch.lconfig.R;
import ru.yugsys.vvvresearch.lconfig.Services.GPSTracker;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.Device;
import ru.yugsys.vvvresearch.lconfig.presenters.MainPresentable;
import ru.yugsys.vvvresearch.lconfig.presenters.MainPresenter;

import java.lang.reflect.Field;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainViewable, View.OnClickListener {
    private ContentAdapter adapter;
    private RecyclerView recyclerView;
    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        recyclerView = findViewById(R.id.lc5_recycler_view);
        adapter = new ContentAdapter(recyclerView.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MainPresentable mainPresenter = new MainPresenter(((App) getApplication()).getModel());
        mainPresenter.bind(this);
        for (int i = 0; i < 10; i++) {
            Device d = new Device();
            for (Field field :
                    Device.class.getFields()) {
                try {
                    if (field.getType() == String.class) {

                        field.set(d, " Somthing");

                    } else if (field.getType() == double.class) {
                        field.set(d, 2.3);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            //((App) getApplication()).getModel().saveDevice(d);
        }
        GPSTracker gpsTracker = GPSTracker.instance();
        gpsTracker.setContext(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_CODE);
        }
        gpsTracker.OnStartGPS();


        mainPresenter.fireUpdateDataForView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView typeOfLC5;
        public TextView isOTAA;
        public TextView devEUI;
        public TextView appEUI;
        public TextView appKey;
        public TextView nwkID;
        public TextView devAdr;
        public TextView nwkSKey;
        public TextView appSKey;
        public TextView gps;
        public TextView outType;
        ;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.lc5_item_list, parent, false));
            typeOfLC5 = itemView.findViewById(R.id.lc5_type);
            isOTAA = itemView.findViewById(R.id.lc5_isOTAA);
            devEUI = itemView.findViewById(R.id.lc5_deveui);
            appEUI = itemView.findViewById(R.id.lc5_appEUI);
            appKey = itemView.findViewById(R.id.lc5_appKey);
            nwkID = itemView.findViewById(R.id.lc5_nwkID);
            devAdr = itemView.findViewById(R.id.lc5_devAdr);
            nwkSKey = itemView.findViewById(R.id.lc5_nwkSKey);
            appSKey = itemView.findViewById(R.id.lc5_appSKey);
            gps = itemView.findViewById(R.id.lc5_gps);
            outType = itemView.findViewById(R.id.lc5_out_type);
        }
    }

    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        List<Device> devices;

        public ContentAdapter(Context context) {

        }

        public void setDevices(List<Device> devices) {
            this.devices = devices;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.typeOfLC5.setText(devices.get(position).type);
            //holder.isOTAA.setText(devices.get(position).);
            holder.devEUI.setText(devices.get(position).getEui());
            holder.appEUI.setText(devices.get(position).appeui);
            holder.appKey.setText(devices.get(position).appkey);
            holder.nwkID.setText(devices.get(position).nwkid);
            holder.devAdr.setText(devices.get(position).devadr);
            holder.nwkSKey.setText(devices.get(position).nwkskey);
            holder.appSKey.setText(devices.get(position).appskey);
            holder.gps.setText(devices.get(position).Latitude + ", " + devices.get(position).Longitude);
            holder.outType.setText(devices.get(position).outType);
        }

        @Override
        public int getItemCount() {
            return devices.size();
        }
    }

}
