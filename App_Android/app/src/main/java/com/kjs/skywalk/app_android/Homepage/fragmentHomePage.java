package com.kjs.skywalk.app_android.Homepage;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kjs.skywalk.app_android.Apartment.Activity_ApartmentDetail;
import com.kjs.skywalk.app_android.R;
import com.kjs.skywalk.app_android.commonFun;
import com.kjs.skywalk.communicationlibrary.CommandManager;
import com.kjs.skywalk.communicationlibrary.CommunicationError;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface;
import com.kjs.skywalk.communicationlibrary.IApiResults;

import java.util.HashMap;

import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_HOUSE_INFO;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_HOUSE_LIST;

/**
 * Created by sailor.zhou on 2017/1/11.
 */

public class fragmentHomePage extends Fragment {
    public fragmentHomePage() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);
        ListView lvContent = (ListView) view.findViewById(R.id.lv_content);
        lvContent.setFocusable(false);

        homepage_apartment_listitem_adapter adapter = new homepage_apartment_listitem_adapter(getActivity());
        lvContent.setAdapter(adapter);

        lvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                commonFun.showToast_resId(getActivity().getApplicationContext(), view);
                Intent intent = new Intent(getActivity().getApplicationContext(), Activity_ApartmentDetail.class);
                startActivity(intent);
            }
        });

        // get recommend house info

        CommandManager CmdMgr = new CommandManager(getActivity(), mCmdListener, mProgreessListener);
        int result = CmdMgr.GetHouseList(0, 0, 0);
        if(result != CommunicationError.CE_ERROR_NO_ERROR) {

        }

        return view;
    }

    CommunicationInterface.CICommandListener mCmdListener = new CommunicationInterface.CICommandListener() {
        @Override
        public void onCommandFinished(int command, IApiResults.ICommon result) {
            if (null == result) {
                Log.w("ServerAPI", "result is null");
                return;
            }
//            mResultString = "Request: " + command + "\n" + result.DebugString();

            if (CommunicationError.CE_ERROR_NO_ERROR != result.GetErrCode()) {
                Log.e("ServerAPI", "Command:"+ command + " finished with error: " + result.GetErrDesc());
                return;
            }

            if (command == CMD_GET_HOUSE_LIST) {
                IApiResults.IResultList resultList = (IApiResults.IResultList) result;
            }
        }
    };

    CommunicationInterface.CIProgressListener mProgreessListener = new CommunicationInterface.CIProgressListener() {

        @Override
        public void onProgressChanged(int i, String s, HashMap<String, String> hashMap) {

        }
    };

}
