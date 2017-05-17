package com.kjs.skywalk.communicationlibrary;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kenny on 2017/5/16.
 */

class ResGetEventInfo extends ResBase implements IApiResults.IGetHouseEventInfo {
    private int     mEventId    = 0;    // event id
    private int     mHouseId    = 0;    // house id
    private String  mProperty   = "";   // property name
    private int     mBuildingNo = 0;    // the building number the house belong to
    private String  mHouseNo    = "";   // exact house number. like house 1305#
    private String  mSender     = "";   // people who send the event
    private String  mReceiver   = "";   // people who the event send to
    private String  mCreateTime = "";   // exact time when the event created
    private String  mReadTime   = "";   // exact time when the event get readed
    private String  mEventType  = "";   // event type
    private String  mEventDesc  = "";   // event description
    private int     mProcCount  = 0;

    ResGetEventInfo(int nErrCode, JSONObject jObject) {
        super(nErrCode);
        parse(jObject);
    }

    @Override
    public String DebugString() {
        super.DebugString();

        mString += "  id: " + mEventId + "\n";
        mString += "  hosue id: " + mHouseId + "\n";
        mString += "  property: " + mProperty + "\n";
        mString += "  " + mBuildingNo + "栋 " + mHouseNo + "室";
        mString += "  sender: " + mSender + ", receiver:" + mReceiver + "\n";
        mString += "  Create: " + mCreateTime + "\n";
        mString += "  Read: " + mReadTime + "\n";
        mString += "  Type: " + mEventType + "\n";
        mString += "  Desc: " + mEventDesc + "\n";
        mString += "  Proc: " + mProcCount + "\n";

        return mString;
    }

    @Override
    protected int parseResult(JSONObject obj) {
        try {
            JSONObject jEvent = obj.getJSONObject("Event");

            mEventId    = jEvent.getInt("Id");
            mHouseId    = jEvent.getInt("HouseId");
            mProperty   = jEvent.getString("Property");
            mBuildingNo = jEvent.getInt("Building");
            mHouseNo    = jEvent.getString("HouseNo");
            mSender     = jEvent.getString("Sender");
            mReceiver   = jEvent.getString("Receiver");
            mCreateTime = jEvent.getString("CreateTime");
            mReadTime   = jEvent.getString("ReadTime");
            mEventType  = jEvent.getString("Type");
            mEventDesc  = jEvent.getString("Desc");
            mProcCount  = jEvent.getInt("ProcCount");

        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }

        return 0;
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
        return mBuildingNo;
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
        return mEventType;
    }

    @Override
    public String EventDesc() {
        return mEventDesc;
    }
}
