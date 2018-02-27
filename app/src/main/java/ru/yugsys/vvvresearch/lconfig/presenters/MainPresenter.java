package ru.yugsys.vvvresearch.lconfig.presenters;

import ru.yugsys.vvvresearch.lconfig.model.Device;
import ru.yugsys.vvvresearch.lconfig.model.Model;
import ru.yugsys.vvvresearch.lconfig.model.listeners.OnDataRecieveListener;
import ru.yugsys.vvvresearch.lconfig.views.MainViewable;

import java.util.List;

public class MainPresenter implements MainPresentable,OnDataRecieveListener{
    private Model model;
    MainViewable mainView;

    public MainPresenter(Model model) {
        this.model = model;
        model.subscribe(this);
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
        model.loadAllData();
    }


    @Override
    public void OnDataRecived(List<Device> devices) {
        if (mainView != null) {
            mainView.setContentForView(devices);
        }

    }
}
