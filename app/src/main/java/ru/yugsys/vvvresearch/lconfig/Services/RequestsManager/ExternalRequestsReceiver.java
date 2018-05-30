package ru.yugsys.vvvresearch.lconfig.Services.RequestsManager;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.concurrent.TimeUnit;

public class ExternalRequestsReceiver extends BroadcastReceiver {
    public ExternalRequestsReceiver() {
        Log.d("test", "onConstructor: ");
    }


    private static int JobID = 1;
    public JobScheduler jobScheduler;
    public static final String ACTION = "ru.yugsys.vvvresearch.lconfig.Services.RequestsManager";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("test", "OnReceiver: ");
        if (intent.getAction().equals(ACTION)) {
            Log.d("test", "OnReceiver in if: " + intent.getAction());
            scheduleJob(context);
        } else {
            throw new IllegalArgumentException("No supporting action");
        }
    }

    private void scheduleJob(Context context) {
        Log.d("test", "scheduleJob");

        ComponentName mService = new ComponentName(context, RequestJob.class);
        JobInfo jobInfo = new JobInfo.Builder(JobID, mService)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setRequiresCharging(false)
                .setRequiresDeviceIdle(false)
                .build();
        jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        int res = jobScheduler.schedule(jobInfo);
        Log.d("test", "JobID: " + String.valueOf(JobID));
        if (res == JobScheduler.RESULT_SUCCESS) {
            Log.d("test", "Job scheduled successfully!");
        }
    }

    private void stopSchedule() {

    }
}
