package ru.yugsys.vvvresearch.lconfig.presenters.Presentable;

import ru.yugsys.vvvresearch.lconfig.model.DaoSession;
import ru.yugsys.vvvresearch.lconfig.model.Dev_Data;
import ru.yugsys.vvvresearch.lconfig.model.Interfaces.ModelListener;

import java.util.List;

public interface BasePresent extends ModelListener<Dev_Data> {
    @Override
    void OnDataRecived(List<Dev_Data> devList);

    @Override
    void OnNFCconnected(boolean flag);

    @Override
    void OnCheckedDevData(boolean check);

    void updateView();

    void AddDev(String n, String g);

    void loadData();

    void getSession(DaoSession s);
}
