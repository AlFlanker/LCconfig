package ru.yugsys.vvvresearch.lconfig.Services.RequestsManager.Strategy;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.RESTData;

import java.nio.charset.StandardCharsets;
import java.security.spec.ECField;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class POST extends AsyncTask<JSONObject, Void, RESTData> implements REST {
    /**
     * для callBack
     *
     * @param hostAPI    - hostAPI
     * @param parameters - pair key -> value
     * @param jsonObject - Send JSONObject
     */
    public POST(String hostAPI, EnumMap<REST.REST_PRM, String> parameters, JSONObject jsonObject) {
        this.hostAPI = hostAPI;
        this.parameters = parameters;
        this.object = jsonObject;
    }

    private String hostAPI;
    private EnumMap<REST.REST_PRM, String> parameters;
    private JSONObject object;
    private UriComponentsBuilder builder;

    @Override
    protected void onPreExecute() {
        builder = UriComponentsBuilder.fromHttpUrl(hostAPI);
        builder.queryParam("token", parameters.get(REST_PRM.token));

        Log.d("TEST", builder.toUriString());

    }

    @Override
    protected RESTData doInBackground(JSONObject... params) {
        String s = "{\"token\":\"1c68a488ec0d4dde80439e9627d23154\",\"device\":{\"activationType\":\"ABP\",\"alias\":\"LCTest\",\"eui\":\"74-e1-4a-4f-97-c4-3f-64\",\"applicationEui\":\"00-00-00-00-00-00-00-01\",\"devAddr\":\"64-3f-c4-97\",\"networkSessionKey\":\"2B7E151628AED2A6ABF7158809CF4F3C\",\"applicationSessionKey\":\"2B7E151628AED2A6ABF7158809CF4F3C\",\"access\":\"Private\",\"loraClass\":\"a\",\"model\":{\"name\":\"LC5xx\",\"version\":\"1.0\"},\"address\":{\"countryddress\":\"Russia\",\"region\":\"Krd\",\"city\":\"Krasnodar\",\"street\":\".....\"}}}";
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
//        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<String>(params[0].toString(), httpHeaders);
            Log.d("TEST", entity.getBody());
            Log.d("TEST",params[0].toString());

            ResponseEntity<String> test = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.POST, entity, String.class);
            Log.d("TEST",String.valueOf(test.getStatusCode()));

        } catch (HttpClientErrorException e) {
            Log.d("TEST", e.toString());
        } catch (Exception e) {
            Log.d("TEST", e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(RESTData restData) {


    }

    @Override
    public int send() {
        this.execute(this.object);
        return 0;
    }
}

