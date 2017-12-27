package com.kjs.skywalk.communicationlibrary;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kenny on 2017/2/22.
 */

class ResBase implements IApiResults.ICommon {
    protected   int                 mErrCode    = -1;   // Unknown. refer to CE_COMMAND_ERROR_XXXX
    protected   String              mErrDesc    = "";
    protected   String              mString     = "";
    private     IApiArgs.IArgsBase  mCmdArgs    = null;

    ResBase(int nErrCode) {
        mErrCode = nErrCode;
    }

    ResBase(int nErrCode, JSONObject jObject) {
        mErrCode = nErrCode;
        parse(jObject);
    }

    @Override
    public int GetErrCode() {
        return mErrCode;
    }

    @Override
    public String GetErrDesc() {
        return mErrDesc;
    }

    @Override
    public String DebugString() {
        mString += ("mErrCode: " + mErrCode + "\n");
        mString += ("mErrDesc: " + mErrDesc + "\n");
        return mString;
    }

    @Override
    public IApiArgs.IArgsBase GetArgs() {
        return mCmdArgs;
    }

    protected void SetArgs(IApiArgs.IArgsBase args) {
        mCmdArgs = args;
    }

    protected int parse(JSONObject obj) {
        if (null != obj) {  // decode error and information from jason object
            try {
                mErrCode = obj.getInt("ErrCode");
                mErrDesc = obj.getString("ErrString");
            } catch (JSONException e) {
                e.printStackTrace();
                return -2;
            }
            if (0 == mErrCode) {
                return parseResult(obj);
            }
        } else {    // use the error code passed in
            mErrDesc = CommunicationError.getErrorDescription(mErrCode);
        }

        return 0;
    }

    protected int parseResult(JSONObject obj) {
        return 0;
    }

    protected String PicFullUrl(String pic) {
        return CUtilities.PicFullUrl(pic);
    }
}
