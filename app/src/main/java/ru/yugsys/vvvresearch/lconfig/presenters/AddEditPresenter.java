package ru.yugsys.vvvresearch.lconfig.presenters;

import ru.yugsys.vvvresearch.lconfig.fakemodel.Device;
import ru.yugsys.vvvresearch.lconfig.fakemodel.Model;
import ru.yugsys.vvvresearch.lconfig.fakemodel.listeners.OnNFCConnectedListener;
import ru.yugsys.vvvresearch.lconfig.views.AddEditViewable;

public class AddEditPresenter implements AddEditPresentable, OnNFCConnectedListener{

    private Model model;
    AddEditViewable addEditView;

    public AddEditPresenter(Model model) {
        this.model = model;
    }

    @Override
    public void bind(AddEditViewable addEditView) {

    }

    @Override
    public void unBindAll() {

    }

    @Override
    public void OnNFCConnected(Device device) {
        addEditView.setDeviceFields(device);

    }

    @Override
    public void fireNewDevice(Device device) {
        model.addNewDevice(device);
    }
}
