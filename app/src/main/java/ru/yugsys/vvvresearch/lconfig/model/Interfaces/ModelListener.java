package ru.yugsys.vvvresearch.lconfig.model.Interfaces;


import java.util.List;

public interface ModelListener<T> {
    void OnDataRecived(List<T> devList);

    void OnNFCconnected(boolean flag);

    void OnCheckedDevData(boolean check);


}
