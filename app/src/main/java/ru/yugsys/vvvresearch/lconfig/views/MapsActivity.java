package ru.yugsys.vvvresearch.lconfig.views;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.util.Log;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import ru.yugsys.vvvresearch.lconfig.App;
import ru.yugsys.vvvresearch.lconfig.R;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DeviceEntry;
import ru.yugsys.vvvresearch.lconfig.model.Interfaces.Model;
import ru.yugsys.vvvresearch.lconfig.model.Interfaces.ModelListener;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, ModelListener.OnDataRecived {

    public static final String LOGITUDE = "LOGITUDE";
    public static final String LATITUDE = "LATITUDE";
    public static final String COMMENT = "COMMENT";
    public static final String SHOW_MODE_ALL = "SHOW_MODE";
    private GoogleMap mMap;
    private double longitude;
    private double latitude;
    private String comment;
    private boolean show_all;
    private List<DeviceEntry> devices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
//        App.getInstance().getModel().getEventManager().subscribeOnDataRecive(this);
//        ((App) getApplication()).getModel().loadAllDeviceDataByProperties(Model.Properties.DateOfChange, Model.Direction.Reverse);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Intent intent = getIntent();
        longitude = intent.getDoubleExtra(LOGITUDE,45.0);
        latitude = intent.getDoubleExtra(LATITUDE,45.0);
        comment = intent.getStringExtra(COMMENT);
        show_all = intent.getBooleanExtra(SHOW_MODE_ALL, false);
        App.getInstance().getModel().getEventManager().subscribeOnDataRecive(this);
        ((App) getApplication()).getModel().loadAllDeviceDataByProperties(Model.Properties.DateOfChange, Model.Direction.Reverse);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (!show_all) {
            LatLng pos = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(pos).title("".equals(comment) ? "marker" : comment));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(18.0f));
        } else {
            MarkerOptions[] markers = new MarkerOptions[devices.size()];
            for (int i = 0; i < devices.size(); i++) {
                markers[i] = new MarkerOptions()
                        .position(new LatLng(devices.get(i).getLatitude(), devices.get(i).getLongitude()));
                mMap.addMarker(markers[i].title(devices.get(i).getComment()));
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(devices.get(devices.size() - 1).getLatitude(),
                            devices.get(devices.size() - 1).getLongitude()), 15f));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(15.0f));
        }
    }

    @Override
    public void OnDataRecived(List<DeviceEntry> devList) {
        this.devices = devList;
        Log.d("map", "OnDataRecived");
        Log.d("map", "devList.size() = " + devList.size());

    }
}
