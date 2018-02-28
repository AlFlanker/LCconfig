package ru.yugsys.vvvresearch.lconfig.presenters;

import ru.yugsys.vvvresearch.lconfig.views.MainViewable;

public interface Presentable<V> {
    void bind(V mainView);
    void unBindAll();
}
