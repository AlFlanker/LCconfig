package ru.yugsys.vvvresearch.lconfig.Services.RequestsManager;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.yugsys.vvvresearch.lconfig.Services.RequestsManager.Strategy.REST;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.RESTData;
import ru.yugsys.vvvresearch.lconfig.views.MainActivity;

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
//        Log.d("net868", "onHandleIntent : " + intent.getAction());
        String s = "{\"token\":\"1c68a488ec0d4dde80439e9627d23154\",\"device\":{\"activationType\":\"ABP\",\"alias\":\"LCTest\",\"eui\":\"74-e1-4a-4f-97-c4-3f-64\",\"applicationEui\":\"00-00-00-00-00-00-00-01\",\"devAddr\":\"64-3f-c4-97\",\"networkSessionKey\":\"2B7E151628AED2A6ABF7158809CF4F3C\",\"applicationSessionKey\":\"2B7E151628AED2A6ABF7158809CF4F3C\",\"access\":\"Private\",\"loraClass\":\"a\",\"model\":{\"name\":\"LC5xx\",\"version\":\"1.0\"},\"address\":{\"countryddress\":\"Russia\",\"region\":\"Krd\",\"city\":\"Krasnodar\",\"street\":\".....\"}}}";
        String token = intent.getStringExtra("token");
        String hostAPI = intent.getStringExtra("hostAPI");
        final String payload = intent.getStringExtra("JSONobject");
//        Log.d("net868: ", token);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(hostAPI);
        builder.queryParam("token", token);
//        Log.d("net868", "UriComponentsBuilder: " + builder.toUriString());
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<String>(payload, httpHeaders);
            Log.d("net868: ", payload);
            Log.d("net868: ", entity.getHeaders().toString());

// для теста отключил 2 строчки ниже:
            ResponseEntity<String> test = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.POST, entity, String.class);
            Log.d("net868", "result of request: " + String.valueOf(test.getStatusCode()));
            if (test.getStatusCode() == HttpStatus.NO_CONTENT) {
                Intent check = new Intent();
                check.setAction(CheckRequest.ACTION);
                check.putExtra("result", true);
                check.putExtra("eui", payload.substring(payload.indexOf("eui\":\"") + 6, payload.indexOf(",\"applicationEui") - 1).replace("-", ""));
                Log.d("net868", "send: " + ExternalRequestsReceiver.ACTION);
                sendBroadcast(check);
                Intent si = new Intent().setAction(MainActivity.responseFromIS).
                        putExtra("alias", payload.
                                substring(payload.indexOf("alias\":\"") + 8, payload.indexOf(",\"eui") - 1)).
                        putExtra("eui", payload.
                                substring(payload.indexOf("eui\":\"") + 6, payload.indexOf(",\"applicationEui") - 1));
                sendBroadcast(si);
            } else if (test.getStatusCode() == HttpStatus.BAD_REQUEST) {
//                Intent si = new Intent().setAction(MainActivity.responseFromIS).
//                        putExtra("alias",
//                                "already exist").
//                        putExtra("eui",payload.
//                                substring(payload.indexOf("eui\":\"") + 6, payload.indexOf(",\"applicationEui") - 1));
//                sendBroadcast(si);
            }

        } catch (HttpClientErrorException e) {
            Log.d("net868", e.toString());
        } catch (Exception e) {
            Log.d("net868", e.toString());
        }
    }



}
