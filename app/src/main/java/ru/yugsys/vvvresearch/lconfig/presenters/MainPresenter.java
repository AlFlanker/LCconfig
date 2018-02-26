package ru.yugsys.vvvresearch.lconfig.presenters;

import ru.yugsys.vvvresearch.lconfig.views.MainViewable;

public class MainPresenter implements MainPresentable{
    MainViewable mainView;
    @Override
    public void bind(MainViewable mainView) {
        this.mainView = mainView;
    }

    @Override
    public void unBindAll() {

    }
}
