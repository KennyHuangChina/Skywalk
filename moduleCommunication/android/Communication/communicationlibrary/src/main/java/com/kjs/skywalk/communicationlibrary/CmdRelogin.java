package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.*;

//import CommunicationBase;

/**
 * Created by kenny on 2017/2/19.
 */

class CmdRelogin extends CommunicationBase {

    private String mSession     = "";   // "1234567890";
    private String mRadom       = "";

    CmdRelogin(Context context, String user) {
        super(context, CmdID.CMD_RELOGIN);
        mMethodType = "POST";
        mNeedLogin = false;

        // generate a random
        mRadom = generateRandom();
        // Load session for previous login
        SKSessionStore sessStore = SKSessionStore.getInstance(mContext);
        if (null != sessStore) {
            mSession = sessStore.get();
        }

        mArgs = new ArgsUserName(user);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/admin/relogin/" + mSession;
        return mCommandURL;
    }
    @Override
    public void generateRequestData() {
        mRequestData = ("ln=" + ((ArgsUserName)mArgs).getUserName());
        mRequestData += "&";
        mRequestData += ("ver=" + mVersion);
        mRequestData += "&";
        mRequestData += ("rd=" + mRadom);

        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public int doOperation(CICommandListener commandListener, CIProgressListener progressListener)
    {
        String FN = "[doOperation] ";
        if (mSession.isEmpty() /*0 == mSession.length()*/) {
            Log.e(TAG, FN + "mSession is empty");
            return CommunicationError.CE_COMMAND_ERROR_NOT_LOGIN;
        }

        Log.d(TAG, FN + "mSession:" + mSession);
        NativeCall pNC = NativeCall.GetNativeCaller();
        byte[] sid = pNC.EncryptReloginSession(((ArgsUserName)mArgs).getUserName(), mRadom, mSession, mVersion);
        if (null == sid) {
            Log.e(TAG, FN + "Fail to encrypt relogin session");
            return CommunicationError.CE_COMMAND_ERROR_FATAL_ERROR;
        }
        Log.w(TAG, FN + "session:" + sid);
//        map.put(CommunicationParameterKey.CPK_PASSWORD, new String(pass));
        String strSession = new String(sid);
        mSession = strSession;
        Log.d(TAG, FN + "mSession:" + mSession);

        return super.doOperation(commandListener, progressListener);
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        // Store the current login session
        try {
            SKSessionStore sessStore = SKSessionStore.getInstance(mContext);
            if (null == sessStore) {
                return null;
            }
            // if login not success, the Sid field will be empty, which means the Sid stored before
            //    will be cleaned, just like what API Logout did
            String loginSession = "";
            if (null != jObject) {
                loginSession = jObject.getString("Sid");
            }
            sessStore.save(loginSession);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        ResLogin result = new ResLogin(nErrCode, jObject);
        return result;
    }
}
