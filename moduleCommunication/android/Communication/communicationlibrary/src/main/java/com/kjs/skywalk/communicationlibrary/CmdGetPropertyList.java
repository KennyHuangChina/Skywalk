package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;
import java.util.HashMap;

//import CommunicationBase;

class CmdGetPropertyList extends CommunicationBase {

    CmdGetPropertyList(Context context, String property, int begin, int cnt) {
        super(context, CommunicationInterface.CmdID.CMD_GET_PROPERTY_LIST);
        mNeedLogin = false;
        mArgs = new Args(property, begin, cnt);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/property/list";
        return mCommandURL;
    }
    @Override
    public void generateRequestData() {
        mRequestData = ("name=" + ((Args)mArgs).getName());
        mRequestData += "&";
        mRequestData += ("bgn=" + ((Args)mArgs).getBeginPosi());
        mRequestData += "&";
        mRequestData += ("cnt=" + ((Args)mArgs).getFetchCnt());
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResGetPropertyList result = new ResGetPropertyList(nErrCode, jObject);
        return result;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //
    class Args extends ApiArgFetchList implements IApiArgs.IArgsGetPropertyList {
        private String mPropertyName = "";

        Args(String property, int begin, int cnt) {
            super(begin, cnt);
            mPropertyName = property;
        }

        @Override
        public boolean checkArgs() {
            if (null == mPropertyName || mPropertyName.isEmpty()) {
                return false;
            }
            return super.checkArgs();
        }

        @Override
        public boolean isEqual(IApiArgs.IArgsBase arg2) {
            if (!super.isEqual(arg2)) {
                return false;
            }

            Args arg_chk = (Args)arg2;
            if (null == mPropertyName || null == arg_chk.mPropertyName || !mPropertyName.equals(arg_chk.mPropertyName)) {
                return false;
            }
            return true;
        }

        @Override
        public String getName() {
            return mPropertyName;
        }
    }
}
