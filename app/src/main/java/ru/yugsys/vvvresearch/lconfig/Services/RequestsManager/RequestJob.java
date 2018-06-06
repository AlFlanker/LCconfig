package ru.yugsys.vvvresearch.lconfig.Services.RequestsManager;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.util.Log;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.yugsys.vvvresearch.lconfig.App;
import ru.yugsys.vvvresearch.lconfig.Services.WebSocketListener;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.DeviceEntryDao;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.GeoDataDao;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.NetDataDao;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DeviceEntry;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.GeoData;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.NetData;

import java.text.DecimalFormat;
import java.util.List;


public class RequestJob extends JobService {

    public RequestJob() {

        Log.d("net868", "RequestJob: ");

    }

    @Override
    public boolean onStartJob(JobParameters params) {

        List<DeviceEntry> devsList = getList();
        NetData netData = getNetData();
        NetData currentService = getCurrentService();
        Intent data = new Intent(getApplicationContext(), RequestManager.class);
        data.setAction(RequestManager.SEND_REST_REQUEST);
        int i = 0;
//        RequestManager.startRequestManager(getApplicationContext());
        if (currentService != null) {
            if (currentService.getServiceName().equals("net868.ru")) {
                Log.d("Sync", "selected service: " + currentService.getServiceName());
                if (devsList != null) {
                    Log.d("net868", "quantity of devices: " + devsList.size());
                    for (DeviceEntry dev : devsList) {
                        i++;
                        data.putExtra("hostAPI", netData.getAddress() + RequestMaster.net868API.get(RequestMaster.REST_FUNCTION.CreateDevice));
                        data.putExtra("token", netData.getToken());
                        data.putExtra("JSONobject", RequestMaster.convert2StringJSON(dev, netData.getToken()));
                        getApplicationContext().startService(data);
                    }
                    Log.d("net868", " counter: " + String.valueOf(i));

                }
            } else if (currentService.getServiceName().equals("Вега")) {
                Log.d("Sync", "selected service: " + currentService.getServiceName());

                final JSONObject dataForVega = getDataForVega(devsList);
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(currentService.getAddress()).build();
//            WebSocketListener listener = new WebSocketListener();
                WebSocket ws = client.newWebSocket(request, new okhttp3.WebSocketListener() {
                    @Override
                    public void onMessage(WebSocket webSocket, String text) {
                        Log.d("Sync", text);
                        if (text.indexOf("token") != -1) {
                            webSocket.send(dataForVega.toString());
                            Log.d("Sync", "send data: ");
                            Log.d("Sync", "---> " + dataForVega.toString());
                        } else {
                            JSONObject resp = new JSONObject();
                            try {
                                resp.getJSONObject(text);
                                Log.d("Sync", resp.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
//                    webSocket.close(1000,"closing");
                    }
                });
                ws.send(getAuthData(currentService));


                client.dispatcher().executorService().shutdown();
                /* ВЕГА API!*/

            }
        }

        return false;
    }

    private String getAuthData(NetData currentService) {
        try {
            JSONObject authData = new JSONObject();
            authData.put("cmd", "auth_req");
            authData.put("login", currentService.getLogin());
            authData.put("password", currentService.getPassword());
            return authData.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean onStopJob(JobParameters params) {

        Log.d("net868", "onStopJob: " + params.getJobId());
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

    private List<NetData> getAllService() {
        return ((App) getApplication()).getDaoSession().getNetDataDao().queryBuilder().build().list();
    }

    private NetData getCurrentService() {
        List<NetData> services = getAllService();
        for (NetData netData : services) {
            if (netData.getCheckMain()) {
                return netData;
            }
        }
        return null;
    }

    private JSONObject getDataForVega(List<DeviceEntry> devices) {
        JSONObject body = new JSONObject();
        try {
            body.put("cmd", "manage_devices_req");
            body.put("devices_list", getJSONArray(devices));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return body;
    }

    private JSONArray getJSONArray(List<DeviceEntry> devices) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        JSONObject inner;
        JSONObject typeReq;
        JSONObject geo;
        for (DeviceEntry dev : devices) {
            inner = new JSONObject();
            inner.put("devEui", dev.getEui());
            inner.put("devName", dev.getComment());
            typeReq = new JSONObject();
            if (!dev.getIsOTTA()) {
                typeReq.put("devAddress", dev.getDevadr());
                typeReq.put("appsKey", dev.getAppskey());
                typeReq.put("nwksKey", dev.getNwkskey());
                inner.put("ABP", typeReq);
            } else {
                typeReq.put("appEui", dev.getAppeui());
                typeReq.put("appKey", dev.getAppkey());
                inner.put("OTTA", typeReq);
            }
            geo = new JSONObject();
            geo.put("longitude", dev.getLongitude());
            geo.put("latitude", dev.getLatitude());
            inner.put("position", geo);
            inner.put("class", "CLASS_C");
            jsonArray.put(inner);
        }
        return jsonArray;
    }




}
