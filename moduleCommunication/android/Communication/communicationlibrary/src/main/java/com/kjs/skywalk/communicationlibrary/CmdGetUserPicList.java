package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import static com.kjs.skywalk.communicationlibrary.IApiArgs.PIC_TYPE_MAJOR_User;

/**
 * Created by kenny on 2017/10/28.
 */

class CmdGetUserPicList extends CmdGetXPicList {

    CmdGetUserPicList(Context context, int user, int type, int size) {
        super(context, user, type, size, CommunicationInterface.CmdID.CMD_GET_USER_PIC_LIST);
    }

    @Override
    protected String getBaseURL() {
        return "/v1/pic/user/" + ((ApiArgsGetXPiclst)mArgs).getXId();
    }

    @Override
    protected boolean checkPicType(ResPicList res) {
        return (res.mPicType == PIC_TYPE_MAJOR_User) ? true : false;
    }
}
