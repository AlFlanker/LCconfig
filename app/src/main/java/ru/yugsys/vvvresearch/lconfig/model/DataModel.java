package ru.yugsys.vvvresearch.lconfig.model;


import android.location.Location;
import android.util.Log;
import org.greenrobot.greendao.query.Query;
import ru.yugsys.vvvresearch.lconfig.Logger;
import ru.yugsys.vvvresearch.lconfig.Services.GPScallback;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.DaoSession;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.DeviceDao;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.MDeviceDao;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.Device;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.MDevice;
import ru.yugsys.vvvresearch.lconfig.model.Interfaces.Model;
import ru.yugsys.vvvresearch.lconfig.model.Manager.EventManager;


public class DataModel implements Model, GPScallback<Location> {


    private EventManager eventManager = new EventManager();
    private Location mCurrentLocation;
    // private DataDevice currentDataDevice = new DataDevice();
    private MDevice currentDevice;
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
    public void setCurrentDevice(MDevice dev) {
        this.currentDevice = dev;
    }

    @Override
    public MDevice getCurrentDevice() {
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
    public void saveDevice(MDevice device) {
        Log.d("BD", "datamodel -> saveDevice ->" + device.getType());
        MDeviceDao dataDao = this.daoSession.getMDeviceDao();
        Log.d("BD", "device.type = " + device.getType());
        MDevice devFromDB;
        devFromDB = dataDao.queryBuilder().where(MDeviceDao.Properties.Eui.eq(device.getEui())).build().unique();
        if (devFromDB == null) {
            dataDao.insert(device);
            eventManager.notifyOnDevDataChecked(true);
        } else {
//            devFromDB.copyFields(device);
            device.setId(devFromDB.getId());
            dataDao.update(device);
            eventManager.notifyOnDevDataChecked(true);
        }
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
        MDeviceDao dataDao = this.daoSession.getMDeviceDao();
        Query<MDevice> queue = dataDao.queryBuilder().orderAsc(MDeviceDao.Properties.Id).build();
        eventManager.notifyOnDataReceive(queue.list());
    }

    @Override
    public void loadAllDeviceDataByProperties(Properties property, Direction direction) {
        MDeviceDao dataDao = this.daoSession.getMDeviceDao();
        switch (property) {
            case Id:
                if (direction == Direction.Straight) {
                    Query<MDevice> queue = dataDao.queryBuilder().orderAsc(MDeviceDao.Properties.Id).build();
                    eventManager.notifyOnDataReceive(queue.list());
                } else {
                    Query<MDevice> queue = dataDao.queryBuilder().orderDesc(MDeviceDao.Properties.Id).build();
                    eventManager.notifyOnDataReceive(queue.list());
                }
                break;
            case Type:
                if (direction == Direction.Straight) {
                    Query<MDevice> queue = dataDao.queryBuilder().orderAsc(MDeviceDao.Properties.Type).build();
                    eventManager.notifyOnDataReceive(queue.list());
                } else {
                    Query<MDevice> queue = dataDao.queryBuilder().orderDesc(MDeviceDao.Properties.Type).build();
                    eventManager.notifyOnDataReceive(queue.list());
                }
                break;
            case isOTTA:
                if (direction == Direction.Straight) {
                    Query<MDevice> queue = dataDao.queryBuilder().orderAsc(MDeviceDao.Properties.IsOTTA).build();
                    eventManager.notifyOnDataReceive(queue.list());
                } else {
                    Query<MDevice> queue = dataDao.queryBuilder().orderDesc(MDeviceDao.Properties.IsOTTA).build();
                    eventManager.notifyOnDataReceive(queue.list());
                }
                break;
            case EUI:
                if (direction == Direction.Straight) {
                    Query<MDevice> queue = dataDao.queryBuilder().orderAsc(MDeviceDao.Properties.Eui).build();
                    eventManager.notifyOnDataReceive(queue.list());
                } else {
                    Query<MDevice> queue = dataDao.queryBuilder().orderDesc(MDeviceDao.Properties.Eui).build();
                    eventManager.notifyOnDataReceive(queue.list());
                }
                break;
            case APPEUI:
                if (direction == Direction.Straight) {
                    Query<MDevice> queue = dataDao.queryBuilder().orderAsc(MDeviceDao.Properties.Appeui).build();
                    eventManager.notifyOnDataReceive(queue.list());
                } else {
                    Query<MDevice> queue = dataDao.queryBuilder().orderDesc(MDeviceDao.Properties.Appeui).build();
                    eventManager.notifyOnDataReceive(queue.list());
                }
                break;
            case APPKEY:
                if (direction == Direction.Straight) {
                    Query<MDevice> queue = dataDao.queryBuilder().orderAsc(MDeviceDao.Properties.Appkey).build();
                    eventManager.notifyOnDataReceive(queue.list());
                } else {
                    Query<MDevice> queue = dataDao.queryBuilder().orderDesc(MDeviceDao.Properties.Appkey).build();
                    eventManager.notifyOnDataReceive(queue.list());
                }
                break;
            case NWKID:
                if (direction == Direction.Straight) {
                    Query<MDevice> queue = dataDao.queryBuilder().orderAsc(MDeviceDao.Properties.Nwkid).build();
                    eventManager.notifyOnDataReceive(queue.list());
                } else {
                    Query<MDevice> queue = dataDao.queryBuilder().orderDesc(MDeviceDao.Properties.Nwkid).build();
                    eventManager.notifyOnDataReceive(queue.list());
                }
                break;
            case DEVADR:
                if (direction == Direction.Straight) {
                    Query<MDevice> queue = dataDao.queryBuilder().orderAsc(MDeviceDao.Properties.Devadr).build();
                    eventManager.notifyOnDataReceive(queue.list());
                } else {
                    Query<MDevice> queue = dataDao.queryBuilder().orderDesc(MDeviceDao.Properties.Devadr).build();
                    eventManager.notifyOnDataReceive(queue.list());
                }
                break;
            case NWKSKEY:
                if (direction == Direction.Straight) {
                    Query<MDevice> queue = dataDao.queryBuilder().orderAsc(MDeviceDao.Properties.Nwkskey).build();
                    eventManager.notifyOnDataReceive(queue.list());
                } else {
                    Query<MDevice> queue = dataDao.queryBuilder().orderDesc(MDeviceDao.Properties.Nwkskey).build();
                    eventManager.notifyOnDataReceive(queue.list());
                }
                break;
            case APPSKEY:
                if (direction == Direction.Straight) {
                    Query<MDevice> queue = dataDao.queryBuilder().orderAsc(MDeviceDao.Properties.Appskey).build();
                    eventManager.notifyOnDataReceive(queue.list());
                } else {
                    Query<MDevice> queue = dataDao.queryBuilder().orderDesc(MDeviceDao.Properties.Appskey).build();
                    eventManager.notifyOnDataReceive(queue.list());
                }
                break;
            case OUTTYPE:
                if (direction == Direction.Straight) {
                    Query<MDevice> queue = dataDao.queryBuilder().orderAsc(MDeviceDao.Properties.OutType).build();
                    eventManager.notifyOnDataReceive(queue.list());
                } else {
                    Query<MDevice> queue = dataDao.queryBuilder().orderDesc(MDeviceDao.Properties.OutType).build();
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
