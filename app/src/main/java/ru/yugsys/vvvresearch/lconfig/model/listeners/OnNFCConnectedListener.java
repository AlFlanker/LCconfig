package ru.yugsys.vvvresearch.lconfig.model.listeners;

import ru.yugsys.vvvresearch.lconfig.model.Device;

public interface OnNFCConnectedListener extends ModelListeners{
    void OnNFCConnected(Device device);
}
