package ru.yugsys.vvvresearch.lconfig.Services.GPS;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import java.util.Arrays;

public class AddressResultReceiver extends ResultReceiver {
    public static GPScallback.AddresCallBack callBack;

    public AddressResultReceiver(Handler handler) {
        super(handler);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (resultData == null) return;
        String eui = resultData.getString(Constant.LOCATION_DATA_DEVICE_EUI);
        String country = resultData.getString(Constant.LOCATION_DATA_COUNTRY);
        String region = resultData.getString(Constant.LOCATION_DATA_REGION);
        String address = resultData.getString(Constant.LOCATION_DATA_ADDRESS);
        if (callBack != null) callBack.OnAddressSuccess(Arrays.asList(new String[]{eui, country, region, address}));

    }
}
