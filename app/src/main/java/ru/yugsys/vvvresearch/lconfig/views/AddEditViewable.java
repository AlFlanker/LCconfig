package ru.yugsys.vvvresearch.lconfig.views;


import android.location.Location;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DeviceEntry;

public interface AddEditViewable {
    void setDeviceFields(DeviceEntry device);
    void setLocationFields(Location location);
}
