package ru.yugsys.vvvresearch.lconfig.model;

import android.app.Application;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import org.greenrobot.greendao.query.Query;
import ru.yugsys.vvvresearch.lconfig.App;
import ru.yugsys.vvvresearch.lconfig.Services.GPSTracker;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.DaoSession;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.DeviceDao;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.Device;
import ru.yugsys.vvvresearch.lconfig.model.Interfaces.BaseModel;
import ru.yugsys.vvvresearch.lconfig.model.Manager.EventManager;

import java.util.*;

import static android.content.Context.LOCATION_SERVICE;

public class DataModel extends Service implements BaseModel<Device>,LocationListener {
    public DataModel() {

    }

    public EventManager eventManager = new EventManager();
    double latitude;
    double longitude;

    @Override
    public void save(Device device) {
        DeviceDao dataDao = this.daoSession.getDeviceDao();
        dataDao.insert(device);
        eventManager.notify(EventManager.TypeEvent.OnDevDataChecked, false, true, Collections.emptyList(), null);
    }

    public DataModel(DaoSession daoSession) {
        this.daoSession = daoSession;



    }

    public DaoSession daoSession;

    @Override
    public void setSession(DaoSession s) {
        this.daoSession = s;
        Log.d("GPS","inside");
    }

    @Override
    public void load() {
        DeviceDao dataDao = this.daoSession.getDeviceDao();
        Query<Device> queue = dataDao.queryBuilder().orderAsc(DeviceDao.Properties.Id).build();
        eventManager.notify(EventManager.TypeEvent.OnDataReceive, false, false, queue.list(), null);


    }

    @Override
    public void Location() {


    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
