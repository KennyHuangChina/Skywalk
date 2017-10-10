package com.kjs.skywalk.communicationlibrary;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kenny on 2017/5/16.
 */

class ResGetSysMsgInfo extends ResBase implements IApiResults.ISysMsgInfo {
    private SysMsgInfo mSysMsgInfo = null;

    ResGetSysMsgInfo(int nErrCode, JSONObject jObject) {
        super(nErrCode);
        parse(jObject);
    }

    @Override
    public String DebugString() {
        super.DebugString();

        if (null != mSysMsgInfo) {
            mString += mSysMsgInfo.DebugString();
        }

        return mString;
    }

    @Override
    protected int parseResult(JSONObject obj) {
        try {
            JSONObject jEvent = obj.getJSONObject("Msg");
            mSysMsgInfo = new SysMsgInfo(jEvent);
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }

        return 0;
    }

    @Override
    public int MsgId() {
        if (null != mSysMsgInfo) {
            return mSysMsgInfo.MsgId();
        }
        return 0;
    }

    @Override
    public int MsgType() {
        if (null != mSysMsgInfo) {
            return mSysMsgInfo.MsgType();
        }
        return 0;
    }

    @Override
    public int RefId() {
        if (null != mSysMsgInfo) {
            return mSysMsgInfo.RefId();
        }
        return 0;
    }

    @Override
    public int MsgPriority() {
        if (null != mSysMsgInfo) {
            return mSysMsgInfo.MsgPriority();
        }
        return 0;
    }

    @Override
    public String Property() {
        if (null != mSysMsgInfo) {
            return mSysMsgInfo.Property();
        }
        return null;
    }

    @Override
    public String BuildingNo() {
        if (null != mSysMsgInfo) {
            return mSysMsgInfo.BuildingNo();
        }
        return null;
    }

    @Override
    public String HouseNo() {
        if (null != mSysMsgInfo) {
            return mSysMsgInfo.HouseNo();
        }
        return null;
    }

    @Override
    public int Livingrooms() {
        if (null != mSysMsgInfo) {
            return mSysMsgInfo.Livingrooms();
        }
        return 0;
    }

    @Override
    public int Bedrooms() {
        if (null != mSysMsgInfo) {
            return mSysMsgInfo.Bedrooms();
        }
        return 0;
    }

    @Override
    public int Bathrooms() {
        if (null != mSysMsgInfo) {
            return mSysMsgInfo.Bathrooms();
        }
        return 0;
    }

    @Override
    public String Receiver() {
        if (null != mSysMsgInfo) {
            return mSysMsgInfo.Receiver();
        }
        return null;
    }

    @Override
    public String CreateTime() {
        if (null != mSysMsgInfo) {
            return mSysMsgInfo.CreateTime();
        }
        return null;
    }

    @Override
    public String ReadTime() {
        if (null != mSysMsgInfo) {
            return mSysMsgInfo.ReadTime();
        }
        return null;
    }

    @Override
    public String MsgBody() {
        if (null != mSysMsgInfo) {
            return mSysMsgInfo.MsgBody();
        }
        return null;
    }
}

class SysMsgInfo implements IApiResults.ISysMsgInfo, InternalDefines.IListItemInfoInner {
    protected int      mMsgId       = 0;    // message id
    protected int      mMsgType     = -1;   // message type
    protected int      mMsgPriority = 0;    // message priority
    protected int      mMsgRefId    = 0;    // reference id, depend on what the message type is
    protected String   mProperty    = "";   // property name
    protected String   mBuilding    = "";    // bulding number
    protected String   mHouseNo     = "";   // house number
    protected int      mLivingrooms = 0;
    protected int      mBedrooms    = 0;
    protected int      mBathrooms   = 0;
    protected String   mReceiver    = "";   // message Receiver
    protected String   mCreateTime  = "";   // message create time
    protected String   mReadTime    = "";   // message read time
    protected String   mMsgTxt      = "";   // Message body

    public SysMsgInfo(JSONObject jMsg) {
        try {
            mMsgId          = jMsg.getInt("Id");
            mMsgType        = jMsg.getInt("Type");
            mMsgRefId       = jMsg.getInt("RefId");
            mReceiver       = jMsg.getString("Receiver");
            mMsgPriority    = jMsg.getInt("Priority");
            mCreateTime     = jMsg.getString("CreateTime");
            mReadTime       = jMsg.getString("ReadTime");
            mMsgTxt         = jMsg.getString("Msg");

            JSONObject jHouse = jMsg.getJSONObject("House");

            mProperty       = jHouse.getString("Property");
            mBuilding       = jHouse.getString("BuildingNo");
            mHouseNo        = jHouse.getString("HouseNo");
            mLivingrooms    = jHouse.getInt("Livingrooms");
            mBedrooms       = jHouse.getInt("Bedrooms");
            mBathrooms      = jHouse.getInt("Bathrooms");

        } catch (JSONException e) {
            e.printStackTrace();
//            return -1;
        }
    }

    public String DebugString() {
        String dbString = "";
        dbString += "  id: " + MsgId() + ", type:" + MsgType() + ", ref to:" + RefId() + "\n";
        dbString += "  Priority: " + MsgPriority() + "\n";
        dbString += "  Property: " + Property() + "\n";
        dbString += "  " + BuildingNo() + "栋 " + HouseNo() + "室\n";
        dbString += "  " + Bedrooms() + "房 " + Livingrooms() + "厅 " + Bathrooms() + "卫\n";
        dbString += "  Receiver:" + Receiver() + "\n";
        dbString += "  Create: " + CreateTime() + "\n";
        dbString += "  Read: " + ReadTime() + "\n";
        dbString += "  Text: " + MsgBody() + "\n";

        return dbString;
    }

    @Override
    public int MsgId() {
        return mMsgId;
    }

    @Override
    public int MsgType() {
        return mMsgType;
    }

    @Override
    public int RefId() {
        return mMsgRefId;
    }

    @Override
    public int MsgPriority() {
        return mMsgPriority;
    }

    @Override
    public String Property() {
        return mProperty;
    }

    @Override
    public String BuildingNo() {
        return mBuilding;
    }

    @Override
    public String HouseNo() {
        return mHouseNo;
    }

    @Override
    public int Livingrooms() {
        return mLivingrooms;
    }

    @Override
    public int Bedrooms() {
        return mBedrooms;
    }

    @Override
    public int Bathrooms() {
        return mBathrooms;
    }

    @Override
    public String Receiver() {
        return mReceiver;
    }

    @Override
    public String CreateTime() {
        return mCreateTime;
    }

    @Override
    public String ReadTime() {
        return mReadTime;
    }

    @Override
    public String MsgBody() {
        return mMsgTxt;
    }

    @Override
    public String ListItemInfo2String() {
        return DebugString();
    }
}