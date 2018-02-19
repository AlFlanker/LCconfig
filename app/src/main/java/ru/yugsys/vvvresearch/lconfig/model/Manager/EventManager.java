package ru.yugsys.vvvresearch.lconfig.model.Manager;

import ru.yugsys.vvvresearch.lconfig.model.Dev_Data;
import ru.yugsys.vvvresearch.lconfig.model.Interfaces.ModelListener;

import java.util.*;

public class EventManager {
    public enum TypeEvent {
        OnDataReceive, OnNFCconnected, OnDevDataChecked
    }

    private EnumMap<TypeEvent, ArrayList<ModelListener<Dev_Data>>> listeners = new EnumMap<>(TypeEvent.class);

    public EventManager() {
        for (TypeEvent event : TypeEvent.values())
            this.listeners.put(event, new ArrayList<ModelListener<Dev_Data>>());
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

    public void notify(TypeEvent evType, boolean flag, boolean check, List devList) {

        switch (evType) {
            case OnDevDataChecked:
                for (ModelListener<Dev_Data> listener : listeners.get(evType)) {
                    listener.OnCheckedDevData(check);
                }
                break;
            case OnDataReceive:
                for (ModelListener<Dev_Data> listener : listeners.get(evType)) {
                    listener.OnDataRecived(devList);
                }
                break;
            case OnNFCconnected:
                for (ModelListener<Dev_Data> listener : listeners.get(evType)) {
                    listener.OnNFCconnected(flag);
                }
                break;
        }


    }

}
