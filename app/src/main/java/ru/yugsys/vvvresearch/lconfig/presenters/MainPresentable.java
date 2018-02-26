package ru.yugsys.vvvresearch.lconfig.presenters;

import ru.yugsys.vvvresearch.lconfig.views.MainViewable;

public interface MainPresentable {
    void bind(MainViewable loginView);
    void unBindAll();
}
