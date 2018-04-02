package ru.yugsys.vvvresearch.lconfig.model.Interfaces;


import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.DaoSession;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DataDevice;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.Device;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.MainDevice;
import ru.yugsys.vvvresearch.lconfig.model.Manager.EventManager;

import java.util.Collections;


public interface Model {
    enum Properties {
        Id, Type, isOTTA, EUI, APPEUI, APPKEY, NWKID, DEVADR, NWKSKEY, APPSKEY, OUTTYPE
    }

    enum Direction {
        Reverse, Straight
    }


    void setCurrentDevice(MainDevice dev);

    MainDevice getCurrentDevice();

    void setSession(DaoSession s);

    void loadAllDeviceData();

    void saveDevice(MainDevice t);


    void writeAuthData(String login, String password, String server);

    boolean testLoginConnection(String login, String password, String server);

    void getGPSLocation();

    EventManager getEventManager();

    void DeleteDevice(long id);

    void loadAllDeviceDataByProperties(Properties property, Direction direction);
    //void stopGPS();


}
