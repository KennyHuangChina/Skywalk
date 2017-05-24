package com.kjs.skywalk.communicationlibrary;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kenny on 2017/5/16.
 */

class ResGetEventInfo extends ResBase implements IApiResults.IHouseEventInfo {
    private HouseEventInfo mHouseEventInfo = null;

    ResGetEventInfo(int nErrCode, JSONObject jObject) {
        super(nErrCode);
        parse(jObject);
    }

    @Override
    public String DebugString() {
        super.DebugString();

        if (null != mHouseEventInfo) {
            mString += mHouseEventInfo.DebugString();
        }

        return mString;
    }

    @Override
    protected int parseResult(JSONObject obj) {
        try {
            JSONObject jEvent = obj.getJSONObject("Event");
            mHouseEventInfo = new HouseEventInfo(jEvent);
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }

        return 0;
    }

    @Override
    public int EventId() {
        if (null != mHouseEventInfo) {
            return mHouseEventInfo.EventId();
        }
        return 0;
    }

    @Override
    public int HouseId() {
        if (null != mHouseEventInfo) {
            return mHouseEventInfo.HouseId();
        }
        return 0;
    }

    @Override
    public String Property() {
        if (null != mHouseEventInfo) {
            return mHouseEventInfo.Property();
        }
        return null;
    }

    @Override
    public int BuildingNo() {
        if (null != mHouseEventInfo) {
            return mHouseEventInfo.BuildingNo();
        }
        return 0;
    }

    @Override
    public String HouseNo() {
        if (null != mHouseEventInfo) {
            return mHouseEventInfo.HouseNo();
        }
        return null;
    }

    @Override
    public String Sender() {
        if (null != mHouseEventInfo) {
            return mHouseEventInfo.Sender();
        }
        return null;
    }

    @Override
    public String Receiver() {
        if (null != mHouseEventInfo) {
            return mHouseEventInfo.Receiver();
        }
        return null;
    }

    @Override
    public String CreateTime() {
        if (null != mHouseEventInfo) {
            return mHouseEventInfo.CreateTime();
        }
        return null;
    }

    @Override
    public String ReadTime() {
        if (null != mHouseEventInfo) {
            return mHouseEventInfo.ReadTime();
        }
        return null;
    }

    @Override
    public String EventType() {
        if (null != mHouseEventInfo) {
            return mHouseEventInfo.EventType();
        }
        return null;
    }

    @Override
    public String EventDesc() {
        if (null != mHouseEventInfo) {
            return mHouseEventInfo.EventDesc();
        }
        return null;
    }

    @Override
    public int ProcCnt() {
        if (null != mHouseEventInfo) {
            return mHouseEventInfo.ProcCnt();
        }
        return 0;
    }
}

class HouseEventInfo implements IApiResults.IHouseEventInfo {
    protected int      mEventId    = 0;    // event id
    protected int      mHouseId    = 0;    // house id
    protected String   mProperty   = "";   // property name
    protected int      mBuilding   = 0;    // bulding number
    protected String   mHouseNo    = "";   // house number
    protected String   mSender     = "";   // Event Sender
    protected String   mReceiver   = "";   // Event Receiver
    protected String   mCreateTime = "";   // Event create time
    protected String   mReadTime   = "";   // Event read time
    protected String   mType       = "";   // Event type
    protected String   mDesc       = "";   // Event Description
    protected int      mProcCount  = 0;    // How many processing follows the event}

    public HouseEventInfo(JSONObject jsonObject) {
        try {
            mEventId    = jsonObject.getInt("Id");
            mHouseId    = jsonObject.getInt("HouseId");
            mProperty   = jsonObject.getString("Property");
            mBuilding   = jsonObject.getInt("Building");
            mHouseNo    = jsonObject.getString("HouseNo");
            mSender     = jsonObject.getString("Sender");
            mReceiver   = jsonObject.getString("Receiver");
            mCreateTime = jsonObject.getString("CreateTime");
            mReadTime   = jsonObject.getString("ReadTime");
            mType       = jsonObject.getString("Type");
            mDesc       = jsonObject.getString("Desc");
            mProcCount  = jsonObject.getInt("ProcCount");

        } catch (JSONException e) {
            e.printStackTrace();
//            return -1;
        }
    }

    public String DebugString() {
        String dbString = "";
        dbString += "  id: " + mEventId + "\n";
        dbString += "  Hosue id: " + mHouseId + "\n";
        dbString += "  Property: " + mProperty + "\n";
        dbString += "  " + mBuilding + "栋 " + mHouseNo + "室";
        dbString += "  Sender: " + mSender + ", Receiver:" + mReceiver + "\n";
        dbString += "  Create: " + mCreateTime + "\n";
        dbString += "  Read: " + mReadTime + "\n";
        dbString += "  Type: " + mType + "\n";
        dbString += "  Desc: " + mDesc + "\n";
        dbString += "  Proc: " + mProcCount + "\n";

        return dbString;
    }

    @Override
    public int EventId() {
        return mEventId;
    }

    @Override
    public int HouseId() {
        return mHouseId;
    }

    @Override
    public String Property() {
        return mProperty;
    }

    @Override
    public int BuildingNo() {
        return mBuilding;
    }

    @Override
    public String HouseNo() {
        return mHouseNo;
    }

    @Override
    public String Sender() {
        return mSender;
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
    public String EventType() {
        return mType;
    }

    @Override
    public String EventDesc() {
        return mDesc;
    }

    @Override
    public int ProcCnt() {
        return mProcCount;
    }
}