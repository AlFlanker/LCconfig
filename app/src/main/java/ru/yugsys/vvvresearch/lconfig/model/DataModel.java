package ru.yugsys.vvvresearch.lconfig.model;


import android.location.Location;
import android.util.Log;
import org.greenrobot.greendao.query.Query;
import ru.yugsys.vvvresearch.lconfig.Logger;
import ru.yugsys.vvvresearch.lconfig.Services.GPS.GPScallback;
import ru.yugsys.vvvresearch.lconfig.Services.RequestsManager.CheckRequest;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.DaoSession;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.DeviceEntryDao;

import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DeviceEntry;

import ru.yugsys.vvvresearch.lconfig.model.Interfaces.Model;
import ru.yugsys.vvvresearch.lconfig.model.Manager.EventManager;

import java.util.Date;


public class DataModel implements Model, GPScallback<Location>, CheckRequest.CheckRequestListener {


    private EventManager eventManager = new EventManager();
    private Location mCurrentLocation;
    private DeviceEntry currentDevice;
    private Logger log = Logger.getInstance();


    /*------------------------------------------------------------------------*/
    /* Methods and Classes block*/
    @Override
    public void loadDeviceByEUI(String EUI) {
        DeviceEntry dev = this.daoSession.getDeviceEntryDao().queryBuilder().where(DeviceEntryDao.Properties.Eui.eq(EUI)).build().unique();
    }

    @Override
    public void clearDataBase() {
        this.daoSession.getDeviceEntryDao().deleteAll();
        loadAllDeviceData(); // for update
    }

    @Override
    public void DeleteDevice(long id) {
        if (this.daoSession.getDeviceEntryDao().load(id) != null) {
            this.daoSession.getDeviceEntryDao().deleteByKey(id);
            eventManager.notifyOnDevDataChecked(true);
        } else eventManager.notifyOnDevDataChecked(false);
    }


    @Override
    public void setCurrentDevice(DeviceEntry dev) {
        this.currentDevice = dev;
    }

