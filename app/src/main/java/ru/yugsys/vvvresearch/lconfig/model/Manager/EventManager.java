package ru.yugsys.vvvresearch.lconfig.model.Manager;


import android.location.Location;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.Device;
import ru.yugsys.vvvresearch.lconfig.model.Interfaces.ModelListener;

import java.util.*;

/**
 * @author AlexFlanker
 * @version 1.0
 * @since 2018-03-06
 */

/**
 * Supportable Events:
 * OnDataReceive - вычитывание данных из БД;
 * OnNFCconnected - данные от NFC;
 * OnDevDataChecked - данные сохранены в БД;
 * OnGPSdata - поступление данных от GPS.
 */
public class EventManager {


    public enum TypeEvent {
        OnDataReceive, OnNFCconnected, OnDevDataChecked, OnGPSdata
    }

    private EnumMap<TypeEvent, List> listeners = new EnumMap<>(TypeEvent.class);


    /**
     * Создает новый объект с заданными значениями
     *
     * @see EventManager#EventManager()
     */
    public EventManager() {
        listeners.put(TypeEvent.OnDataReceive, new ArrayList<ModelListener.OnDataRecived>());
        listeners.put(TypeEvent.OnGPSdata, new ArrayList<ModelListener.OnGPSdata>());
        listeners.put(TypeEvent.OnNFCconnected, new ArrayList<ModelListener.OnNFCConnected>());
        listeners.put(TypeEvent.OnDevDataChecked, new ArrayList<ModelListener.OnCheckedDevData>());
    }

    /**
     * Подписка на обновление GPS координат
     *
     * @return Location
     */
    @SuppressWarnings("unchecked")
    public void subscribeOnGPS(ModelListener.OnGPSdata listener) {

        listeners.get(TypeEvent.OnGPSdata).add(listener);
    }

    /**
     * Подписка на получение данных NFC.
     * @return new Device()
     */
    @SuppressWarnings("unchecked")
    public void subscribeOnNFC(ModelListener.OnNFCConnected listener) {
        listeners.get(TypeEvent.OnNFCconnected).add(listener);
    }

    /**
     * Подписка на получение данных из БД
     * @return ArrayList<Device>
     */
    @SuppressWarnings("unchecked")
    public void subscribeOnDataRecive(ModelListener.OnDataRecived listener) {
        listeners.get(TypeEvent.OnDataReceive).add(listener);
    }

    /**
     * Подписка на получение уведомления о успешном сохранении в БД
     * @return Boolean
     */
    @SuppressWarnings("unchecked")
    public void subscribeOnDevDataChecked(ModelListener.OnCheckedDevData listener) {
        listeners.get(TypeEvent.OnDevDataChecked).add(listener);
    }


    public void unsubscribeOnGPS(ModelListener.OnGPSdata listener) {
        listeners.get(TypeEvent.OnGPSdata).remove(listener);
    }

    public void unsubscribeOnNFC(ModelListener.OnNFCConnected listener) {
        listeners.get(TypeEvent.OnNFCconnected).remove(listener);
    }

    public void unsubscribeOnDataRecive(ModelListener.OnDataRecived listener) {
        listeners.get(TypeEvent.OnDataReceive).remove(listener);
    }

    public void unsubscribeOnDevDataChecked(ModelListener.OnCheckedDevData listener) {
        listeners.get(TypeEvent.OnDevDataChecked).remove(listener);
    }



    public void notifyOnGPS(Location location) {

        for (Object listener : listeners.get(TypeEvent.OnGPSdata)) {
            ((ModelListener.OnGPSdata) listener).OnGPSdata(location);
        }
    }

    public void notifyOnNFC(Device dev) {
        for (Object listener : listeners.get(TypeEvent.OnNFCconnected)) {
            ((ModelListener.OnNFCConnected) listener).OnNFCConnected(dev);
        }
    }

    public void notifyOnDataReceive(List<Device> devList) {
        for (Object listener : listeners.get(TypeEvent.OnDataReceive)) {
            ((ModelListener.OnDataRecived) listener).OnDataRecived(devList);
        }
    }

    public void notifyOnDevDataChecked(boolean check) {
        for (Object listener : listeners.get(TypeEvent.OnDevDataChecked)) {
            ((ModelListener.OnCheckedDevData) listener).OnCheckedDevData(check);
        }
    }


}
