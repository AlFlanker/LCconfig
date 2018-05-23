package ru.yugsys.vvvresearch.lconfig.Services.RequestsManager.Strategy;

import android.os.AsyncTask;
import android.util.Log;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.RESTData;

import java.util.EnumMap;
import java.util.Map;

public class GET extends AsyncTask<Void, Void, Void> implements REST {


    /**
     * для callBack
     *
     * @param hostAPI    - hostAPI
     * @param parameters - parameters
     */
    public GET(String hostAPI, EnumMap<REST_PRM, String> parameters) {
        this.hostAPI = hostAPI;
        this.parameters = parameters;

    }

    private String hostAPI;
    private EnumMap<REST_PRM, String> parameters;
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
    protected Void doInBackground(Void... params) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity<?> entity = new HttpEntity<>(httpHeaders);
        HttpEntity<String> test = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);
        Log.d("TEST", test.getBody());
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {


    }

    @Override
    public int send() {
        this.execute();
        return 0;
    }

}
