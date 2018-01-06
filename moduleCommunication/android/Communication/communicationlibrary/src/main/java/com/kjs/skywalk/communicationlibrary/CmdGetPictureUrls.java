package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

import static com.kjs.skywalk.communicationlibrary.IApiArgs.*;

/**
 * Created by kenny on 2017/5/13.
 */

class CmdGetPictureUrls extends CommunicationBase {
//    private int mPicId = 0;
//    private int mPicSize = 0;

    CmdGetPictureUrls(Context context, int picId, int picSize) {
        super(context, CommunicationInterface.CmdID.CMD_GET_PIC_URL);
        mNeedLogin  = false;
        mArgs       = new Args(picId, picSize);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/pic/" + ((Args)mArgs).getId();
        if (((Args)mArgs).getPicSize() > 0) {
            mCommandURL += "?size=" + ((Args)mArgs).getPicSize();
        }
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        super.generateRequestData();
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResGetUrls result = new ResGetUrls(nErrCode, jObject);
        return result;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    //
    class Args extends ApiArgsObjId implements IApiArgs.IArgsGetPicUrls {
        private int mPicSize = 0;

        Args(int pic, int size) {
            super(pic);
            mPicSize = size;
        }

        @Override
        public boolean checkArgs() {
            if (!super.checkArgs()) {
                return false;
            }
            if (mPicSize < PIC_SIZE_ALL || mPicSize > PIC_SIZE_Max) {
                Log.e(TAG, "[checkArgs] mPicSize:" + mPicSize);
                return false;
            }
            return true;
        }

        @Override
        public boolean isEqual(IApiArgs.IArgsBase arg2) {
            return super.isEqual(arg2) && mPicSize == ((Args)arg2).mPicSize;
        }

        @Override
        public int getPicSize() {
            return mPicSize;
        }
    }
}
