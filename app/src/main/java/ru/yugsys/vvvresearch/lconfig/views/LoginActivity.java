package ru.yugsys.vvvresearch.lconfig.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Text;
import ru.yugsys.vvvresearch.lconfig.App;
import ru.yugsys.vvvresearch.lconfig.R;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.NetData;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private EditText mLoginView;
    private EditText mServerView;
    private EditText mPasswordView;
    private TextView mLoginCaption;
    private TextView mPasswordCaption;
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
        typeOfServerSpinner = (Spinner) findViewById(R.id.typeOfService);
        netList = getAllService();
        mLoginCaption = findViewById(R.id.loginCaption);
        mPasswordCaption = findViewById(R.id.passwordCaption);
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
                        mLoginView.setHint(R.string.promt_token);
                        mPasswordCaption.setVisibility(View.GONE);
                        mLoginCaption.setText(R.string.promt_token);
                        mLoginView.setText(currentService.getToken(), TextView.BufferType.EDITABLE);
                        mPasswordView.setVisibility(View.GONE);
                        mServerView.setText(currentService.getAddress(), TextView.BufferType.EDITABLE);
//                        chosenService(currentService);

//                        mLoginView.setText("");
                        break;
                    case "Вега":
                        mLoginView.setHint(R.string.promt_login);
                        mPasswordView.setHint(getString(R.string.promt_pass));
                        mLoginView.setText(currentService.getLogin(), TextView.BufferType.EDITABLE);
                        mPasswordView.setText(currentService.getPassword(), TextView.BufferType.EDITABLE);
                        mPasswordView.setVisibility(View.VISIBLE);
                        mPasswordCaption.setVisibility(View.VISIBLE);
                        mServerView.setText(currentService.getAddress(), TextView.BufferType.EDITABLE);
//                        chosenService(currentService);
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

                        if (CheckData(mLoginView.getText().toString(), mServerView.getText().toString())) {
                            net = new NetData();
                            net.setAddress(mServerView.getText().toString());
                            net.setToken(mLoginView.getText().toString());
                            net.setPassword("pass");
                            net.setLogin("login");
                            net.setServiceName("net868.ru");
//                            Toast.makeText(getApplicationContext(), getString(R.string.WriteSucessfull), Toast.LENGTH_SHORT);
                            showDiffrentSnackBar(getString(R.string.WriteSucessfull), 1);

                            ((App) getApplication()).getModel().addNetData(net);
                            chosenService(net);
                        } else {
//                            Toast.makeText(getApplicationContext(), getString(R.string.incorrectlyToken) + " or URL", Toast.LENGTH_SHORT).show();
                            showDiffrentSnackBar(getString(R.string.incorrectlyToken), 0);
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
//            Toast.makeText(getApplicationContext(), body, Toast.LENGTH_SHORT).show();
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

    private boolean CheckData(String token, String url) {
        int errorColor;
        int successColor;
        boolean check = true;
        if (Build.VERSION.SDK_INT >= 23) {
            errorColor = ContextCompat.getColor(getApplicationContext(), R.color.errorColor);
            successColor = ContextCompat.getColor(getApplicationContext(), R.color.successColor);
        } else {
            errorColor = getResources().getColor(R.color.errorColor);
            successColor = getResources().getColor(R.color.successColor);
        }
        ForegroundColorSpan errorColorSpan = new ForegroundColorSpan(errorColor);
        ForegroundColorSpan successColorSpan = new ForegroundColorSpan(successColor);
        SpannableStringBuilder successSB;
        SpannableStringBuilder spannableStringBuilder;

        Pattern pattern = Pattern.compile("[\\d|\\w]{32}");

        if (!pattern.matcher(token).matches()) {
            spannableStringBuilder = new SpannableStringBuilder("Error");
            spannableStringBuilder.setSpan(errorColorSpan, 0, "Error".length(), 0);
            mLoginView.setError(spannableStringBuilder);
            return false;

        }

        pattern = Pattern.compile("^((https://)|(http://))\\S+");
        if (!pattern.matcher(url).matches()) {
            spannableStringBuilder = new SpannableStringBuilder("Error");
            spannableStringBuilder.setSpan(errorColorSpan, 0, "Error".length(), 0);
            mServerView.setError(spannableStringBuilder);
            return false;

        }
        return true;
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
//        Toast.makeText(getApplicationContext(), netData.getServiceName(), Toast.LENGTH_SHORT).show();
        showDiffrentSnackBar(netData.getServiceName(), 1);
        Log.d("Service:", "chosen Service: " + netData.getServiceName());
    }

    private void showDiffrentSnackBar(String msg, int me) {

        int color;
        if (me == 1) {

            color = Color.WHITE;
        } else {

            color = Color.RED;
        }

        Snackbar snackbar = Snackbar
                .make(mLoginFormView, msg, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }






}


