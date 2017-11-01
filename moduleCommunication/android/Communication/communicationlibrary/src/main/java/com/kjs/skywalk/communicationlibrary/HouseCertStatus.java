package com.kjs.skywalk.communicationlibrary;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kenny on 2017/11/1.
 */

class HouseCertStatus implements IApiResults.IHouseCertDigestInfo {
    private int     mCertStat   = 0;
    private Date    mCertTime   = null;
    private String  mCertDesc   = null;

    HouseCertStatus(JSONObject joHouseCertStat) {
        parse(joHouseCertStat);
    }

    private int parse(JSONObject joHouseCertStat) {
        try {
            mCertStat = joHouseCertStat.getInt("CertStat");
            mCertDesc = joHouseCertStat.getString("CertDesc");

            DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String strCertTime = joHouseCertStat.getString("CertTime");
            if (null != strCertTime && !strCertTime.isEmpty()) {
                try {
                    mCertTime = format.parse(strCertTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return -2;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }

        return 0;
    }

    @Override
    public int CertStat() {
        return mCertStat;
    }

    @Override
    public Date CertTime() {
        return mCertTime;
    }

    @Override
    public String CertDesc() {
        return mCertDesc;
    }

    public String DebugString() {
        String sDebugString = "";
        sDebugString += (" Cert Status: " + CertStat() + "\n");
        sDebugString += (" Cert Desc: " + CertDesc() + "\n");
        sDebugString += (" Cert Time: " + CertTime() + "\n");

        return sDebugString;
    }
}
