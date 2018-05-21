package ru.yugsys.vvvresearch.lconfig.Services;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DeviceEntry;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;


public class RestManager {
    private static String hostAPI = "https://bs.net868.ru:20010/externalapi/appdata?";
//    public enum REST_FUNCTION {
//        CreateDevice, DeleteDevice, AppData, CommandTypesOfDevice, SendCommand
//    }

    public class GetRequest extends AsyncTask<Void, Void, String> {

        private String restFunc;
        private String prm;
        private String data;
        private DeviceEntry deviceEntry;
        private JSONObject jo;
        private DataOutputStream bos;


        public GetRequest(String prm) throws JSONException {
            this.prm = prm;

        }

        private String getParamets(DeviceEntry devEntry) {
            return new String();
        }

        @Override
        protected void onPreExecute() {
            StringBuilder stringBuilder = new StringBuilder(hostAPI);
            try {
                URL url = new URL(stringBuilder.append(prm).toString());
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setRequestMethod("GET"); // test
                conn.setDoInput(true);
//                conn.connect();
                int responseCode = conn.getResponseCode();
                StringBuilder res = new StringBuilder();
                String t;
                if (responseCode == 200) {
                    BufferedReader is = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while (!("".equals(t = is.readLine()))) {
                        res.append(t);
                    }
                    Log.d("TEST", res.toString());
                } else {
                    BufferedReader is = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while (!("".equals(t = is.readLine()))) {
                        res.append(t);
                    }
                    Log.d("TEST", res.toString());
                }


            } catch (MalformedURLException e) {

                //resultString = "MalformedURLException:" + e.getMessage();
            } catch (IOException e) {

                //            resultString = "IOException:" + e.getMessage();
            } catch (Exception e) {

                //            resultString = "Exception:" + e.getMessage();
            } finally {
//                try {
//                    bos.flush(); //очищает поток output-a
//                    bos.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        }

        @Override
        protected String doInBackground(Void... objects) {
            try {
                String resultString;
                String myURL = "https://bs.net868.ru:20010/externalapi/";
                String parammetrs = "appdata?token=1c68a488ec0d4dde80439e9627d23154&count=60&offset=0&order=desc";

                byte[] data = null;
                InputStream is = null;


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
}
