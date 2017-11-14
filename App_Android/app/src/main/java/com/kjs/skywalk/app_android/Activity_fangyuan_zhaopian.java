package com.kjs.skywalk.app_android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kjs.skywalk.app_android.Server.ImageDelete;
import com.kjs.skywalk.app_android.Server.ImageFetchForHouse;
import com.kjs.skywalk.app_android.Server.ImageFetchForUser;
import com.kjs.skywalk.app_android.Server.ImageUpload;
import com.kjs.skywalk.communicationlibrary.CommandManager;
import com.kjs.skywalk.communicationlibrary.CommunicationError;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface;
import com.kjs.skywalk.communicationlibrary.IApiResults;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_HOUSE_PIC_LIST;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_PIC_URL;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.PIC_SIZE_ALL;

public class Activity_fangyuan_zhaopian extends SKBaseActivity implements ImageUpload.UploadFinished,
        ImageDelete.DeleteFinished, ImageFetchForHouse.HouseFetchFinished
{
    private LinearLayout mContainer = null;
    private PopupWindowWaitingUnclickable mWaitingWindow = null;
    ViewPager mVPHuXing;
    ViewPager mVpFangJianJieGou;
    ViewPager mVpJiaJuYongPin;
    ViewPager mVpDianQi;
    TextView mTvStatus1;
    TextView mTvStatus2;
    TextView mTvStatus3;
    TextView mTvStatus4;
    TextView mTvUpload;
    TextView mTvDelete;
    ArrayList<ClassDefine.PicList> mHuXingPicLst;
    ArrayList<ClassDefine.PicList> mFangJianJieGouPicLst;
    ArrayList<ClassDefine.PicList> mJiaJuYongPinPicLst;
    ArrayList<ClassDefine.PicList> mDianQiPicLst;

    private ArrayList<String> mHuXingList = new ArrayList<>();
    private ArrayList<String> mFangJianJieGouList = new ArrayList<>();
    private ArrayList<String> mJiaJuYongPinList = new ArrayList<>();
    private ArrayList<String> mDianQiList = new ArrayList<>();

    boolean mIsPicSelectMode = false;
    int mPhotoPickerHostId;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            List<String> photos = null;
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                onPhotoPickerReturn(photos);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fangyuan_zhaopian);

        mContainer = (LinearLayout)findViewById(R.id.root_container);

        mTvUpload = (TextView) findViewById(R.id.tv_upload);
        mTvDelete = (TextView) findViewById(R.id.tv_delete);

        getHousePictures(0);

        // test pics
        ArrayList<String> photosLst = commonFun.getTestPicList(this);
        int testCount = 0;
        mHuXingPicLst = new ArrayList<> ();
        mFangJianJieGouPicLst = new ArrayList<> ();
        mJiaJuYongPinPicLst = new ArrayList<> ();
        mDianQiPicLst = new ArrayList<> ();

//        for (String photoPath : photosLst) {
//            testCount++;
//            mHuXingPicLst.add(new ClassDefine.PicList("户型图" + testCount, photoPath, 0, false));
//        mFangJianJieGouPicLst.add(new ClassDefine.PicList("房间结构图" + testCount, photoPath, 0, false));
//        mJiaJuYongPinPicLst.add(new ClassDefine.PicList("家居用品图" + testCount, photoPath, 0, false));
//    }

    // 户型图
        ((TextView) findViewById(R.id.tv_name_picgroup1)).setText("户型图");
    mTvStatus1 = (TextView) findViewById(R.id.tv_status_picgroup1);
    mVPHuXing = (ViewPager) findViewById(R.id.vp_huxing);
//        mHuXingPicLst = new ArrayList<> (
//                Arrays.asList(
//                        new ClassDefine.PicList("户型图一", "", R.drawable.huxingtu1, false),
//                        new ClassDefine.PicList("户型图二", "", R.drawable.huxingtu2, false),
//                        new ClassDefine.PicList("户型图三", "", R.drawable.huxingtu2, false),
//                        new ClassDefine.PicList("户型图四", "", R.drawable.huxingtu2, false),
//                        new ClassDefine.PicList("户型图五", "", R.drawable.huxingtu2, false),
//                        new ClassDefine.PicList("户型图六", "", R.drawable.huxingtu2, false)
//             )
//        );

    fillPicGroupInfo(mTvStatus1, mVPHuXing, mHuXingPicLst);

    //        // 房间结构
        ((TextView) findViewById(R.id.tv_name_picgroup2)).setText("房间结构");
        mTvStatus2 = (TextView) findViewById(R.id.tv_status_picgroup2);
        mVpFangJianJieGou = (ViewPager) findViewById(R.id.vp_fangjianjiegou);
