package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

import static com.kjs.skywalk.communicationlibrary.IApiArgs.RECOMMEND_HOUSE;
import static com.kjs.skywalk.communicationlibrary.IApiArgs.UNRECOMMEND_HOUSE;

/**
 * Created by kenny on 2017/3/11.
 */

class CmdRecommendHouse extends CommunicationBase {

    CmdRecommendHouse(Context context, int house, int act) {
        super(context, CommunicationInterface.CmdID.CMD_GET_RECOMMEND_HOUSE);
        mMethodType = "PUT";
        mArgs = new Args(house, act);
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/house/recommend/" + ((Args)mArgs).getHouseId();
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("act=" + ((Args)mArgs).getRecommendAct());
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    //
    class Args extends ApiArgHouseId implements IApiArgs.IArgsRecommendHouse {
        private int mAct = RECOMMEND_HOUSE;   // 1 - recommend; 2 - unrecommend

        Args(int house, int act) {
            super(house);
            mAct = act;
        }

        @Override
        public boolean checkArgs() {
            if (!super.checkArgs()) {
                return false;
            }
            if (mAct < RECOMMEND_HOUSE || mAct > UNRECOMMEND_HOUSE) {
                Log.e(TAG, "[checkArgs] mAct:" + mAct);
            }
            return true;
        }

        @Override
        public boolean isEqual(IApiArgs.IArgsBase arg2) {
            if (!super.isEqual(arg2)) {
                return false;
            }
            if (mAct != ((Args)arg2).mAct) {
                return false;
            }
            return true;
        }

        @Override
        public int getRecommendAct() {
            return mAct;
        }
    }
}
