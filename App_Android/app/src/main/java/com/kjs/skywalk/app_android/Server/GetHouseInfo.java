package com.kjs.skywalk.app_android.Server;

import android.content.Context;
import android.os.AsyncTask;

import com.kjs.skywalk.app_android.ClassDefine;
import com.kjs.skywalk.app_android.kjsLogUtil;
import com.kjs.skywalk.communicationlibrary.CommandManager;
import com.kjs.skywalk.communicationlibrary.CommunicationError;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface;
import com.kjs.skywalk.communicationlibrary.IApiResults;

import java.util.ArrayList;
import java.util.HashMap;

import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_BRIEF_PUBLIC_HOUSE_INFO;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_HOUSE_DIGEST_LIST;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_HOUSE_INFO;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.HouseFilterCondition.SORT_PUBLISH_TIME;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.HouseFilterCondition.SORT_PUBLISH_TIME_DESC;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.HouseFilterCondition.SORT_RENTAL;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.HouseFilterCondition.SORT_RENTAL_DESC;

/**
 * Created by Jackie on 2017/7/6.
 */

public class GetHouseInfo extends SKBaseAsyncTask {

    private TaskFinished mTaskFinished = null;
    private int mHouseId = -1;
    private boolean mBackEnd = false;

    public GetHouseInfo(Context context, TaskFinished listener) {
        mContext = context;
        mTaskFinished = listener;
    }

    public interface TaskFinished {
        void onTaskFinished(ClassDefine.HouseInfo houseInfo);
    }

    @Override
    protected  void onPostExecute(Integer result) {
        //mTaskFinished.onTaskFinished(mHouseList, result.intValue());
        //???????some times this function does not called....Why
    }

    @Override
    protected Integer doInBackground(Integer... params) {
        int result = 0;
        mHouseId = params[0].intValue();
        if(params[1].intValue() == 0) {
            mBackEnd = false;
        } else {
            mBackEnd = true;
        }

        mResultGot = false;

        CommandManager CmdMgr = CommandManager.getCmdMgrInstance(mContext, mCmdListener, mProgressListener);
        result = CmdMgr.GetHouseInfo(mHouseId, mBackEnd);
        if(result != CommunicationError.CE_ERROR_NO_ERROR) {
            kjsLogUtil.e("Get House Info failed!");
            return -1;
        }

        if(!waitResult(1000)) {
            kjsLogUtil.i(String.format("[doInBackground] ------ get house info timeout"));
            return -1;
        }

        return 0;
    }

    CommunicationInterface.CICommandListener mCmdListener = new CommunicationInterface.CICommandListener() {
        @Override
        public void onCommandFinished(int command, IApiResults.ICommon iResult) {
            GetHouseInfo.super.onCommandFinished(command, iResult);
            if (null == iResult) {
                kjsLogUtil.w("result is null");
                mResultGot = true;
                return;
            }
            kjsLogUtil.i(String.format("[command: %d] --- %s" , command, iResult.DebugString()));
            if (CommunicationError.CE_ERROR_NO_ERROR != iResult.GetErrCode()) {
                kjsLogUtil.e("Command:" + command + " finished with error: " + iResult.GetErrDesc());
                mResultGot = true;
                mTaskFinished.onTaskFinished(null);
                return;
            }

            if (command == CMD_GET_HOUSE_INFO) {
                IApiResults.IGetHouseInfo info = (IApiResults.IGetHouseInfo) iResult;
                ClassDefine.HouseInfo houseInfo = new ClassDefine.HouseInfo();
                houseInfo.floor = info.Floorthis();
                houseInfo.totalFloor = info.FloorTotal();
                houseInfo.bedRooms = info.Bedrooms();
                houseInfo.livingRooms = info.Livingrooms();
                houseInfo.bathRooms = info.Bathrooms();
                houseInfo.area = info.Acreage();
                houseInfo.landlordId = info.Landlord();
                houseInfo.submitTime = info.SubmitTime();
                mTaskFinished.onTaskFinished(houseInfo);
            }

            mResultGot = true;
        }
    };
}
