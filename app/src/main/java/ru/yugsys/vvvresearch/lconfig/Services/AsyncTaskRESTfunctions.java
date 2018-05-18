package ru.yugsys.vvvresearch.lconfig.Services;

import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AsyncTaskRESTfunctions extends AsyncTask<Void, Void, String> {
    public enum REST_FUNCTION {
        CreateDevice, DeleteDevice, AppData, CommandTypesOfDevice, SendCommand
    }

    ;
    private String hostAPI = "https://bs.net868.ru:20010/externalapi/";
    private String restFunc;
    private String prm;
    private String data;

    public AsyncTaskRESTfunctions(REST_FUNCTION type, String prm) {

        switch (type) {
            case CreateDevice:
                restFunc = "createdevice?";
                break;
            case DeleteDevice:
                restFunc = "deletedevice?";
                break;
            case AppData:
                restFunc = "appdata?";
                break;
            case CommandTypesOfDevice:
                restFunc = "commandTypesOfDevice?";
                break;
            case SendCommand:
                restFunc = "sendCommand?";
                break;

        }
        StringBuilder stringBuilder = new StringBuilder(hostAPI);
        stringBuilder.append(restFunc).append(prm);
        this.data = stringBuilder.toString();

    }

    @Override
    protected String doInBackground(Void... objects) {
        try {
            String resultString;
            String myURL = "https://bs.net868.ru:20010/externalapi/";
            String parammetrs = "appdata?token=1c68a488ec0d4dde80439e9627d23154&count=60&offset=0&order=desc";

            byte[] data = null;
            InputStream is = null;

            try {
                URL url = new URL(myURL + parammetrs);
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setRequestProperty("Content-Length", "" + Integer.toString(parammetrs.getBytes().length));
                conn.connect();
                int responseCode = conn.getResponseCode();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                if (responseCode == 200) {
                    is = conn.getInputStream();

                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    while ((bytesRead = is.read(buffer)) != -1) {
                        baos.write(buffer, 0, bytesRead);
                    }
                    data = baos.toByteArray();
                    resultString = new String(data, "UTF-8");
                    return resultString;
                } else {
                }


            } catch (MalformedURLException e) {

                //resultString = "MalformedURLException:" + e.getMessage();
            } catch (IOException e) {

                //resultString = "IOException:" + e.getMessage();
            } catch (Exception e) {

                //resultString = "Exception:" + e.getMessage();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;


    }


    @Override
    protected void onPostExecute(String o) {
        if (o != null) {
            Log.d("REST", o);
            String[] items = o.split("id");
            for (String item : items) Log.d("REST", item + "\n");
        } else {
            Log.d("REST", "null");
        }
    }
}