//        mFangJianJieGouPicLst = new ArrayList<> (
//                Arrays.asList(
//                        new ClassDefine.PicList("房间结构图一", "", R.drawable.huxingtu1, false),
//                        new ClassDefine.PicList("房间结构图二", "", R.drawable.huxingtu2, false)
//                )
//        );
        fillPicGroupInfo(mTvStatus2, mVpFangJianJieGou, mFangJianJieGouPicLst);

        //        // 家居用品
        ((TextView) findViewById(R.id.tv_name_picgroup3)).setText("家居用品");
        mTvStatus3 = (TextView) findViewById(R.id.tv_status_picgroup3);
        mVpJiaJuYongPin = (ViewPager) findViewById(R.id.vp_jiajuyongpin);
//        mJiaJuYongPinPicLst = new ArrayList<> (
//                Arrays.asList(
//                        new ClassDefine.PicList("家居用品图一", "", R.drawable.huxingtu1, false),
//                        new ClassDefine.PicList("家居用品图二", "", R.drawable.huxingtu2, false)
//                )
//        );
        fillPicGroupInfo(mTvStatus3, mVpJiaJuYongPin, mJiaJuYongPinPicLst);

        //        // 电器
        ((TextView) findViewById(R.id.tv_name_picgroup4)).setText("电器");
        mTvStatus4 = (TextView) findViewById(R.id.tv_status_picgroup4);
        mVpDianQi = (ViewPager) findViewById(R.id.vp_dianqi);
