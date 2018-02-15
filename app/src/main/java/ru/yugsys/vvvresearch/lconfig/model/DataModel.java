package ru.yugsys.vvvresearch.lconfig.model;

import org.greenrobot.greendao.query.Query;
import ru.yugsys.vvvresearch.lconfig.model.Interfaces.BaseModel;

import java.util.List;
import java.util.Queue;

public class DataModel implements BaseModel<Dev_Data> {
    @Override
    public void save(Dev_Data dev_data) {
        Dev_DataDao dataDao = this.daoSession.getDev_DataDao();
        dataDao.insert(dev_data);
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
    public Query<Dev_Data> load() {
        Dev_DataDao dataDao = this.daoSession.getDev_DataDao();
        return dataDao.queryBuilder().orderAsc(Dev_DataDao.Properties.Id).build();


    }
}
