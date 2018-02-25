package ru.yugsys.vvvresearch.lconfig.presenters.Presentable;

import android.location.Location;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.Device;
import ru.yugsys.vvvresearch.lconfig.model.Interfaces.ModelListener;

import java.util.List;

public interface BasePresent extends ModelListener<Device> {
    @Override
    void OnGPSdata(Location location);

    @Override
    void OnDataRecived(List<Device> devList);

    @Override
    void OnNFCconnected(boolean flag);

    @Override
    void OnCheckedDevData(boolean check);

    void updateView();

    void AddDev(String n, String g);

    void loadData();

    //void getSession(DaoSession s);
}
