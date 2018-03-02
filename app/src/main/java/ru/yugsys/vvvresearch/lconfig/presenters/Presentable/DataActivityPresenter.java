package ru.yugsys.vvvresearch.lconfig.presenters.Presentable;

import android.location.Location;
import android.util.Log;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DataDevice;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.Device;
import ru.yugsys.vvvresearch.lconfig.model.Interfaces.BaseModel;
import ru.yugsys.vvvresearch.lconfig.views.Interfaces.IEditView;

import java.util.List;


public class DataActivityPresenter extends ListPresenter<BaseModel, IEditView> {
    @Override
    public void OnNFCconnected(Device device) {
        this.dev = device;
        Log.d("NFC", "DEVICE: ");
        Log.d("NFC", "type - " + dev.type);
        Log.d("NFC", "eui - " + dev.eui);
        Log.d("NFC", "appeui -" + dev.appeui);
        Log.d("NFC", "devAdr -" + dev.devadr);
        Log.d("NFC", "nwkSKey - " + dev.nwkskey);
        Log.d("NFC", "appsKey - " + dev.appskey);
        Log.d("NFC", "Lat -" + dev.getLatitude());
        Log.d("NFC", "Lon -" + dev.getLongitude());
    }

    @Override
    public void callReadNFC() {
        model.setCurrentDataDevice(currentDataDevice);
        model.readNfcDev();
    }

    @Override
    public List<Device> getList() {
        return devList;
    }

    public List<Device> devList;
    private boolean NFCenable;
    private Location location;

    @Override
    public void loadData() {
        model.load();


    }

    @Override
    public void AddDev(String n, String g) {
        if (n.trim().equals("") || g.trim().equals("")) {
            getView().ShowError();
        }
        Device dev = new Device();
        //тут данные заполнить
        this.model.save(dev);
        this.loadData();

    }

    public Location getLocation() {
        return location;
    }

    @Override
    public void resetStat() {

    }

    @Override
    public void updateView() {
        getView().update(devList);
    }


    // Implements method from Model Listener

    @Override
    public void OnDataRecived(List<Device> devList) {
        Log.d("MyTag", "OnDataRecived: " + " work!");
        this.devList = devList;
        //updateView();
    }



    @Override
    public void OnCheckedDevData(boolean check) {
        Log.d("MyTag", "OnCheckedDevData: " + " work!" + " " + Boolean.toString(check));

    }

    @Override
    public void OnGPSdata(Location location) {
        this.location = location;
        Log.d("GPS", "Presenter GPS data: " + " " + location.toString());
    }
}
