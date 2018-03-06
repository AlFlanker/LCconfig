package ru.yugsys.vvvresearch.lconfig.presenters;


import android.location.Location;
import ru.yugsys.vvvresearch.lconfig.Services.GPSTracker;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.Device;
import ru.yugsys.vvvresearch.lconfig.model.Interfaces.Model;
import ru.yugsys.vvvresearch.lconfig.model.Interfaces.ModelListener;
import ru.yugsys.vvvresearch.lconfig.model.Manager.EventManager;
import ru.yugsys.vvvresearch.lconfig.views.MainViewable;

import java.util.List;

public class MainPresenter implements MainPresentable {
    private Model model;
    MainViewable mainView;


    public MainPresenter(Model model) {
        this.model = model;

    }

    @Override
    public void bind(MainViewable mainView) {
        this.mainView = mainView;
    }

    @Override
    public void unBindAll() {
        mainView = null;
    }

    @Override
    public void fireUpdateDataForView() {
        model.loadAllDeviceData();
    }




}
