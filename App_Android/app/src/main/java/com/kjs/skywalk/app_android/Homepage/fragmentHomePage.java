package com.kjs.skywalk.app_android.Homepage;

import android.app.Activity;
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
import com.kjs.skywalk.app_android.Server.GetHouseListTask;
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
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_HOUSE_DIGEST_LIST;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_HOUSE_INFO;

/**
 * Created by sailor.zhou on 2017/1/11.
 */

public class fragmentHomePage extends Fragment {
    homepage_apartment_listitem_adapter mAdapterRecommend;
    homepage_apartment_listitem_adapter mAdapterDeducted;
    homepage_apartment_listitem_adapter mAdapterNew;
    public fragmentHomePage() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);

        // 今日推荐
        ListView lvRecommend = (ListView) view.findViewById(R.id.lv_recommend);
        lvRecommend.setFocusable(false);
        mAdapterRecommend = new homepage_apartment_listitem_adapter(getActivity(), "今日推荐");
        lvRecommend.setAdapter(mAdapterRecommend);
//        mAdapterRecommend.updateList(loadTestData());
        mAdapterRecommend.setApartmentListCallback(mApartListCallback);
        // get recommend house info
        new GetHouseListTask(getActivity(), new GetHouseListTask.TaskFinished() {
            @Override
            public void onTaskFinished(ArrayList<ClassDefine.HouseDigest> houseList, int totalCount) {
                updateHouseList(houseList, 1, totalCount);
            }
        }).execute(GetHouseListTask.TYPE_RECOMMAND, 0, 2);

        // 降价房源
        ListView lvDeducted = (ListView) view.findViewById(R.id.lv_deducted);
        lvDeducted.setFocusable(false);
        mAdapterDeducted = new homepage_apartment_listitem_adapter(getActivity(), "降价房源");
        lvDeducted.setAdapter(mAdapterDeducted);
        mAdapterDeducted.setApartmentListCallback(mApartListCallback);
        // get deducted house info
        new GetHouseListTask(getActivity(), new GetHouseListTask.TaskFinished() {
            @Override
            public void onTaskFinished(ArrayList<ClassDefine.HouseDigest> houseList, int totalCount) {
                updateHouseList(houseList, 2, totalCount);
            }
        }).execute(GetHouseListTask.TYPE_DEDUCTED, 0, 2);

        // 最新房源
        ListView lvNew = (ListView) view.findViewById(R.id.lv_new);
        lvNew.setFocusable(false);
        mAdapterNew = new homepage_apartment_listitem_adapter(getActivity(), "最新房源");
        lvNew.setAdapter(mAdapterNew);
        mAdapterNew.setApartmentListCallback(mApartListCallback);
        // get new house info
        new GetHouseListTask(getActivity(), new GetHouseListTask.TaskFinished() {
            @Override
            public void onTaskFinished(ArrayList<ClassDefine.HouseDigest> houseList, int totalCount) {
                updateHouseList(houseList, 3, totalCount);
            }
        }).execute(GetHouseListTask.TYPE_NEW, 0, 2);

        return view;
    }

    homepage_apartment_listitem_adapter.ApartmentListCallback mApartListCallback = new homepage_apartment_listitem_adapter.ApartmentListCallback() {
        @Override
        public void onItemClicked(ClassDefine.HouseDigest houseDigest) {
            kjsLogUtil.i("clicked: " + houseDigest.property);
            commonFun.startActivityWithHouseId(getActivity(), Activity_ApartmentDetail.class, houseDigest.houseId);
        }
    };

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

    private void updateHouseList(final ArrayList<ClassDefine.HouseDigest> list, final int type, final int totalCount) {
        Activity activity = getActivity();
        if(activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (type == 1) {
                        mAdapterRecommend.updateList(list, totalCount);
                    } else if (type == 2) {
                        mAdapterDeducted.updateList(list, totalCount);
                    } else if (type == 3) {
                        mAdapterNew.updateList(list, totalCount);
                    }
                }
            });
        }
    }

}
