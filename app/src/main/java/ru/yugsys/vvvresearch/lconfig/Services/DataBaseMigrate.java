package ru.yugsys.vvvresearch.lconfig.Services;

import android.util.Log;
import org.greenrobot.greendao.query.Query;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.DaoSession;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.DeviceEntryDao;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.MDeviceDao;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DeviceEntry;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.MDevice;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataBaseMigrate {
    private String[] defaultName = new String[]{ "type",
            "isOTTA",
            "eui",
            "appeui",
            "appkey",
            "nwkid",
            "devadr",
            "nwkskey",
            "appskey",
            "Latitude",
            "Longitude",
            "outType",
            "kV",
            "kI"};
    public DataBaseMigrate(DaoSession daoSession) {
        this.daoSession = daoSession;
        Log.d("Migrate","InConstructor");
    }

    private DaoSession daoSession;
    private List<MDevice> load(){
        MDeviceDao dataDao = this.daoSession.getMDeviceDao();
        Log.d("Migrate","load");
        List<MDevice> res = dataDao.queryBuilder().orderAsc(MDeviceDao.Properties.Id).build().list();
        Log.d("Migrate","load" + " - " + res.size());
        return res;
    }
    private DeviceEntry getDeviceEntry(MDevice mdev) throws NoSuchFieldException, IllegalAccessException {
        Field mfield;
        Field sfield;
        DeviceEntry deviceEntry = new DeviceEntry();
        for(String name: defaultName) {
            mfield = DeviceEntry.class.getDeclaredField(name);
            sfield = MDevice.class.getDeclaredField(name);
            mfield.setAccessible(true);
            sfield.setAccessible(true);
            mfield.set(deviceEntry,sfield.get(mdev));
        }
        deviceEntry.setDateOfLastChange(new Date());
        deviceEntry.setIsDeleted(false);
        deviceEntry.setComment("default");
        Log.d("Migrate","getDeviceEntry" + " - " + deviceEntry.getEui());
        return deviceEntry;
    }
    private List<DeviceEntry> convert2DeviceEntry(List<MDevice> dev) throws NoSuchFieldException, IllegalAccessException {
        Field mfield;
        Field sfield;
        ArrayList<DeviceEntry> deviceEntries = new ArrayList<>(dev.size());
        for(MDevice mdev: dev){
            deviceEntries.add(getDeviceEntry(mdev));
        }
        Log.d("Migrate","convert2DeviceEntry - " + deviceEntries.size());
        return deviceEntries;
    }
    private  void saveDevice(DeviceEntry device) {
        DeviceEntryDao dataDao = this.daoSession.getDeviceEntryDao();
        DeviceEntry devFromDB;
        devFromDB = dataDao.queryBuilder().where(DeviceEntryDao.Properties.Eui.eq(device.getEui())).build().unique();
        if (devFromDB == null) {
            dataDao.save(device);
            Log.d("Migrate","saveDevice - " + device.getEui());
        } else {
//
            device.setId(devFromDB.getId());
            Log.d("Migrate","update Device - " + device.getEui());
            dataDao.update(device);
        }
    }
    public void migrate(){
        try {
            List<DeviceEntry> list = convert2DeviceEntry(load());
            for(DeviceEntry deviceEntry: list){
                saveDevice(deviceEntry);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
