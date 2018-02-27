package ru.yugsys.vvvresearch.lconfig.presenters;

import ru.yugsys.vvvresearch.lconfig.views.MainViewable;

public interface MainPresentable {
    void bind(MainViewable mainView);
    void unBindAll();
    void fireUpdateDataForView();
}
