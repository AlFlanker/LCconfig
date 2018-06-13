package ru.yugsys.vvvresearch.lconfig.Services.RequestsManager;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.yugsys.vvvresearch.lconfig.App;
import ru.yugsys.vvvresearch.lconfig.R;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.DeviceEntryDao;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.NetDataDao;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DeviceEntry;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.NetData;
import ru.yugsys.vvvresearch.lconfig.views.MainActivity;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


public class RequestJob extends JobService {

    public RequestJob() {

        Log.d("net868", "RequestJob: ");

    }

    @Override
    public boolean onStartJob(JobParameters params) {

        List<DeviceEntry> devsList = getList();
        Log.d("Sync", "quantity: " + devsList.size());

        NetData currentService = getCurrentService();
        Intent data = new Intent(getApplicationContext(), RequestManager.class);
        data.setAction(RequestManager.SEND_REST_REQUEST);
        int i = 0;
        if (currentService != null) {
            if (currentService.getServiceName().equals("net868.ru") && !currentService.getToken().equals("")
                    && !currentService.getAddress().equals("")) {
                Log.d("Sync", "selected service: " + currentService.getServiceName());
                if (devsList != null) {
                    Log.d("Sync", "quantity of devices: " + devsList.size());
                    for (DeviceEntry dev : devsList) {
                        i++;
                        data.putExtra("hostAPI", currentService.getAddress() + RequestMaster.net868API.get(RequestMaster.REST_FUNCTION.CreateDevice));
                        data.putExtra("token", currentService.getToken());
                        data.putExtra("JSONobject", RequestMaster.convert2StringJSON(dev, currentService.getToken()));
                        getApplicationContext().startService(data);
                    }
                    Log.d("Sync", " counter: " + String.valueOf(i));

                }
            } else if (currentService.getServiceName().equals("Вега") &&
                    !currentService.getAddress().equals("") &&
                    !currentService.getLogin().equals("") &&
                    !currentService.getPassword().equals("")) {
                Log.d("Sync", "selected service: " + currentService.getServiceName());

                final JSONObject dataForVega = getDataForVega(devsList);
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(currentService.getAddress()).build();

                WebSocket ws = client.newWebSocket(request, new WebSocketListener() {
                    @Override
                    public void onClosing(WebSocket webSocket, int code, String reason) {
                        Log.d("Sync", reason);
                    }

                    @Override
                    public void onMessage(WebSocket webSocket, String text) {
                        Log.d("Sync", text);
                        if (text.indexOf("token") != -1) {
                            webSocket.send(dataForVega.toString());
                            Log.d("Sync", "send data: ");
                            Log.d("Sync", "-> " + dataForVega.toString());
                        } else {
                            JSONObject resp = null;
                            try {
                                resp = new JSONObject(text);
                                if (resp.getString("cmd").equals("manage_devices_resp")) {
                                    /*if there are registered device - create new JSONObject without OTTA/ABP data
                                    and send again*/
                                    JSONObject existing = analizeResponse(DevStatus(text));
                                    if (existing.getJSONArray("devices_list").length() > 0) {
                                        Log.d("Sync", "existing devices: " + existing.toString());
                                        webSocket.send(existing.toString());
                                    } else if (existing.getJSONArray("devices_list").length() == 0) {
                                        webSocket.close(1000, "closing");
                                    }

                                }
                                if (resp.getString("err_string").equals("invalid_login_or_password")) {
                                    sendToMain("false", getString(R.string.Invalid_login_or_password));
                                }
                                Log.d("Sync", resp.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
                ws.send(getAuthData(currentService));
                client.dispatcher().executorService().shutdown();
                /* ВЕГА API!*/

            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.AddService), Toast.LENGTH_SHORT);
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

    private DeviceEntry getExistingDevice(String eui) {
        return ((App) getApplication()).getDaoSession().getDeviceEntryDao().queryBuilder().where(DeviceEntryDao.Properties.Eui.eq(eui)).build().unique();

    }


    private NetData getNetData() {
        return ((App) getApplication()).
                getDaoSession().
                getNetDataDao().
                queryBuilder().
                where(NetDataDao.
                        Properties.ServiceName.eq("net868.ru")).
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
                typeReq.put("devAddress", new BigInteger(dev.getDevadr().trim(), 16));
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

    private JSONObject getDataForVegaWithoutRegInfo(List<DeviceEntry> devices) {
        JSONObject body = new JSONObject();
        try {
            body.put("cmd", "manage_devices_req");
            body.put("devices_list", getJSONArrayWithoutRegInfo(devices));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return body;
    }

    private JSONArray getJSONArrayWithoutRegInfo(List<DeviceEntry> devices) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        JSONObject inner;
        JSONObject geo;
        for (DeviceEntry dev : devices) {
            inner = new JSONObject();
            inner.put("devEui", dev.getEui());
            inner.put("devName", dev.getComment());

            geo = new JSONObject();
            geo.put("longitude", dev.getLongitude());
            geo.put("latitude", dev.getLatitude());
            inner.put("position", geo);
            inner.put("class", "CLASS_C");
            jsonArray.put(inner);
        }
        return jsonArray;
    }


    private JSONObject getResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<JSONObject> DevStatus(String response) {
        JSONObject object;
        object = getResponse(response);
        List<JSONObject> devs = new ArrayList<>();
        try {
            if ((Boolean) object.get("status")) {
                JSONArray arr = object.getJSONArray("device_add_status");
                for (int i = 0; i < arr.length(); i++) {
                    devs.add(arr.getJSONObject(i));
                }
                return devs;
            } else return null;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private JSONObject analizeResponse(List<JSONObject> list) {
        List<DeviceEntry> existingDev = new ArrayList<>();
        for (JSONObject obj : list) {
            try {
                if (obj.getString("status").equals("added") ||
                        obj.getString("status").equals("updated") ||
                        obj.getString("status").equals("nothingToUpdate") ||
                        obj.getString("status").equals("updateViaMacBuffer")
                        )
                    checkOK("true", obj.getString("devEui"));
                else if (obj.getString("status").
                        equals("abpReginfoAlreadyExist") ||
                        obj.getString("status").
                                equals("otaaReginfoAlreadyExist"))

                {
                    existingDev.add(getExistingDevice(obj.getString("devEui")));
                } else {
                    checkOK("fasle", obj.getString("devEui"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return getDataForVegaWithoutRegInfo(existingDev);
    }

    private void checkOK(String result, String eui) {
        Intent check = new Intent();
        check.setAction(CheckRequest.ACTION);
        check.putExtra("result", result);
        check.putExtra("eui", eui);
        Log.d("Sync", "send broadcast: result - " + result + "; eui - " + eui);

        sendBroadcast(check);

        sendToMain(result, eui);
    }

    private void sendToMain(String result, String message) {
        if (result.equals("true")) {
            Intent si = new Intent().setAction(MainActivity.responseFromIS).
                    putExtra("alias", "device").
                    putExtra("message", message);
            sendBroadcast(si);
        } else if (result.equals("false")) {
            Intent si = new Intent().setAction(MainActivity.responseFromIS).
                    putExtra("alias", "false").
                    putExtra("message", message);
            sendBroadcast(si);
        }
    }


}
