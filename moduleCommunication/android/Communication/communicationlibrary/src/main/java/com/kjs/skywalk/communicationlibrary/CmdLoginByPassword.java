package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.kjs.skywalk.communicationlibrary.IApiArgs.*;

/**
 * Created by admin on 2017/1/23.
 */

class CmdLoginByPassword extends CommunicationBase {

    CmdLoginByPassword(Context context, int client, String salt, String user, String pass, String rand) {
        super(context, CommunicationInterface.CmdID.CMD_LOGIN_BY_PASSWORD);
        mMethodType = "POST";
        mSessionID  = "xxxxx";
        mNeedLogin  = false;

        // generate a random
        if (null == rand || rand.isEmpty()) {
            mRadom = generateRandom();
        } else {
            mRadom = rand;
        }

        mArgs = new Args(client, salt, user, pass);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/admin/loginpass";
        return mCommandURL;
    }
    @Override
    public void generateRequestData() {
        mRequestData = ("ver=" +  mVersion);
        mRequestData += "&";
        mRequestData += ("ln=" + ((Args)mArgs).getUserName());
        mRequestData += "&";
//        mRequestData += ("pw=123456abcdefg")
        mRequestData += ("pw=" + ((Args)mArgs).getPassword());
        mRequestData += "&";
        mRequestData += ("rd=" + mRadom);
        mRequestData += "&";
        mRequestData += ("typ=" + ((Args)mArgs).getClientType());

        Log.d(TAG, "mRequestData: " + mRequestData);
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

    //////////////////////////////////////////////////////////////////////////////////////////////
    //
    class Args extends ArgsUserName implements IApiArgs.IArgsLoginPass {
        private int     mClietnType = CLIENT_APP;    // client type: CLIENT_WEB, CLIENT_APP
        private String  mSalt       = "";   // user salt get from server
        private String  mPassword   = "";

        Args(int client, String salt, String user, String pass) {
            super(user);
            mClietnType = client;
            mSalt       = salt;
            mPassword   = pass;
        }

        @Override
        public int getClientType() {
            return mClietnType;
        }

        @Override
        public String getSalt() {
            return mSalt;
        }

        @Override
        public String getPassword() {
            return mPassword;
        }

        @Override
        public boolean checkArgs() {
            String Fn = "[checkArgs] ";

            if (mClietnType != CLIENT_WEB && mClietnType != CLIENT_APP) {
                Log.e(TAG, Fn + "Invalid client: " + mClietnType);
                return false;
            }

            if (!super.checkArgs()) {
                return false;
            }

            if (mPassword == null || mPassword.isEmpty()) {
                Log.e(TAG, Fn + "Invalid password: " + mPassword);
                return false;
            }

            if (mSalt == null || mSalt.isEmpty()) {
                Log.e(TAG, Fn + "Invalid salt: " + mSalt);
                return false;
            }

            NativeCall pNC = NativeCall.GetNativeCaller();
            byte[] pass = pNC.EncryptPassword(mPassword, mSalt, mRadom, mVersion);
            if (null == pass) {
                return false;
            }
            Log.w(TAG, "pass:" + pass);
//        map.put(CommunicationParameterKey.CPK_PASSWORD, new String(pass));
//        String strNewPass = new String(pass);
            mPassword = Base64.encodeToString(pass, Base64.URL_SAFE);
            Log.d(TAG, "mPassword:" + mPassword);

            return true;
        }

        @Override
        public boolean isEqual(IApiArgs.IArgsBase arg2) {
            if (!super.isEqual(arg2)) {
                return false;
            }

            Args ac = (Args)arg2;
            if (mClietnType != ac.mClietnType || !mSalt.equals(ac.mSalt) || !mPassword .equals(ac.mPassword)) {
                return false;
            }

            return true;
        }
    }
}
