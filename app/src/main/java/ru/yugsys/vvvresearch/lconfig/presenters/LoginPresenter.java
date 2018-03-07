package ru.yugsys.vvvresearch.lconfig.presenters;

import android.os.AsyncTask;
import android.text.TextUtils;
import ru.yugsys.vvvresearch.lconfig.R;
import ru.yugsys.vvvresearch.lconfig.model.Interfaces.Model;
import ru.yugsys.vvvresearch.lconfig.model.LoginData;
import ru.yugsys.vvvresearch.lconfig.views.LoginViewable;

public class LoginPresenter implements LoginPresentable {
    private Model model;
    private LoginViewable loginView = null;
    private UserLoginTask mAuthTask = null;

    public LoginPresenter(Model model) {
        this.model = model;
    }


    @Override
    public void bind(LoginViewable loginView) {
        this.loginView = loginView;
    }

    @Override
    public void unBind() {
        this.loginView = null;
    }

    public void attemptToLogin() {
        if (mAuthTask != null) {
            return;
        }

        String login = loginView.getLogin();
        String password = loginView.getPassword();
        String server = loginView.getServer();

        boolean cancel = false;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {

            loginView.firePasswordError(R.string.error_invalid_password);

            cancel = true;
        }

        if (TextUtils.isEmpty(login)) {
            loginView.fireLoginError(R.string.error_field_required);
            cancel = true;
        } else if (!isLoginValid(login)) {
            loginView.fireLoginError(R.string.error_invalid_login);
            cancel = true;
        }

        if (!cancel) {
            loginView.fireShowSignProgress(true);
            mAuthTask = new UserLoginTask(login, password, server);
            mAuthTask.execute((Void) null);
        }
    }

    @Override
    public void saveLoginData() {
        String login = loginView.getLogin();
        String password = loginView.getPassword();
        String server = loginView.getServer();
        model.writeAuthData(login, password, server);
        loginView.fireCloseLoginView();
    }

    @Override
    public LoginData loadLoginData() {
        return null;
    }

    private boolean isLoginValid(String login) {
        return login.matches("^[a-zA-Z0-9]+$");
    }

    private boolean isPasswordValid(String password) {
        return password.matches("^[a-zA-Z0-9]+$");
    }

    private boolean isServerValid(String serverURL) {
        return true;
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String login;
        private final String password;
        private final String server;

        UserLoginTask(String login, String password, String server) {
            this.login = login;
            this.password = password;
            this.server = server;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return model.testLoginConnection(login, password, server);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            loginView.fireShowSignProgress(false);
            if (!success) {
                loginView.firePasswordError(R.string.error_incorrect_password);
            } else
                loginView.fireCloseLoginView();
        }

    }

}
