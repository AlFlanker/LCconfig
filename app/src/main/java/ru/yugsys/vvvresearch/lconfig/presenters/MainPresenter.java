package ru.yugsys.vvvresearch.lconfig.presenters;


import android.location.Location;
import ru.yugsys.vvvresearch.lconfig.Services.GPSTracker;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.Device;
import ru.yugsys.vvvresearch.lconfig.model.Interfaces.Model;
import ru.yugsys.vvvresearch.lconfig.model.Interfaces.ModelListener;
import ru.yugsys.vvvresearch.lconfig.model.Manager.EventManager;
import ru.yugsys.vvvresearch.lconfig.views.MainViewable;

import java.util.List;

public class MainPresenter implements MainPresentable, ModelListener.OnDataRecived {
    private Model model;
    MainViewable mainView;


    public MainPresenter(Model model) {
        this.model = model;
        this.model.getEventManager().subscribeOnDataRecive(this);

    }

    @Override
    public void bind(MainViewable mainView) {
        this.mainView = mainView;
    }

    @Override
    public void unBind() {
        mainView = null;
    }

    @Override
    public void fireUpdateDataForView() {
        model.loadAllDeviceData();
    }


    @Override
    public void OnDataRecived(List<Device> devList) {
        if (mainView != null) {
            mainView.setContentForView(devList);
        }
    }
}
