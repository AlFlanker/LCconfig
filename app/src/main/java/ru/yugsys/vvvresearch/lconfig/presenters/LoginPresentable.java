package ru.yugsys.vvvresearch.lconfig.presenters;

import ru.yugsys.vvvresearch.lconfig.model.LoginData;
import ru.yugsys.vvvresearch.lconfig.views.LoginViewable;

public interface LoginPresentable extends Presentable<LoginViewable>{
    void attemptToLogin();
    void saveLoginData();
    LoginData loadLoginData();
}
