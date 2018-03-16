package ru.yugsys.vvvresearch.lconfig.views;


import android.location.Location;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.Device;

public interface AddEditViewable {
    void setDeviceFields(Device device);
    void setLocationFields(Location location);
}
