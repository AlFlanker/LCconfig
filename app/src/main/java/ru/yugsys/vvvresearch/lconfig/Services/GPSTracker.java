package ru.yugsys.vvvresearch.lconfig.Services;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import com.google.android.gms.location.*;

public class GPSTracker {
    /**
     * @autor Alex Flanker
     * @version 1.0
     *
     */
    /**
     * Добавляем контекс activity
     *
     * @param context - Context
     */
    public void setContext(Context context) {
        this.mContext = context;
    }
    private  Context mContext;
    private static final GPSTracker instance = new GPSTracker();
    private static final String TAG = "GPS";
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private LocationSettingsRequest locationSettingsRequest;
    private GPScallback<Location> GPScb;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    public boolean mRequestingLocationUpdates = false;


    private GPSTracker() {
        Log.d(TAG, "GPSt constructor");
        this.locationRequest = new LocationRequest();
        this.locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        this.locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
     //   this.locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        this.locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        // точность +-100 метров
        Log.d(TAG, "GPSt request compl");
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(this.locationRequest);
        this.locationSettingsRequest = builder.build();
        Log.d(TAG, "GPSt callBack");
        this.locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location currentLocation = locationResult.getLastLocation();
                Log.d(TAG, "Tracker: " + currentLocation.toString());
                if (GPScb!=null)
                    GPScb.OnGPSdata(currentLocation);//в мой класс обратка
            }
        };
        Log.d(TAG, "client");

    }
    public void OnStartGPS(){

        // проверка доступа и разрешений ОБЯЗАТЕЛЬНО!!!для андройд выше 6.0
        this.mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "perm fall");


        }
        mRequestingLocationUpdates = true;
        Log.d(TAG, "permission compl");
        this.mFusedLocationClient.requestLocationUpdates(this.locationRequest, this.locationCallback, Looper.myLooper());
    }

    public static GPSTracker instance() {

        return instance;
    }

    /** для callBack
     * @param cb - callBack function
     */
    public void onChange(GPScallback<Location> cb) {
        this.GPScb = cb;
    }

    public LocationSettingsRequest getLocationSettingsRequest() {
        return this.locationSettingsRequest;
    }

    public void stop() {
        Log.d(TAG, "stop() Stopping location tracking");
        mRequestingLocationUpdates = false;
        this.mFusedLocationClient.removeLocationUpdates(this.locationCallback);
    }

    public void OnResumeGPS() {
        this.mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        this.mFusedLocationClient.requestLocationUpdates(this.locationRequest, this.locationCallback, Looper.myLooper());
        mRequestingLocationUpdates = true;
        Log.d("GPS", "OnResume");
    }

}

