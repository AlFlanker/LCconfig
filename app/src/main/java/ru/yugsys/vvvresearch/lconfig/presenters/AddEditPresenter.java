package ru.yugsys.vvvresearch.lconfig.presenters;

import android.location.Location;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.Device;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.MainDevice;
import ru.yugsys.vvvresearch.lconfig.model.Interfaces.Model;
import ru.yugsys.vvvresearch.lconfig.model.Interfaces.ModelListener;
import ru.yugsys.vvvresearch.lconfig.views.AddEditViewable;

public class AddEditPresenter implements AddEditPresentable, ModelListener.OnNFCConnected, ModelListener.OnGPSdata {

    private Model model;
    private AddEditViewable addEditView;


    public AddEditPresenter(Model model) {
        this.model = model;
        model.getEventManager().subscribeOnNFC(this);
        model.getEventManager().subscribeOnGPS(this);
    }

    @Override
    public void bind(AddEditViewable addEditView) {
        this.addEditView = addEditView;
        model.getGPSLocation();
    }

    @Override
    public void unBind() {

    }

    @Override
    public void OnNFCConnected(MainDevice device) {
        addEditView.setDeviceFields(device);

    }

    @Override
    public void fireNewDevice(MainDevice device) {
        model.saveDevice(device);
    }

    @Override
    public void fireGetNewGPSData() {

        model.getGPSLocation();
    }


    @Override
    public void OnGPSdata(Location location) {
        if (location != null) {
            addEditView.setLocationFields(location);
        }

    }
}
