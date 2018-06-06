package ru.yugsys.vvvresearch.lconfig.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import org.greenrobot.greendao.query.Query;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.yugsys.vvvresearch.lconfig.App;
import ru.yugsys.vvvresearch.lconfig.R;
import ru.yugsys.vvvresearch.lconfig.Services.WebSocketListener;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.DeviceEntryDao;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.NetDataDao;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DeviceEntry;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.NetData;
import ru.yugsys.vvvresearch.lconfig.model.LoginData;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private EditText mLoginView;
    private EditText mServerView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Spinner typeOfServerSpinner;
    private List<NetData> netList;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String[] type = getResources().getStringArray(R.array.serviceList);
        setContentView(R.layout.activity_login);
        mLoginView = (EditText) findViewById(R.id.login);
        mPasswordView = (EditText) findViewById(R.id.password);
        typeOfServerSpinner = (Spinner) findViewById(R.id.typeServer);
        netList = getAllService();
        String service = "";
        for (NetData netData : netList) {
            if (netData.getCheckMain() == true) {
                service = netData.getServiceName();
                break;
            }
        }

        for (int i = 0; i < type.length; i++) {
            if (service.equals(type[i]))
                typeOfServerSpinner.setSelection(i);
        }
        typeOfServerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int position, long id) {
                netList = getAllService();
                NetData currentService = new NetData();
                for (NetData nd : netList) {
                    if (nd.getServiceName().equals(type[position])) {
                        currentService = nd;
                    }
                }
                switch (type[position]) {
                    case "net868.ru":
                        mLoginView.setHint((CharSequence) "token");
                        mLoginView.setText(currentService.getToken(), TextView.BufferType.EDITABLE);
                        mPasswordView.setVisibility(View.GONE);
                        mServerView.setText(currentService.getAddress(), TextView.BufferType.EDITABLE);
                        chosenService(currentService);

//                        mLoginView.setText("");
                        break;
                    case "Вега":
                        mLoginView.setHint((CharSequence) "login");
                        mPasswordView.setHint((CharSequence) "pass");
                        mLoginView.setText(currentService.getLogin(), TextView.BufferType.EDITABLE);
                        mPasswordView.setText(currentService.getPassword(), TextView.BufferType.EDITABLE);
                        mPasswordView.setVisibility(View.VISIBLE);
                        mServerView.setText(currentService.getAddress(), TextView.BufferType.EDITABLE);
                        chosenService(currentService);
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mServerView = (EditText) findViewById(R.id.connectServer);



        mServerView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
//                  Toast.makeText(getApplicationContext(), "Sign button!", Toast.LENGTH_SHORT);
                    return true;
                }
                return false;
            }
        });

        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);

        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                final NetData net;

                JSONObject jsonObject = new JSONObject();
                String[] type = getResources().getStringArray(R.array.serviceList);
                switch ((String) typeOfServerSpinner.getAdapter().getItem(typeOfServerSpinner.getSelectedItemPosition())) {
                    case "net868.ru":
                        Toast.makeText(getApplicationContext(), "net868.ru", Toast.LENGTH_SHORT).show();
                        if (checkToken(mLoginView.getText().toString()) &&
                                checkUrl(mServerView.getText().toString())) {
                            net = new NetData();
                            net.setAddress(mServerView.getText().toString());
                            net.setToken(mLoginView.getText().toString());
                            net.setPassword("pass");
                            net.setLogin("login");
                            net.setServiceName("net868.ru");
                            Toast.makeText(getApplicationContext(), getString(R.string.WriteSucessfull), Toast.LENGTH_SHORT);

                            ((App) getApplication()).getModel().addNetData(net);
                            chosenService(net);
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.incorrectlyToken) + " or URL", Toast.LENGTH_SHORT).show();
                        }


                        break;
                    case "Вега":

                        net = new NetData();
                        net.setServiceName("Вега");
                        net.setLogin(mLoginView.getText().toString());
                        net.setPassword(mPasswordView.getText().toString());
                        net.setAddress(mServerView.getText().toString());
                        net.setToken("emptyToken");
                        ((App) getApplication()).getModel().addNetData(net);
                        chosenService(net);
                        break;
                }
            }
        });
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgressBar(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }



    private class AuthThread extends AsyncTask<Void, Void, Void> {
        private String name;
        private String password;
        private String body;

        @Override
        protected void onPreExecute() {
            name = mLoginView.getText().toString();
            password = mPasswordView.getText().toString();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(getApplicationContext(), body, Toast.LENGTH_SHORT).show();
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            final String url = "http://iot.net868.ru/auth";
            HttpAuthentication httpAuthentication = new HttpBasicAuthentication("e1850@mail.ru"
                    , "pfndh34g");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setAuthorization(httpAuthentication);
            httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            Log.d("test", "before");
            try {
                ResponseEntity<String> r = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<Object>(httpHeaders), String.class);
                Log.d("test", r.getBody());
                Log.d("test", r.getStatusCode().toString());
                body = r.getBody();

            } catch (HttpClientErrorException e) {

            }
            return null;
        }
    }

    private boolean checkToken(String token) {
        Pattern pattern = Pattern.compile("[\\d|\\w]{32}");
        return pattern.matcher(token).matches();
    }

    private boolean checkUrl(String url) {
        Pattern pattern = Pattern.compile("^((https://)|(http://))\\S+");
        return pattern.matcher(url).matches();
    }

    private static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected;
        if (networkInfo != null) isConnected = networkInfo.isConnected();
        else isConnected = false;
        return isConnected;
    }

    private List<NetData> getAllService() {
        return ((App) getApplication()).getModel().daoSession.getNetDataDao().queryBuilder().build().list();
    }

    private void chosenService(NetData netData) {
        List<NetData> list = getAllService();
        for (NetData nd : list) {
            if (!nd.getServiceName().equals(netData.getServiceName())) {
                nd.setCheckMain(false);
                ((App) getApplication()).getModel().addNetData(nd);
            } else {
                nd.setCheckMain(true);
                ((App) getApplication()).getModel().addNetData(nd);

            }
        }
        Log.d("Service:", "chosen Service: " + netData.getServiceName());
    }


}


