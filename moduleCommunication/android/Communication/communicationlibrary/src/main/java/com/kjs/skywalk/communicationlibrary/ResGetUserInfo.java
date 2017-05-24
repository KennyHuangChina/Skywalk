package com.kjs.skywalk.communicationlibrary;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kenny on 2017/2/27.
 */

class ResGetUserInfo extends ResBase implements IApiResults.IGetUserInfo {

    private int mId = 0;
    private String mName = "";
    private String mPhoneNo = "";
    private String mIdNo = "";
    private String mHeadPotrait = "";
    private String mRole = "";
    private String mRoleDesc = "";

    ResGetUserInfo(int nErrCode, JSONObject jObject) {
        super(nErrCode);
        parse(jObject);
    }

    @Override
    public String DebugString() {
        super.DebugString();
        mString += "  user id: " + mId + "\n";
        mString += "  user name: " + mName + "\n";
        mString += "  phone: " + mPhoneNo + "\n";
        mString += "  ID: " + mIdNo + "\n";
        mString += "  header potrait: " + mHeadPotrait + "\n";
        mString += "  role: " + mRole + " (" + mRoleDesc + ")\n";
        return mString;
    }

    @Override
    protected int parseResult(JSONObject obj) {
        try {
            mId = obj.getInt("Id");
            mName = obj.getString("Name");
            mPhoneNo = obj.getString("Phone");
            mIdNo = obj.getString("IdNo");
            mHeadPotrait = obj.getString("HeadPortrait");
            mRole = obj.getString("Role");
            mRoleDesc = obj.getString("RoleDesc");
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
    public String GerRole() {
        return mRole;
    }

    @Override
    public String GetRoleDesc() {
        return mRoleDesc;
    }
}
