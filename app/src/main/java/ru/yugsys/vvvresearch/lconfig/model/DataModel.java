package ru.yugsys.vvvresearch.lconfig.model;

import android.app.Application;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import org.greenrobot.greendao.query.Query;
import ru.yugsys.vvvresearch.lconfig.App;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.DaoSession;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.DeviceDao;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.Device;
import ru.yugsys.vvvresearch.lconfig.model.Interfaces.BaseModel;
import ru.yugsys.vvvresearch.lconfig.model.Manager.EventManager;

import java.util.Collections;
import java.util.List;
import java.util.Queue;

public class DataModel implements BaseModel<Device> {




    public EventManager eventManager = new EventManager();


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
}
