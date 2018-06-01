package ru.yugsys.vvvresearch.lconfig.Services.GPS;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

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
            Log.d("geoService", "onReceiveResult");
            String eui = resultData.getString(Constant.LOCATION_DATA_DEVICE_EUI);
            String country = resultData.getString(Constant.LOCATION_DATA_COUNTRY);
            String region = resultData.getString(Constant.LOCATION_DATA_REGION);
            String address = resultData.getString(Constant.LOCATION_DATA_ADDRESS);
            String city = resultData.getString(Constant.LOCATION_DATA_CITY);
            if (callBack != null) {
                Log.d("geoService", "CallBack");
                callBack.OnAddressSuccess(Arrays.asList(eui, country, region, address, city));
            }
        }

    }
}
