package ru.yugsys.vvvresearch.lconfig.model;



import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import org.greenrobot.greendao.query.Query;
import ru.yugsys.vvvresearch.lconfig.Services.GPScallback;
import ru.yugsys.vvvresearch.lconfig.Services.Helper;
import ru.yugsys.vvvresearch.lconfig.Services.NFCCommand;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.DaoSession;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.DeviceDao;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DataDevice;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DataRead;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.Device;
import ru.yugsys.vvvresearch.lconfig.model.Interfaces.Model;
import ru.yugsys.vvvresearch.lconfig.model.Manager.EventManager;


import java.nio.ByteBuffer;
import java.util.*;


public class DataModel implements Model, GPScallback<Location> {


    private EventManager eventManager = new EventManager();
    private Location mCurrentLocation;
    // private DataDevice currentDataDevice = new DataDevice();
    private Device currentDevice;


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
    public void setCurrentDevice(Device dev) {
        this.currentDevice = dev;
    }

    @Override
    public Device getCurrentDevice() {
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
    public void saveDevice(Device device) {
        Log.d("BD","datamodel -> saveDevice ->" + device.type);
        DeviceDao dataDao = this.daoSession.getDeviceDao();
        Log.d("BD","device.type = "+device.type);
        try {
            dataDao.insert(device);
        }
        catch (Exception e){
           Log.d("BD","exception: ",e);
        }
        Log.d("BD","Save into");
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
        DeviceDao dataDao = this.daoSession.getDeviceDao();
        Query<Device> queue = dataDao.queryBuilder().orderAsc(DeviceDao.Properties.Id).build();
        eventManager.notifyOnDataReceive(queue.list());
    }

    @Override
    public void loadAllDeviceDataByProperties(Properties property, Direction direction) {
        DeviceDao dataDao = this.daoSession.getDeviceDao();
        switch (property) {
            case Id:
                if (direction == Direction.Straight) {
                    Query<Device> queue = dataDao.queryBuilder().orderAsc(DeviceDao.Properties.Id).build();
                    eventManager.notifyOnDataReceive(queue.list());
                } else {
                    Query<Device> queue = dataDao.queryBuilder().orderDesc(DeviceDao.Properties.Id).build();
                    eventManager.notifyOnDataReceive(queue.list());
                }
                break;
            case Type:
                if (direction == Direction.Straight) {
                    Query<Device> queue = dataDao.queryBuilder().orderAsc(DeviceDao.Properties.Type).build();
                    eventManager.notifyOnDataReceive(queue.list());
                } else {
                    Query<Device> queue = dataDao.queryBuilder().orderDesc(DeviceDao.Properties.Type).build();
                    eventManager.notifyOnDataReceive(queue.list());
                }
                break;
            case isOTTA:
                if (direction == Direction.Straight) {
                    Query<Device> queue = dataDao.queryBuilder().orderAsc(DeviceDao.Properties.IsOTTA).build();
                    eventManager.notifyOnDataReceive(queue.list());
                } else {
                    Query<Device> queue = dataDao.queryBuilder().orderDesc(DeviceDao.Properties.IsOTTA).build();
                    eventManager.notifyOnDataReceive(queue.list());
                }
                break;
            case EUI:
                if (direction == Direction.Straight) {
                    Query<Device> queue = dataDao.queryBuilder().orderAsc(DeviceDao.Properties.Eui).build();
                    eventManager.notifyOnDataReceive(queue.list());
                } else {
                    Query<Device> queue = dataDao.queryBuilder().orderDesc(DeviceDao.Properties.Eui).build();
                    eventManager.notifyOnDataReceive(queue.list());
                }
                break;
            case APPEUI:
                if (direction == Direction.Straight) {
                    Query<Device> queue = dataDao.queryBuilder().orderAsc(DeviceDao.Properties.Appeui).build();
                    eventManager.notifyOnDataReceive(queue.list());
                } else {
                    Query<Device> queue = dataDao.queryBuilder().orderDesc(DeviceDao.Properties.Appeui).build();
                    eventManager.notifyOnDataReceive(queue.list());
                }
                break;
            case APPKEY:
                if (direction == Direction.Straight) {
                    Query<Device> queue = dataDao.queryBuilder().orderAsc(DeviceDao.Properties.Appkey).build();
                    eventManager.notifyOnDataReceive(queue.list());
                } else {
                    Query<Device> queue = dataDao.queryBuilder().orderDesc(DeviceDao.Properties.Appkey).build();
                    eventManager.notifyOnDataReceive(queue.list());
                }
                break;
            case NWKID:
                if (direction == Direction.Straight) {
                    Query<Device> queue = dataDao.queryBuilder().orderAsc(DeviceDao.Properties.Nwkid).build();
                    eventManager.notifyOnDataReceive(queue.list());
                } else {
                    Query<Device> queue = dataDao.queryBuilder().orderDesc(DeviceDao.Properties.Nwkid).build();
                    eventManager.notifyOnDataReceive(queue.list());
                }
                break;
            case DEVADR:
                if (direction == Direction.Straight) {
                    Query<Device> queue = dataDao.queryBuilder().orderAsc(DeviceDao.Properties.Devadr).build();
                    eventManager.notifyOnDataReceive(queue.list());
                } else {
                    Query<Device> queue = dataDao.queryBuilder().orderDesc(DeviceDao.Properties.Devadr).build();
                    eventManager.notifyOnDataReceive(queue.list());
                }
                break;
            case NWKSKEY:
                if (direction == Direction.Straight) {
                    Query<Device> queue = dataDao.queryBuilder().orderAsc(DeviceDao.Properties.Nwkskey).build();
                    eventManager.notifyOnDataReceive(queue.list());
                } else {
                    Query<Device> queue = dataDao.queryBuilder().orderDesc(DeviceDao.Properties.Nwkskey).build();
                    eventManager.notifyOnDataReceive(queue.list());
                }
                break;
            case APPSKEY:
                if (direction == Direction.Straight) {
                    Query<Device> queue = dataDao.queryBuilder().orderAsc(DeviceDao.Properties.Appskey).build();
                    eventManager.notifyOnDataReceive(queue.list());
                } else {
                    Query<Device> queue = dataDao.queryBuilder().orderDesc(DeviceDao.Properties.Appskey).build();
                    eventManager.notifyOnDataReceive(queue.list());
                }
                break;
            case OUTTYPE:
                if (direction == Direction.Straight) {
                    Query<Device> queue = dataDao.queryBuilder().orderAsc(DeviceDao.Properties.OutType).build();
                    eventManager.notifyOnDataReceive(queue.list());
                } else {
                    Query<Device> queue = dataDao.queryBuilder().orderDesc(DeviceDao.Properties.OutType).build();
                    eventManager.notifyOnDataReceive(queue.list());
                }
                break;
        }
    }


    @Override
    public void OnGPSdata(Location location) {
        this.mCurrentLocation = location;
        Log.d("GPS", "In Model: " + mCurrentLocation.toString());
        //eventManager.notifyOnGPS(mCurrentLocation);
    }
}
