package ru.yugsys.vvvresearch.lconfig.Services.RequestsManager.Strategy;

public interface REST {

    int send();

    String run();

    public enum REST_PRM {
        token, count, offset, startDate, endDate, deviceEui, sort, order
    }
}
