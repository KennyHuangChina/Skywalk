package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by kenny on 2017/5/14.
 */

class CmdGetHousePicList extends CmdGetXPicList {

    CmdGetHousePicList(Context context, int house, int type, int size) {
        super(context, house, type, size, CommunicationInterface.CmdID.CMD_GET_HOUSE_PIC_LIST);
    }

    @Override
    protected String getBaseURL() {
        return "/v1/pic/house/" + mXId;
    }
}
