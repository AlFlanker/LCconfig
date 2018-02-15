package ru.yugsys.vvvresearch.lconfig.model;

public class BusinessModel implements Model {
    private static BusinessModel ourInstance = new BusinessModel();

    public static Model getInstance() {
        return ourInstance;
    }

    private BusinessModel() {

    }


    @Override
    public void setAuthenticationData(String login, String pass, String server) {

    }
}
