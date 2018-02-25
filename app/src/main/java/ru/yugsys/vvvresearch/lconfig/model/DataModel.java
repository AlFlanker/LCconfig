package ru.yugsys.vvvresearch.lconfig.model;


import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;
import org.greenrobot.greendao.query.Query;
import ru.yugsys.vvvresearch.lconfig.Services.GPScallback;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.DaoSession;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.DeviceDao;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.Device;
import ru.yugsys.vvvresearch.lconfig.model.Interfaces.BaseModel;
import ru.yugsys.vvvresearch.lconfig.model.Manager.EventManager;
import java.util.*;


public class DataModel extends Service implements BaseModel<Device>,GPScallback<Location> {
    public DaoSession daoSession;
    public EventManager eventManager = new EventManager();
    private Location mCurrentLocation;

    @Override
    public void save(Device device) {
        Log.d("BD","datamodel -> save ->" + device.type);
        DeviceDao dataDao = this.daoSession.getDeviceDao();
        Log.d("BD","device.type = "+device.type);
        try {
            dataDao.insert(device);
        }
        catch (Exception e){
           Log.d("BD","exception: ",e);
        }
        Log.d("BD","Save into");
        eventManager.notify(EventManager.TypeEvent.OnDevDataChecked, false, true, Collections.emptyList(), null);
    }

    public DataModel(DaoSession daoSession) {
        this.daoSession = daoSession;

    }


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
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void OnGPSdata(Location location) {
        this.mCurrentLocation = location;
        eventManager.notify(EventManager.TypeEvent.OnGPSdata,false,false, Collections.emptyList(),mCurrentLocation);
    }
}
