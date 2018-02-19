package ru.yugsys.vvvresearch.lconfig.model;

import org.greenrobot.greendao.query.Query;
import ru.yugsys.vvvresearch.lconfig.model.Interfaces.BaseModel;
import ru.yugsys.vvvresearch.lconfig.model.Manager.EventManager;

import java.util.Collections;
import java.util.List;
import java.util.Queue;

public class DataModel implements BaseModel<Dev_Data> {
    public EventManager eventManager = new EventManager();
    @Override
    public void save(Dev_Data dev_data) {
        Dev_DataDao dataDao = this.daoSession.getDev_DataDao();
        dataDao.insert(dev_data);
        eventManager.notify(EventManager.TypeEvent.OnDevDataChecked, false, true, Collections.emptyList());
    }

    public DataModel(DaoSession daoSession) {
        this.daoSession = daoSession;
    }

    public DaoSession daoSession;

    @Override
    public void setSession(DaoSession s) {
        this.daoSession = s;
    }

    @Override
    public void load() {
        Dev_DataDao dataDao = this.daoSession.getDev_DataDao();
        Query<Dev_Data> queue = dataDao.queryBuilder().orderAsc(Dev_DataDao.Properties.Id).build();
        eventManager.notify(EventManager.TypeEvent.OnDataReceive, false, false, queue.list());


    }
}
