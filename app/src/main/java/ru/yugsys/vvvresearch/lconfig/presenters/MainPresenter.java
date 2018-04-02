package ru.yugsys.vvvresearch.lconfig.presenters;


import ru.yugsys.vvvresearch.lconfig.model.DataEntity.Device;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.MainDevice;
import ru.yugsys.vvvresearch.lconfig.model.Interfaces.Model;
import ru.yugsys.vvvresearch.lconfig.model.Interfaces.ModelListener;
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

        // model.loadAllDeviceData();
        model.loadAllDeviceDataByProperties(Model.Properties.APPEUI, Model.Direction.Straight);
    }


    @Override
    public void OnDataRecived(List<MainDevice> devList) {
        if (mainView != null) {
            mainView.setContentForView(devList);
        }
    }
}
