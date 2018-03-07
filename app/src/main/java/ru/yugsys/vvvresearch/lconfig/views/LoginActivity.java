package ru.yugsys.vvvresearch.lconfig.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import ru.yugsys.vvvresearch.lconfig.App;
import ru.yugsys.vvvresearch.lconfig.R;
import ru.yugsys.vvvresearch.lconfig.model.LoginData;
import ru.yugsys.vvvresearch.lconfig.presenters.LoginPresentable;
import ru.yugsys.vvvresearch.lconfig.presenters.LoginPresenter;

public class LoginActivity extends AppCompatActivity implements LoginViewable {
    private EditText mLoginView;
    private EditText mServerView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Spinner typeOfServerSpinner;
    private LoginPresentable presenter;

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
        mServerView = (EditText) findViewById(R.id.connectServer);
        typeOfServerSpinner = (Spinner) findViewById(R.id.typeServer);
        presenter = new LoginPresenter(((App) getApplication()).getModel());
        presenter.bind(this);
        LoginData loginData = presenter.loadLoginData();
        if (loginData != null) {
            mLoginView.setText(loginData.getLogin());
            mPasswordView.setText("");
            mServerView.setText(loginData.getTypeOfServer());
            String[] arrayTypeOfServer = loginData.getArrayTypeOfServer();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayTypeOfServer);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            typeOfServerSpinner.setAdapter(adapter);
            String currentTypeServer = loginData.getTypeOfServer();
            ArrayAdapter adapter1 = (ArrayAdapter) typeOfServerSpinner.getAdapter();
            int position = adapter1.getPosition(currentTypeServer);
            typeOfServerSpinner.setSelection(position);
        }

        mServerView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    presenter.attemptToLogin();
                    return true;
                }
                return false;
            }
        });

        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.attemptToLogin();
            }
        });
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unBind();
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
}


