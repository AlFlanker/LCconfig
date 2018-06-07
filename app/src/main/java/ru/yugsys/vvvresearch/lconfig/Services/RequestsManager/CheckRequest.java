package ru.yugsys.vvvresearch.lconfig.Services.RequestsManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class CheckRequest extends BroadcastReceiver {


    public static CheckRequest.CheckRequestListener checkRequestListener;
    private static Context mContext;
    public static String ACTION = "ru.yugsys.vvvresearch.lconfig.Services.CHECKRESULT";


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Sync:", "Check Reciver " + intent.getAction());
        if (intent.getAction().equals(ACTION)) {
            Log.d("Sync:", "Check Reciver " + intent.getStringExtra("result"));
            if (intent.getStringExtra("result").equals("true")) {
                if (checkRequestListener != null)
                    Log.d("Sync:", "Callback ok");
                    checkRequestListener.checkRequestChanged(intent.getStringExtra("eui"));
            }
        }

    }


    public interface CheckRequestListener {
        void checkRequestChanged(String eui);
    }

}

