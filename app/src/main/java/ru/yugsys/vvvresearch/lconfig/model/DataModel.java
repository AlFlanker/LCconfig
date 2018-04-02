package ru.yugsys.vvvresearch.lconfig.model;


import android.location.Location;
import android.util.Log;
import org.greenrobot.greendao.query.Query;
import ru.yugsys.vvvresearch.lconfig.Logger;
import ru.yugsys.vvvresearch.lconfig.Services.GPScallback;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.DaoSession;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.DeviceDao;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.MainDeviceDao;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.Device;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.MainDevice;
import ru.yugsys.vvvresearch.lconfig.model.Interfaces.Model;
import ru.yugsys.vvvresearch.lconfig.model.Manager.EventManager;

import java.util.List;


public class DataModel implements Model, GPScallback<Location> {


    private EventManager eventManager = new EventManager();
    private Location mCurrentLocation;
    // private DataDevice currentDataDevice = new DataDevice();
    private MainDevice currentDevice;
    private Logger log = Logger.getInstance();


    /*------------------------------------------------------------------------*/
    /* Methods and Classes block*/
    @Override
    public void DeleteDevice(long id) {
        if (this.daoSession.getDeviceDao().load(id) != null) {
            this.daoSession.getDeviceDao().deleteByKey(id);
            eventManager.notifyOnDevDataChecked(true);
        } else eventManager.notifyOnDevDataChecked(false);
    }

    @Override
    public void setCurrentDevice(MainDevice dev) {
        this.currentDevice = dev;
    }

    @Override
    public MainDevice getCurrentDevice() {
        return this.currentDevice;
    }

    /*-----------------------------------------------------------------------*/
    public DaoSession daoSession;

    @Override
    public void writeAuthData(String login, String password, String server) {

    }

    @Override
    public boolean testLoginConnection(String login, String password, String server) {
        return true;

    }

    @Override
    public void getGPSLocation() {
        eventManager.notifyOnGPS(mCurrentLocation);
    }

    @Override
    public void saveDevice(MainDevice device) {
        Log.d("BD", "datamodel -> saveDevice ->" + device.getType());
        MainDeviceDao dataDao = this.daoSession.getMainDeviceDao();
        Log.d("BD", "device.type = " + device.getType());
        dataDao.insert(device);
            eventManager.notifyOnDevDataChecked(true);
        }


    public DataModel(DaoSession daoSession) {
        this.daoSession = daoSession;

    }


    @Override
    public EventManager getEventManager() {
        return eventManager;
    }


    @Override
    public void setSession(DaoSession s) {
        this.daoSession = s;
    }

    /**
     * загрузка объектов Device из БД в List
     */
    @Override
    public void loadAllDeviceData() {
        MainDeviceDao dataDao = this.daoSession.getMainDeviceDao();
        Query<MainDevice> queue = dataDao.queryBuilder().orderAsc(MainDeviceDao.Properties.Id).build();
        eventManager.notifyOnDataReceive(queue.list());
    }

