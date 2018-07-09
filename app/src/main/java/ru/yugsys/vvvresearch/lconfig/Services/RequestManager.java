package ru.yugsys.vvvresearch.lconfig.Services;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DeviceEntry;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.RESTData;

import java.util.Objects;

public class RequestManager {
    public RequestManager(String hostAPI,String prm) {
        this.hostAPI = hostAPI;
        this.prm = prm;
    }

    public enum REST_FUNCTION {
        CreateDevice, DeleteDevice, AppData, CommandTypesOfDevice, SendCommand
    }
    String hostAPI;
    String prm;


    public class GetRequest extends AsyncTask<String, Void, RESTData> {
        @Override
        protected RESTData doInBackground(String... params) {
// The connection URL
            String net868 = "https://bs.net868.ru:20010/externalapi/appdata?token=1c68a488ec0d4dde80439e9627d23154&count=1&offset=0&startDate=2018-05-22T10:50:33Z&endDate=2018-05-22T16:50:33Z&order=desc";
            String url = "https://ajax.googleapis.com/ajax/" +
                    "services/search/web?v=1.0&q={query}";
            RestTemplate restTemplate = new RestTemplate();

            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

            String test = restTemplate.getForObject(net868,String.class);
            Log.d("TEST",test);
            return null;
        }

        @Override
        protected void onPostExecute(RESTData restData) {


        }
    }
    public class POSTRequest extends AsyncTask<DeviceEntry, Void, RESTData> {
        @Override
        protected RESTData doInBackground(DeviceEntry... params) {
// The connection URL
            String net868 = "https://bs.net868.ru:20010/externalapi/appdata?token=1c68a488ec0d4dde80439e9627d23154&count=1&offset=0&startDate=2018-05-22T10:50:33Z&endDate=2018-05-22T16:50:33Z&order=desc";
            String url = "https://ajax.googleapis.com/ajax/" +
                    "services/search/web?v=1.0&q={query}";
            RestTemplate restTemplate = new RestTemplate();

            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

            String test = restTemplate.getForObject(net868,String.class);
            Log.d("TEST",test);
            return null;
        }

        @Override
        protected void onPostExecute(RESTData restData) {


        }
    }

    private JSONObject convert2JSON(DeviceEntry dev){
        JSONObject jsonObject = new JSONObject();
        JSONObject device = new JSONObject();
        JSONObject model = new JSONObject();
        JSONObject geo = new JSONObject();
        try {
            geo.put("countryddress", "Россия");
            geo.put("region", "Краснодарский край");
            geo.put("city", "Краснодар");
            geo.put("street","Московская");

            model.put("name","LC5xx");
            model.put("version","1.0");

            device.put("activationType","ABP");
            device.put("alias", (dev.getComment().isEmpty()) ? "LC5xx" : dev.getComment());
            device.put("eui",dev.getEui().toLowerCase());
            device.put("applicationEui",dev.getAppeui().toLowerCase());
//            device.put("devAddr":"64-3f-c4-97");
//            device.put("networkSessionKey":"2B7E151628AED2A6ABF7158809CF4F3C");
//            device.put("applicationSessionKey":"2B7E151628AED2A6ABF7158809CF4F3C");
//            device.put("access":"Private");
//            device.put("loraClass":"a");
//            device.put();


        }
        catch (JSONException e){
            Log.e("json",e.getMessage());
        }
        return jsonObject;
    }
}
