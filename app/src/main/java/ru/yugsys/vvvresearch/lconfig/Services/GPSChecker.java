package ru.yugsys.vvvresearch.lconfig.Services;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;

import static android.content.Context.LOCATION_SERVICE;

public class GPSChecker {


    public static void showSettingsAlert(final Context mContext) {
        LocationManager locationManager = (LocationManager) mContext
                .getSystemService(LOCATION_SERVICE);

        if (!(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);


            alertDialog.setTitle("GPS is settings");


            alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");


            alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    mContext.startActivity(intent);
                }
            });


            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });


            alertDialog.show();
        }

    }
}