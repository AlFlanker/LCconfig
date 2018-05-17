package ru.yugsys.vvvresearch.lconfig.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class DetectInternetConnection extends BroadcastReceiver {


    public static ConnectivityReceiverListener connectivityReceiverListener;
    private static Context mContext;

    public DetectInternetConnection() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        if (connectivityReceiverListener != null)
            connectivityReceiverListener.OnNetworkConnectionChanged(isConnected(context));
    }

    private static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected;
        if (networkInfo != null) isConnected = networkInfo.isConnectedOrConnecting();
        else isConnected = false;
        return isConnected;
    }

    public static boolean isConnected() {
        return isConnected(mContext);
    }

    public interface ConnectivityReceiverListener {
        void OnNetworkConnectionChanged(boolean isConnected);
    }

}
