package ru.yugsys.vvvresearch.lconfig.model.Interfaces;


import android.location.Location;
import ru.yugsys.vvvresearch.lconfig.model.DaoSession;


public interface BaseModel<T> {
    void setSession(DaoSession s);

    void load();

    void save(T t);

    Location getLocation();

}
