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

import com.kjs.skywalk.communicationlibrary.CommunicationCommand;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface;
import com.kjs.skywalk.communicationlibrary.CommunicationManager;
import com.kjs.skywalk.communicationlibrary.CommunicationParameterKey;
import com.kjs.skywalk.communicationlibrary.IApiResults;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment
        implements CommunicationInterface.CICommandListener, CommunicationInterface.CIProgressListener{

    private final String TAG = "MainActivityFragment";
    private MainActivity mainActivity = null;
    private String mResultString = "";

    private TextView mTextViewResult = null;
    private EditText mEditText = null;
    private int mListTotal = 0;

    public MainActivityFragment() {
    }

    private void doTestGetApi() {
        doTestGetApi_GetBriefPublicHouseInfo();
    }
    private void doTestGetApi_GetBriefPublicHouseInfo() {
        CommunicationManager mManager = new CommunicationManager(this.getContext());
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_INDEX, "2");
        mManager.execute(CommunicationCommand.CC_GET_BRIEF_PUBLIC_HOUSE_INFO, pMap, this, this);
    }

    private void doTestGetList() {
        doTestGetList_PropertyList();
    }

    private void doTestGetList_PropertyList() {
        CommunicationManager mManager = new CommunicationManager(this.getContext());
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_PROPERTY_NAME, mEditText.getText().toString());
        pMap.put(CommunicationParameterKey.CPK_LIST_BEGIN, "0");
        pMap.put(CommunicationParameterKey.CPK_LIST_CNT, "" + mListTotal);
        mManager.execute(CommunicationCommand.CC_GET_PROPERTY_LIST, pMap, this, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        Button testButton = (Button)view.findViewById(R.id.testList);
        mTextViewResult = (TextView) view.findViewById(R.id.textViewResult);
        mEditText = (EditText) view.findViewById(R.id.editText);
        testButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mResultString = "";
                mTextViewResult.setText("");
                doTestGetList();
            }
        });
        Button btnTestGetApi = (Button)view.findViewById(R.id.testGetApi);
        btnTestGetApi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mResultString = "";
                mTextViewResult.setText("");
                doTestGetApi();
            }
        });;

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
    public void onCommandFinished1(String command, String returnCode, String description, IApiResults.ICommon result) {
        if(command.equals(CommunicationCommand.CC_GET_PROPERTY_LIST)) {
            IApiResults.IResultList res = (IApiResults.IResultList)result;
            int nTotal = res.GetTotalNumber();
            mListTotal = nTotal;
            int nFetched = res.GetFetchedNumber();
            if (nFetched > 0 ) {
                ArrayList<Object> arry = res.GetList();
                IApiResults.IPropertyInfo prop = (IApiResults.IPropertyInfo)arry.get(0);
                prop.GetName();
            }
        } else if (command.equals(CommunicationCommand.CC_GET_BRIEF_PUBLIC_HOUSE_INFO)) {
            IApiResults.IHouseDigest res = (IApiResults.IHouseDigest)result;
            res.GetHouseId();

            IApiResults.IResultList lst = (IApiResults.IResultList)result;
            if (lst.GetFetchedNumber() > 0) {
                ArrayList<Object> arry = lst.GetList();
                IApiResults.IHouseTag tag = (IApiResults.IHouseTag)arry.get(0);
                tag.GetName();
            }
        }
        mResultString = result.DebugString();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextViewResult.setText(mResultString);
            }
        });
    }

    @Override
    public void onCommandFinished(String command, String returnCode, String description, HashMap<String, String> map) {
        if(command.equals(CommunicationCommand.CC_GET_BRIEF_PUBLIC_HOUSE_INFO)) {
            if(returnCode.equals("0")) {
//                showResult(command, map);
            } else {
                Log.e(TAG, "Command "+ CommunicationCommand.CC_GET_BRIEF_PUBLIC_HOUSE_INFO + " finished with error: " + description);
            }
        }
    }

    @Override
    public void onProgressChanged(String command, String percent, HashMap<String, String> map) {

    }
}
