package ru.yugsys.vvvresearch.lconfig.presenters;

import ru.yugsys.vvvresearch.lconfig.views.LoginViewable;

public interface LoginPresentable {
    void bind(LoginViewable loginView);

    void unBindAll();

    void attemptLogin();
}
