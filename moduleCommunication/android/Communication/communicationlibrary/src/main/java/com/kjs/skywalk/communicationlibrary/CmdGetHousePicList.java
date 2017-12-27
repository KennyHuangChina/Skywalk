package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import static com.kjs.skywalk.communicationlibrary.IApiArgs.PIC_TYPE_MAJOR_House;

/**
 * Created by kenny on 2017/5/14.
 */

class CmdGetHousePicList extends CmdGetXPicList {

    CmdGetHousePicList(Context context, int house, int type, int size) {
        super(context, house, type, size, CommunicationInterface.CmdID.CMD_GET_HOUSE_PIC_LIST);
    }

    @Override
    protected String getBaseURL() {
        return "/v1/pic/house/" + ((ApiArgsGetXPiclst)mArgs).getXId();
    }

    @Override
    protected boolean checkPicType(ResPicList res) {
        return (res.mPicType == PIC_TYPE_MAJOR_House) ? true : false;
    }
}
