package ru.yugsys.vvvresearch.lconfig.model.Manager;

import android.location.Location;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.Device;
import ru.yugsys.vvvresearch.lconfig.model.Interfaces.ModelListener;

import java.util.*;

public class EventManager {
    public enum TypeEvent {
        OnDataReceive, OnNFCconnected, OnDevDataChecked, OnGPSdata
    }

    private EnumMap<TypeEvent, ArrayList<ModelListener<Device>>> listeners = new EnumMap<>(TypeEvent.class);

    public EventManager() {
        for (TypeEvent event : TypeEvent.values())
            this.listeners.put(event, new ArrayList<ModelListener<Device>>());
    }

    public boolean subscribe(TypeEvent event, ModelListener listener) {
        if (listeners.containsKey(event)) {
            listeners.get(event).add(listener);
            return true;
        }
        return false;
    }

    public boolean unsubscribe(TypeEvent event, ModelListener listener) {
        if (listeners.containsKey(event)) {
            return listeners.get(event).remove(listener);

        }
        return false;
    }

    public void notify(TypeEvent evType, Device dev, boolean check, List devList, Location location) {

        switch (evType) {
            case OnDevDataChecked:
                for (ModelListener<Device> listener : listeners.get(evType)) {
                    listener.OnCheckedDevData(check);
                }
                break;
            case OnDataReceive:
                for (ModelListener<Device> listener : listeners.get(evType)) {
                    listener.OnDataRecived(devList);
                }
                break;
            case OnNFCconnected:
                for (ModelListener<Device> listener : listeners.get(evType)) {
                    listener.OnNFCconnected(dev);
                }
                break;
            case OnGPSdata:
                for (ModelListener<Device> listener : listeners.get(evType)) {
                    listener.OnGPSdata(location);
                }
                break;
        }


    }

}
