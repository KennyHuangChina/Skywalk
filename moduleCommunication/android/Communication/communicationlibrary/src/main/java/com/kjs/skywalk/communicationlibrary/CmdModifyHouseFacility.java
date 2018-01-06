package com.kjs.skywalk.communicationlibrary;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by kenny on 2017/5/4.
 */

class CmdModifyHouseFacility extends CommunicationBase {
//    private int     mId     = 0;    // house facility id
//    private int     mFid    = 0;    // facility id
//    private int     mQty    = 0;    // facility quantity
//    private String  mDesc   = "";   // facility description

    CmdModifyHouseFacility(Context context, int house, int fid, int qty, String desc) {
        super(context, CommunicationInterface.CmdID.CMD_EDIT_HOUSE_FACILITY);
        mMethodType = "PUT";
        mArgs = new Args(house, new FacilityItem(fid, qty, desc));
    }

    @Override
    public String getRequestURL() {
        mCommandURL = "/v1/accessory/housefacility/" + ((Args)mArgs).getId();
        return mCommandURL;
    }

    @Override
    public void generateRequestData() {
        mRequestData = ("fid=" + ((Args)mArgs).mFacility.mFId);
        mRequestData += ("&fqty=" + ((Args)mArgs).mFacility.mQty);
        mRequestData += ("&fdesc=" + String2Base64(((Args)mArgs).mFacility.mDesc));
        Log.d(TAG, "mRequestData: " + mRequestData);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    //
    class Args extends ApiArgsObjId implements IApiArgs.IArgsEditHouseFacility {
        private  FacilityItem mFacility = null;

        Args(int house, FacilityItem facility) {
            super(house);
            mFacility = facility;
        }

        @Override
        public boolean checkArgs() {
            if (!super.checkArgs()) {
                return false;
            }
            if (null == mFacility) {
                Log.e(TAG, "[checkArgs] mFacility:" + mFacility);
                return false;
            }

            return mFacility.checkFacilityItem();
        }

        @Override
        public boolean isEqual(IApiArgs.IArgsBase arg2) {
            if (!super.isEqual(arg2)) {
                return false;
            }

            return mFacility.equal(((Args)arg2).mFacility);
        }

        @Override
        public IApiArgs.IFacilityItem getFacility() {
            return mFacility;
        }
    }
}
