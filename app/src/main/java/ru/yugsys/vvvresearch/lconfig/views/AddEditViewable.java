package ru.yugsys.vvvresearch.lconfig.views;


import android.location.Location;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.MDevice;

public interface AddEditViewable {
    void setDeviceFields(MDevice device);
    void setLocationFields(Location location);
}
