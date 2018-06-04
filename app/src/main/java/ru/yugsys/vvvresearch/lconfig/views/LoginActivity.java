package ru.yugsys.vvvresearch.lconfig.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
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
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.yugsys.vvvresearch.lconfig.App;
import ru.yugsys.vvvresearch.lconfig.R;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.NetData;
import ru.yugsys.vvvresearch.lconfig.model.LoginData;


import java.util.Collections;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements LoginViewable {
    private EditText mLoginView;
    private EditText mServerView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Spinner typeOfServerSpinner;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mLoginView = (EditText) findViewById(R.id.login);
        mPasswordView = (EditText) findViewById(R.id.password);

        typeOfServerSpinner = (Spinner) findViewById(R.id.typeServer);
        typeOfServerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int position, long id) {
                String[] type = getResources().getStringArray(R.array.serviceList);
                switch (type[position]) {
                    case "net868.ru":
                        mLoginView.setHint((CharSequence) "token");
                        mPasswordView.setVisibility(View.GONE);
                        mLoginView.setText("");

                        break;
                    case "Вега":
                        mLoginView.setHint((CharSequence) "login");
                        mPasswordView.setHint((CharSequence) "pass");
                        mLoginView.setText("");
                        mPasswordView.setText("");
                        mPasswordView.setVisibility(View.VISIBLE);
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
//                    presenter.attemptToLogin();
                    return true;
                }
                return false;
            }
        });

        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                String[] type = getResources().getStringArray(R.array.serviceList);
                switch ((String) typeOfServerSpinner.getAdapter().getItem(typeOfServerSpinner.getSelectedItemPosition())) {
                    case "net868.ru":
                        Toast.makeText(getApplicationContext(), "net868.ru", Toast.LENGTH_SHORT).show();
                        if (checkToken(mLoginView.getText().toString()) &&
                                checkUrl(mServerView.getText().toString())) {
                            NetData net = new NetData();
                            net.setAddress(mServerView.getText().toString());
                            net.setToken(mLoginView.getText().toString());
                            net.setPassword("pass");
                            net.setLogin("login");
                            net.setServiceName("net868.ru");
                            Toast.makeText(getApplicationContext(), getString(R.string.WriteSucessfull), Toast.LENGTH_SHORT);
                            ((App) getApplication()).getModel().addNetData(net);
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.incorrectlyToken) + " or URL", Toast.LENGTH_SHORT).show();
                        }


                        break;
                    case "Вега":
                        Toast.makeText(getApplicationContext(), "Вега", Toast.LENGTH_SHORT).show();

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

    //LoginViewable implements section
    @Override
    public String getLogin() {
        return mLoginView.getText().toString();
    }

    @Override
    public String getPassword() {
        return mPasswordView.getText().toString();
    }

    @Override
    public String getServer() {
        return mServerView.getText().toString();
    }

    @Override
    public void fireLoginError(int resIDErrorMessage) {
        mLoginView.setError(getString(resIDErrorMessage));
        mLoginView.requestFocus();
    }

    @Override
    public void firePasswordError(int resIDErrorMessage) {
        mPasswordView.setError(getString(resIDErrorMessage));
        mPasswordView.requestFocus();
    }

    @Override
    public void fireServerError(int resIDErrorMessage) {
        mServerView.setError(getString(resIDErrorMessage));
        mServerView.requestFocus();
    }

    @Override
    public void fireShowSignProgress(boolean isShow) {
        showProgressBar(isShow);
    }

    @Override
    public void fireCloseLoginView() {
        finish();
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
}


