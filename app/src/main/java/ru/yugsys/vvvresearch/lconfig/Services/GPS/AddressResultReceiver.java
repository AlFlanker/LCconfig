package ru.yugsys.vvvresearch.lconfig.Services.GPS;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import java.util.Arrays;

public class AddressResultReceiver extends ResultReceiver {
    public GPScallback.AddresCallBack callBack;

    public AddressResultReceiver(Handler handler) {
        super(handler);
    }

    public void setCallBack(GPScallback.AddresCallBack receiver) {
        this.callBack = receiver;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (resultData == null) return;
        if (resultCode == Constant.SUCCESS_RESULT) {
            String eui = resultData.getString(Constant.LOCATION_DATA_DEVICE_EUI);
            String country = resultData.getString(Constant.LOCATION_DATA_COUNTRY);
            String region = resultData.getString(Constant.LOCATION_DATA_REGION);
            String address = resultData.getString(Constant.LOCATION_DATA_ADDRESS);
            if (callBack != null) callBack.OnAddressSuccess(Arrays.asList(eui, country, region, address));
        }

    }
}
