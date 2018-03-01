package ru.yugsys.vvvresearch.lconfig.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.*;
import android.content.pm.PackageManager;
import android.location.Location;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.LoginFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

import ru.yugsys.vvvresearch.lconfig.App;
import ru.yugsys.vvvresearch.lconfig.Manifest;
import ru.yugsys.vvvresearch.lconfig.R;
import ru.yugsys.vvvresearch.lconfig.Services.GPSTracker;

import ru.yugsys.vvvresearch.lconfig.Services.GPScallback;
import ru.yugsys.vvvresearch.lconfig.Services.Helper;
import ru.yugsys.vvvresearch.lconfig.Services.NFCCommand;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.DeviceDao;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DataDevice;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DataRead;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.Device;
import ru.yugsys.vvvresearch.lconfig.model.DataModel;
import ru.yugsys.vvvresearch.lconfig.model.Manager.EventManager;
import ru.yugsys.vvvresearch.lconfig.presenters.Presentable.DataActivityPresenter;
import ru.yugsys.vvvresearch.lconfig.presenters.Presentable.ListPresenter;
import ru.yugsys.vvvresearch.lconfig.views.Activities.EditAndViewActivity;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>,GPScallback<Location> {
    private static final int REQUEST_READ_CONTACTS = 0;
    private static final int PERMISSION_REQUEST_CODE = 100;
    protected ListPresenter listPresenter = new DataActivityPresenter();
    private GPSTracker gps;
    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;
    final static String TAG = "NFC";
    public DataDevice dataDevice;
    private UserLoginTask mAuthTask = null;
    byte[] GetSystemInfoAnswer = null;
    byte[] ReadMultipleBlockAnswer = null;
    byte[] numberOfBlockToRead = null;
    private long cpt = 0L;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    List<DataRead> listOfData = null;
    private Button BtnRead;
    String[] catBlocks = null;
    String[] catValueBlocks = null;
    private DataModel dataModel;
    String startAddressString;
    byte[] addressStart = null;
    String sNbOfBlock = null;
    int nbblocks = 0;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // для сохранения инфы о активность - для gps итд
    }

    @Override
    public void OnGPSdata(Location gps) {
        Log.d("GPS",gps.toString());
    }

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */


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
        gps = GPSTracker.instance();
        gps.setContext(this);
        gps.OnStartGPS();
        gps.onChange(this);
        DataModel dataModel = new DataModel(((App)getApplication()).getDaoSession());
        dataModel.eventManager.subscribe(EventManager.TypeEvent.OnDataReceive, listPresenter);
        dataModel.eventManager.subscribe(EventManager.TypeEvent.OnDevDataChecked, listPresenter);
        dataModel.eventManager.subscribe(EventManager.TypeEvent.OnNFCconnected, listPresenter);
        listPresenter.bindView(this);
        listPresenter.setModel(dataModel);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        BtnRead = (Button) findViewById(R.id.read);
        BtnRead.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new StartReadTask().execute(new Void[0]);
            }
        });
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        //*************************************************
        mAdapter = NfcAdapter.getDefaultAdapter(this);
        if(mAdapter.isEnabled()){
            mPendingIntent = PendingIntent.getActivity(this, 0,new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
            IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
            mFilters = new IntentFilter[] {ndef,};
            mTechLists = new String[][] { new String[] { android.nfc.tech.NfcV.class.getName() } };
        }
    }


    @Override
    protected void onNewIntent(Intent intent)
    {

        // TODO Auto-generated method stub
        Log.d(TAG,"onNewIntent");
        super.onNewIntent(intent);
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action))
        {

            Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            dataDevice = new DataDevice();
            Log.d(TAG,dataDevice.getManufacturer() +  " 1");
            dataDevice.setCurrentTag(tagFromIntent);
            Log.d(TAG,dataDevice.getManufacturer() +  " 2");

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

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
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
        gps.onChange(this);
        gps.OnResumeGPS();
        Log.d(TAG,"OnResume");
        //Used for DEBUG : Log.v("NFCappsActivity.java", "ON RESUME NFC APPS ACTIVITY");
        mPendingIntent = PendingIntent.getActivity(this, 0,new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists);
        Log.d(TAG,"OnResume end");

    }

    @Override
    protected void onPause() {
        super.onPause();
        gps.stop();
        this.cpt = 500L;
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
        Intent act = new Intent(this, EditAndViewActivity.class);
        startActivity(act);
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

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
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
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
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

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }


    private class StartReadTask extends AsyncTask<Void, Void, Void> {
        // private final ProgressDialog dialog;

        private StartReadTask() {
            //this.dialog = new ProgressDialog(LoginActivity.this);
        }

        protected void onPreExecute() {
            DataDevice dataDevice = LoginActivity.this.dataDevice;
            LoginActivity.this.GetSystemInfoAnswer = NFCCommand.SendGetSystemInfoCommandCustom(dataDevice.getCurrentTag(), dataDevice);
            if ((LoginActivity.this.dataDevice = Helper.DecodeGetSystemInfoResponse(LoginActivity.this.GetSystemInfoAnswer, dataDevice)) != null) {
                LoginActivity.this.startAddressString = "0";
                LoginActivity.this.startAddressString = Helper.castHexKeyboard(LoginActivity.this.startAddressString);
                LoginActivity.this.startAddressString = Helper.FormatStringAddressStart(LoginActivity.this.startAddressString, dataDevice);
                LoginActivity.this.addressStart = Helper.ConvertStringToHexBytes(LoginActivity.this.startAddressString);
                LoginActivity.this.sNbOfBlock = "128";
                LoginActivity.this.sNbOfBlock = Helper.FormatStringNbBlockInteger(LoginActivity.this.sNbOfBlock, LoginActivity.this.startAddressString, dataDevice);
                LoginActivity.this.numberOfBlockToRead = Helper.ConvertIntTo2bytesHexaFormat(Integer.parseInt(LoginActivity.this.sNbOfBlock));
            }
        }

        protected Void doInBackground(Void... params) {

            LoginActivity.this.ReadMultipleBlockAnswer = null;
            LoginActivity.this.cpt = 0L;
            if ((dataDevice = Helper.DecodeGetSystemInfoResponse(LoginActivity.this.GetSystemInfoAnswer, dataDevice)) != null) {
                if (LoginActivity.this.dataDevice.isMultipleReadSupported() && Helper.Convert2bytesHexaFormatToInt(LoginActivity.this.numberOfBlockToRead) > 1) {
                    if (Helper.Convert2bytesHexaFormatToInt(LoginActivity.this.numberOfBlockToRead) < 32) {
                        while ((LoginActivity.this.ReadMultipleBlockAnswer == null || LoginActivity.this.ReadMultipleBlockAnswer[0] == 1) && LoginActivity.this.cpt <= 10L) {
                            LoginActivity.this.ReadMultipleBlockAnswer = NFCCommand.SendReadMultipleBlockCommandCustom(dataDevice.getCurrentTag(), LoginActivity.this.addressStart, LoginActivity.this.numberOfBlockToRead[1], dataDevice);
                            LoginActivity.this.cpt = LoginActivity.this.cpt + 1L;
                        }

                        LoginActivity.this.cpt = 0L;
                    } else {
                        while ((LoginActivity.this.ReadMultipleBlockAnswer == null || LoginActivity.this.ReadMultipleBlockAnswer[0] == 1) && LoginActivity.this.cpt <= 10L) {
                            LoginActivity.this.ReadMultipleBlockAnswer = NFCCommand.SendReadMultipleBlockCommandCustom2(dataDevice.getCurrentTag(), LoginActivity.this.addressStart, LoginActivity.this.numberOfBlockToRead, dataDevice);
                            LoginActivity.this.cpt = LoginActivity.this.cpt + 1L;
                        }

                        LoginActivity.this.cpt = 0L;
                    }
                } else {
                    while ((LoginActivity.this.ReadMultipleBlockAnswer == null || LoginActivity.this.ReadMultipleBlockAnswer[0] == 1) && LoginActivity.this.cpt <= 10L) {
                        LoginActivity.this.ReadMultipleBlockAnswer = NFCCommand.Send_several_ReadSingleBlockCommands_NbBlocks(dataDevice.getCurrentTag(), LoginActivity.this.addressStart, LoginActivity.this.numberOfBlockToRead, dataDevice);
                        LoginActivity.this.cpt = LoginActivity.this.cpt + 1L;
                    }

                    LoginActivity.this.cpt = 0L;
                }
            }

            return null;
        }

        protected void onPostExecute(Void unused) {
            Log.d("NFC", "Button Read CLICKED **** On Post Execute ");

            if ((dataDevice = Helper.DecodeGetSystemInfoResponse(LoginActivity.this.GetSystemInfoAnswer, dataDevice)) != null) {
                LoginActivity.this.nbblocks = Integer.parseInt(LoginActivity.this.sNbOfBlock);
                if (LoginActivity.this.ReadMultipleBlockAnswer != null && LoginActivity.this.ReadMultipleBlockAnswer.length - 1 > 0) {
                    if (LoginActivity.this.ReadMultipleBlockAnswer[0] == 0) {
                        LoginActivity.this.catBlocks = Helper.buildArrayBlocks(LoginActivity.this.addressStart, LoginActivity.this.nbblocks);
                        LoginActivity.this.catValueBlocks = Helper.buildArrayValueBlocks(LoginActivity.this.ReadMultipleBlockAnswer, LoginActivity.this.nbblocks);
                        LoginActivity.this.listOfData = new ArrayList();

                        for (int i = 0; i < LoginActivity.this.nbblocks; ++i) {
                            DataRead dataRead = new DataRead(LoginActivity.this.catBlocks[i], LoginActivity.this.catValueBlocks[i]);
                            LoginActivity.this.listOfData.add(dataRead);
                            Log.d("NFC", dataRead.toString());
                        }


                    }
                }
            }

        }
    }


}

