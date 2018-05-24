package ru.yugsys.vvvresearch.lconfig.Services.RequestsManager;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class RequestManager extends IntentService {
    public RequestManager() {
        super("Request RequestManager");
    }

    public RequestManager(String name) {
        super(name);
    }

    private static final String SEND_REST_REQUEST = "ru.yugsys.vvvresearch.lcconfig.Services.RequestManager";


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("test", "onHandleIntent : " + intent.getAction());

    }

    public static void startRequestManager(Context context) {
        Intent intent = new Intent(context, RequestManager.class);
        intent.setAction(SEND_REST_REQUEST);
        context.startService(intent);
//        context.startForegroundService(intent);
    }

}
