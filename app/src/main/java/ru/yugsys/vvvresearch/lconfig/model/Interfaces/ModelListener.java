package ru.yugsys.vvvresearch.lconfig.model.Interfaces;


import android.location.Location;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.Device;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.MDevice;

import java.util.List;

public interface ModelListener {

    interface OnGPSdata {
        void OnGPSdata(Location location);
    }

    interface OnDataRecived {
        void OnDataRecived(List<MDevice> devList);
    }

    interface OnNFCConnected {
        void OnNFCConnected(MDevice dev);
    }

    interface OnCheckedDevData {
        void OnCheckedDevData(boolean check);
    }

    interface OnDeviceDeleted {
        void OnDeviceDeleted(boolean flag);
    }


}
