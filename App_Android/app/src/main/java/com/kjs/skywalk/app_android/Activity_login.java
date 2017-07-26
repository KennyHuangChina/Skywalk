package com.kjs.skywalk.app_android;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.kjs.skywalk.communicationlibrary.CommandManager;
import com.kjs.skywalk.communicationlibrary.CommunicationError;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID;
import com.kjs.skywalk.communicationlibrary.IApiResults;

import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_USER_SALT;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_LOGIN_BY_PASSWORD;

/**
 * A login screen that offers login via email/password.
 */
public class Activity_login extends SKBaseActivity implements
        CommunicationInterface.CICommandListener, CommunicationInterface.CIProgressListener {

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
    private static final String LOGIN_MODE_TELEPHONE = "telephone";
    private static final String LOGIN_MODE_ACCOUNT = "account";

    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    // telephone login
    private AutoCompleteTextView mActv_telephone_num;
    private EditText mEt_verfication_code;
    private TextView mTv_get_verfication_code;

    // account login
    private AutoCompleteTextView mActv_account;
    private EditText mEt_password;

    private String mRand = "";
    private String mSalt = "";
    private final int MSG_LOGIN = 0;
    private final int MSG_HIDE_WAITING_WINDOW = 1;

    PopupWindowWaiting mWaitingWindow = null;
    private LinearLayout mContainer = null;
    private String mLoginMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginMode = SKLocalSettings.UISettings_get(this, SKLocalSettings.UISettingsKey_LoginMode, LOGIN_MODE_TELEPHONE);

        if (mLoginMode.equalsIgnoreCase(LOGIN_MODE_TELEPHONE))
            updateLoginLayoutById(R.id.tv_login_telephone);
        else
            updateLoginLayoutById(R.id.tv_login_account);

        List<String> arrAuto = new ArrayList<>();
        arrAuto.add("15306261804");

        // telephone login layout
        mActv_telephone_num = (AutoCompleteTextView) findViewById(R.id.actv_telephone_num);
        addArrayToAutoComplete(arrAuto, mActv_telephone_num);
        mActv_telephone_num.setThreshold(1);
        mActv_telephone_num.setOnEditorActionListener(mEditorActionListener);

        mEt_verfication_code = (EditText) findViewById(R.id.et_verfication_code);
        mTv_get_verfication_code = (TextView) findViewById(R.id.tv_get_verfication_code);

        // account login layout
        mActv_account = (AutoCompleteTextView) findViewById(R.id.actv_account);
        addArrayToAutoComplete(arrAuto, mActv_account);
        mActv_account.setThreshold(1);
        mActv_account.setOnEditorActionListener(mEditorActionListener);

        mEt_password = (EditText) findViewById(R.id.et_password);

        // Set up the login form.
//        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
//        List<String> arrAuto = new ArrayList<>();
//        arrAuto.add("15306261804");
//        addEmailsToAutoComplete(arrAuto);
//        mEmailView.setThreshold(1);
//
//
        mContainer=(LinearLayout) findViewById(R.id.container);
//
//        mPasswordView = (EditText) findViewById(R.id.password);
//        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
//                if (id == R.id.login || id == EditorInfo.IME_NULL) {
//                    attemptLogin();
//                    return true;
//                }
//                return false;
//            }
//        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

//        DisplayMetrics dm = new DisplayMetrics();
//        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
//        mWaitingWindow = new PopupWindowWaiting(this);
//        mWaitingWindow.setWidth(dm.widthPixels);
//        mWaitingWindow.setHeight(dm.heightPixels);
    }

    EditText.OnEditorActionListener mEditorActionListener = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.actv_telephone_num || id == R.id.actv_account || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
    };

    private void doLogin() {
        CommandManager CmdMgr = CommandManager.getCmdMgrInstance(this, this, this);
        if (mLoginMode.equalsIgnoreCase(LOGIN_MODE_TELEPHONE)) {

        } else {
            if(CmdMgr.LoginByPassword(mActv_account.getText().toString(), mEt_password.getText().toString(), mRand, mSalt) != CommunicationError.CE_ERROR_NO_ERROR) {
                mHandler.sendEmptyMessageDelayed(MSG_HIDE_WAITING_WINDOW, 1000);
            }
        }

    }

    private int logIn() {
        CommandManager CmdMgr = CommandManager.getCmdMgrInstance(this, this, this);
        String strUserSalt;
        if (mLoginMode.equalsIgnoreCase(LOGIN_MODE_TELEPHONE))
            strUserSalt = mActv_telephone_num.getText().toString();
        else
            strUserSalt = mActv_account.getText().toString();

        return CmdMgr.GetUserSalt(strUserSalt);
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_LOGIN:
                    doLogin();
                    break;
                case MSG_HIDE_WAITING_WINDOW: {
//                    mWaitingWindow.dismiss();
                    hideWaiting();
                    break;
                }

            }
        }
    };

    // tv_login_telephone or tv_login_account
    private void updateLoginLayoutById(int viewId) {
        switch (viewId) {
            case R.id.tv_login_telephone: {
                findViewById(R.id.tv_login_telephone).setSelected(true);
                findViewById(R.id.tv_login_account).setSelected(false);
                findViewById(R.id.ll_login_telephone).setVisibility(View.VISIBLE);
                findViewById(R.id.ll_login_account).setVisibility(View.GONE);

                mLoginMode = LOGIN_MODE_TELEPHONE;
                SKLocalSettings.UISettings_set(this, SKLocalSettings.UISettingsKey_LoginMode, mLoginMode);
                break;
            }

            case R.id.tv_login_account: {
                findViewById(R.id.tv_login_telephone).setSelected(false);
                findViewById(R.id.tv_login_account).setSelected(true);
                findViewById(R.id.ll_login_telephone).setVisibility(View.GONE);
                findViewById(R.id.ll_login_account).setVisibility(View.VISIBLE);

                mLoginMode = LOGIN_MODE_ACCOUNT;
                SKLocalSettings.UISettings_set(this, SKLocalSettings.UISettingsKey_LoginMode, mLoginMode);
                break;
            }
        }
    }

    public void onLoginTitleClick(View v) {
        updateLoginLayoutById(v.getId());
    }

    private AlertDialog mPasswordErrorDlg;
    private void showPasswordErrorDlg() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mPasswordErrorDlg == null) {
                    mPasswordErrorDlg = new AlertDialog.Builder(Activity_login.this).create();
                }
                mPasswordErrorDlg.show();
                mPasswordErrorDlg.setContentView(R.layout.dialog_password_error);

                mPasswordErrorDlg.findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPasswordErrorDlg.dismiss();
                    }
                });
            }
        });

    }

    public void onClickResponse(View v) {
        switch (v.getId()) {
            case R.id.tv_info_title: {
                finish();
                break;
            }

            case R.id.tv_login: {

//                showPasswordErrorDlg();

                //attemptLogin();
                InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
                if (this.getCurrentFocus() != null)
                    imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
                if(logIn() == CommunicationError.CE_ERROR_NO_ERROR) {
                    //showProgress(true);
                    showWaiting((View)mContainer.getParent());
//                    mWaitingWindow.showAtLocation((View)mContainer.getParent(), Gravity.CENTER, 0, 0);
                } else {
                    Log.e(this.getClass().getSimpleName(), "Command Error");
                }
                break;
            }

            case R.id.tv_forgot_pw: {
                startActivity(new Intent(Activity_login.this, Activity_PasswordReset.class));
                break;
            }
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

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


    private void addArrayToAutoComplete(List<String> emailAddressCollection, AutoCompleteTextView actv) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(Activity_login.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        actv.setAdapter(adapter);
    }

    @Override
    public void onCommandFinished(int command, IApiResults.ICommon result) {
        kjsLogUtil.i("Activity_ApartmentDetail::onCommandFinished");
        if (null == result) {
            kjsLogUtil.w("result is null");
            return;
        }
        kjsLogUtil.i(String.format("[command: %d] --- %s", command, result.DebugString()));

        if(command == CMD_LOGIN_BY_PASSWORD) {
//            Log.i("Login Activity", "command finished: " + command);
//            Log.i("Login Activity", "Result:  " + result.GetErrCode() + " Description: " + result.GetErrDesc());
            doShowProgress(false);
            hideWaitingWindow();
            if(result.GetErrCode() == CommunicationError.CE_ERROR_NO_ERROR) {
                finish();
            } else {
                commonFun.showToast_info(getApplicationContext(), getWindow().getDecorView(), result.GetErrDesc());
                showPasswordErrorDlg();
            }

        } else if(command == CMD_GET_USER_SALT) {
            IApiResults.IGetUserSalt userSalt = (IApiResults.IGetUserSalt) result;
            if(result.GetErrCode() == CommunicationError.CE_ERROR_NO_ERROR) {
                mSalt = userSalt.GetSalt();
                mRand = userSalt.GetRandom();

                mHandler.sendEmptyMessageDelayed(MSG_LOGIN, 100);
            } else {
                commonFun.showToast_info(getApplicationContext(), getWindow().getDecorView(), result.GetErrDesc());
                doShowProgress(false);
                hideWaitingWindow();
            }
        }
    }

    private void hideWaitingWindow() {
        mHandler.sendEmptyMessageDelayed(MSG_HIDE_WAITING_WINDOW, 1000);
    }

    private void doShowProgress(final boolean b) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showProgress(b);
            }
        });
    }

    @Override
    public void onProgressChanged(int command, String percent, HashMap<String, String> map) {

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
//                mPasswordView.setError(getString(R.string.error_incorrect_password));
//                mPasswordView.requestFocus();
                showPasswordErrorDlg();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