    @Override
    public void loadAllDeviceDataByProperties(Properties property, Direction direction) {
        MainDeviceDao dataDao = this.daoSession.getMainDeviceDao();
        switch (property) {
            case Id:
                if (direction == Direction.Straight) {
                    Query<MainDevice> queue = dataDao.queryBuilder().orderAsc(MainDeviceDao.Properties.Id).build();
                    eventManager.notifyOnDataReceive(queue.list());
                } else {
                    Query<MainDevice> queue = dataDao.queryBuilder().orderDesc(MainDeviceDao.Properties.Id).build();
                    eventManager.notifyOnDataReceive(queue.list());
                }
                break;
            case Type:
                if (direction == Direction.Straight) {
                    Query<MainDevice> queue = dataDao.queryBuilder().orderAsc(MainDeviceDao.Properties.Type).build();
                    eventManager.notifyOnDataReceive(queue.list());
                } else {
                    Query<MainDevice> queue = dataDao.queryBuilder().orderDesc(MainDeviceDao.Properties.Type).build();
                    eventManager.notifyOnDataReceive(queue.list());
                }
                break;
            case isOTTA:
                if (direction == Direction.Straight) {
                    Query<MainDevice> queue = dataDao.queryBuilder().orderAsc(MainDeviceDao.Properties.IsOTTA).build();
                    eventManager.notifyOnDataReceive(queue.list());
                } else {
                    Query<MainDevice> queue = dataDao.queryBuilder().orderDesc(MainDeviceDao.Properties.IsOTTA).build();
                    eventManager.notifyOnDataReceive(queue.list());
                }
                break;
            case EUI:
                if (direction == Direction.Straight) {
                    Query<MainDevice> queue = dataDao.queryBuilder().orderAsc(MainDeviceDao.Properties.Eui).build();
                    eventManager.notifyOnDataReceive(queue.list());
                } else {
                    Query<MainDevice> queue = dataDao.queryBuilder().orderDesc(MainDeviceDao.Properties.Eui).build();
                    eventManager.notifyOnDataReceive(queue.list());
                }
                break;
            case APPEUI:
                if (direction == Direction.Straight) {
                    Query<MainDevice> queue = dataDao.queryBuilder().orderAsc(MainDeviceDao.Properties.Appeui).build();
                    eventManager.notifyOnDataReceive(queue.list());
                } else {
                    Query<MainDevice> queue = dataDao.queryBuilder().orderDesc(MainDeviceDao.Properties.Appeui).build();
                    eventManager.notifyOnDataReceive(queue.list());
                }
                break;
            case APPKEY:
                if (direction == Direction.Straight) {
                    Query<MainDevice> queue = dataDao.queryBuilder().orderAsc(MainDeviceDao.Properties.Appkey).build();
                    eventManager.notifyOnDataReceive(queue.list());
                } else {
                    Query<MainDevice> queue = dataDao.queryBuilder().orderDesc(MainDeviceDao.Properties.Appkey).build();
                    eventManager.notifyOnDataReceive(queue.list());
                }
                break;
            case NWKID:
                if (direction == Direction.Straight) {
                    Query<MainDevice> queue = dataDao.queryBuilder().orderAsc(MainDeviceDao.Properties.Nwkid).build();
                    eventManager.notifyOnDataReceive(queue.list());
                } else {
                    Query<MainDevice> queue = dataDao.queryBuilder().orderDesc(MainDeviceDao.Properties.Nwkid).build();
                    eventManager.notifyOnDataReceive(queue.list());
                }
                break;
            case DEVADR:
                if (direction == Direction.Straight) {
                    Query<MainDevice> queue = dataDao.queryBuilder().orderAsc(MainDeviceDao.Properties.Devadr).build();
                    eventManager.notifyOnDataReceive(queue.list());
                } else {
                    Query<MainDevice> queue = dataDao.queryBuilder().orderDesc(MainDeviceDao.Properties.Devadr).build();
                    eventManager.notifyOnDataReceive(queue.list());
                }
                break;
            case NWKSKEY:
                if (direction == Direction.Straight) {
                    Query<MainDevice> queue = dataDao.queryBuilder().orderAsc(MainDeviceDao.Properties.Nwkskey).build();
                    eventManager.notifyOnDataReceive(queue.list());
                } else {
                    Query<MainDevice> queue = dataDao.queryBuilder().orderDesc(MainDeviceDao.Properties.Nwkskey).build();
                    eventManager.notifyOnDataReceive(queue.list());
                }
                break;
            case APPSKEY:
                if (direction == Direction.Straight) {
                    Query<MainDevice> queue = dataDao.queryBuilder().orderAsc(MainDeviceDao.Properties.Appskey).build();
                    eventManager.notifyOnDataReceive(queue.list());
                } else {
                    Query<MainDevice> queue = dataDao.queryBuilder().orderDesc(MainDeviceDao.Properties.Appskey).build();
                    eventManager.notifyOnDataReceive(queue.list());
                }
                break;
            case OUTTYPE:
                if (direction == Direction.Straight) {
                    Query<MainDevice> queue = dataDao.queryBuilder().orderAsc(MainDeviceDao.Properties.OutType).build();
                    eventManager.notifyOnDataReceive(queue.list());
                } else {
                    Query<MainDevice> queue = dataDao.queryBuilder().orderDesc(MainDeviceDao.Properties.OutType).build();
                    eventManager.notifyOnDataReceive(queue.list());
                }
                break;
        }
    }


    @Override
    public void OnGPSdata(Location location) {
        this.mCurrentLocation = location;
        log.d("GPS", "In Model: " + mCurrentLocation.toString());
        //eventManager.notifyOnGPS(mCurrentLocation);
    }
}
