package ru.yugsys.vvvresearch.lconfig.fakemodel.listeners;

import ru.yugsys.vvvresearch.lconfig.fakemodel.Device;

public interface OnNFCConnectedListener extends ModelListeners{
    void OnNFCConnected(Device device);
}
