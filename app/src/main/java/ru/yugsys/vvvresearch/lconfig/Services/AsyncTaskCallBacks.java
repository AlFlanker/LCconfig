package ru.yugsys.vvvresearch.lconfig.Services;

import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DeviceEntry;

public interface AsyncTaskCallBacks {
    interface ReadCallBack{
        void OnDeviceEntry(DeviceEntry deviceEntry);
    }
    interface WriteCallback {
        void writeComplate(Byte check);
    }
}
