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
        Log.d("onHandleIntent", "onHandleIntent : " + intent.getAction());
        String token = intent.getStringExtra("token");
        String hostAPI = intent.getStringExtra("hostAPI");
        String payload = intent.getStringExtra("JSONobject");
        Log.d("onHandleIntent: ", token);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(hostAPI);
        builder.queryParam("token", token);
        Log.d("onHandleIntent", "UriComponentsBuilder: " + builder.toUriString());
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<String>(payload, httpHeaders);
            Log.d("onHandleIntent: ", payload);
            Log.d("onHandleIntent: ", entity.getHeaders().toString());

// для теста отключил 2 строчки ниже:
            ResponseEntity<String> test = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.POST, entity, String.class);
            Log.d("onHandleIntent", "result of request: " + String.valueOf(test.getStatusCode()));
            if (test.getStatusCode() == HttpStatus.NO_CONTENT) {
                Intent check = new Intent();
                check.setAction(CheckRequest.ACTION);
                check.putExtra("result", true);
                check.putExtra("id", 204l);
                Log.d("CheckRequest", "send: " + ExternalRequestsReceiver.ACTION);
                sendBroadcast(check);
//
            }

        } catch (HttpClientErrorException e) {
            Log.d("onHandleIntent", e.toString());
        } catch (Exception e) {
            Log.d("onHandleIntent", e.toString());
        }
    }

    public static void startRequestManager(Context context) {
        mContext = context;

    }

}
