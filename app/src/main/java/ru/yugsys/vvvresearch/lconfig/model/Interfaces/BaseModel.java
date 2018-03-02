package ru.yugsys.vvvresearch.lconfig.model.Interfaces;


import android.location.Location;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.DaoSession;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DataDevice;


public interface BaseModel<T> {
    void setSession(DaoSession s);

    void load();

    void save(T t);

    void readNfcDev();

    void setCurrentDataDevice(DataDevice d);
    //void stopGPS();


}
