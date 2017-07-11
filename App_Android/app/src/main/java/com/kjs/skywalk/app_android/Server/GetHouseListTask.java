package com.kjs.skywalk.app_android.Server;

import android.content.Context;
import android.os.AsyncTask;

import com.kjs.skywalk.app_android.ClassDefine;
import com.kjs.skywalk.app_android.kjsLogUtil;
import com.kjs.skywalk.communicationlibrary.CommandManager;
import com.kjs.skywalk.communicationlibrary.CommunicationError;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface;
import com.kjs.skywalk.communicationlibrary.IApiResults;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_BRIEF_PUBLIC_HOUSE_INFO;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_HOUSE_DIGEST_LIST;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.HouseFilterCondition.SORT_PUBLISH_TIME;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.HouseFilterCondition.SORT_PUBLISH_TIME_DESC;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.HouseFilterCondition.SORT_RENTAL;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.HouseFilterCondition.SORT_RENTAL_DESC;

/**
 * Created by Jackie on 2017/7/6.
 */

public class GetHouseListTask extends AsyncTask<Integer, Void, Integer> {

    public static int TYPE_ALL = 0;
    public static int TYPE_RECOMMAND = 1;
    public static int TYPE_DEDUCTED = 2;
    public static int TYPE_NEW = 3;

    public static final int SORT_TYPE_DEFAULT = 0;
    public static final int SORT_TYPE_PUBLIC_TIME = 0x01;
    public static final int SORT_TYPE_PUBLIC_TIME_DESC = 0x02;
    public static final int SORT_TYPE_RENTAL = 0x04;
    public static final int SORT_TYPE_RENTAL_DESC = 0x08;
    public static final int SORT_TYPE_APPOINT = 0x10;
    public static final int SORT_TYPE_APPOINT_DESC = 0x20;

    private int mBegin = 0;
    private int mCount = 0;
    private int mType = 0;
    private boolean mResultGot = false;
    private int mTotalCount = 0;
    private Context mContext;
    private TaskFinished mTaskFinished = null;

    private ArrayList<ClassDefine.HouseDigest> mHouseList = new ArrayList<>();

    private CommunicationInterface.HouseFilterCondition mFilter = new CommunicationInterface.HouseFilterCondition();
    private ArrayList<Integer> mSort = new ArrayList<Integer>();
    public GetHouseListTask(Context context, TaskFinished listener) {
        mContext = context;
        mTaskFinished = listener;
    }

    public interface TaskFinished {
        void onTaskFinished(ArrayList<ClassDefine.HouseDigest> houseList, int totalCount);
    }

    public void addFilterRental(int low, int high) {
        mFilter.mRental.FilterBetween(low, high);
    }

    public void addFilterBedRoom() {
    }

    public void addFilterTag() {

    }

    public void setSort(int sortBy) {
        switch (sortBy) {
            case SORT_TYPE_DEFAULT:
                mSort.clear();
                break;

            case SORT_TYPE_PUBLIC_TIME:
                mSort.add(SORT_PUBLISH_TIME);
                break;

            case SORT_TYPE_PUBLIC_TIME_DESC:
                mSort.add(SORT_PUBLISH_TIME_DESC);
                break;

            case SORT_TYPE_RENTAL:
                mSort.add(SORT_RENTAL);
                break;

            case SORT_TYPE_RENTAL_DESC:
                mSort.add(SORT_RENTAL_DESC);
                break;

            case SORT_TYPE_APPOINT:
                mSort.add(SORT_RENTAL);
                break;

            case SORT_TYPE_APPOINT_DESC:
                mSort.add(SORT_RENTAL_DESC);
                break;
        }
    }

    @Override
    protected  void onPostExecute(Integer result) {
        //mTaskFinished.onTaskFinished(mHouseList, result.intValue());
        //???????some times this function does not called....Why
    }

    @Override
    protected Integer doInBackground(Integer... params) {
        int result = 0;

        mType = params[0].intValue();
        mBegin = params[1].intValue();
        mCount = params[2].intValue();

        CommandManager CmdMgr = new CommandManager(mContext, mCmdListener, mProgreessListener);

        mResultGot = false;
        result = CmdMgr.GetHouseDigestList(mType, 0, 0, mFilter, mSort);
        if(!waitResult(3000)) {
            kjsLogUtil.i(String.format("[doInBackground] ------ get count timeout"));
            return 0;
        }

        result = CmdMgr.GetHouseDigestList(mType, mBegin, mCount, mFilter, mSort);
        if(!waitResult(3000)) {
            kjsLogUtil.i(String.format("[doInBackground] ------ get house list timeout"));
            return 0;
        }

        return mTotalCount;
    }

