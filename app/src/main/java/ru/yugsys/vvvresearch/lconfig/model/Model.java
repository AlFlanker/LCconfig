package ru.yugsys.vvvresearch.lconfig.model;

import java.util.List;

public interface Model {
    void OnDataRecived(List<Device> devices);
    void OnNFCConnected(Device device);
    void OnCheckedDevData(boolean check);
    void loadAllData();
    void writeAuthData(String login, String pass, String server);
}