//        mDianQiPicLst = new ArrayList<> ();
//                Arrays.asList(
//                        new ClassDefine.PicList("电器图一", "", R.drawable.huxingtu1, false),
//                        new ClassDefine.PicList("电器图二", "", R.drawable.huxingtu2, false)
//                )
//        );
        fillPicGroupInfo(mTvStatus4, mVpDianQi, mDianQiPicLst);

    }

    private void getHousePictures(final int type) {
        CommandManager.getCmdMgrInstance(this,  new CommunicationInterface.CICommandListener() {

            @Override
            public void onCommandFinished(int command, IApiResults.ICommon iResult) {
                if (null == iResult) {
                    kjsLogUtil.w("result is null");
                    return;
                }
                kjsLogUtil.i(String.format("[command: %d] --- %s" , command, iResult.DebugString()));
                if (CommunicationError.CE_ERROR_NO_ERROR != iResult.GetErrCode()) {
                    kjsLogUtil.e("Command:" + command + " finished with error: " + iResult.GetErrDesc());
                    return;
                }
                // CMD_GET_HOUSE_PIC_LIST,  IApiResults.IResultList(IApiResults.IHousePicInfo)
                if (command == CMD_GET_HOUSE_PIC_LIST) {
                    IApiResults.IResultList resultList = (IApiResults.IResultList) iResult;
                    for (Object item : resultList.GetList()) {
                        IApiResults.IPicInfo picInfo = (IApiResults.IPicInfo) item;
                        getPicUrlById(type, picInfo.GetId());
                    }
                }
            }
        }, this).GetHousePics(mHouseId, type, PIC_SIZE_ALL);
    }

    private void getPicUrlById(int type, final int picId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CommandManager.getCmdMgrInstance(Activity_fangyuan_zhaopian.this,  new CommunicationInterface.CICommandListener() {

                    @Override
                    public void onCommandFinished(int command, IApiResults.ICommon iResult) {
                        if (null == iResult) {
                            kjsLogUtil.w("result is null");
                            return;
                        }
                        kjsLogUtil.i(String.format("[command: %d] --- %s" , command, iResult.DebugString()));
                        if (CommunicationError.CE_ERROR_NO_ERROR != iResult.GetErrCode()) {
                            kjsLogUtil.e("Command:" + command + " finished with error: " + iResult.GetErrDesc());
                            return;
                        }
                        // CMD_GET_PIC_URL,         IApiResults.IPicUrls
                        if (command == CMD_GET_PIC_URL) {
                            IApiResults.IPicUrls picUrls = (IApiResults.IPicUrls) iResult;
                        }
                    }
                }, Activity_fangyuan_zhaopian.this).GetPicUrls(picId, 1);
            }
        });
    }

    private void upload() {
        ImageUpload imageUpload = new ImageUpload(this, this);
        ArrayList<ImageUpload.UploadImageInfo> list = new ArrayList<>();
        for(String path : mHuXingList) {
            ImageUpload.UploadImageInfo info = new ImageUpload.UploadImageInfo();
            info.type = ImageUpload.UPLOAD_TYPE_HUXING;
            info.image = path;
            info.houseId = mHouseId;
            list.add(info);
        }

        for(String path : mFangJianJieGouList) {
            ImageUpload.UploadImageInfo info = new ImageUpload.UploadImageInfo();
            info.type = ImageUpload.UPLOAD_TYPE_FANGJIAN;
            info.image = path;
            info.houseId = mHouseId;
            list.add(info);
        }

        for(String path : mJiaJuYongPinList) {
            ImageUpload.UploadImageInfo info = new ImageUpload.UploadImageInfo();
            info.type = ImageUpload.UPLOAD_TYPE_JIAJU;
            info.image = path;
            info.houseId = mHouseId;
            list.add(info);
        }

        for(String path : mDianQiList) {
            ImageUpload.UploadImageInfo info = new ImageUpload.UploadImageInfo();
            info.type = ImageUpload.UPLOAD_TYPE_DIANQI;
            info.image = path;
            info.houseId = mHouseId;
            list.add(info);
        }

        if(imageUpload.upload(list) != 0) {
            commonFun.showToast_info(this, mContainer, "上传失败");
        } else {
            showWaiting(true);
        }
    }

    private void showWaiting(final boolean show) {
        if (mWaitingWindow == null) {
            mWaitingWindow = new PopupWindowWaitingUnclickable(this, mActScreenWidth, mActScreenHeight);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (show) {
                    mWaitingWindow.show(mContainer);
                } else {
                    mWaitingWindow.hide();
                }
            }
        });
    }

    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
            {
                finish();
            }
            break;
            case R.id.tv_select:
            {
                mIsPicSelectMode = !mIsPicSelectMode;
                if (mIsPicSelectMode) {
                    ((TextView)v).setText("取消");
                    mTvUpload.setVisibility(View.GONE);
                    mTvDelete.setVisibility(View.VISIBLE);
                } else {
                    ((TextView)v).setText("选择");
                    mTvUpload.setVisibility(View.VISIBLE);
                    mTvDelete.setVisibility(View.GONE);
                }
                updateViewrPagerSelectMode(mIsPicSelectMode);
                break;

            }
            case R.id.tv_upload:
            {
                upload();
                break;
            }
            case R.id.tv_delete:
            {
                // show confirm dialog
                new AlertDialog.Builder(this)
                        .setTitle("是否删除选中的照片")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();

                                deleteSelectItem(mHuXingPicLst);
                                fillPicGroupInfo(mTvStatus1, mVPHuXing, mHuXingPicLst);

                                deleteSelectItem(mFangJianJieGouPicLst);
                                fillPicGroupInfo(mTvStatus2, mVpFangJianJieGou, mFangJianJieGouPicLst);

                                deleteSelectItem(mJiaJuYongPinPicLst);
                                fillPicGroupInfo(mTvStatus3, mVpJiaJuYongPin, mJiaJuYongPinPicLst);

                                deleteSelectItem(mDianQiPicLst);
                                fillPicGroupInfo(mTvStatus4, mVpDianQi, mDianQiPicLst);

                                updateViewrPagerSelectMode(mIsPicSelectMode);
                                updateStatus();

                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();

                break;
            }
            default:
                break;
        }
    }

    // refresh view pagers
    private void updateViewrPagerSelectMode(boolean isSelectMode) {
        ((PicFragStatePageAdapter) mVPHuXing.getAdapter()).updateSelectMode(isSelectMode);
        ((PicFragStatePageAdapter) mVpFangJianJieGou.getAdapter()).updateSelectMode(isSelectMode);
        ((PicFragStatePageAdapter) mVpJiaJuYongPin.getAdapter()).updateSelectMode(isSelectMode);
        ((PicFragStatePageAdapter) mVpDianQi.getAdapter()).updateSelectMode(isSelectMode);
        updateStatus();
    }

    private void updateStatus() {
        int total_select_count = 0;
        int select_count = ((PicFragStatePageAdapter)mVPHuXing.getAdapter()).getSelectCount();
        int total = mHuXingPicLst.size();
        if (mIsPicSelectMode) {
            mTvStatus1.setText("(" + select_count + "/" + total + ")");
            total_select_count += select_count;
        } else {
            mTvStatus1.setText("(" + total + ")");
        }

        select_count = ((PicFragStatePageAdapter)mVpFangJianJieGou.getAdapter()).getSelectCount();
        total = mFangJianJieGouPicLst.size();
        if (mIsPicSelectMode) {
            mTvStatus2.setText("(" + select_count + "/" + total + ")");
            total_select_count += select_count;
        } else {
            mTvStatus2.setText("(" + total + ")");
        }

        select_count = ((PicFragStatePageAdapter)mVpJiaJuYongPin.getAdapter()).getSelectCount();
        total = mJiaJuYongPinPicLst.size();
        if (mIsPicSelectMode) {
            mTvStatus3.setText("(" + select_count + "/" + total + ")");
            total_select_count += select_count;
        } else {
            mTvStatus3.setText("(" + total + ")");
        }

        select_count = ((PicFragStatePageAdapter)mVpDianQi.getAdapter()).getSelectCount();
        total = mDianQiPicLst.size();
        if (mIsPicSelectMode) {
            mTvStatus4.setText("(" + select_count + "/" + total + ")");
            total_select_count += select_count;
        } else {
            mTvStatus4.setText("(" + total + ")");
        }

        if (total_select_count > 0) {
            mTvDelete.setEnabled(true);
        } else {
            mTvDelete.setEnabled(false);
        }
    }

    public void onPhotoPickerClicked(View v) {
        switch (v.getId()) {
            case R.id.iv_photopicker_picgroup1:
            case R.id.iv_photopicker_picgroup2:
            case R.id.iv_photopicker_picgroup3:
            case R.id.iv_photopicker_picgroup4:
            {
                startPhotoPickerActivity(v);
            }
            break;

        }
    }

    class PicFragStatePageAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> mLst;
        public PicFragStatePageAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            mLst = list;
        }

        @Override
        public Fragment getItem(int position) {
            return mLst.get(position);
        }

        @Override
        public int getCount() {
            return mLst.size();
        }

        public void updateSelectMode(boolean isSelectMode) {
            for (Fragment fragment : mLst) {
                if (fragment instanceof  fragmentFangYuanZhaoPianGroup) {
                    ((fragmentFangYuanZhaoPianGroup)fragment).updateSelectMode(isSelectMode);
                }
            }
        }

        public int getSelectCount() {
            int count = 0;
            for (Fragment fragment : mLst) {
                if (fragment instanceof  fragmentFangYuanZhaoPianGroup) {
                    count += ((fragmentFangYuanZhaoPianGroup)fragment).getSelectCount();
                }
            }

            return count;
        }

        public void deleteSelectItem() {
            for (Fragment fragment : mLst) {
                if (fragment instanceof  fragmentFangYuanZhaoPianGroup) {
                    ((fragmentFangYuanZhaoPianGroup)fragment).deleteSelectItem();
                }
            }
        }
    }

    private void fillPicGroupInfo(TextView tvStatus, ViewPager viewPager, ArrayList<ClassDefine.PicList> picLst) {
        tvStatus.setText("(" + picLst.size() + ")");
        List<Fragment> fragLst = new ArrayList<Fragment>();
        int lstCnt = picLst.size() % 2 == 0 ? picLst.size() / 2 : picLst.size() / 2 + 1;
        for (int i = 0; i <lstCnt; i++) {
            fragmentFangYuanZhaoPianGroup fragment = fragmentFangYuanZhaoPianGroup.newInstance(viewPager.getId());
            fragment.setZhaoPianGroupCallback(mPicGroupCallback);
            ArrayList<ClassDefine.PicList> list = new ArrayList();

            if (i * 2 < picLst.size())
                list.add(picLst.get(i * 2));
            if ((i * 2 + 1) < picLst.size())
                list.add(picLst.get(i * 2  + 1));

            fragment.setPicList(list);
            fragLst.add(fragment);
        }
        viewPager.setAdapter(new PicFragStatePageAdapter(getSupportFragmentManager(), fragLst));
        viewPager.setCurrentItem(0);
    }

    private void deleteSelectItem(ArrayList<ClassDefine.PicList> picLst) {
        Iterator iter = picLst.iterator();
        while (iter.hasNext()) {
            ClassDefine.PicList item = (ClassDefine.PicList) iter.next();
            if(item.mIsChecked) {
                iter.remove();
            }
        }
    }

    fragmentFangYuanZhaoPianGroup.ZhaoPianGroupCallback mPicGroupCallback = new fragmentFangYuanZhaoPianGroup.ZhaoPianGroupCallback() {

        @Override
        public void onPicSelectChanged() {
            updateStatus();
        }

        @Override
        public void onPicClicked(int hostId, int pos, String picPath) {
//            commonFun.showToast_info(Activity_fangyuan_zhaopian.this, mVPHuXing, "hostId: " + hostId);
            ViewPager viewPager = (ViewPager) findViewById(hostId);
            if (viewPager == null)
                return;

            PicFragStatePageAdapter adapter = ((PicFragStatePageAdapter)viewPager.getAdapter());
            if (adapter == null)
                return;

            ArrayList<String> images = new ArrayList<>();
            int nFragCount = adapter.getCount();
            int curItem = 0;
            for (int i = 0; i < nFragCount; i++) {
                fragmentFangYuanZhaoPianGroup fragment = (fragmentFangYuanZhaoPianGroup) adapter.getItem(i);
                int tmpIndex = i * 2;
                for (ClassDefine.PicList picItem : fragment.mPicList) {
                    String path = picItem.mPath;
                    if (!path.isEmpty()) {
                        if (path.equalsIgnoreCase(picPath))
                            curItem = tmpIndex;
                        tmpIndex += 1;
                        images.add(path);
                    }
                }
            }

            PhotoPreview.builder()
                .setPhotos(images)
                .setCurrentItem(curItem)
                .start(Activity_fangyuan_zhaopian.this);
        }
    };

    private void startPhotoPickerActivity(View host_view) {
        mPhotoPickerHostId = host_view.getId();
        PhotoPicker.builder()
                .start(Activity_fangyuan_zhaopian.this);
    }

    private void mergeList(List<String> source, ArrayList<String> dest) {
        for(String sourcePic : source) {
            boolean exists = false;
            for(String destPic : dest) {
                if(destPic.equals(sourcePic)) {
                    exists = true;
                    break;
                }
            }
            if(!exists) {
                dest.add(sourcePic);
            }
        }
    }

    private void onPhotoPickerReturn(List<String> photos) {
        ArrayList<ClassDefine.PicList> picList;
        PicFragStatePageAdapter adapter;
        ArrayList<String> list;
        switch (mPhotoPickerHostId) {
            case R.id.iv_photopicker_picgroup1:
            {
                picList = mHuXingPicLst;
                adapter = ((PicFragStatePageAdapter) mVPHuXing.getAdapter());
                list = mHuXingList;
                break;
            }
            case R.id.iv_photopicker_picgroup2:
            {
                picList = mFangJianJieGouPicLst;
                adapter = ((PicFragStatePageAdapter) mVpFangJianJieGou.getAdapter());
                list = mFangJianJieGouList;
                break;
            }
            case R.id.iv_photopicker_picgroup3:
            {
                picList = mJiaJuYongPinPicLst;
                adapter = ((PicFragStatePageAdapter) mVpJiaJuYongPin.getAdapter());
                list = mJiaJuYongPinList;
                break;
            }
            case R.id.iv_photopicker_picgroup4:
            {
                picList = mDianQiPicLst;
                adapter = ((PicFragStatePageAdapter) mVpDianQi.getAdapter());
                list =  mDianQiList;
                break;
            }
            default:
                return;
        }

        mergeList(photos, list);

        for (String path : photos) {
            ClassDefine.PicList item = new ClassDefine.PicList("新增图", path, 0, false);
            picList.add(item);
        }

        fillPicGroupInfo(mTvStatus1, mVPHuXing, mHuXingPicLst);
        mVPHuXing.setCurrentItem(mVPHuXing.getAdapter().getCount());

        fillPicGroupInfo(mTvStatus2, mVpFangJianJieGou, mFangJianJieGouPicLst);
        mVpFangJianJieGou.setCurrentItem(mVpFangJianJieGou.getAdapter().getCount());

        fillPicGroupInfo(mTvStatus3, mVpJiaJuYongPin, mJiaJuYongPinPicLst);
        mVpJiaJuYongPin.setCurrentItem(mVpJiaJuYongPin.getAdapter().getCount());

        fillPicGroupInfo(mTvStatus4, mVpDianQi, mDianQiPicLst);
        mVpDianQi.setCurrentItem(mVpDianQi.getAdapter().getCount());

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onHouseImageFetched(ArrayList<ClassDefine.PictureInfo> list) {

    }

    @Override
    public void onDeleteStarted() {

    }

    @Override
    public void onDeleteProgress(int current, int total, int id, int result) {

    }

    @Override
    public void onDeleteEnd() {

    }

    @Override
    public void onUploadStarted() {

    }

    @Override
    public void onUploadProgress(int current, int total, String image, ImageUpload.UploadResult result) {

    }

    @Override
    public void onUploadEnd() {

    }
}
