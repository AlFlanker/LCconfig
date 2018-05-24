package ru.yugsys.vvvresearch.lconfig.Services.RequestsManager;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.util.Log;
import ru.yugsys.vvvresearch.lconfig.App;


import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.DeviceEntryDao;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DeviceEntry;


import java.util.List;

public class RequestJob extends JobService {

    public RequestJob() {
        Log.d("test", "RequestJob: ");

    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d("test", "OnStartJob: " + params.getJobId());
        RequestManager.startRequestManager(getApplicationContext());
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {

        Log.d("test", "onStopJob: " + params.getJobId());
        return false;
    }


    private List<DeviceEntry> getList() {
        return ((App) getApplication()).getDaoSession().getDeviceEntryDao().queryBuilder().where(DeviceEntryDao.Properties.IsSyncServer.eq(false)).build().list();

    }
}
