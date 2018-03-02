package ru.yugsys.vvvresearch.lconfig.model.Interfaces;


import android.location.Location;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.DaoSession;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DataDevice;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.Device;


public interface Model {
    void setSession(DaoSession s);

    void load();

    void saveDevice(Device t);

    void readNfcDev();

    void setCurrentDataDevice(DataDevice d);
    //void stopGPS();


}