    @Override
    public DeviceEntry getCurrentDevice() {
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
    public void saveDevice(DeviceEntry device) {
        Log.d("BD", "datamodel -> saveDevice ->" + device.getType());
        DeviceEntryDao dataDao = this.daoSession.getDeviceEntryDao();
        Log.d("BD", "device.type = " + device.getType());
        DeviceEntry devFromDB;
        devFromDB = dataDao.queryBuilder().where(DeviceEntryDao.Properties.Eui.eq(device.getEui())).build().unique();
        if (devFromDB == null) {
            device.setDateOfLastChange(new Date());
            device.setIsSyncServer(false);
            dataDao.insert(device);
            eventManager.notifyOnDevDataChecked(true);
        } else {
            device.setId(devFromDB.getId());
            device.setIsSyncServer(false);
            device.setDateOfLastChange(new Date());
            dataDao.update(device);
            eventManager.notifyOnDevDataChecked(true);
        }
    }

    @Override
    public void DevSync(long id) {
        DeviceEntry devFromDB;
        DeviceEntryDao dataDao = this.daoSession.getDeviceEntryDao();
        devFromDB = dataDao.queryBuilder().where(DeviceEntryDao.Properties.Id.eq(id)).build().unique();
        if (devFromDB != null) {
            devFromDB.setIsSyncServer(true);
            dataDao.update(devFromDB);
        }
        ;
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
        DeviceEntryDao dataDao = this.daoSession.getDeviceEntryDao();
        Query<DeviceEntry> queue = dataDao.queryBuilder().orderAsc(DeviceEntryDao.Properties.Id).build();
        eventManager.notifyOnDataReceive(queue.list());
    }

    @Override
    public void loadAllDeviceDataByProperties(Properties property, Direction direction) {
        DeviceEntryDao dataDao = this.daoSession.getDeviceEntryDao();
        switch (property) {
            case Id:
                if (direction == Direction.Straight) {
                    Query<DeviceEntry> queue = dataDao.queryBuilder().orderAsc(DeviceEntryDao.Properties.Id).build();
                    eventManager.notifyOnDataReceive(queue.list());
                } else {
                    Query<DeviceEntry> queue = dataDao.queryBuilder().orderDesc(DeviceEntryDao.Properties.Id).build();
                    eventManager.notifyOnDataReceive(queue.list());
                }
                break;
            case Type:
                if (direction == Direction.Straight) {
                    Query<DeviceEntry> queue = dataDao.queryBuilder().orderAsc(DeviceEntryDao.Properties.Type).build();
                    eventManager.notifyOnDataReceive(queue.list());
                } else {
                    Query<DeviceEntry> queue = dataDao.queryBuilder().orderDesc(DeviceEntryDao.Properties.Type).build();
                    eventManager.notifyOnDataReceive(queue.list());
                }
                break;
            case isOTTA:
                if (direction == Direction.Straight) {
                    Query<DeviceEntry> queue = dataDao.queryBuilder().orderAsc(DeviceEntryDao.Properties.IsOTTA).build();
                    eventManager.notifyOnDataReceive(queue.list());
                } else {
                    Query<DeviceEntry> queue = dataDao.queryBuilder().orderDesc(DeviceEntryDao.Properties.IsOTTA).build();
                    eventManager.notifyOnDataReceive(queue.list());
                }
                break;
            case EUI:
                if (direction == Direction.Straight) {
                    Query<DeviceEntry> queue = dataDao.queryBuilder().orderAsc(DeviceEntryDao.Properties.Eui).build();
                    eventManager.notifyOnDataReceive(queue.list());
                } else {
                    Query<DeviceEntry> queue = dataDao.queryBuilder().orderDesc(DeviceEntryDao.Properties.Eui).build();
                    eventManager.notifyOnDataReceive(queue.list());
                }
                break;
            case APPEUI:
                if (direction == Direction.Straight) {
                    Query<DeviceEntry> queue = dataDao.queryBuilder().orderAsc(DeviceEntryDao.Properties.Appeui).build();
                    eventManager.notifyOnDataReceive(queue.list());
                } else {
                    Query<DeviceEntry> queue = dataDao.queryBuilder().orderDesc(DeviceEntryDao.Properties.Appeui).build();
                    eventManager.notifyOnDataReceive(queue.list());
                }
                break;
            case APPKEY:
                if (direction == Direction.Straight) {
                    Query<DeviceEntry> queue = dataDao.queryBuilder().orderAsc(DeviceEntryDao.Properties.Appkey).build();
                    eventManager.notifyOnDataReceive(queue.list());
                } else {
                    Query<DeviceEntry> queue = dataDao.queryBuilder().orderDesc(DeviceEntryDao.Properties.Appkey).build();
                    eventManager.notifyOnDataReceive(queue.list());
                }
                break;
            case NWKID:
                if (direction == Direction.Straight) {
                    Query<DeviceEntry> queue = dataDao.queryBuilder().orderAsc(DeviceEntryDao.Properties.Nwkid).build();
                    eventManager.notifyOnDataReceive(queue.list());
                } else {
                    Query<DeviceEntry> queue = dataDao.queryBuilder().orderDesc(DeviceEntryDao.Properties.Nwkid).build();
                    eventManager.notifyOnDataReceive(queue.list());
                }
                break;
            case DEVADR:
                if (direction == Direction.Straight) {
                    Query<DeviceEntry> queue = dataDao.queryBuilder().orderAsc(DeviceEntryDao.Properties.Devadr).build();
                    eventManager.notifyOnDataReceive(queue.list());
                } else {
                    Query<DeviceEntry> queue = dataDao.queryBuilder().orderDesc(DeviceEntryDao.Properties.Devadr).build();
                    eventManager.notifyOnDataReceive(queue.list());
                }
                break;
            case NWKSKEY:
                if (direction == Direction.Straight) {
                    Query<DeviceEntry> queue = dataDao.queryBuilder().orderAsc(DeviceEntryDao.Properties.Nwkskey).build();
                    eventManager.notifyOnDataReceive(queue.list());
                } else {
                    Query<DeviceEntry> queue = dataDao.queryBuilder().orderDesc(DeviceEntryDao.Properties.Nwkskey).build();
                    eventManager.notifyOnDataReceive(queue.list());
                }
                break;
            case APPSKEY:
                if (direction == Direction.Straight) {
                    Query<DeviceEntry> queue = dataDao.queryBuilder().orderAsc(DeviceEntryDao.Properties.Appskey).build();
                    eventManager.notifyOnDataReceive(queue.list());
                } else {
                    Query<DeviceEntry> queue = dataDao.queryBuilder().orderDesc(DeviceEntryDao.Properties.Appskey).build();
                    eventManager.notifyOnDataReceive(queue.list());
                }
                break;
            case OUTTYPE:
                if (direction == Direction.Straight) {
                    Query<DeviceEntry> queue = dataDao.queryBuilder().orderAsc(DeviceEntryDao.Properties.OutType).build();
                    eventManager.notifyOnDataReceive(queue.list());
                } else {
                    Query<DeviceEntry> queue = dataDao.queryBuilder().orderDesc(DeviceEntryDao.Properties.OutType).build();
                    eventManager.notifyOnDataReceive(queue.list());
                }
                break;
            case DateOfChange:
                if (direction == Direction.Straight) {
                    Query<DeviceEntry> queue = dataDao.queryBuilder().orderAsc(DeviceEntryDao.Properties.DateOfLastChange).build();
                    eventManager.notifyOnDataReceive(queue.list());
                } else {
                    Query<DeviceEntry> queue = dataDao.queryBuilder().orderDesc(DeviceEntryDao.Properties.DateOfLastChange).build();
                    eventManager.notifyOnDataReceive(queue.list());
                }
                break;
        }
    }


    @Override
    public void OnGPSdata(Location location) {
        this.mCurrentLocation = location;
        log.d("GPS", "In Model: " + mCurrentLocation.toString());

        eventManager.notifyOnGPS(mCurrentLocation);
    }

    @Override
    public void checkRequestChanged(String eui) {
        Log.d("onHandleIntent", eui);
        if (eui != null) {
            DeviceEntry dev = this.daoSession.getDeviceEntryDao().queryBuilder().where(DeviceEntryDao.Properties.Eui.eq(eui.toUpperCase())).build().unique();
            if (dev != null) {
                dev.setIsSyncServer(true);
                this.daoSession.getDeviceEntryDao().update(dev);
                Log.d("onHandleIntent", "update sync" + dev.getIsSyncServer());
            }
        }


    }
}
