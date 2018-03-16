package ru.yugsys.vvvresearch.lconfig.presenters;

import android.location.Location;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.Device;
import ru.yugsys.vvvresearch.lconfig.model.Interfaces.Model;
import ru.yugsys.vvvresearch.lconfig.model.Interfaces.ModelListener;
import ru.yugsys.vvvresearch.lconfig.model.Manager.EventManager;
import ru.yugsys.vvvresearch.lconfig.views.AddEditViewable;

import java.util.List;

public class AddEditPresenter implements AddEditPresentable, ModelListener<Device> {

    private Model model;
    private AddEditViewable addEditView;
    private Location location;

    public AddEditPresenter(Model model) {
        this.model = model;
        model.getEventManager().subscribe(EventManager.TypeEvent.OnNFCconnected, this);
    }

    @Override
    public void bind(AddEditViewable addEditView) {
        this.addEditView = addEditView;
    }

    @Override
    public void unBind() {
        this.addEditView = null;
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
    public void OnDataRecived(List<Device> devList) {

    }

    @Override
    public void OnCheckedDevData(boolean check) {

    }

    @Override
    public void OnGPSdata(Location location) {
        this.location = location;
        addEditView.setLocationFields(location);
    }
}
