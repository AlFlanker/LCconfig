package ru.yugsys.vvvresearch.lconfig.Services;


import android.util.Log;
import org.greenrobot.greendao.query.Query;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.DaoSession;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.DevEntryDao;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.MDeviceDao;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DevEntry;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.MDevice;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UpdateTable {
    public UpdateTable(DaoSession daoSession) {
        this.daoSession = daoSession;
    }

    private DaoSession daoSession;

    private List<MDevice> loadOldTable() {
        MDeviceDao dataDao = this.daoSession.getMDeviceDao();
        Query<MDevice> queue = dataDao.queryBuilder().orderAsc(MDeviceDao.Properties.Id).build();
        return queue.list();
    }

    private List<DevEntry> getDevEntryList(List<MDevice> dev) throws NoSuchFieldException, IllegalAccessException {
        Field field;
        Field devEntryFiled;
        ArrayList<DevEntry> newDev = new ArrayList<>(dev.size());
        String[] names = new String[]{
                "type",
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
                "kI"
        };
        for (MDevice mDevice : dev) {
            DevEntry de = new DevEntry();
            for (String name : names) {
                devEntryFiled = DevEntry.class.getDeclaredField(name);
                devEntryFiled.setAccessible(true);
                field = MDevice.class.getDeclaredField(name);
                field.setAccessible(true);
                devEntryFiled.set(de, field.get(mDevice));
            }
            de.setDateOfLastChange(new Date());
            de.setIsDeleted(false);
            de.setComment("");
            newDev.add(de);
        }
        return newDev;
    }

    public void saveInNewTable() {
        DevEntryDao dataDao = this.daoSession.getDevEntryDao();
        try {
            List<DevEntry> data = getDevEntryList(loadOldTable());
            for (DevEntry d : data)
                dataDao.save(d);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        List<DevEntry> list = dataDao.loadAll();
        Log.d("DB", "sizeof DB = " + list.size());
    }
}
