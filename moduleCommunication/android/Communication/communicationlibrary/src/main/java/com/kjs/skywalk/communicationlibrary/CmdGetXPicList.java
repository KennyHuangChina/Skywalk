package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

import static com.kjs.skywalk.communicationlibrary.IApiArgs.PIC_SIZE_ALL;
import static com.kjs.skywalk.communicationlibrary.IApiArgs.PIC_SIZE_Large;
import static com.kjs.skywalk.communicationlibrary.IApiArgs.PIC_TYPE_SUB_HOUSE_BEGIN;
import static com.kjs.skywalk.communicationlibrary.IApiArgs.PIC_TYPE_SUB_HOUSE_END;

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

    //////////////////////////////////////////////////////////////////////////////////////////////
    //
    //      -- API Arguments --
    //
    //////////////////////////////////////////////////////////////////////////////////////////////
    class ApiArgsGetXPiclst extends ApiArgsBase implements IApiArgs.IArgsGetXPicLst {

        private int mXId  = 0;
        private int mType = -1;     // ref to PIC_TYPE_SUB_HOUSE_xxx
        private int mSize = -1;     // ref to

        public ApiArgsGetXPiclst(int xid, int type, int size) {
            mXId    = xid;
            mType   = type;
            mSize   = size;
        }

        @Override
        public boolean checkArgs() {
            String Fn = "[checkArgs] ";
//        return super.checkArgs();
            if (mXId <= 0) {
                Log.e(TAG, "xid:" + mXId);
                return false;
            }
            if (mType < PIC_TYPE_SUB_HOUSE_BEGIN || mType > PIC_TYPE_SUB_HOUSE_END) {
                Log.e(TAG, "type:" + mType);
                return false;
            }
            if (mSize < PIC_SIZE_ALL || mSize > PIC_SIZE_Large) {
                Log.e(TAG, "mType:" + mType);
                return false;
            }

            return true;
        }

        @Override
        public boolean isEqual(IApiArgs.IArgsBase arg2) {
            if (!super.isEqual(arg2)) {
                return false;
            }
            ApiArgsGetXPiclst arg2_chk = (ApiArgsGetXPiclst)arg2;
            if (mXId != arg2_chk.mXId || mType != arg2_chk.mType || mSize != arg2_chk.mSize) {
                return false;
            }

            return true;
        }

        @Override
        public int getXId() {
            return mXId;
        }

        @Override
        public int getSubType() {
            return mType;
        }

        @Override
        public int getSize() {
            return mSize;
        }
    }
}
