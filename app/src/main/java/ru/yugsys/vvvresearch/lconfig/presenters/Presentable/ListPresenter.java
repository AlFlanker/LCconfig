package ru.yugsys.vvvresearch.lconfig.presenters.Presentable;

import android.support.annotation.NonNull;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DataDevice;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.Device;

import java.lang.ref.WeakReference;
import java.util.List;

public abstract class ListPresenter<M, V> implements BasePresent {


    protected M model;
    public Device dev;
    public DataDevice currentDataDevice;
    private WeakReference<V> view;
    public abstract List<Device> getList();
    public void setModel(M model) {
        resetStat();
        this.model = model;
        if (setupComp()) {
            // updateView(); //
        }
    }

    public void bindView(@NonNull V view) {
        this.view = new WeakReference<>(view);
        if (setupComp()) {
            updateView();
        }
    }

    public void unbindView() {
        this.view = null;

    }

    public abstract void resetStat();


    protected boolean setupComp() {
        return model != null && view != null;

    }

    public V getView() {
        return (this.view != null) ? view.get() : null;
    }

    public abstract void callReadNFC();

}
