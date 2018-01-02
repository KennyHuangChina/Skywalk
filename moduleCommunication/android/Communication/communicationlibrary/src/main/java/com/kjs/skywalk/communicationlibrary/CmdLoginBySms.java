package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

import static com.kjs.skywalk.communicationlibrary.IApiArgs.CLIENT_APP;
import static com.kjs.skywalk.communicationlibrary.IApiArgs.CLIENT_WEB;

//import CommunicationBase;

/**
 * Created by kenny on 2017/3/1.
 */

class CmdLoginBySms extends CommunicationBase {
    private String  mPicSessionId   = "";   // picture session id. ref to captcha GUID of API sec_pic
    private String  mPicResult      = "";   // picture result

    CmdLoginBySms(Context context, int client, String user, String sms) {
        super(context, CommunicationInterface.CmdID.CMD_LOGIN_BY_SMS);
        mMethodType = "POST";
        mNeedLogin  = false;

        mArgs = new Args(client, user, sms);

        // generate a random
        mRadom = generateRandom();
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        // Store current login session to local
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

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/admin/loginsms";
        return mCommandURL;
    }
    @Override
    public void generateRequestData() {
        mRequestData = ("ver=" +  mVersion);
        mRequestData += "&";
        mRequestData += ("ln=" + ((Args)mArgs).getUserName());
        mRequestData += "&";
        mRequestData += ("sms=" + ((Args)mArgs).getSms());
        mRequestData += "&";
//        mRequestData += ("rd=" + mRadom);
//        mRequestData += "&";
        mRequestData += ("typ=" + ((Args)mArgs).getClientType());

        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    //
    class Args extends ArgsUserName implements IApiArgs.IArgsLoginSms {
        private String  mSMS            = "";           // sms code
        private int     mClientType     = CLIENT_APP;   // client type. 0 - web; 1 - APP

        Args(int client, String user, String sms) {
            super(user);
            mClientType = client;
            mSMS        = sms;
        }

        @Override
        public int getClientType() {
            return mClientType;
        }

        @Override
        public String getSms() {
            return mSMS;
        }

        @Override
        public boolean checkArgs() {
            String Fn = "[checkArgs] ";
            if (mClientType != CLIENT_APP && mClientType != CLIENT_WEB) {
                Log.e(TAG, Fn + "Invalid client:" + mClientType);
                return false;
            }

            if (null == mSMS || mSMS.isEmpty()) {
                Log.e(TAG, Fn + "Invalid SMS:" + mSMS);
                return false;
            }

            return super.checkArgs();
        }

        @Override
        public boolean isEqual(IApiArgs.IArgsBase arg2) {
            if (!super.isEqual(arg2)) {
                return false;
            }

            Args ac = (Args)arg2;
            if (mClientType != ac.mClientType || !mSMS.equals(ac.mSMS)){
                return false;
            }
            return true;
        }
    }
}
