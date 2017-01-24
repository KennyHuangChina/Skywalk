package com.kjs.skywalk.communicationdemo;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.kjs.skywalk.communicationlibrary.CommunicationCommand;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface;
import com.kjs.skywalk.communicationlibrary.CommunicationManager;
import com.kjs.skywalk.communicationlibrary.CommunicationParameterKey;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Jackie on 2017/1/23.
 */

public class LogInOutFragment extends Fragment
        implements CommunicationInterface.CICommandListener, CommunicationInterface.CIProgressListener{

    private final String TAG = "LogInOutFragment";
    private String mResultString = "";

    TextView mTextViewResult = null;

    public LogInOutFragment() {
    }

    private void doLogin() {
        CommunicationManager mManager = new CommunicationManager(this.getContext());
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_USER_NAME, "TestUserName");
        pMap.put(CommunicationParameterKey.CPK_PASSWORD, "TestPassword");
        pMap.put(CommunicationParameterKey.CPK_RANDOM, "TestRandom");
        mManager.execute(CommunicationCommand.CC_LOG_IN_BY_PASSWORD, pMap, this, this);
    }

    private void doLogout() {
        CommunicationManager mManager = new CommunicationManager(this.getContext());
        HashMap<String, String> pMap = new HashMap<String, String>();
        mManager.execute(CommunicationCommand.CC_LOG_OUT, pMap, this, this);
    }

    private void doTest() {
        CommunicationManager mManager = new CommunicationManager(this.getContext());
        HashMap<String, String> pMap = new HashMap<String, String>();
        mManager.execute(CommunicationCommand.CC_TEST, pMap, this, this);
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
        return view;
    }

    private void showResult(String command, HashMap<String, String> map) {
        if(command != null || !command.isEmpty()) {
            mResultString = command;
            mResultString += "\n\n\n";

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
    public void onCommandFinished(String command, String returnCode, String description, HashMap<String, String> map) {
        if(command.equals(CommunicationCommand.CC_LOG_IN_BY_PASSWORD)) {
            if(returnCode.equals("0")) {
                Log.i(TAG, "Login Success");
                showResult(command, map);
            } else {
                Log.e(TAG, "Command "+ CommunicationCommand.CC_LOG_IN_BY_PASSWORD + " finished with error: " + description);
            }
        }
    }

    @Override
    public void onProgressChanged(String command, String percent, HashMap<String, String> map) {

    }
}
