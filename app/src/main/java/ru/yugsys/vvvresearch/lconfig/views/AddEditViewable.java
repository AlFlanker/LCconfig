package ru.yugsys.vvvresearch.lconfig.views;


import android.location.Location;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.Device;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.MainDevice;

public interface AddEditViewable {
    void setDeviceFields(MainDevice device);
    void setLocationFields(Location location);
}
