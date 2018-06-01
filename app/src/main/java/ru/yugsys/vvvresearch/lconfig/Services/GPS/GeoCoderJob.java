package ru.yugsys.vvvresearch.lconfig.Services.GPS;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import ru.yugsys.vvvresearch.lconfig.App;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DeviceEntry;
import ru.yugsys.vvvresearch.lconfig.model.DataModel;


public class GeoCoderJob extends JobService {

    public GeoCoderJob() {


    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d("geoService", "start GeoService");
        DeviceEntry dev = ((App) getApplication()).getModel().getDevQueue().poll();

        Location location;
        if (dev != null) {
            location = new Location("");
            location.setLatitude(dev.getLatitude());
            location.setLongitude(dev.getLongitude());

            if (location != null) {
                Intent intent = new Intent(getApplicationContext(), AddressService.class);
                intent.putExtra(Constant.RECEIVER, ((App) getApplication()).getModel().receiver);
                intent.putExtra(Constant.LOCATION_DATA_DEVICE_EUI, dev.getEui());
                intent.putExtra(Constant.LOCATION_DATA_EXTRA, location);
                Log.d("geoService", "start GeoService");
                startService(intent);
            }
        }


        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
