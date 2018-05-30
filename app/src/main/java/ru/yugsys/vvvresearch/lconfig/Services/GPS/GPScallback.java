package ru.yugsys.vvvresearch.lconfig.Services.GPS;

import java.util.List;

public interface GPScallback<T> {

    public void OnGPSdata(T t);

    interface AddresCallBack {
        void OnAddressSuccess(List<String> list);
    }
}
