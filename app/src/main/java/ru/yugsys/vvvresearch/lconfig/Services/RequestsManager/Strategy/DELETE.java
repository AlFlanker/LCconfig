package ru.yugsys.vvvresearch.lconfig.Services.RequestsManager.Strategy;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.EnumMap;
import java.util.Map;

public class DELETE extends AsyncTask<JSONObject, Void, Void> implements REST {

    /**
     * для callBack
     *
     * @param hostAPI    - hostAPI
     * @param parameters - parameters
     */
    public DELETE(String hostAPI, EnumMap<REST_PRM, String> parameters, JSONObject object) {
        this.hostAPI = hostAPI;
        this.parameters = parameters;
        this.object = object;

    }

    private String hostAPI;
    private EnumMap<REST_PRM, String> parameters;
    private JSONObject object;
    private UriComponentsBuilder builder;


    @Override
    protected void onPreExecute() {
        builder = UriComponentsBuilder.fromHttpUrl(hostAPI);
        for (Map.Entry<REST_PRM, String> e : parameters.entrySet()) {
            if (e.getValue() != null) builder.queryParam(e.getKey().toString(), e.getValue());
        }
        Log.d("TEST", builder.toUriString());
    }


    @Override
    protected Void doInBackground(JSONObject... params) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> entity = new HttpEntity<>(params[0], httpHeaders);
        HttpEntity<String> test = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.DELETE, entity, String.class);
        Log.d("TEST", test.getBody());
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {


    }

    @Override
    public int send() {
        this.execute(this.object);
        return 0;
    }
}
