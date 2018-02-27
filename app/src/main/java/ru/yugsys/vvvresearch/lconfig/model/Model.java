package ru.yugsys.vvvresearch.lconfig.model;

import ru.yugsys.vvvresearch.lconfig.model.listeners.ModelListeners;

import java.util.List;

public interface Model {
    void loadAllData();
    void writeAuthData(String login, String pass, String server);
    void addNewDevice (Device device);
    void subscribe(ModelListeners modelListener);
    void unSubscribe(ModelListeners modelListener);
}
