package ru.yugsys.vvvresearch.lconfig.Services.GPS;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static ru.yugsys.vvvresearch.lconfig.Services.GPS.Constant.RECEIVER;

public class AddressService extends IntentService {


    public AddressService(String name) {
        super(name);
    }

    public AddressService() {
        super("AddressService");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final ResultReceiver resultReceiver = intent.getParcelableExtra(RECEIVER);
        Log.d("geo", "in service");
        if (intent == null) return;
        String error = "";
        String eui = intent.getStringExtra(Constant.LOCATION_DATA_DEVICE_EUI);
        Location location = intent.getParcelableExtra(Constant.LOCATION_DATA_EXTRA);
        List<Address> addresses = null;
        Log.d("geo", "in servuce: " + location.toString());
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(), 1);
            eui = intent.getStringExtra(Constant.LOCATION_DATA_DEVICE_EUI);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        if (addresses == null || addresses.size() == 0) {

        } else {
            Address address = addresses.get(0);
            Bundle bundle = new Bundle();
            String street = address.getThoroughfare();
            ArrayList<String> addressFragment = new ArrayList<>();
            bundle.putString(Constant.LOCATION_DATA_DEVICE_EUI, eui);
            bundle.putString(Constant.LOCATION_DATA_COUNTRY, address.getCountryName());
            bundle.putString(Constant.LOCATION_DATA_REGION, address.getAdminArea());
            bundle.putString(Constant.LOCATION_DATA_ADDRESS, street == null ? address.getAddressLine(0).split(",")[0] : street);
            resultReceiver.send(Constant.SUCCESS_RESULT, bundle);
            Log.d("LOCATION_DATA_COUNTRY", address.getCountryName());
            Log.d("LOCATION_DATA_REGION", address.getAdminArea());
            Log.d("LOCATION_DATA_ADDRESS", street == null ? address.getAddressLine(0).split(",")[0] : street);

        }
    }

}
