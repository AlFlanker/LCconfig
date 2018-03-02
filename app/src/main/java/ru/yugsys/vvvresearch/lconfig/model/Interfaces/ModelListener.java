package ru.yugsys.vvvresearch.lconfig.model.Interfaces;


import android.location.Location;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.Device;

import java.util.List;

public interface ModelListener<T> {

    void OnDataRecived(List<T> devList);

    void OnNFCconnected(Device dev);

    void OnCheckedDevData(boolean check);

    void OnGPSdata(Location location);


}
