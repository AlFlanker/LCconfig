package ru.yugsys.vvvresearch.lconfig.presenters.Presentable;

import ru.yugsys.vvvresearch.lconfig.model.DaoSession;

public interface BasePresent {
    void updateView();

    void AddDev(String n, String g);

    void loadData();

    void getSession(DaoSession s);
}
