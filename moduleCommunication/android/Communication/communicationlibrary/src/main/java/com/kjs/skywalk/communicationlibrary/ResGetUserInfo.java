package com.kjs.skywalk.communicationlibrary;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kenny on 2017/2/27.
 */

class ResGetUserInfo extends ResBase implements IApiResults.IGetUserInfo {

    private int     mId             = 0;
    private String  mName           = "";
    private String  mPhoneNo        = "";
    private String  mIdNo           = "";
    private String  mHeadPotrait    = "";
    private boolean mbAgent         = false;
    private boolean mbAdmin         = false;

    ResGetUserInfo(int nErrCode, JSONObject jObject) {
        super(nErrCode);
        parse(jObject);
    }

    @Override
    public String DebugString() {
        super.DebugString();
        mString += "  user id: " + GetUserId() + "\n";
        mString += "  user name: " + GetName() + "\n";
        mString += "  phone: " + GetPhoneNo() + "\n";
        mString += "  ID: " + GetIdNo() + "\n";
        mString += "  header potrait: " + GetHead() + "\n";
        mString += "  Is Admin: " + IsAdmin() + "\n";
        mString += "  Is Agentn: " + IsAgent() + "\n";
        return mString;
    }

    @Override
    protected int parseResult(JSONObject obj) {
        try {
            mId             = obj.getInt("Id");
            mName           = obj.getString("Name");
            mPhoneNo        = obj.getString("Phone");
            mIdNo           = obj.getString("IdNo");
            mHeadPotrait    = obj.getString("HeadPortrait");
            mbAdmin         = obj.getBoolean("Admin");
            mbAgent         = obj.getBoolean("Agent");
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }

        return 0;
    }

    @Override
    public int GetUserId() {
        return mId;
    }

    @Override
    public String GetName() {
        return mName;
    }

    @Override
    public String GetPhoneNo() {
        return mPhoneNo;
    }

    @Override
    public String GetIdNo() {
        return mIdNo;
    }

    @Override
    public String GetHead() {
        return mHeadPotrait;
    }

    @Override
    public Boolean IsAdmin() {
        return mbAdmin;
    }

    @Override
    public Boolean IsAgent() {
        return mbAgent;
    }
}
