package ru.yugsys.vvvresearch.lconfig.presenters;


import android.location.Location;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.Device;
import ru.yugsys.vvvresearch.lconfig.model.Interfaces.Model;
import ru.yugsys.vvvresearch.lconfig.model.Interfaces.ModelListener;
import ru.yugsys.vvvresearch.lconfig.model.Manager.EventManager;
import ru.yugsys.vvvresearch.lconfig.views.MainViewable;

import java.util.List;

public class MainPresenter implements MainPresentable, ModelListener<Device> {
    private Model model;
    MainViewable mainView;

    public MainPresenter(Model model) {
        this.model = model;
        model.getEventManager().subscribe(EventManager.TypeEvent.OnDataReceive, this);
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


    @Override
    public void OnDataRecived(List<Device> devices) {
        if (mainView != null) {
            mainView.setContentForView(devices);
        }
    }

    @Override
    public void OnNFCConnected(Device dev) {

    }

    @Override
    public void OnCheckedDevData(boolean check) {

    }

    @Override
    public void OnGPSdata(Location location) {

    }
}
