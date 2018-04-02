package ru.yugsys.vvvresearch.lconfig.views;

import ru.yugsys.vvvresearch.lconfig.model.DataEntity.Device;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.MDevice;

import java.util.List;

public interface MainViewable {
    void setContentForView(List<MDevice> devices);
}
