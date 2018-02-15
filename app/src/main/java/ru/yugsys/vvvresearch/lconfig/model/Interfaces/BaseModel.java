package ru.yugsys.vvvresearch.lconfig.model.Interfaces;

import org.greenrobot.greendao.query.Query;
import ru.yugsys.vvvresearch.lconfig.model.DaoSession;


public interface BaseModel<T> {
    void setSession(DaoSession s);

    Query<T> load();

    void save(T t);

}
