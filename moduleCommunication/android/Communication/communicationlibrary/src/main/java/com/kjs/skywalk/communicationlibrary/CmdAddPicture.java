package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.kjs.skywalk.communicationlibrary.IApiArgs.*;

/**
 * Created by kenny on 2017/5/8.
 */

class CmdAddPicture extends CommunicationBase {

    CmdAddPicture(Context context, String file, int house, int type, int refId, String desc) {
        super(context, CommunicationInterface.CmdID.CMD_ADD_PICTURE);
        mMethodType = "POST";
        mArgs = new Args(house, file, type, refId, desc);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/pic/newpic";
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("house=" + ((Args)mArgs).getHouse());
        mRequestData += "&";
        mRequestData += ("type=" + ((Args)mArgs).getType());
        mRequestData += "&";
        mRequestData += ("rid=" + ((Args)mArgs).getObjId());
        mRequestData += "&";
        mRequestData += ("desc=" + ((Args)mArgs).getDesc());
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    @Override
    public boolean checkParameter(HashMap<String, String> map) {
        if (!super.checkParameter(map)) {
            return false;
        }
        mFile = ((Args)mArgs).getFile();
        return true;
    }

    @Override
    public IApiResults.ICommon doParseResult(int nErrCode, JSONObject jObject) {
        ResAddPic result = new ResAddPic(nErrCode, jObject);
        return result;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //
    //
    class Args extends ApiArgsBase implements IApiArgs.IArgsAddPic {
        private int     mHouse  = 0;
        private int     mType   = -1;
        private int     mRefId  = -1;
        private String  mDesc   = null;
        private String  mFile   = null;

        Args(int house, String file, int type, int refId, String desc) {
            mHouse  = house;
            mType   = type;
            mRefId  = refId;
            mDesc   = desc;
            mFile   = file;
        }

        @Override
        public boolean checkArgs() {
            if (mRefId < 0) {
                Log.e(TAG, "mRefId: " + mRefId);
                return false;
            }
            if (!checkPicType()) {
                Log.e(TAG, "mType: " + mType);
                return false;
            }

            if (null != mDesc && !mDesc.isEmpty()) {
                mDesc = String2Base64(mDesc);
                Log.d(TAG, "mDesc: " + mDesc);
            }

            if (mFile.length() == 0) {
                Log.e(TAG, "picture file not assigned");
                return false;
            }
            if (!CUtilities.isPicture(mFile)) {
                Log.e(TAG, "picture file not exist");
                return false;
            }
            Log.i(TAG, "house:" + mHouse + ", type:" + mType + ", desc:" + mDesc + ", file:" + mFile);

            return true;
        }

        @Override
        public boolean isEqual(IApiArgs.IArgsBase arg2) {
            if (!super.isEqual(arg2)) {
                return false;
            }

            Args ac = (Args)arg2;
            if (mHouse != ac.mHouse || mType != ac.mType || mRefId != ac.mRefId ||
                    !mDesc.equals(ac.mDesc) || !mFile.equals(ac.mFile)) {
                return false;
            }
            return true;
        }

        @Override
        public int getHouse() {
            return mHouse;
        }

        @Override
        public int getType() {
            return mType;
        }

        @Override
        public int getObjId() {
            return mRefId;
        }

        @Override
        public String getFile() {
            return mFile;
        }

        @Override
        public String getDesc() {
            return mDesc;
        }

        boolean checkPicType() {
            if (mType >= PIC_TYPE_MAJOR_User + PIC_TYPE_SUB_USER_Begin &&
                    mType <= PIC_TYPE_MAJOR_User + PIC_TYPE_SUB_USER_End) {
                return true;
            }
            if (mType >= PIC_TYPE_MAJOR_House + PIC_TYPE_SUB_HOUSE_BEGIN &&
                    mType <= PIC_TYPE_MAJOR_House + PIC_TYPE_SUB_HOUSE_END) {
                return true;
            }
            if (mType >= PIC_TYPE_MAJOR_Rental + PIC_TYPE_SUB_RENTAL_Begin &&
                    mType <= PIC_TYPE_MAJOR_Rental + PIC_TYPE_SUB_RENTAL_End) {
                return true;
            }
            return false;
        }
    }
}
