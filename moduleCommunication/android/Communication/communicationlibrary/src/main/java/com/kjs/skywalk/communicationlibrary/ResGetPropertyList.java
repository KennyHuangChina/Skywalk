package com.kjs.skywalk.communicationlibrary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by kenny on 2017/2/22.
 */
public class ResGetPropertyList extends ResBase {
    private int mTotal = 0;
    private int mCount = 0;
    private ArrayList<ProperryInfo> mList = null;

    ResGetPropertyList(JSONObject obj) {
        mList = new ArrayList();
        parse(obj);
    }

    private int parse(JSONObject obj) {
        try {
            mTotal = obj.getInt("Total");
            mCount = obj.getInt("Count");
            JSONArray array = obj.getJSONArray("Properties");
            if (null == array) {
                return -1;
            }
            for (int n = 0; n < array.length(); n++) {
                ProperryInfo newProp = new ProperryInfo(array.getJSONObject(n));
                if (null == newProp) {
                    return -2;
                }
                mList.add(newProp);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public class ProperryInfo {
        private int       mId         = 0;
        private String    mName       = "";
        private String    mAddress    = "";
        private String    mDesciption = "";

        ProperryInfo(JSONObject obj) {
            try {
                mId = obj.getInt("Id");
                mName = obj.getString("PropName");
                mAddress = obj.getString("PropAddress");
                mDesciption = obj.getString("PropDesc");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public int GetId() { return mId; }
        public String GetName() { return mName; }
        public String GetAddress() { return mAddress; }
        public String GetDesc() { return mDesciption; }
    }
}
