package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

//import CommunicationBase;

/**
 * Created by kenny on 2017/2/19.
 */

public class CmdRelogin extends CommunicationBase {

    private String mSession = "1234567890";
    private String mUserName = "";
    private String mRadom = "";

    CmdRelogin(Context context) {
        super(context);
        TAG = "CmdRelogin";
//        Log.i(TAG, "Constructor");
        mAPI = CommunicationCommand.CC_RELOGIN;
        mMethodType = "POST";
        mVersion =1;

        // generate a random
        mRadom = generateRandom();
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map)
    {
        if (!map.containsKey(CommunicationParameterKey.CPK_USER_NAME)) // ||
//                !map.containsKey(CommunicationParameterKey.CPK_SESSION_ID) ||
//                !map.containsKey(CommunicationParameterKey.CPK_RANDOM))
        {
            return false;
        }

        mUserName = map.get(CommunicationParameterKey.CPK_USER_NAME);
        if (null == mUserName || mUserName.isEmpty()) {
            return false;
        }
//        mSession = map.get(CommunicationParameterKey.CPK_SESSION_ID);
//        if (null == mSession || mSession.isEmpty()) {
//            return false;
//        }
//        mRadom = map.get(CommunicationParameterKey.CPK_RANDOM);
//        if (null == mRadom || mRadom.isEmpty()) {
//            return false;
//        }

        Log.d(TAG, "mSession:" + mSession);
        NativeCall pNC = NativeCall.GetNativeCaller();
        byte[] sid = pNC.EncryptReloginSession(mUserName, mRadom, mSession, mVersion);
        if (null == sid) {
            return false;
        }
        Log.w(TAG, "session:" + sid);
//        map.put(CommunicationParameterKey.CPK_PASSWORD, new String(pass));
        String strSession = new String(sid);
        mSession = strSession;
        Log.d(TAG, "mSession:" + mSession);

        return true;
    }

    private void generateRequestData() {
        mRequestData = ("ln=" + mUserName);
        mRequestData += "&";
        mRequestData += ("ver=" + mVersion);
        mRequestData += "&";
        mRequestData += ("rd=" + mRadom);

        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public int doOperation(HashMap<String, String> map,
                           CommunicationInterface.CICommandListener commandListener,
                           CommunicationInterface.CIProgressListener progressListener)
    {
//        Log.i(TAG, "doOperation");

        // POST /v1/admin/relogin/:id
        mCommandURL = "/v1/admin/relogin/" + mSession;

        generateRequestData();
        super.doOperation(map, commandListener, progressListener);

//        Log.i(TAG, "doOperation ... out");
        return CommunicationError.CE_ERROR_NO_ERROR;
    }
}
