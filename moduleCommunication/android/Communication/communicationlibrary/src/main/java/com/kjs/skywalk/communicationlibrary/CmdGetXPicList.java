package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/10/28.
 */

class CmdGetXPicList extends CommunicationBase {
    protected int mXId  = 0;
    protected int mType = -1;
    protected int mSize = -1;

    CmdGetXPicList(Context context, int Xid, int type, int size, int cmd) {
        super(context, cmd);
        mXId    = Xid;
        mType   = type;
        mSize   = size;
    }

    protected String getBaseURL() {
        return "";
    }

    @Override
    public String getRequestURL() {
        String Fn = "[getRequestURL] ";
        mCommandURL = getBaseURL();
        String sArg = "";
        if (mType > 0) {
            sArg = "st=" + mType;
        }
        if (mSize >= 0) {
            if (!sArg.isEmpty()) {
                sArg += "&";
            }
            sArg += "sz=" + mSize;
        }
        if (sArg.length() > 0) {
            mCommandURL += "?" + sArg;
        }
        Log.d(TAG, Fn + "mCommandURL:" + mCommandURL);
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        super.generateRequestData();
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
//        return super.checkParameter(map);
        if (mXId <= 0) {
            Log.e(TAG, "xid:" + mXId);
            return false;
        }
        if (mType < CommunicationInterface.PIC_TYPE_SUB_HOUSE_BEGIN || mType > CommunicationInterface.PIC_TYPE_SUB_HOUSE_END) {
            Log.e(TAG, "type:" + mType);
            return false;
        }
        if (mSize < CommunicationInterface.PIC_SIZE_ALL || mSize > CommunicationInterface.PIC_SIZE_Large) {
            Log.e(TAG, "mType:" + mType);
            return false;
        }

        return true;
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResPicList result = new ResPicList(nErrCode, jObject);
        return result;
    }

    @Override
    protected boolean isCmdEqual(CommunicationBase cmd2Chk) {
        CmdGetXPicList cmdChk = (CmdGetXPicList)cmd2Chk;
        if (mXId == cmdChk.mXId && mType == cmdChk.mType && mSize == cmdChk.mSize) {
            return true;
        }
        return false;
    }

    protected boolean checkPicType(ResPicList res) {
        return false;
    }

    @Override
    protected boolean checkCmdRes(IApiResults.ICommon res) {
        ResPicList resPicLst = (ResPicList)res;
        if (!checkPicType(resPicLst)) {
            return false;
        }
        if (mType != resPicLst.mPicSubType) {
            return false;
        }
        return true;
    }
}
