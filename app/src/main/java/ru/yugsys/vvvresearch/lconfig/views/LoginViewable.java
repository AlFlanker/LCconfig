package ru.yugsys.vvvresearch.lconfig.views;

public interface LoginViewable {
    String getLogin();
    String getPassword();
    String getServer();
    void fireLoginError(int resIDErrorMessage);
    void firePasswordError(int resIDErrorMessage);
    void fireServerError(int resIDErrorMessage);
    void fireShowSignProgress(boolean isShow);
    void fireCloseLoginView();
}
