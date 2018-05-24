package ru.yugsys.vvvresearch.lconfig.Services.RequestsManager;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.util.Log;
import ru.yugsys.vvvresearch.lconfig.App;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.DeviceEntryDao;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.NetDataDao;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DeviceEntry;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.NetData;

import java.util.List;


public class RequestJob extends JobService {
    public RequestJob() {
        Log.d("test", "RequestJob: ");

    }

    @Override
    public boolean onStartJob(JobParameters params) {
        List<DeviceEntry> devsList = getList();
        NetData netData = getNetData();
        Intent data = new Intent(getApplicationContext(), RequestManager.class);
        data.setAction(RequestManager.SEND_REST_REQUEST);
        int i = 0;
//        RequestManager.startRequestManager(getApplicationContext());
        if (devsList != null) {
            for (DeviceEntry dev : devsList) {
                i++;
                data.putExtra("hostAPI", netData.getAddress() + RequestMaster.net868API.get(RequestMaster.REST_FUNCTION.CreateDevice));
                data.putExtra("token", netData.getToken());
                data.putExtra("JSONobject", RequestMaster.convert2StringJSON(dev));

                getApplicationContext().startService(data);
            }
            Log.d("onStartJob", " counter: " + String.valueOf(i));
        }

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

    private NetData getNetData() {
        return ((App) getApplication()).
                getDaoSession().
                getNetDataDao().
                queryBuilder().
                where(NetDataDao.
                        Properties.ServiceName.eq("net868")).
                build().
                unique();
    }


}
