package ru.yugsys.vvvresearch.lconfig.model.Interfaces;


import android.location.Location;

import java.util.List;

public interface ModelListener<T> {

    void OnDataRecived(List<T> devList);

    void OnNFCconnected(boolean flag);

    void OnCheckedDevData(boolean check);

    void OnGPSdata(Location location);


}
