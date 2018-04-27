package ru.yugsys.vvvresearch.lconfig.presenters;


import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DeviceEntry;
import ru.yugsys.vvvresearch.lconfig.model.Interfaces.Model;
import ru.yugsys.vvvresearch.lconfig.model.Interfaces.ModelListener;
import ru.yugsys.vvvresearch.lconfig.views.MainViewable;

import java.util.List;

public class MainPresenter implements MainPresentable, ModelListener.OnDataRecived,ModelListener.OnLoadDevice {


    private Model model;
    MainViewable mainView;


    public MainPresenter(Model model) {
        this.model = model;
        this.model.getEventManager().subscribeOnDataRecive(this);
        this.model.getEventManager().subscribeOnLoadDevice(this);

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

        // model.loadAllDeviceData();
        model.loadAllDeviceDataByProperties(Model.Properties.DateOfChange, Model.Direction.Reverse);
    }


    @Override
    public void OnDataRecived(List<DeviceEntry> devList) {
        if (mainView != null) {
            mainView.setContentForView(devList);
        }
    }

    @Override
    public void OnLoadDevice(DeviceEntry dev) {
        mainView.OnCardFullView(dev);
    }
    @Override
    public void loadDevByEUI(String eui) {
        if(!("".equals(eui))) {
            model.loadDeviceByEUI(eui);
        }
    }
}
