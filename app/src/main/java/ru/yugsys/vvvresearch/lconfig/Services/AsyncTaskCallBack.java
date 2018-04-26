package ru.yugsys.vvvresearch.lconfig.Services;

import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DeviceEntry;

public interface AsyncTaskCallBack {
    interface ReadCallBack{
        void getDeviceEntry(DeviceEntry deviceEntry);
    }
    interface WriteCallback {
        void writeComplate(boolean check);
    }
}
