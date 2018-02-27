package ru.yugsys.vvvresearch.lconfig.model.listeners;

import ru.yugsys.vvvresearch.lconfig.model.Device;

import java.util.List;

public interface OnDataRecieveListener extends ModelListeners{
    void OnDataRecived(List<Device> devices);

}
