package ru.yugsys.vvvresearch.lconfig.model;

import java.util.List;

public class BusinessModel implements Model {
    private static BusinessModel ourInstance = new BusinessModel();

    public static Model getInstance() {
        return ourInstance;
    }

    private BusinessModel() {

    }


}
