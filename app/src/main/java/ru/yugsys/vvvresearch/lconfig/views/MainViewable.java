package ru.yugsys.vvvresearch.lconfig.views;

import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DeviceEntry;

import java.util.List;

public interface MainViewable {
    void setContentForView(List<DeviceEntry> devices);
}
