package ru.yugsys.vvvresearch.lconfig.presenters;

import ru.yugsys.vvvresearch.lconfig.model.Device;
import ru.yugsys.vvvresearch.lconfig.views.AddEditViewable;

public interface AddEditPresentable extends Presentable<AddEditViewable>{
    void fireNewDevice(Device device);


}
