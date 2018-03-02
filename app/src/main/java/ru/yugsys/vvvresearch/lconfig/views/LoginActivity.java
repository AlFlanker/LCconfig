package ru.yugsys.vvvresearch.lconfig.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import ru.yugsys.vvvresearch.lconfig.App;
import ru.yugsys.vvvresearch.lconfig.R;
import ru.yugsys.vvvresearch.lconfig.Services.GPSTracker;
import ru.yugsys.vvvresearch.lconfig.Services.GPScallback;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DataDevice;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DataRead;
import ru.yugsys.vvvresearch.lconfig.model.DataModel;
import ru.yugsys.vvvresearch.lconfig.model.Manager.EventManager;
import ru.yugsys.vvvresearch.lconfig.presenters.LoginPresentable;
import ru.yugsys.vvvresearch.lconfig.presenters.LoginPresenter;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoginViewable {
    // UI references.
    private EditText mLoginView;
    private EditText mServerView;

    private static final int REQUEST_READ_CONTACTS = 0;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private GPSTracker gps;
    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;
    final static String TAG = "NFC";
    public DataDevice dataDevice;
    private LoginPresenter.UserLoginTask mAuthTask = null;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private LoginPresentable presenter;

    private Button BtnRead;

    private DataModel dataModel;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // для сохранения инфы о активность - для gps итд
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("OnCreate", "create activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //  Intent intent = new Intent(this, GPSTracker.class);
//        startService(intent);
        Log.d("OnCreate", "t");
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_CODE);
        }

//        listPresenter.bindView(this);
//        listPresenter.setModel(dataModel);
//        dataModel = new DataModel(((App) getApplication()).getDaoSession());
//        dataModel.eventManager.subscribe(EventManager.TypeEvent.OnDataReceive, listPresenter);
//        dataModel.eventManager.subscribe(EventManager.TypeEvent.OnDevDataChecked, listPresenter);
//        dataModel.eventManager.subscribe(EventManager.TypeEvent.OnNFCconnected, listPresenter);
//        dataModel.eventManager.subscribe(EventManager.TypeEvent.OnGPSdata, listPresenter);

        gps = GPSTracker.instance();
        gps.setContext(this);
        gps.OnStartGPS();
        gps.onChange(dataModel);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
//        populateAutoComplete();
//
//        presenter = new LoginPresenter(BusinessModel.getInstance());
//        presenter.bind(this);

        mLoginView = (EditText) findViewById(R.id.login);
        mPasswordView = (EditText) findViewById(R.id.password);
        mServerView = (EditText) findViewById(R.id.connectServer);

        mServerView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    presenter.attemptLogin();
                    return true;
                }
                return false;
            }
        });


        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.attemptLogin();
            }
        });
        BtnRead = (Button) findViewById(R.id.read);
//        BtnRead.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listPresenter.callReadNFC();
//            }
//        });
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        //*************************************************
        mAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mAdapter.isEnabled()) {
            mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
            IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
            mFilters = new IntentFilter[]{ndef,};
            mTechLists = new String[][]{new String[]{android.nfc.tech.NfcV.class.getName()}};
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {

        // TODO Auto-generated method stub
        Log.d(TAG, "onNewIntent");
        super.onNewIntent(intent);
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            dataDevice = new DataDevice();
            dataDevice.setCurrentTag(tagFromIntent);
//            listPresenter.currentDataDevice = dataDevice;
//            GetSystemInfoAnswer= NFCCommand.SendGetSystemInfoCommandCustom(tagFromIntent,dataDevice);
//            Log.d(TAG,GetSystemInfoAnswer.toString());
//            Log.d(TAG,dataDevice.getUid());
//            Log.d(TAG,dataDevice.getManufacturer());
//            dataDevice = Helper.DecodeGetSystemInfoResponse(GetSystemInfoAnswer,dataDevice);
//            if(dataDevice!=null)
//            {
//                Log.d(TAG,"DecodeGetSystemInfoResponse");
//                Log.d(TAG,tagFromIntent.toString());
//                Log.d(TAG,dataDevice.getManufacturer());
//                Log.d(TAG,dataDevice.getUid());
//                //Intent intentScan = new Intent(this, Scan.class);
//                // startActivity(intentScan);
//            }
//            else
//            {
//                return;
//            }
        }
    }

//    private void populateAutoComplete() {
//        if (!mayRequestContacts()) {
//            return;
//        }
//
//        getLoaderManager().initLoader(0, null, this);
//    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
//            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
//                    .setAction(android.R.string.ok, new View.OnClickListener() {
//                        @Override
//                        @TargetApi(Build.VERSION_CODES.M)
//                        public void onClick(View v) {
//                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
//                        }
//                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);

        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //**************

    @Override
    protected void onResume() {
        super.onResume();
        gps.setContext(this);
        gps.onChange(dataModel);
        gps.OnResumeGPS();
        Log.d(TAG, "OnResume");
        //Used for DEBUG : Log.v("NFCappsActivity.java", "ON RESUME NFC APPS ACTIVITY");
        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists);
        Log.d(TAG, "OnResume end");

    }

    @Override
    protected void onPause() {
        super.onPause();
        gps.stop();
//        this.cpt = 500L;
        mAdapter.disableForegroundDispatch(this);

    }


    //****************


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }
//        Intent act = new Intent(this, EditAndViewActivity.class);
//        startActivity(act);
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }



        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
//            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }



    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
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
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
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
        showProgress(isShow);
    }

    @Override
    public void fireCloseLoginView() {
        finish();
    }
}


