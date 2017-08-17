package com.kjs.skywalk.communicationdemo;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kjs.skywalk.communicationlibrary.CommandManager;
import com.kjs.skywalk.communicationlibrary.CommunicationError;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID;
import com.kjs.skywalk.communicationlibrary.IApiResults;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Jackie on 2017/1/23.
 */

public class LogInOutFragment extends Fragment
        implements CommunicationInterface.CICommandListener, CommunicationInterface.CIProgressListener{

    private final String TAG = getClass().getSimpleName();
    private String mResultString = "";

    TextView mTextViewResult = null;
    EditText mEditUserName  = null;
    EditText mEditPassword  = null;
    EditText mEditSms       = null;

    private String mSalt = "";
    private String mRand = "";

    public LogInOutFragment() {
    }

    private void doLogin() {
        CommandManager CmdMgr = CommandManager.getCmdMgrInstance(this.getContext(), this, this);
        CmdMgr.LoginByPassword(mEditUserName.getText().toString(), mEditPassword.getText().toString(), mRand, mSalt);
    }

    private void doLoginSms() {
        CommandManager CmdMgr = CommandManager.getCmdMgrInstance(this.getContext(), this, this);
        CmdMgr.LoginBySms(mEditUserName.getText().toString(), mEditSms.getText().toString());
    }

    private void doRestPass() {
        CommandManager CmdMgr = CommandManager.getCmdMgrInstance(this.getContext(), this, this);
        CmdMgr.ResetLoginPass(mEditUserName.getText().toString(), mEditPassword.getText().toString(), mEditSms.getText().toString(), mSalt, mRand);
    }

    private void doLogout() {
        CommandManager CmdMgr = CommandManager.getCmdMgrInstance(this.getContext(), this, this);
        CmdMgr.Logout();
    }

    private void doTest() {
        CommandManager CmdMgr = CommandManager.getCmdMgrInstance(this.getContext(), this, this);
        CmdMgr.GetLoginUserInfo();
    }

    private void doGetUserSalt() {
        CommandManager CmdMgr = CommandManager.getCmdMgrInstance(this.getContext(), this, this);
        CmdMgr.GetUserSalt(mEditUserName.getText().toString());
    }

    private void doRelogin() {
        CommandManager CmdMgr = CommandManager.getCmdMgrInstance(this.getContext(), this, this);
        CmdMgr.Relogin(mEditUserName.getText().toString());
    }

    private void doGetSmsCode() {
        CommandManager CmdMgr = CommandManager.getCmdMgrInstance(this.getContext(), this, this);
        CmdMgr.GetSmsCode(mEditUserName.getText().toString());
    }

    private void clearResultDisplay() {
        mResultString = "";
        mTextViewResult.setText("");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log_in_out, container, false);
        mTextViewResult = (TextView)view.findViewById(R.id.textViewResult);
        mEditUserName   = (EditText)view.findViewById(R.id.editTextUserName);
        mEditPassword   = (EditText)view.findViewById(R.id.editTextPassword);
        mEditSms        = (EditText)view.findViewById(R.id.editSms);

        Button loginButton = (Button)view.findViewById(R.id.buttonLogin);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                clearResultDisplay();
                doLogin();
            }
        });

        Button logoutButton = (Button)view.findViewById(R.id.buttonLogout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                clearResultDisplay();
                doLogout();
            }
        });
        Button testButton = (Button)view.findViewById(R.id.buttonTest);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                clearResultDisplay();
                doTest();
            }
        });

        Button btnGetUserSalt = (Button)view.findViewById(R.id.buttonGetUserSalt);
        btnGetUserSalt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                clearResultDisplay();
                doGetUserSalt();
            }
        });

        Button btnRelogin = (Button)view.findViewById(R.id.btnRelogin);
        btnRelogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                clearResultDisplay();
                doRelogin();
            }
        });

        Button btnGetSmsCode = (Button)view.findViewById(R.id.btnGetSmsCode);
        btnGetSmsCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                clearResultDisplay();
                doGetSmsCode();
            }
        });

        Button btnLoginSms = (Button)view.findViewById(R.id.btnLoginBySms);
        btnLoginSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                clearResultDisplay();
                doLoginSms();
            }
        });

        Button bnRestPass = (Button)view.findViewById(R.id.btnRestPass);
        bnRestPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearResultDisplay();
                doRestPass();
            }
        });


        return view;
    }

    private void showError(String strCmd, String errCode, String errDesc) {

        mResultString = "";
        if(strCmd != null || !strCmd.isEmpty()) {
            mResultString = strCmd;
            mResultString += "\n\n";
        }

        mResultString += errCode;
        mResultString += "\n\n";
        mResultString += errDesc;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextViewResult.setText(mResultString);
            }
        });
    }

    private void showResult(String command, HashMap<String, String> map) {
        if(command != null || !command.isEmpty()) {
            mResultString = command;
            mResultString += "\n\n";

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTextViewResult.setText(mResultString);
                }
            });
        }

        if(map == null || map.size()== 0) {
            return;
        }

        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            mResultString += (key + ": " + map.get(key) + "\n");
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextViewResult.setText(mResultString);
            }
        });
    }

    @Override
    public void onCommandFinished(int command, IApiResults.ICommon result) {
        if (null == result) {
            Log.w(TAG, "result is null");
            return;
        }
        String cmd = CommunicationInterface.CmdID.GetCmdDesc(command);
        mResultString = String.format("command(%d): %s \nResult: %s", command, cmd, result.DebugString());

        if (CommunicationError.CE_ERROR_NO_ERROR != result.GetErrCode()) {
            Log.e(TAG, "Command:"+ command + " finished with error: " + result.GetErrDesc());
//            showError(command, returnCode, description);
//            return;
        } else {
            Log.e(TAG, "Command:"+ command + " finished successes");
            if (command == CmdID.CMD_GET_USER_SALT) {
                IApiResults.IGetUserSalt slt = (IApiResults.IGetUserSalt)result;
                mSalt = slt.GetSalt();
                mRand = slt.GetRandom();
            }
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextViewResult.setText(mResultString);
            }
        });
    }

    @Override
    public void onProgressChanged(int command, String percent, HashMap<String, String> map) {

    }
}
