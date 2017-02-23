package com.kjs.skywalk.communicationlibrary;

//import ResBase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kenny on 2017/2/23.
 */

public class ResList extends ResBase {
    private int mTotal = 0;
    private int mFetched = 0;
    protected ArrayList<Object> mList = null;

    ResList() {
        mList = new ArrayList();
    }

    public ArrayList<Object> GetPropertyList() {
        return mList;
    }

    protected int parse(JSONObject obj) {
        try {
            mTotal = obj.getInt("Total");
            mFetched = obj.getInt("Count");
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }

        if (mFetched > 0) {
            return parseList(obj);
        }

        return 0;
    }

    protected int parseList(JSONObject obj) {
        return 0;
    }

    public int GetTotalNumber() { return mTotal; }
    public int GetFetchedNumber() { return mFetched; }
}
