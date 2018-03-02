package ru.yugsys.vvvresearch.lconfig.model.Interfaces;


import android.location.Location;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.DaoSession;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DataDevice;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.Device;
import ru.yugsys.vvvresearch.lconfig.model.Manager.EventManager;


public interface Model {
    void setSession(DaoSession s);

    void loadAllDeviceData();

    void saveDevice(Device t);

    void readNfcDev();

    void setCurrentDataDevice(DataDevice d);
    void writeAuthData(String login, String password,String server);

    EventManager getEventManager();
    //void stopGPS();


}
