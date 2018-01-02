package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

import static com.kjs.skywalk.communicationlibrary.IApiArgs.CLIENT_APP;

/**
 * Created by kenny on 2017/8/6.
 */

class CmdResetLoginPassword extends CommunicationBase {
    private String mSession = "";   // "1234567890";

    CmdResetLoginPassword(Context context, String user, String newPass, String sms, String salt, String rand) {
        super(context, CommunicationInterface.CmdID.CMD_RESET_LOGIN_PASS);
        mMethodType = "PUT";
        mNeedLogin  = false;
        mArgs = new Args(user, salt, sms, newPass);

//        mRadom = rand;
        // generate a random
        mRadom = generateRandom();

        // Load session for previous login
        SKSessionStore sessStore = SKSessionStore.getInstance(mContext);
        if (null != sessStore) {
            mSession = sessStore.get();
        }
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/admin/resetpass";
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("v=1.0");
        String user = ((Args)mArgs).getUserName();
        if (null != user && !user.isEmpty()) {
            mRequestData += "&";
            mRequestData += ("u=" + user);
        }
        String newPass = ((Args)mArgs).getPassword();
        if (null != newPass && !newPass.isEmpty()) {
            mRequestData += "&";
            mRequestData += ("p=" + newPass);
        }
        if (null != mRadom && !mRadom.isEmpty()) {
            mRequestData += "&";
            mRequestData += ("r=" + mRadom);
        }
        String sms = ((Args)mArgs).getSms();
        if (null != sms && !sms.isEmpty()) {
            mRequestData += "&";
            mRequestData += ("s=" + sms);
        }
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public int doOperation(CommunicationInterface.CICommandListener commandListener,
                           CommunicationInterface.CIProgressListener progressListener) {
        String FN = "[doOperation] ";

        if (mSession.isEmpty() /*0 == mSession.length()*/) {
            Log.e(TAG, FN + "mSession is empty");
            return CommunicationError.CE_COMMAND_ERROR_NOT_LOGIN;
        }
/*
        NativeCall pNC = NativeCall.GetNativeCaller();
        byte[] pass = pNC.EncryptNewPassword(((Args)mArgs).getPassword(), ((Args)mArgs).getSalt(), mRadom, mVersion);
//        byte[] pass = pNC.EncryptNewPassword(mNewPass, "f1c0fb8578e356746e0f98ce07b7a27f", "666666", mVersion);
        if (null == pass) {
            Log.e(TAG, FN + "Fail to encrypt relogin session");
            return CommunicationError.CE_COMMAND_ERROR_FATAL_ERROR;
        }
        Log.w(TAG, "pass:" + pass);
//        map.put(CommunicationParameterKey.CPK_PASSWORD, new String(pass));
//        String strNewPass = new String(pass);
        ((Args)mArgs).mNewPass = new String(pass);
//        mNewPass = Base64.encodeToString(pass, Base64.URL_SAFE);
        Log.d(TAG, "mNewPass:" + mNewPass);
*/
        return super.doOperation(commandListener, progressListener);
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResResetPass res = new ResResetPass(nErrCode, jObject);
        return res;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //
    class Args extends ArgsUserName implements IApiArgs.IArgsResetLoginPwd {
        private String mNewPass = null;
        private String mSalt    = null;
        private String mSms     = null;

        Args(String user, String salt, String sms, String newPwd) {
            super(user);
            mNewPass    = newPwd;
            mSalt       = salt;
            mSms        = sms;
        }

        @Override
        public boolean checkArgs() {
            String Fn = "[checkArgs] ";
            if (!super.checkArgs()) {
                return false;
            }

            if (null == mNewPass || mNewPass.isEmpty()) {
                Log.e(TAG, Fn + "mNewPass:" + mNewPass);
                return false;
            }
            if (null == mSms || mSms.isEmpty()) {
                Log.e(TAG, Fn + "mSms:" + mSms);
                return false;
            }
            if (null == mSalt || mSalt.isEmpty()) {
                Log.e(TAG, Fn + "mSalt:" + mSalt);
                return false;
            }

            // Encrypt the password
            NativeCall pNC = NativeCall.GetNativeCaller();
            byte[] pass = pNC.EncryptNewPassword(mNewPass, mSalt, mRadom, mVersion);
//        byte[] pass = pNC.EncryptNewPassword(mNewPass, "f1c0fb8578e356746e0f98ce07b7a27f", "666666", mVersion);
            if (null == pass) {
                Log.e(TAG, Fn + "Fail to encrypt password");
                return false;
            }
            Log.w(TAG, "pass:" + pass);
//        map.put(CommunicationParameterKey.CPK_PASSWORD, new String(pass));
//        String strNewPass = new String(pass);
            mNewPass = new String(pass);
//        mNewPass = Base64.encodeToString(pass, Base64.URL_SAFE);
            Log.d(TAG, "mNewPass:" + mNewPass);

            return true;
        }

        @Override
        public boolean isEqual(IApiArgs.IArgsBase arg2) {
            if (!super.isEqual(arg2)) {
                return false;
            }
            Args ac = (Args)arg2;
            if (!mSalt.equals(ac.mSalt) || !mSms.equals(ac.mSms) || !mNewPass.equals(ac.mNewPass)) {
                return false;
            }
            return true;
        }

        @Override
        public String getSalt() {
            return mSalt;
        }

        @Override
        public String getPassword() {
            return mNewPass;
        }

        @Override
        public String getSms() {
            return mSms;
        }
    }
}
