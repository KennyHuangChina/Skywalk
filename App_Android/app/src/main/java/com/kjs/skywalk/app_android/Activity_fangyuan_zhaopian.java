package com.kjs.skywalk.app_android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Picture;
import android.os.Handler;
import android.os.Message;
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

import static com.kjs.skywalk.app_android.Server.ImageDelete.DELETE_RESULT_INTERRUPT;
import static com.kjs.skywalk.app_android.Server.ImageDelete.DELETE_RESULT_OK;
import static com.kjs.skywalk.app_android.Server.ImageUpload.UPLOAD_RESULT_INTERRUPT;
import static com.kjs.skywalk.app_android.Server.ImageUpload.UPLOAD_RESULT_OK;
import static com.kjs.skywalk.app_android.Server.ImageUpload.UPLOAD_TYPE_DIANQI;
import static com.kjs.skywalk.app_android.Server.ImageUpload.UPLOAD_TYPE_FANGJIAN;
import static com.kjs.skywalk.app_android.Server.ImageUpload.UPLOAD_TYPE_HUXING;
import static com.kjs.skywalk.app_android.Server.ImageUpload.UPLOAD_TYPE_JIAJU;
import static com.kjs.skywalk.communicationlibrary.IApiArgs.*;

public class Activity_fangyuan_zhaopian extends SKBaseActivity implements ImageUpload.UploadFinished,
        ImageDelete.DeleteFinished
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

    ArrayList<Integer> mDeleteList = new ArrayList<>();

    private ArrayList<String> mNewPictureList = new ArrayList<>();

    boolean mIsPicSelectMode = false;
    int mPhotoPickerHostId;

    private ArrayList<ClassDefine.PictureInfo> mPictureListHuXing = new ArrayList<>();
    private ArrayList<ClassDefine.PictureInfo> mPictureListFangJianJieGou = new ArrayList<>();
    private ArrayList<ClassDefine.PictureInfo> mPictureListJiaJuYongPin = new ArrayList<>();
    private ArrayList<ClassDefine.PictureInfo> mPictureListDianQi = new ArrayList<>();

    private ImageFetchForHouse imageFetchHuXing = null;
    private ImageFetchForHouse imageFetchFangJianJieGou = null;
    private ImageFetchForHouse imageFetchJiaJuYongPin = null;
    private ImageFetchForHouse imageFetchDianQi = null;

    private final int   MSG_UPLOAD_ALL_DONE             = 0,
                        MSG_UPLOAD_FINISHED_WITH_ERROR  = 1,
                        MSG_GET_PICTURES_DONE           = 0x100,
                        MSG_DELETE_ALL_DONE             = 0x200,
                        MSG_DELETE_FINISHED_WITH_ERROR  = 0x201,
                        MSG_GET_HOUSE_INFO_DONE         = 0x300;

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

        mTvUpload.setVisibility(View.VISIBLE);
        mTvDelete.setVisibility(View.GONE);

        mHuXingPicLst = new ArrayList<> ();
        mFangJianJieGouPicLst = new ArrayList<> ();
        mJiaJuYongPinPicLst = new ArrayList<> ();
        mDianQiPicLst = new ArrayList<> ();

        ((TextView) findViewById(R.id.tv_name_picgroup1)).setText("户型图");
        mTvStatus1 = (TextView) findViewById(R.id.tv_status_picgroup1);
        mVPHuXing = (ViewPager) findViewById(R.id.vp_huxing);
        fillPicGroupInfo(mTvStatus1, mVPHuXing, mHuXingPicLst);

        ((TextView) findViewById(R.id.tv_name_picgroup2)).setText("房间结构");
        mTvStatus2 = (TextView) findViewById(R.id.tv_status_picgroup2);
        mVpFangJianJieGou = (ViewPager) findViewById(R.id.vp_fangjianjiegou);
        fillPicGroupInfo(mTvStatus2, mVpFangJianJieGou, mFangJianJieGouPicLst);

        ((TextView) findViewById(R.id.tv_name_picgroup3)).setText("家居用品");
        mTvStatus3 = (TextView) findViewById(R.id.tv_status_picgroup3);
        mVpJiaJuYongPin = (ViewPager) findViewById(R.id.vp_jiajuyongpin);
        fillPicGroupInfo(mTvStatus3, mVpJiaJuYongPin, mJiaJuYongPinPicLst);

        ((TextView) findViewById(R.id.tv_name_picgroup4)).setText("电器");
        mTvStatus4 = (TextView) findViewById(R.id.tv_status_picgroup4);
        mVpDianQi = (ViewPager) findViewById(R.id.vp_dianqi);
        fillPicGroupInfo(mTvStatus4, mVpDianQi, mDianQiPicLst);

        getPictures();
    }

    private void upload() {
        ArrayList<ImageUpload.UploadImageInfo> list = new ArrayList<>();

        for(ClassDefine.PicList pic : mHuXingPicLst) {
            if(pic.mIsLocal) {
                ImageUpload.UploadImageInfo info = new ImageUpload.UploadImageInfo();
                info.type = UPLOAD_TYPE_HUXING;
                info.image = pic.mPath;
                info.houseId = mHouseId;
                list.add(info);
            }
        }

        for(ClassDefine.PicList pic : mFangJianJieGouPicLst) {
            if(pic.mIsLocal) {
                ImageUpload.UploadImageInfo info = new ImageUpload.UploadImageInfo();
                info.type = UPLOAD_TYPE_FANGJIAN;
                info.image = pic.mPath;
                info.houseId = mHouseId;
                list.add(info);
            }
        }

        for(ClassDefine.PicList pic : mJiaJuYongPinPicLst) {
            if(pic.mIsLocal) {
                ImageUpload.UploadImageInfo info = new ImageUpload.UploadImageInfo();
                info.type = UPLOAD_TYPE_JIAJU;
                info.image = pic.mPath;
                info.houseId = mHouseId;
                list.add(info);
            }
        }

        for(ClassDefine.PicList pic : mDianQiPicLst) {
            if(pic.mIsLocal) {
                ImageUpload.UploadImageInfo info = new ImageUpload.UploadImageInfo();
                info.type = UPLOAD_TYPE_DIANQI;
                info.image = pic.mPath;
                info.houseId = mHouseId;
                list.add(info);
            }
        }

        if(list.size() == 0) {
            commonFun.showToast_info(this, mContainer, "没有可以上传的图片");
            return;
        }

        for(ImageUpload.UploadImageInfo info : list) {
            kjsLogUtil.i("upload: " + info.image);
        }

        ImageUpload imageUpload = new ImageUpload(this, this);
        if(imageUpload.upload(list) != 0) {
            commonFun.showToast_info(this, mContainer, "上传失败");
        } else {
            showWaiting(true);
        }
    }

    private void getHuXingPictures() {
        mPictureListHuXing.clear();
        mHuXingPicLst.clear();

        ImageFetchForHouse.HouseFetchFinished listener = new ImageFetchForHouse.HouseFetchFinished() {
            @Override
            public void onHouseImageFetched(ArrayList<ClassDefine.PictureInfo> list) {
                if(list.size() == 0) {
                    kjsLogUtil.i("no picture got for Floor Plan");
                    return;
                }
                mPictureListHuXing = list;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int i = 1;
                        for(ClassDefine.PictureInfo info : mPictureListHuXing) {
                            ClassDefine.PicList pic = new ClassDefine.PicList("户型图" + i, info.smallPicUrl, 0, false, false, info.mId);
                            mHuXingPicLst.add(pic);
                            i ++;
                        }

                        fillPicGroupInfo(mTvStatus1, mVPHuXing, mHuXingPicLst);
                        imageFetchHuXing.unregisterListener();
                    }
                });
            }
        };

        imageFetchHuXing = new ImageFetchForHouse(this);
        imageFetchHuXing.registerListener(listener);
        imageFetchHuXing.fetch(mHouseId, PIC_TYPE_SUB_HOUSE_FLOOR_PLAN, PIC_SIZE_ALL);
    }

    private void getDianQiPictures() {
        mPictureListDianQi.clear();
        mDianQiPicLst.clear();

        ImageFetchForHouse.HouseFetchFinished listener = new ImageFetchForHouse.HouseFetchFinished() {
            @Override
            public void onHouseImageFetched(ArrayList<ClassDefine.PictureInfo> list) {
                if(list.size() == 0) {
                    kjsLogUtil.i("no picture got for Appliance");
                    return;
                }
                mPictureListDianQi = list;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int i = 1;
                        for(ClassDefine.PictureInfo info : mPictureListDianQi) {
                            ClassDefine.PicList pic = new ClassDefine.PicList("电器" + i, info.smallPicUrl, 0, false, false, info.mId);
                            mDianQiPicLst.add(pic);
                            i ++;
                        }

                        fillPicGroupInfo(mTvStatus4, mVpDianQi, mDianQiPicLst);
                        imageFetchDianQi.unregisterListener();
                    }
                });
            }
        };

        imageFetchDianQi = new ImageFetchForHouse(this);
        imageFetchDianQi.registerListener(listener);
        imageFetchDianQi.fetch(mHouseId, PIC_TYPE_SUB_HOUSE_APPLIANCE, PIC_SIZE_ALL);
    }

    private void getJiaJuYongPinPictures() {
        mPictureListJiaJuYongPin.clear();
        mJiaJuYongPinPicLst.clear();

        ImageFetchForHouse.HouseFetchFinished listener = new ImageFetchForHouse.HouseFetchFinished() {
            @Override
            public void onHouseImageFetched(ArrayList<ClassDefine.PictureInfo> list) {
                if(list.size() == 0) {
                    kjsLogUtil.i("no picture got for Furniture");
                    return;
                }
                mPictureListJiaJuYongPin = list;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int i = 1;
                        for(ClassDefine.PictureInfo info : mPictureListJiaJuYongPin) {
                            ClassDefine.PicList pic = new ClassDefine.PicList("家居用品" + i, info.smallPicUrl, 0, false, false, info.mId);
                            mJiaJuYongPinPicLst.add(pic);
                            i ++;
                        }

                        fillPicGroupInfo(mTvStatus3, mVpJiaJuYongPin, mJiaJuYongPinPicLst);
                        imageFetchJiaJuYongPin.unregisterListener();
                    }
                });
            }
        };

        imageFetchJiaJuYongPin = new ImageFetchForHouse(this);
        imageFetchJiaJuYongPin.registerListener(listener);
        imageFetchJiaJuYongPin.fetch(mHouseId, PIC_TYPE_SUB_HOUSE_FURNITURE, PIC_SIZE_ALL);
    }

    private void getFangJianJieGouPictures() {
        mPictureListFangJianJieGou.clear();
        mFangJianJieGouPicLst.clear();

        ImageFetchForHouse.HouseFetchFinished listener = new ImageFetchForHouse.HouseFetchFinished() {
            @Override
            public void onHouseImageFetched(ArrayList<ClassDefine.PictureInfo> list) {
                if(list.size() == 0) {
                    kjsLogUtil.i("no picture got for Real Map");
                    return;
                }
                mPictureListFangJianJieGou = list;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int i = 1;
                        for(ClassDefine.PictureInfo info : mPictureListFangJianJieGou) {
                            ClassDefine.PicList pic = new ClassDefine.PicList("房间结构" + i, info.smallPicUrl, 0, false, false, info.mId);
                            mFangJianJieGouPicLst.add(pic);
                            i ++;
                        }

                        fillPicGroupInfo(mTvStatus2, mVpFangJianJieGou, mFangJianJieGouPicLst);
                        imageFetchFangJianJieGou.unregisterListener();
                    }
                });
            }
        };

        imageFetchFangJianJieGou = new ImageFetchForHouse(this);
        imageFetchFangJianJieGou.registerListener(listener);
        imageFetchFangJianJieGou.fetch(mHouseId, PIC_TYPE_SUB_HOUSE_RealMap, PIC_SIZE_ALL);
    }

    private void getPictures() {
        getHuXingPictures();
        getFangJianJieGouPictures();
        getJiaJuYongPinPictures();
        getDianQiPictures();
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

                                mDeleteList.clear();

                                deleteSelectItem(mHuXingPicLst);
                                deleteSelectItem(mFangJianJieGouPicLst);
                                deleteSelectItem(mJiaJuYongPinPicLst);
                                deleteSelectItem(mDianQiPicLst);

                                mIsPicSelectMode = false;
                                updateViewrPagerSelectMode(mIsPicSelectMode);
                                updateStatus();

                                delete();
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

        TextView selectButton = (TextView)findViewById(R.id.tv_select);
        if(mIsPicSelectMode) {
            selectButton.setText("取消");
        } else {
            selectButton.setText("选择");
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
            fragment.setZhaoPianGroupCallback(mPicGroupCallback, this);
            ArrayList<ClassDefine.PicList> list = new ArrayList();

            if (i * 2 < picLst.size())
                list.add(picLst.get(i * 2));
            if ((i * 2 + 1) < picLst.size())
                list.add(picLst.get(i * 2  + 1));

            fragment.setPicList(list);
            fragLst.add(fragment);
        }
        viewPager.removeAllViews();
        // Kenny: 第二次打开会 crash
        viewPager.setAdapter(new PicFragStatePageAdapter(getSupportFragmentManager(), fragLst));
        viewPager.setCurrentItem(0);
    }

    private void delete() {
        ImageDelete imageDelete = new ImageDelete(this, this);

        if(mDeleteList.size() == 0) {
            commonFun.showToast_info(this, mContainer, "没有选中的图片");
            return;
        }

        if(imageDelete.delete(mDeleteList) != 0) {
            commonFun.showToast_info(this, mContainer, "删除失败");
        } else {
            showWaiting(true);
        }
    }

    private void deleteSelectItem(ArrayList<ClassDefine.PicList> picLst) {
        Iterator iter = picLst.iterator();
        while (iter.hasNext()) {
            ClassDefine.PicList item = (ClassDefine.PicList) iter.next();
            if(item.mIsChecked) {
                mDeleteList.add(item.mId);
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
        if(photos.size() == 0) {
            return;
        }
        mNewPictureList.clear();
        ArrayList<ClassDefine.PicList> picList;
        PicFragStatePageAdapter adapter;

        switch (mPhotoPickerHostId) {
            case R.id.iv_photopicker_picgroup1:
            {
                picList = mHuXingPicLst;
                adapter = ((PicFragStatePageAdapter) mVPHuXing.getAdapter());
                break;
            }
            case R.id.iv_photopicker_picgroup2:
            {
                picList = mFangJianJieGouPicLst;
                adapter = ((PicFragStatePageAdapter) mVpFangJianJieGou.getAdapter());
                break;
            }
            case R.id.iv_photopicker_picgroup3:
            {
                picList = mJiaJuYongPinPicLst;
                adapter = ((PicFragStatePageAdapter) mVpJiaJuYongPin.getAdapter());
                break;
            }
            case R.id.iv_photopicker_picgroup4:
            {
                picList = mDianQiPicLst;
                adapter = ((PicFragStatePageAdapter) mVpDianQi.getAdapter());
                break;
            }
            default:
                return;
        }

        for (String path : photos) {
            ClassDefine.PicList item = new ClassDefine.PicList("新增图", path, 0, false, true, -1);
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

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_UPLOAD_ALL_DONE:
                showWaiting(false);
                getPictures();
                break;
            case MSG_DELETE_ALL_DONE:
                showWaiting(false);
                getPictures();
                break;
            case MSG_GET_PICTURES_DONE:
                break;
            case MSG_GET_HOUSE_INFO_DONE:
                getPictures();
                break;
        }
        }
    };

    private void removePictureFromList(int id) {

    }

    @Override
    public void onDeleteStarted() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mWaitingWindow != null) {
                    String text = "开始删除，请稍候...";
                    mWaitingWindow.updateProgressText(text);
                }
            }
        });
    }

    @Override
    public void onDeleteProgress(final int current, final int total, int id, int result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mWaitingWindow != null) {
                    String text = "正在删除照片 ... " + current + "/" + total;
                    mWaitingWindow.updateProgressText(text);
                }
            }
        });
        if(result == DELETE_RESULT_INTERRUPT) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mWaitingWindow != null) {
                        String text = "删除失败, 请重新删除";
                        mWaitingWindow.updateProgressText(text);
                    }
                }
            });
            mHandler.sendEmptyMessageDelayed(MSG_DELETE_FINISHED_WITH_ERROR, 1000);
        }
        if(result == DELETE_RESULT_OK) {
            removePictureFromList(id);
        }
    }

    @Override
    public void onDeleteEnd() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mWaitingWindow != null) {
                    String text = "删除完成";
                    mWaitingWindow.updateProgressText(text);
                    mHandler.sendEmptyMessageDelayed(MSG_DELETE_ALL_DONE, 1000);
                }
            }
        });
    }

    @Override
    public void onUploadStarted() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mWaitingWindow != null) {
                    String text = "开始上传，请稍候...";
                    mWaitingWindow.updateProgressText(text);
                }
            }
        });
    }

    @Override
    public void onUploadProgress(final int current, final int total, String image, ImageUpload.UploadResult result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mWaitingWindow != null) {
                    String text = "正在上传照片 ... " + current + "/" + total;
                    mWaitingWindow.updateProgressText(text);
                }
            }
        });
        if(result.mResult == UPLOAD_RESULT_INTERRUPT) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mWaitingWindow != null) {
                        String text = "上传失败, 请重新上传";
                        mWaitingWindow.updateProgressText(text);
                    }
                }
            });
            mHandler.sendEmptyMessageDelayed(MSG_UPLOAD_FINISHED_WITH_ERROR, 1000);
        } else if(result.mResult == UPLOAD_RESULT_OK) {
            //上传成功需要记录id和md5到数据库
            String str = String.format("Photo %d ->  id: %d md5:%s", current, result.mId, result.mMD5);
            kjsLogUtil.i(str);
        }
    }

    @Override
    public void onUploadEnd() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mWaitingWindow != null) {
                    String text = "上传完成";
                    mWaitingWindow.updateProgressText(text);
                    mHandler.sendEmptyMessageDelayed(MSG_UPLOAD_ALL_DONE, 1000);
                }
            }
        });
    }
}
