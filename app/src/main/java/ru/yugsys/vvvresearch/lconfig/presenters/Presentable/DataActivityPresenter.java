package ru.yugsys.vvvresearch.lconfig.presenters.Presentable;

import org.greenrobot.greendao.query.Query;
import ru.yugsys.vvvresearch.lconfig.model.DaoSession;
import ru.yugsys.vvvresearch.lconfig.model.Dev_Data;
import ru.yugsys.vvvresearch.lconfig.model.Dev_DataDao;
import ru.yugsys.vvvresearch.lconfig.model.Interfaces.BaseModel;
import ru.yugsys.vvvresearch.lconfig.views.Interfaces.IEditView;


public class DataActivityPresenter extends ListPresenter<BaseModel, IEditView> {


    private Dev_DataDao mDevDao;
    private Query<Dev_Data> devsQuery;
    public DaoSession daoSession;

    @Override
    public void loadData() {
        devsQuery = model.load();
        updateView();
    }

    public void AddDev(String n, String g) {
        if (n.trim().equals("") || g.trim().equals("")) {
            getView().ShowError();
        }
        Dev_Data dev = new Dev_Data();
        dev.setName(n);
        dev.setGeo(Float.parseFloat(g));
        this.model.save(dev);
        updateView();
    }

    @Override
    public void resetStat() {

    }

    @Override
    public void updateView() {
        getView().update(devsQuery.list());
    }

    @Override
    public void getSession(DaoSession s) {
        this.daoSession = s;

    }
}
