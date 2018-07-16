package ru.yugsys.vvvresearch.lconfig.Services.GPS;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.util.Log;
import ru.yugsys.vvvresearch.lconfig.App;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.DeviceEntryDao;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DeviceEntry;
import ru.yugsys.vvvresearch.lconfig.model.DataModel;

import java.util.List;
import java.util.regex.Pattern;


public class GeoCoderJob extends JobService {
    private List<DeviceEntry> devList;

    public GeoCoderJob() {


    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d("geoService", "start GeoService");
//        DeviceEntry dev = ((App) getApplication()).getModel().getDevQueue().poll();
        devList = getUntracketDevice();
        Log.d("geoService", "quantity of device: " + devList.size());
        Location location;
        for (DeviceEntry dev : devList) {
            location = new Location("");
            location.setLongitude(dev.getLongitude());
            location.setLatitude(dev.getLatitude());
                Intent intent = new Intent(getApplicationContext(), AddressService.class);
                intent.putExtra(Constant.RECEIVER, ((App) getApplication()).getModel().receiver);
                intent.putExtra(Constant.LOCATION_DATA_DEVICE_EUI, dev.getEui());
                intent.putExtra(Constant.LOCATION_DATA_EXTRA, location);
            Log.d("geoService", "send device with eui: " + dev.getEui());
            if (Build.VERSION.SDK_INT >= 26) {
                getApplicationContext().startForegroundService(intent);

            } else {
                startService(intent);
            }

        }


        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    private List<DeviceEntry> getUntracketDevice() {
        return ((App) getApplication()).getDaoSession().getDeviceEntryDao().queryBuilder().where(DeviceEntryDao.Properties.IsGeoOK.eq(false)).build().list();

    }
}
