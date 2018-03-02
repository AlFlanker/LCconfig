package ru.yugsys.vvvresearch.lconfig.presenters;

import ru.yugsys.vvvresearch.lconfig.views.LoginViewable;

public interface LoginPresentable extends Presentable<LoginViewable>{
    void attemptLogin();
}