    CommunicationInterface.CICommandListener mCmdListener = new CommunicationInterface.CICommandListener() {
        @Override
        public void onCommandFinished(int command, IApiResults.ICommon iResult) {
            if (null == iResult) {
                kjsLogUtil.w("result is null");
                mResultGot = true;
                return;
            }
            kjsLogUtil.i(String.format("[command: %d] --- %s" , command, iResult.DebugString()));
            if (CommunicationError.CE_ERROR_NO_ERROR != iResult.GetErrCode()) {
                kjsLogUtil.e("Command:" + command + " finished with error: " + iResult.GetErrDesc());
                mResultGot = true;
                return;
            }

            if (command == CMD_GET_HOUSE_DIGEST_LIST) {
                IApiResults.IResultList resultList = (IApiResults.IResultList) iResult;
                int nFetch = resultList.GetFetchedNumber();
                if (nFetch == -1) {
                    mTotalCount = resultList.GetTotalNumber();
                } else  {
                    // IApiResults.IHouseDigest
                    ArrayList<Object> houseDigests = resultList.GetList();
                    for (Object houseDigestObj : houseDigests) {
                        IApiResults.IHouseDigest houseDigestRes = (IApiResults.IHouseDigest) houseDigestObj;
                        ClassDefine.HouseDigest houseDigest = new ClassDefine.HouseDigest();
                        houseDigest.houseId = houseDigestRes.GetHouseId();
                        houseDigest.property = houseDigestRes.GetProperty();
                        houseDigest.addr = houseDigestRes.GetPropertyAddr();
                        houseDigest.Bedrooms = houseDigestRes.GetBedrooms();
                        houseDigest.Livingrooms = houseDigestRes.GetLivingrooms();
                        houseDigest.Bathrooms = houseDigestRes.GetBathrooms();
                        double acreage = (double)houseDigestRes.GetAcreage() / 100.0;
                        houseDigest.Acreage = Double.valueOf(String.format("%.02f", acreage));
                        double rental = (double)houseDigestRes.GetRental() / 100.0;
                        houseDigest.Rental = Double.valueOf(String.format("%.02f", rental));
                        double pricing = (double)houseDigestRes.GetPricing() / 100.0;
                        houseDigest.Pricing = Double.valueOf(String.format("%.02f", pricing));
                        houseDigest.CoverImage = houseDigestRes.GetCoverImage();
                        houseDigest.CoverImageUrlS = houseDigestRes.GetCoverImageUrlS();
                        houseDigest.CoverImageUrlM = houseDigestRes.GetCoverImageUrlM();

                        ArrayList<Object> houseTags = ((IApiResults.IResultList) houseDigestRes).GetList();
                        houseDigest.houseTags = new ArrayList<>();
                        for (Object houseTagObj : houseTags) {
                            IApiResults.IHouseTag tag = (IApiResults.IHouseTag) houseTagObj;
                            ClassDefine.HouseTag houseTag = new ClassDefine.HouseTag(tag.GetTagId(), tag.GetName());
                            houseDigest.houseTags.add(houseTag);
                        }

                        mHouseList.add(houseDigest);
                    }

                    mTaskFinished.onTaskFinished(mHouseList, mTotalCount);
                }
            }

            if (command == CMD_GET_BRIEF_PUBLIC_HOUSE_INFO) {
                IApiResults.IHouseDigest houseDigestRes = (IApiResults.IHouseDigest) iResult;
                ClassDefine.HouseDigest houseDigest = new ClassDefine.HouseDigest();
                houseDigest.houseId = houseDigestRes.GetHouseId();
                houseDigest.property = houseDigestRes.GetProperty();
                houseDigest.addr = houseDigestRes.GetPropertyAddr();
                houseDigest.Bedrooms = houseDigestRes.GetBedrooms();
                houseDigest.Livingrooms = houseDigestRes.GetLivingrooms();
                houseDigest.Bathrooms = houseDigestRes.GetBathrooms();
                double acreage = (double)houseDigestRes.GetAcreage() / 100.0;
                houseDigest.Acreage = Double.valueOf(String.format("%.02f", acreage));
                double rental = (double)houseDigestRes.GetRental() / 100.0;
                houseDigest.Rental = Double.valueOf(String.format("%.02f", rental));
                double pricing = (double)houseDigestRes.GetPricing() / 100.0;
                houseDigest.Pricing = Double.valueOf(String.format("%.02f", pricing));
                houseDigest.CoverImage = houseDigestRes.GetCoverImage();

                IApiResults.IResultList lst = (IApiResults.IResultList) iResult;
                if (lst.GetFetchedNumber() > 0) {
                    ArrayList<Object> array = lst.GetList();
                    houseDigest.houseTags = new ArrayList<>();
                    for (Object obj : array) {
                        IApiResults.IHouseTag tag = (IApiResults.IHouseTag)obj;
                        ClassDefine.HouseTag houseTag = new ClassDefine.HouseTag(tag.GetTagId(), tag.GetName());
                        houseDigest.houseTags.add(houseTag);
                    }
                }

                mHouseList.add(houseDigest);
            }

            mResultGot = true;
        }
    };

    CommunicationInterface.CIProgressListener mProgreessListener = new CommunicationInterface.CIProgressListener() {
        @Override
        public void onProgressChanged(int i, String s, HashMap<String, String> hashMap) {
        }
    };

    boolean waitResult(int nTimeoutMs) {
        if (nTimeoutMs < 100)
            return mResultGot;

        int wait_count = 0;
        while (wait_count < nTimeoutMs / 100) {
            if (mResultGot)
                return true;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            wait_count++;
        }

        return false;
    }
}
