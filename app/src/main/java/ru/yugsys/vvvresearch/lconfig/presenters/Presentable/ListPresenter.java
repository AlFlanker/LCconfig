package ru.yugsys.vvvresearch.lconfig.presenters.Presentable;

import android.support.annotation.NonNull;
import android.view.View;
import ru.yugsys.vvvresearch.lconfig.model.Dev_Data;

import java.lang.ref.WeakReference;

public abstract class ListPresenter<M, V> implements BasePresent {


    protected M model;
    private WeakReference<V> view;

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


}
