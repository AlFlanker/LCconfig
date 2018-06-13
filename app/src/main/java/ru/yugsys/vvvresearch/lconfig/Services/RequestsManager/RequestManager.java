package ru.yugsys.vvvresearch.lconfig.Services.RequestsManager;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.yugsys.vvvresearch.lconfig.Services.RequestsManager.Strategy.REST;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.RESTData;
import ru.yugsys.vvvresearch.lconfig.views.MainActivity;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.EnumMap;


public class RequestManager extends IntentService {
    private static Context mContext;
    public RequestManager() {
        super("Request RequestManager");
    }

    public RequestManager(String name) {
        super(name);
    }

    public static final String SEND_REST_REQUEST = "ru.yugsys.vvvresearch.lcconfig.Services.RequestManager";



    @Override
    protected void onHandleIntent(Intent intent) {

        String token = intent.getStringExtra("token");
        String hostAPI = intent.getStringExtra("hostAPI");
        final String payload = intent.getStringExtra("JSONobject");
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(hostAPI);
        builder.queryParam("token", token);
        /*Spring does't work with method DELETE with BODY*/
        /*body is left!!!*/
        try {

            HttpsURLConnection httpsURLConnection = null;
            DataOutputStream dataOutputStream = null;
            try {
                Log.d("Sync: ", "in");
                URL url = new URL(hostAPI.replace("createdevice", "deletedevice") + "token=" + token);
                Log.d("Sync: ", url.toString());
                httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setRequestMethod("DELETE");
                httpsURLConnection.setRequestProperty("Content-Type", "application/json");
                httpsURLConnection.setDoOutput(true);
                httpsURLConnection.setDoInput(true);
                Log.d("Sync", httpsURLConnection.getRequestMethod());
                dataOutputStream = new DataOutputStream(httpsURLConnection.getOutputStream());
                JSONObject ob = new JSONObject();

                ob.put("token", token).put("eui", payload.substring(payload.indexOf("eui\":\"") + 6, payload.indexOf("applicationEui") - 3));

                Log.d("Sync", ob.toString());

                dataOutputStream.write(ob.toString().getBytes(StandardCharsets.UTF_8));
                dataOutputStream.flush();
                dataOutputStream.close();
                httpsURLConnection.connect();
                BufferedReader in = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while ((temp = in.readLine()) != null) {
                    stringBuilder.append(temp);
                }
                in.close();
                Log.d("Sync", stringBuilder.toString());
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*For TEST!*/
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /*end*/

        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<String>(payload, httpHeaders);
            Log.d("Sync: ", payload);
            Log.d("Sync: ", entity.getHeaders().toString());

// для теста отключил 2 строчки ниже:
            ResponseEntity<String> test = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.POST, entity, String.class);
            Log.d("Sync", "result of request: " + String.valueOf(test.getStatusCode()));
            if (test.getStatusCode() == HttpStatus.NO_CONTENT) {
                checkOK(payload);
            } else if (test.getStatusCode() == HttpStatus.BAD_REQUEST) {

            }

        } catch (HttpClientErrorException e) {
            Log.d("Sync", e.getResponseBodyAsString());
            checkOK(payload);

        } catch (Exception e) {
            Log.d("Sync", e.toString());
        }
    }

    private void checkOK(String payload) {
        Intent check = new Intent();
        check.setAction(CheckRequest.ACTION);
        check.putExtra("result", "true");
        check.putExtra("eui", payload.substring(payload.indexOf("eui\":\"") + 6, payload.indexOf(",\"applicationEui") - 1).replace("-", ""));
        Log.d("Sync", "send: " + ExternalRequestsReceiver.ACTION);
        sendBroadcast(check);


        Intent si = new Intent().setAction(MainActivity.responseFromIS).
                putExtra("alias", payload.
                        substring(payload.indexOf("alias\":\"") + 8, payload.indexOf(",\"eui") - 1)).
                putExtra("message", payload.
                        substring(payload.indexOf("eui\":\"") + 6, payload.indexOf(",\"applicationEui") - 1));
        sendBroadcast(si);
    }


}
