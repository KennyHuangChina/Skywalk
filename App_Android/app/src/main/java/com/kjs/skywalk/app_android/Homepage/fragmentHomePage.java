package com.kjs.skywalk.app_android.Homepage;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kjs.skywalk.app_android.Apartment.Activity_ApartmentDetail;
import com.kjs.skywalk.app_android.ClassDefine;
import com.kjs.skywalk.app_android.R;
import com.kjs.skywalk.app_android.commonFun;
import com.kjs.skywalk.app_android.kjsLogUtil;
import com.kjs.skywalk.communicationlibrary.CommandManager;
import com.kjs.skywalk.communicationlibrary.CommunicationError;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface;
import com.kjs.skywalk.communicationlibrary.IApiResults;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static android.content.ContentValues.TAG;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_BRIEF_PUBLIC_HOUSE_INFO;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_HOUSE_INFO;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_HOUSE_LIST;

/**
 * Created by sailor.zhou on 2017/1/11.
 */

public class fragmentHomePage extends Fragment {
    homepage_apartment_listitem_adapter mAdapterApartmentItem;
    public fragmentHomePage() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);
        ListView lvContent = (ListView) view.findViewById(R.id.lv_content);
        lvContent.setFocusable(false);

        mAdapterApartmentItem = new homepage_apartment_listitem_adapter(getActivity());
        lvContent.setAdapter(mAdapterApartmentItem);
        mAdapterApartmentItem.updateList(loadTestData());
        mAdapterApartmentItem.setApartmentListCallback(new homepage_apartment_listitem_adapter.ApartmentListCallback() {
            @Override
            public void onItemClicked(ClassDefine.HouseDigest houseDigest) {
                kjsLogUtil.i("clicked: " + houseDigest.property);

                Intent intent = new Intent(getActivity().getApplicationContext(), Activity_ApartmentDetail.class);
                Bundle bundle = new Bundle();
                bundle.putString("property", houseDigest.property);
                bundle.putString("addr", houseDigest.addr);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        // get recommend house info
        (new GetRecommendHouseListTask()).execute();

        return view;
    }

    private ArrayList<ClassDefine.HouseDigest> loadTestData() {
        ArrayList<ClassDefine.HouseDigest> houseDigestsList = new ArrayList<>();
        ClassDefine.HouseDigest digest = new ClassDefine.HouseDigest();
        digest.property = "证大大拇指广场";
        digest.addr = "姑苏白杨湾街道";
        digest.Acreage = 120;
        digest.houseTags = new ArrayList<>();
        digest.houseTags.add(new ClassDefine.HouseTag(3, "满二唯一"));
        digest.houseTags.add(new ClassDefine.HouseTag(2, "靠近地铁"));
        houseDigestsList.add(digest);

        ClassDefine.HouseDigest digest1 = new ClassDefine.HouseDigest();
        digest1.property = "证大大拇指广场1";
        digest1.addr = "姑苏白杨湾街道1";
        digest1.Acreage = 130;
        digest1.houseTags = new ArrayList<>();
        digest1.houseTags.add(new ClassDefine.HouseTag(1, "满三唯一"));
        digest1.houseTags.add(new ClassDefine.HouseTag(5, "不靠近地铁"));
        houseDigestsList.add(digest1);

        return houseDigestsList;
    }

    private void updateHouseList(final ArrayList<ClassDefine.HouseDigest> list) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapterApartmentItem.updateList(list);
            }
        });
    }


    public class GetRecommendHouseListTask extends AsyncTask<Void, Void, Boolean> {

        private boolean mResultGot = false;
        private int[] mHouseIds = null;
        private ArrayList<ClassDefine.HouseDigest> mHouseList = new ArrayList<>();

        @Override
        protected Boolean doInBackground(Void... voids) {
            // get house ids
            CommandManager CmdMgr = new CommandManager(getActivity(), mCmdListener, mProgreessListener);
            int result = CmdMgr.GetHouseList(1, 0, 4);
            mResultGot = false;
            if(!waitResult(1000)) {
                // timeout
                return null;
            }
            if (mHouseIds == null)
                return null;

            // get house info
            for (int houseId : mHouseIds) {
                CmdMgr.GetBriefPublicHouseInfo(houseId);
                mResultGot = false;
                if(!waitResult(1000)) {
                    // timeout
                    continue;
                }
            }

            updateHouseList(mHouseList);

            return true;
        }

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

                if (command == CMD_GET_HOUSE_LIST) {
                    IApiResults.IResultList resultList = (IApiResults.IResultList) iResult;
                    int nFetch = resultList.GetFetchedNumber();
                    if (nFetch > 0) {
                        mHouseIds = new int[nFetch];
                        int i = 0;
                        ArrayList<Object> houseIDs = resultList.GetList();
                        for (Object houseID : houseIDs) {
                            mHouseIds[i] = (int) houseID;
                            i++;
                        }
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
                    houseDigest.Acreage = houseDigestRes.GetAcreage();
                    houseDigest.Rental = houseDigestRes.GetRental();
                    houseDigest.Pricing = houseDigestRes.GetPricing();
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

    }

}
