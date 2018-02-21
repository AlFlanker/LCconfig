package ru.yugsys.vvvresearch.lconfig.model.Interfaces;


import android.location.Location;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.DaoSession;


public interface BaseModel<T> {
    void setSession(DaoSession s);

    void load();

    void save(T t);

    void Location();


}
