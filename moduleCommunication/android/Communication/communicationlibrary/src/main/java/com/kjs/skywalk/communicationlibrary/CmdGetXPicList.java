package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/10/28.
 */

class CmdGetXPicList extends CommunicationBase {

    CmdGetXPicList(Context context, int Xid, int type, int size, int cmd) {
        super(context, cmd);
        mArgs = new ApiArgsGetXPiclst(Xid, type, size);
    }

    protected String getBaseURL() {
        return "";
    }

    @Override
    public String getRequestURL() {
        String Fn = "[getRequestURL] ";
        ApiArgsGetXPiclst args = (ApiArgsGetXPiclst)mArgs;
        mCommandURL = getBaseURL();
        String sArg = "";
        if (args.getSubType() > 0) {
            sArg = "st=" + args.getSubType();
        }
        if (args.getSize() >= 0) {
            if (!sArg.isEmpty()) {
                sArg += "&";
            }
            sArg += "sz=" + args.getSize();
        }
        if (sArg.length() > 0) {
            mCommandURL += "?" + sArg;
        }
        Log.d(TAG, Fn + "mCommandURL:" + mCommandURL);
        return mCommandURL;
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResPicList result = new ResPicList(nErrCode, jObject);
        return result;
    }

    protected boolean checkPicType(ResPicList res) {
        return false;
    }

    @Override
    protected boolean checkCmdRes(IApiResults.ICommon res) {
        if (!super.checkCmdRes(res)) {
            return false;
        }

        ResPicList resPicLst = (ResPicList)res;
        if (!checkPicType(resPicLst)) {
            return false;
        }
        if (((ApiArgsGetXPiclst)mArgs).getSubType() != resPicLst.mPicSubType) {
            return false;
        }
        return true;
    }
}
