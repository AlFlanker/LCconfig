package ru.yugsys.vvvresearch.lconfig.model.Interfaces;


import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.DaoSession;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.MDevice;
import ru.yugsys.vvvresearch.lconfig.model.Manager.EventManager;


public interface Model {
    enum Properties {
        Id, Type, isOTTA, EUI, APPEUI, APPKEY, NWKID, DEVADR, NWKSKEY, APPSKEY, OUTTYPE
    }

    enum Direction {
        Reverse, Straight
    }


    void setCurrentDevice(MDevice dev);

    MDevice getCurrentDevice();

    void setSession(DaoSession s);

    void loadAllDeviceData();

    void saveDevice(MDevice t);

    void loadDeviceByEUI(String EUI);


    void writeAuthData(String login, String password, String server);

    boolean testLoginConnection(String login, String password, String server);

    void getGPSLocation();

    EventManager getEventManager();

    void DeleteDevice(long id);

    void loadAllDeviceDataByProperties(Properties property, Direction direction);
    //void stopGPS();
    void clearDataBase();


}
