package ru.yugsys.vvvresearch.lconfig.fakemodel;

import ru.yugsys.vvvresearch.lconfig.fakemodel.listeners.ModelListeners;
import ru.yugsys.vvvresearch.lconfig.fakemodel.listeners.OnCheckDeviceListener;
import ru.yugsys.vvvresearch.lconfig.fakemodel.listeners.OnDataRecieveListener;
import ru.yugsys.vvvresearch.lconfig.fakemodel.listeners.OnNFCConnectedListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BusinessModel implements Model {
    private static BusinessModel ourInstance = new BusinessModel();
    private List<Device> deviceList = new ArrayList<>();
    private List<OnCheckDeviceListener> onCheckDeviceListeners = new ArrayList<>();
    private List<OnDataRecieveListener> onDataRecieveListeners = new ArrayList<>();
    private List<OnNFCConnectedListener> onNFCConnectedListeners = new ArrayList<>();

    public static Model getInstance() {
        return ourInstance;
    }

    private BusinessModel() {
        //fake device list
        fakeDeviceList();

    }

    private void fakeDeviceList() {
        Random random = new Random(90);
        for (int i = 0; i < 10; i++) {
            Device device = new Device();
            device.Longitude = random.nextDouble();
            device.Latitude = random.nextDouble();
            device.appeui = device.appkey = device.appskey = device.devadr = device.eui = Integer.toHexString(random.nextInt());
            deviceList.add(device);
        }
    }


    @Override
    public void loadAllData() {
        for (OnDataRecieveListener onDataRecieveListener :
                onDataRecieveListeners) {
            onDataRecieveListener.OnDataRecived(deviceList);
        }
    }

    @Override
    public void writeAuthData(String login, String pass, String server) {

    }

    @Override
    public void addNewDevice(Device device) {
        if (device != null) {
            deviceList.add(device);
            loadAllData();
        }

    }

    @Override
    public void subscribe(ModelListeners modelListener) {
        if (modelListener instanceof OnCheckDeviceListener)
            onCheckDeviceListeners.add((OnCheckDeviceListener) modelListener);
        else if (modelListener instanceof OnDataRecieveListener)
            onDataRecieveListeners.add((OnDataRecieveListener) modelListener);
        else if (modelListener instanceof OnNFCConnectedListener)
            onNFCConnectedListeners.add((OnNFCConnectedListener) modelListener);
    }

    @Override
    public void unSubscribe(ModelListeners modelListener) {
        if (modelListener != null) {
            onNFCConnectedListeners.remove(modelListener);
            onDataRecieveListeners.remove(modelListener);
            onCheckDeviceListeners.remove(modelListener);
        }


    }
}
