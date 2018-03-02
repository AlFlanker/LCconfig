package ru.yugsys.vvvresearch.lconfig.fakemodel.listeners;

import ru.yugsys.vvvresearch.lconfig.fakemodel.Device;

import java.util.List;

public interface OnDataRecieveListener extends ModelListeners{
    void OnDataRecived(List<Device> devices);

}
