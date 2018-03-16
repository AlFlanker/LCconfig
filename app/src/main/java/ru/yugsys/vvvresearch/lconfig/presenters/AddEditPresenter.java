package ru.yugsys.vvvresearch.lconfig.presenters;

import android.location.Location;
import android.util.Log;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.Device;
import ru.yugsys.vvvresearch.lconfig.model.Interfaces.Model;
import ru.yugsys.vvvresearch.lconfig.model.Interfaces.ModelListener;
import ru.yugsys.vvvresearch.lconfig.model.Manager.EventManager;
import ru.yugsys.vvvresearch.lconfig.views.AddEditViewable;

import java.util.List;

public class AddEditPresenter implements AddEditPresentable, ModelListener.OnNFCConnected, ModelListener.OnGPSdata {

    private Model model;
    private AddEditViewable addEditView;
    private Location location;

    public AddEditPresenter(Model model) {
        this.model = model;
        model.getEventManager().subscribeOnNFC(this);
        model.getEventManager().subscribeOnGPS(this);
    }

    @Override
    public void bind(AddEditViewable addEditView) {

    }

    @Override
    public void unBind() {

    }

    @Override
    public void OnNFCConnected(Device device) {
        addEditView.setDeviceFields(device);

    }

    @Override
    public void fireNewDevice(Device device) {
        model.saveDevice(device);
    }


    @Override
    public void OnGPSdata(Location location) {
        this.location = location;
        addEditView.setLocationFields(location);
    }
}
