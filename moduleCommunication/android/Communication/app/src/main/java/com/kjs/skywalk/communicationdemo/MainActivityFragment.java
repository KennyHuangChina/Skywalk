package com.kjs.skywalk.communicationdemo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.kjs.skywalk.communicationlibrary.IApiResults;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.*;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.*;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment
        implements CICommandListener, CIProgressListener{

    private final String TAG = getClass().getSimpleName();
    private MainActivity mainActivity = null;
    private String mResultString = "";

    private TextView mTextViewResult = null;
    private EditText mEditText = null;
    private EditText mEditText1 = null;
    private EditText mEditText2 = null;
    private int mListTotal = 0;
    private boolean mCertificate = false;

    public MainActivityFragment() {
    }

    private void doTestDeleApi() {
        CommandManager CmdMgr = new CommandManager(this.getContext(), this, this);
        CmdMgr.DelePicture(Integer.parseInt(mEditText.getText().toString()));
    }

    private void doTestModifyApi() {
        CommandManager CmdMgr = new CommandManager(this.getContext(), this, this);
//        CmdMgr.ModifyPropertyInfo(7, mEditText.getText().toString(),
//                                    mEditText1.getText().toString(),
//                                    mEditText2.getText().toString());
//        CmdMgr.RecommendHouse(Integer.parseInt(mEditText.getText().toString()),
//                                Integer.parseInt(mEditText1.getText().toString()));
//        CmdMgr.SetHouseCoverImg(Integer.parseInt(mEditText.getText().toString()),
//                                Integer.parseInt(mEditText1.getText().toString()));
//        CmdMgr.CertificateHouse(Integer.parseInt(mEditText.getText().toString()), mCertificate,
//                                mCertificate ? "已经和业主核实，同意发布" : "撤销发布");
//        mCertificate = !mCertificate;

//        doTestModifyApi_ModifyHouse(CmdMgr);
//        CmdMgr.ModifyDeliverable(Integer.parseInt(mEditText.getText().toString()), mEditText1.getText().toString());
//        CmdMgr.EditFacilityType(Integer.parseInt(mEditText.getText().toString()), mEditText1.getText().toString());
//        CmdMgr.EditFacility(Integer.parseInt(mEditText.getText().toString()),
//                Integer.parseInt(mEditText1.getText().toString()),
//                mEditText2.getText().toString(), "/sdcard/tv_n.png");
//        CmdMgr.EditHouseFacility(Integer.parseInt(mEditText.getText().toString()),
//                Integer.parseInt(mEditText1.getText().toString()),
//                Integer.parseInt(mEditText2.getText().toString()), "");
//        CmdMgr.ReadNewEvent(Integer.parseInt(mEditText.getText().toString()));
//        CmdMgr.ModifyHouseEvent(Integer.parseInt(mEditText.getText().toString()), mEditText1.getText().toString());
        CmdMgr.MofidyAgency(Integer.parseInt(mEditText.getText().toString()), Integer.parseInt(mEditText1.getText().toString()),
                Integer.parseInt(mEditText2.getText().toString()), 2016);
        CmdMgr.SetHouseShowtime(Integer.parseInt(mEditText.getText().toString()), Integer.parseInt(mEditText1.getText().toString()),
                Integer.parseInt(mEditText2.getText().toString()), mEditText1.getText().toString() + mEditText2.getText().toString());
    }
    private void doTestModifyApi_ModifyHouse(CommandManager CmdMgr) {
        CommunicationInterface.HouseInfo houseInfo = new CommunicationInterface.HouseInfo(6, 2, "57", "1606", 35, 16, 2, 4, 3, 17788, false, true, 3, "2017-06-08");
        CmdMgr.AmendHouse(houseInfo);
   }

    private void doTestAddApi() {
        CommandManager CmdMgr = new CommandManager(this.getContext(), this, this);
//        CmdMgr.AddProperty(mEditText.getText().toString(), mEditText1.getText().toString(), mEditText2.getText().toString());
//        CmdMgr.AddDeliverable(mEditText.getText().toString());
//        CmdMgr.AddHouseDeliverable(Integer.parseInt(mEditText.getText().toString()),
//                                    Integer.parseInt(mEditText1.getText().toString()),
//                                    Integer.parseInt(mEditText2.getText().toString()), "交付物说明");
//        CmdMgr.AddFacilityType(mEditText.getText().toString());
//        CmdMgr.AddFacility(Integer.parseInt(String.valueOf(mEditText.getText())), mEditText1.getText().toString(), null/*"/sdcard/tv_n.png"*/);
//
//        CommunicationInterface.HouseInfo houseInfo = new CommunicationInterface.HouseInfo(0, 1, "177X", "2305", 35, 23, 4, 3, 2, 157678, false, true, 3, "2017-06-07");
//        CmdMgr.CommitHouseByOwner(houseInfo, 0);

        // test AddHouse
//        ArrayList<CommunicationInterface.FacilityItem> list = new ArrayList<CommunicationInterface.FacilityItem>();
//        list.add(new CommunicationInterface.FacilityItem(6, 2, "沙发茶几说明"));
//        list.add(new CommunicationInterface.FacilityItem(4, 3, "电视机说明"));
//        list.add(new CommunicationInterface.FacilityItem(7, 4, "立式空调说明"));
//        CmdMgr.AddHouseFacility(6, list);
        CmdMgr.AddPicture(Integer.parseInt(mEditText.getText().toString()), Integer.parseInt(mEditText1.getText().toString()),
                Integer.parseInt(mEditText2.getText().toString()), "test picture 3",  "/sdcard/3.jpg");
//        CmdMgr.SetHousePrice(11, 1000, 800, true, 500000, 470000);
    }
    private void doTestGetApi() {
        CommandManager CmdMgr = new CommandManager(this.getContext(), this, this);
//        CmdMgr.GetPropertyInfo(Integer.parseInt(mEditText.getText().toString()));
//        CmdMgr.GetUserInfo(Integer.parseInt(mEditText.getText().toString()));
        CmdMgr.GetHouseInfo(Integer.parseInt(mEditText.getText().toString()), Boolean.parseBoolean(mEditText1.getText().toString()));
//        CmdMgr.GetBriefPublicHouseInfo(Integer.parseInt(mEditText.getText().toString()));
//        CmdMgr.GetPicUrls(Integer.parseInt(mEditText.getText().toString()), Integer.parseInt(mEditText1.getText().toString()));
//        CmdMgr.GetHousePics(Integer.parseInt(mEditText.getText().toString()), Integer.parseInt(mEditText1.getText().toString()));
//        CmdMgr.GetNewEventCount();
//        CmdMgr.GetHouseNewEvent();
//        CmdMgr.GetHouseEventInfo(Integer.parseInt(mEditText.getText().toString()));
//        CmdMgr.GetHouseEventProcList(Integer.parseInt(mEditText.getText().toString()));
//        CmdMgr.GetHouseEventList(Integer.parseInt(mEditText.getText().toString()), 0, 0, 0, 10, Boolean.parseBoolean(mEditText1.getText().toString()));
//        CmdMgr.GetAgencyList(0, 10);
//        CmdMgr.GetHousePrice(Integer.parseInt(mEditText.getText().toString()), 0, Integer.parseInt(mEditText1.getText().toString()));
//        CmdMgr.GetHouseShowtime(Integer.parseInt(mEditText.getText().toString()));
    }
    private void doTestGetList() {
        CommandManager CmdMgr = new CommandManager(this.getContext(), this, this);
//        CmdMgr.GetPropertyListByName(mEditText.getText().toString(), 0, mListTotal);
//        CmdMgr.GetDeliverableList();
//        CmdMgr.GetHouseDeliverables(Integer.parseInt(String.valueOf(mEditText.getText())));
//        CmdMgr.GetFacilityTypeList();
//        CmdMgr.GetFacilityList(Integer.parseInt(String.valueOf(mEditText.getText())));
//        CmdMgr.GetBehalfHouses(Integer.parseInt(mEditText.getText().toString()), 0, mListTotal);
//        CmdMgr.GetHouseDigestList(Integer.parseInt(mEditText.getText().toString()), 0, mListTotal);
//        CmdMgr.GetHouseFacilityList(Integer.parseInt(mEditText.getText().toString()));
        CmdMgr.GetAgencyList(0, mListTotal);
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

        Button btnTestModifyApi = (Button)view.findViewById(R.id.testModifyApiApi);
        btnTestModifyApi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mResultString = "";
                mTextViewResult.setText("");
                doTestModifyApi();
            }
        });

        Button btnUploadPic = (Button)view.findViewById(R.id.testUploadPic);
        btnUploadPic.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent it = new Intent();
                it.setClass(getActivity(), UploadPictureActivity.class);
                startActivity(it);
            }
        });

        Button btntestDeleteApi = (Button)view.findViewById(R.id.testDeleteApi);
        btntestDeleteApi.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                mResultString = "";
                mTextViewResult.setText("");
                doTestDeleApi();
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
    public void onCommandFinished(int command, IApiResults.ICommon result) {
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
            if (CMD_GET_PROPERTY_LIST == command) {
                IApiResults.IResultList res = (IApiResults.IResultList) result;
                int nTotal = res.GetTotalNumber();
                mListTotal = nTotal;
                int nFetched = res.GetFetchedNumber();
                if (nFetched > 0) {
                    ArrayList<Object> arry = res.GetList();
                    IApiResults.IPropertyInfo prop = (IApiResults.IPropertyInfo) arry.get(0);
                    prop.GetName();
                    mListTotal = 0;
                }
            } else if (command == CMD_GET_AGENCY_LIST) {
                IApiResults.IResultList res = (IApiResults.IResultList) result;
                int nTotal = res.GetTotalNumber();
                mListTotal = nTotal;
                int nFetched = res.GetFetchedNumber();
                if (nFetched > 0) {
                    ArrayList<Object> arry = res.GetList();
                    IApiResults.IAgencyInfo agency = (IApiResults.IAgencyInfo) arry.get(0);
                    agency.Name();
                    mListTotal = 0;
                }
            } else if (command == CMD_GET_HOUSE_DIGEST_LIST || command == CMD_GET_BEHALF_HOUSE_LIST) {
                    IApiResults.IResultList res = (IApiResults.IResultList) result;
                    int nTotal = res.GetTotalNumber();
                    mListTotal = nTotal;
                    int nFetched = res.GetFetchedNumber();
                    if (nFetched > 0) {
                        ArrayList<Object> HouseList = res.GetList();
                        for (int n = 0; n < HouseList.size(); n ++) {
                            IApiResults.IHouseDigest house = (IApiResults.IHouseDigest)HouseList.get(n);
                            String houseProp = house.GetProperty();
                            String housePropAddr = house.GetPropertyAddr();
                            ArrayList<Object> tags = ((IApiResults.IResultList)house).GetList();
                            for (int m = 0; m < tags.size(); m++) {
                                IApiResults.IHouseTag tag = (IApiResults.IHouseTag)tags.get(m);
                                int tagId = tag.GetTagId();
                                String tagName = tag.GetName();
                            }
                            mListTotal = 0;
                        }
                    }
            } else if (command == CMD_GET_BRIEF_PUBLIC_HOUSE_INFO) {
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
    public void onProgressChanged(int command, String percent, HashMap<String, String> map) {

    }
}
