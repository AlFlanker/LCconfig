package ru.yugsys.vvvresearch.lconfig.fakemodel;

import ru.yugsys.vvvresearch.lconfig.fakemodel.listeners.ModelListeners;

public interface Model {
    void loadAllData();
    void writeAuthData(String login, String pass, String server);
    void addNewDevice (Device device);
    void subscribe(ModelListeners modelListener);
    void unSubscribe(ModelListeners modelListener);
}
