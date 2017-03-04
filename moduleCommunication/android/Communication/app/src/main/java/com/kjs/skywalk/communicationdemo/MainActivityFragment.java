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
import com.kjs.skywalk.communicationlibrary.CommunicationError;
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
    private EditText mEditText1 = null;
    private EditText mEditText2 = null;
    private int mListTotal = 0;

    public MainActivityFragment() {
    }


    private void doTestAddApi() {
//        doTestAddApi_AddProperty();
        doTestAddApi_CommitHouseByOwner();
    }
    private void doTestAddApi_CommitHouseByOwner() {
        CommunicationManager mManager = new CommunicationManager(this.getContext());
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_PROPERTY_ID, "1");
        pMap.put(CommunicationParameterKey.CPK_BUILDING_NO, "177");
        pMap.put(CommunicationParameterKey.CPK_HOUSE_NO, "2305");
        pMap.put(CommunicationParameterKey.CPK_FLOOR_TOTA, "35");
        pMap.put(CommunicationParameterKey.CPK_FLOOR_THIS, "23");
        pMap.put(CommunicationParameterKey.CPK_LIVINGROOMS, "4");
        pMap.put(CommunicationParameterKey.CPK_BEDROOMS, "2");
        pMap.put(CommunicationParameterKey.CPK_BATHROOMS, "3");
        pMap.put(CommunicationParameterKey.CPK_ACREAGE, "15678");
        pMap.put(CommunicationParameterKey.CPK_4SALE, "false");
        pMap.put(CommunicationParameterKey.CPK_4RENT, "true");
        pMap.put(CommunicationParameterKey.CPK_AGENT, "0");
        mManager.execute(CommunicationCommand.CC_GET_COMMIT_HOUSE_BY_OWNER, pMap, this, this);
    }
    private void doTestAddApi_AddProperty() {
        CommunicationManager mManager = new CommunicationManager(this.getContext());
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_PROPERTY_NAME, mEditText.getText().toString());
        pMap.put(CommunicationParameterKey.CPK_PROPERTY_ADDR, mEditText1.getText().toString());
        pMap.put(CommunicationParameterKey.CPK_PROPERTY_DESC, mEditText2.getText().toString());
        mManager.execute(CommunicationCommand.CC_GET_ADD_PROPERTY, pMap, this, this);
    }

    private void doTestGetApi() {
//        doTestGetApi_GetBriefPublicHouseInfo();
//        doTestGetApi_GetUserInfo();
//        doTestGetApi_GetHouseInfo();
        doTestGetApi_GetPropertyInfo();
    }
    private void doTestGetApi_GetPropertyInfo() {
        CommunicationManager mManager = new CommunicationManager(this.getContext());
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_INDEX, mEditText.getText().toString()); // "2");
        mManager.execute(CommunicationCommand.CC_GET_GET_PROPERTY_INFO, pMap, this, this);
    }
    private void doTestGetApi_GetHouseInfo() {
        CommunicationManager mManager = new CommunicationManager(this.getContext());
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_INDEX, mEditText.getText().toString()); // "2");
        mManager.execute(CommunicationCommand.CC_GET_GET_HOUSE_INFO, pMap, this, this);
    }
    private void doTestGetApi_GetBriefPublicHouseInfo() {
        CommunicationManager mManager = new CommunicationManager(this.getContext());
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_INDEX, mEditText.getText().toString()); // "2");
        mManager.execute(CommunicationCommand.CC_GET_BRIEF_PUBLIC_HOUSE_INFO, pMap, this, this);
    }
    private void doTestGetApi_GetUserInfo() {
        CommunicationManager mManager = new CommunicationManager(this.getContext());
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_INDEX, mEditText.getText().toString()); //"4");
        mManager.execute(CommunicationCommand.CC_GET_USER_INFO, pMap, this, this);
    }

    private void doTestGetList() {
//        doTestGetList_PropertyList();
        doTestGetList_HouseList();
    }

    private void doTestGetList_HouseList() {
        CommunicationManager mManager = new CommunicationManager(this.getContext());
        HashMap<String, String> pMap = new HashMap<String, String>();
        pMap.put(CommunicationParameterKey.CPK_HOUSE_TYPE, mEditText.getText().toString());
        pMap.put(CommunicationParameterKey.CPK_LIST_BEGIN, "0");
        pMap.put(CommunicationParameterKey.CPK_LIST_CNT, "" + mListTotal);
        mManager.execute(CommunicationCommand.CC_GET_HOUSE_LIST, pMap, this, this);
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
        mEditText1 = (EditText) view.findViewById(R.id.editText1);
        mEditText2 = (EditText) view.findViewById(R.id.editText2);

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
        });

        Button btnTestAddApi = (Button)view.findViewById(R.id.testAddApi);
        btnTestAddApi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mResultString = "";
                mTextViewResult.setText("");
                doTestAddApi();
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
    public void onCommandFinished(String command, IApiResults.ICommon result) {
        if (null == result) {
            Log.w(TAG, "result is null");
            return;
        }
        mResultString = "Request: " + command + "\n" + result.DebugString();

        if (CommunicationError.CE_ERROR_NO_ERROR != result.GetErrCode()) {
            Log.e(TAG, "Command:"+ command + " finished with error: " + result.GetErrDesc());
//            showError(command, returnCode, description);
//            return;
        } else {
            if (command.equals(CommunicationCommand.CC_GET_PROPERTY_LIST)) {
                IApiResults.IResultList res = (IApiResults.IResultList) result;
                int nTotal = res.GetTotalNumber();
                mListTotal = nTotal;
                int nFetched = res.GetFetchedNumber();
                if (nFetched > 0) {
                    ArrayList<Object> arry = res.GetList();
                    IApiResults.IPropertyInfo prop = (IApiResults.IPropertyInfo) arry.get(0);
                    prop.GetName();
                }
            } else if (command.equals(CommunicationCommand.CC_GET_HOUSE_LIST)) {
                    IApiResults.IResultList res = (IApiResults.IResultList) result;
                    int nTotal = res.GetTotalNumber();
                    mListTotal = nTotal;
                    int nFetched = res.GetFetchedNumber();
                    if (nFetched > 0) {
                        ArrayList<Object> HouseIDs = res.GetList();
                        for (int n = 0; n < HouseIDs.size(); n++) {
                            Integer id = (Integer)HouseIDs.get(n);
                            Log.d(TAG, "house(" + n + ") id:" + id + "\n");
                        }
                    }
            } else if (command.equals(CommunicationCommand.CC_GET_BRIEF_PUBLIC_HOUSE_INFO)) {
                IApiResults.IHouseDigest res = (IApiResults.IHouseDigest) result;
                res.GetHouseId();

                IApiResults.IResultList lst = (IApiResults.IResultList) result;
                if (lst.GetFetchedNumber() > 0) {
                    ArrayList<Object> arry = lst.GetList();
                    IApiResults.IHouseTag tag = (IApiResults.IHouseTag) arry.get(0);
                    tag.GetName();
                }
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
    public void onProgressChanged(String command, String percent, HashMap<String, String> map) {

    }
}
