package ru.yugsys.vvvresearch.lconfig.model.Interfaces;


import android.location.Location;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.Device;

import java.util.List;

public interface ModelListener {

    interface OnGPSdata {
        void OnGPSdata(Location location);
    }

    interface OnDataRecived {
        void OnDataRecived(List<Device> devList);
    }

    interface OnNFCConnected {
        void OnNFCConnected(Device dev);
    }

    interface OnCheckedDevData {
        void OnCheckedDevData(boolean check);
    }


}
