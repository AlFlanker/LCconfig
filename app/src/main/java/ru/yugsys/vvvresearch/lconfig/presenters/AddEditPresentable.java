package ru.yugsys.vvvresearch.lconfig.presenters;


import ru.yugsys.vvvresearch.lconfig.model.DataEntity.Device;
import ru.yugsys.vvvresearch.lconfig.views.AddEditViewable;

public interface AddEditPresentable extends Presentable<AddEditViewable>{
    void fireNewDevice(Device device);


}
