package com.kjs.skywalk.app_android;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ArrayRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ConfigurationHelper;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kjs.skywalk.app_android.Server.ImageDelete;
import com.kjs.skywalk.app_android.Server.ImageFetchForHouse;
import com.kjs.skywalk.app_android.Server.ImageUpload;
import com.kjs.skywalk.communicationlibrary.CommandManager;
import com.kjs.skywalk.communicationlibrary.CommunicationError;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface;
import com.kjs.skywalk.communicationlibrary.IApiResults;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import me.iwf.photopicker.PhotoPicker;

import static com.kjs.skywalk.app_android.Server.ImageDelete.DELETE_RESULT_INTERRUPT;
import static com.kjs.skywalk.app_android.Server.ImageDelete.DELETE_RESULT_OK;
import static com.kjs.skywalk.app_android.Server.ImageUpload.UPLOAD_RESULT_INTERRUPT;
import static com.kjs.skywalk.app_android.Server.ImageUpload.UPLOAD_RESULT_OK;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_HOUSE_INFO;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_HOUSE_PIC_LIST;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.PIC_SIZE_ALL;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.PIC_TYPE_MAJOR_House;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.PIC_TYPE_MAJOR_User;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.PIC_TYPE_SUB_HOUSE_OwnershipCert;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.PIC_TYPE_SUB_USER_IDCard;

public class Activity_fangyuan_renzheng_customer extends SKBaseActivity implements ImageUpload.UploadFinished,
        ImageDelete.DeleteFinished, ImageFetchForHouse.HouseFetchFinished
{
    private int mPhotoPickerHostId;
    private ArrayList<String> mCertList = new ArrayList<>();
    private ArrayList<String> mIdList = new ArrayList<>();
    private PopupWindowWaitingUnclickable mWaitingWindow = null;
    private RelativeLayout mContainer = null;
    private int mLandlordId = 0;
    private boolean isSelectMode = false;

    private ImageView mCertImage1 = null;
    private ImageView mCertImage2 = null;
    private ImageView mIdCardImage1 = null;
    private ImageView mIdCardImage2 = null;

    private CheckBox mBoxCert1 = null;
    private CheckBox mBoxCert2 = null;
    private CheckBox mBoxIdCard1 = null;
    private CheckBox mBoxIdCard2 = null;

    private boolean mGetIDPicturesOk = false;
    private boolean mGetPOCPicturesOk = false;

    private ArrayList<ClassDefine.PictureInfo> mPictureList = new ArrayList<>();

    private final int MSG_UPLOAD_ALL_DONE = 0;
    private final int MSG_UPLOAD_FINISHED_WITH_ERROR = 1;
    private final int MSG_GET_PICTURES_DONE = 0x100;
    private final int MSG_DELETE_ALL_DONE = 0x200;
    private final int MSG_DELETE_FINISHED_WITH_ERROR = 0x201;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__fangyuan_renzheng_customer);

        mCertImage1 = (ImageView)findViewById(R.id.imageZhengjian1);
        mCertImage2 = (ImageView)findViewById(R.id.imageZhengjian2);
        mBoxCert1 = (CheckBox)findViewById(R.id.cb_pic_checkflag_hid1);
        mBoxCert2 = (CheckBox)findViewById(R.id.cb_pic_checkflag_hid2);

        mIdCardImage1 = (ImageView)findViewById(R.id.imageIdCard1);
        mIdCardImage2 = (ImageView)findViewById(R.id.imageIdCard2);
        mBoxIdCard1 = (CheckBox)findViewById(R.id.cb_pic_checkflag_id1);
        mBoxIdCard2 = (CheckBox)findViewById(R.id.cb_pic_checkflag_id2);


        mContainer = (RelativeLayout)findViewById(R.id.activity_container);

        getHouseInfo();

        getPictures();
    }

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

    public void onClickResponse(View v) {
        switch (v.getId()) {
            case R.id.textViewBack: {
                finish();
                break;
            }
            case R.id.tv_upload_pic: {
                upload();
                break;
            }
            case R.id.tv_delete_pic: {
                delete();
                break;
            }
            case R.id.textViewSelect: {
                isSelectMode = !isSelectMode;

                processButtonSelect();
                break;
            }
            case R.id.rl_pic_checkflag_hid1: {
                CheckBox box = (CheckBox)findViewById(R.id.cb_pic_checkflag_hid1);
                box.setChecked(!box.isChecked());
                break;
            }
            case R.id.rl_pic_checkflag_hid2: {
                CheckBox box = (CheckBox)findViewById(R.id.cb_pic_checkflag_hid2);
                box.setChecked(!box.isChecked());
                break;
            }
            case R.id.rl_pic_checkflag_id1: {
                CheckBox box = (CheckBox)findViewById(R.id.cb_pic_checkflag_id1);
                box.setChecked(!box.isChecked());
                break;
            }
            case R.id.rl_pic_checkflag_id2: {
                CheckBox box = (CheckBox)findViewById(R.id.cb_pic_checkflag_id2);
                box.setChecked(!box.isChecked());
                break;
            }
        }
    }

    private void cleanCheckBoxSelection() {
        CheckBox box;
        box = (CheckBox)findViewById(R.id.cb_pic_checkflag_hid1);
        box.setChecked(false);
        box = (CheckBox)findViewById(R.id.cb_pic_checkflag_hid2);
        box.setChecked(false);
        box = (CheckBox)findViewById(R.id.cb_pic_checkflag_id1);
        box.setChecked(false);
        box = (CheckBox)findViewById(R.id.cb_pic_checkflag_id2);
        box.setChecked(false);
    }

    private void processButtonSelect() {
        TextView button = (TextView)findViewById(R.id.textViewSelect);
        if(isSelectMode) {
            button.setText("取消");
        } else {
            button.setText("选择");
        }

        RelativeLayout hid1 = (RelativeLayout)findViewById(R.id.rl_pic_checkflag_hid1);
        RelativeLayout hid2 = (RelativeLayout)findViewById(R.id.rl_pic_checkflag_hid2);
        RelativeLayout id1 = (RelativeLayout)findViewById(R.id.rl_pic_checkflag_id1);
        RelativeLayout id2 = (RelativeLayout)findViewById(R.id.rl_pic_checkflag_id2);
        TextView upload = (TextView)findViewById(R.id.tv_upload_pic);
        TextView delete = (TextView)findViewById(R.id.tv_delete_pic);
        if(isSelectMode) {
            hid1.setVisibility(View.VISIBLE);
            hid2.setVisibility(View.VISIBLE);
            id1.setVisibility(View.VISIBLE);
            id2.setVisibility(View.VISIBLE);

            upload.setVisibility(View.GONE);
            delete.setVisibility(View.VISIBLE);

            cleanCheckBoxSelection();
        } else {
            hid1.setVisibility(View.INVISIBLE);
            hid2.setVisibility(View.INVISIBLE);
            id1.setVisibility(View.INVISIBLE);
            id2.setVisibility(View.INVISIBLE);

            upload.setVisibility(View.VISIBLE);
            delete.setVisibility(View.GONE);
        }
    }

    private void delete() {
        ImageDelete imageDelete = new ImageDelete(this, this);
        ArrayList<Integer> list = new ArrayList<>();
        if(mBoxCert1.isChecked()) {
            if(mBoxCert1.getTag() != null) {
                list.add((int)mBoxCert1.getTag());
            }
        }

        if(mBoxCert2.isChecked()) {
            if(mBoxCert2.getTag() != null) {
                list.add((int)mBoxCert2.getTag());
            }
        }

        if(mBoxIdCard1.isChecked()) {
            if(mBoxIdCard1.getTag() != null) {
                list.add((int)mBoxIdCard1.getTag());
            }
        }

        if(mBoxIdCard2.isChecked()) {
            if(mBoxIdCard2.getTag() != null) {
                list.add((int)mBoxIdCard2.getTag());
            }
        }

        if(list.size() == 0) {
            commonFun.showToast_info(this, mContainer, "没有选中的图片");
            return;
        }

        if(imageDelete.delete(list) != 0) {
            commonFun.showToast_info(this, mContainer, "删除失败");
        } else {
            showWaiting(true);
        }
    }

    private void upload() {
        ImageUpload imageUpload = new ImageUpload(this, this);
        ArrayList<ImageUpload.UploadImageInfo> list = new ArrayList<>();
        for(String path : mCertList) {
            ImageUpload.UploadImageInfo info = new ImageUpload.UploadImageInfo();
            info.type = ImageUpload.UPLOAD_TYPE_OWNER_CERT;
            info.image = path;
            info.houseId = mHouseId;
            list.add(info);
        }

        for(String path : mIdList) {
            ImageUpload.UploadImageInfo info = new ImageUpload.UploadImageInfo();
            info.type = ImageUpload.UPLOAD_TYPE_IDCARD;
            info.image = path;
            info.userId = mLandlordId;
            list.add(info);
        }

        if(imageUpload.upload(list) != 0) {
            commonFun.showToast_info(this, mContainer, "上传失败");
        } else {
            showWaiting(true);
        }
    }

    public void onPhotoPickerClicked(View v) {
        switch (v.getId()) {
            case R.id.iv_photopicker_hpc: {
                mCertList.clear();
                break;
            }
            case R.id.iv_photopicker_idcard: {
                mIdList.clear();
                break;
            }
            default:
                return;
        }

        startPhotoPickerActivity(v);
    }

    private void startPhotoPickerActivity(View host_view) {
        mPhotoPickerHostId = host_view.getId();
        PhotoPicker.builder().setPhotoCount(2).start(Activity_fangyuan_renzheng_customer.this);
    }

    private void showPictures() {
        int indexPOC = 0;
        int indexID = 0;
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.no_picture);
        mCertImage1.setImageDrawable(drawable);
        mCertImage1.setTag(null);
        mCertImage2.setImageDrawable(drawable);
        mCertImage2.setTag(null);
        mIdCardImage1.setImageDrawable(drawable);
        mIdCardImage1.setTag(null);
        mIdCardImage2.setImageDrawable(drawable);
        mIdCardImage2.setTag(null);

        for(ClassDefine.PictureInfo info : mPictureList) {
            if(info.mType == PIC_TYPE_SUB_HOUSE_OwnershipCert) {
                if (indexPOC == 0) {
                    commonFun.displayImageByURL(this, info.smallPicUrl, mCertImage1);
                    mCertImage1.setTag(info.mId);
                    mBoxCert1.setTag(info.mId);
                    indexPOC++;
                } else if (indexPOC == 1) {
                    commonFun.displayImageByURL(this, info.smallPicUrl, mCertImage2);
                    mCertImage2.setTag(info.mId);
                    mBoxCert2.setTag(info.mId);
                    indexPOC = 0x9999;
                }
            } else if(info.mType == PIC_TYPE_SUB_USER_IDCard) {
                if(indexID == 0) {
                    commonFun.displayImageByURL(this, info.smallPicUrl, mIdCardImage1);
                    mIdCardImage1.setTag(info.mId);
                    mBoxIdCard1.setTag(info.mId);
                    indexID ++;
                }
                if(indexID == 1) {
                    commonFun.displayImageByURL(this, info.smallPicUrl, mIdCardImage2);
                    mIdCardImage1.setTag(info.mId);
                    mBoxIdCard2.setTag(info.mId);
                    indexID = 0x9999;
                }
            }
        }
    }

    private void onPhotoPickerReturn(List<String> photos) {
        switch (mPhotoPickerHostId) {
            case R.id.iv_photopicker_hpc:
            {
                mCertImage1.setVisibility(View.INVISIBLE);
                mCertImage2.setVisibility(View.INVISIBLE);

                int index = 0;
                for (String path : photos) {
                    if (index == 0) {
                        mCertImage1.setImageBitmap(commonFun.getScaleBitmapFromLocal(path, 320, 240));
                        mCertImage1.setVisibility(View.VISIBLE);
                    }
                    if (index == 1) {
                        mCertImage2.setImageBitmap(commonFun.getScaleBitmapFromLocal(path, 320, 240));
                        mCertImage2.setVisibility(View.VISIBLE);
                    }
                    index++;

                    mCertList.add(path);
                }

                break;
            }

            case R.id.iv_photopicker_idcard:
            {
                mIdCardImage1.setVisibility(View.INVISIBLE);
                mIdCardImage2.setVisibility(View.INVISIBLE);

                int index = 0;
                for (String path : photos) {
                    if (index == 0) {
                        mIdCardImage1.setImageBitmap(commonFun.getScaleBitmapFromLocal(path, 320, 240));
                        mIdCardImage1.setVisibility(View.VISIBLE);
                    }
                    if (index == 1) {
                        mIdCardImage2.setImageBitmap(commonFun.getScaleBitmapFromLocal(path, 320, 240));
                        mIdCardImage2.setVisibility(View.VISIBLE);
                    }
                    index++;

                    mIdList.add(path);
                }

                break;
            }
        }
    }

    private void getPictures() {
        mPictureList.clear();

        ImageFetchForHouse fetchForHouse = new ImageFetchForHouse(this, this);
        fetchForHouse.fetch(mHouseId, PIC_TYPE_SUB_HOUSE_OwnershipCert, PIC_SIZE_ALL);
    }

    private int getHouseInfo() {
        CommunicationInterface.CICommandListener listener = new CommunicationInterface.CICommandListener() {
            @Override
            public void onCommandFinished(int command, IApiResults.ICommon iResult) {
                if (null == iResult) {
                    kjsLogUtil.w("result is null");
                    return;
                }

                if (CommunicationError.CE_ERROR_NO_ERROR != iResult.GetErrCode()) {
                    commonFun.showToast_info(getApplicationContext(), mContainer, "获取房屋信息失败:" + iResult.GetErrDesc());
                    return;
                }

                if (command == CMD_GET_HOUSE_INFO) {
                    IApiResults.IGetHouseInfo info = (IApiResults.IGetHouseInfo) iResult;
                    mLandlordId = info.Landlord();
                }
            }
        };

        CommandManager CmdMgr = CommandManager.getCmdMgrInstance(this, listener, this);
        int result = CmdMgr.GetHouseInfo(mHouseId, true);
        if(result != CommunicationError.CE_ERROR_NO_ERROR) {
            commonFun.showToast_info(getApplicationContext(), mContainer, "获取房屋信息失败");
            return -1;
        }

        return 0;
    }

    private void removePictureFromList(int id) {
        for(ClassDefine.PictureInfo info : mPictureList) {
            if(info.mId == id) {
                mPictureList.remove(info);
                break;
            }
        }
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
                    isSelectMode = false;
                    processButtonSelect();
                    showWaiting(false);
                    getPictures();
                    break;
                case MSG_GET_PICTURES_DONE:
                    showPictures();
                    break;
            }
        }
    };

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
    public void onDeleteProgress(final int current, final int total, final int id, int result) {
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

    @Override
    public void onHouseImageFetched(ArrayList<ClassDefine.PictureInfo> list) {
        for(ClassDefine.PictureInfo info : list) {
            mPictureList.add(info);
        }
        mHandler.sendEmptyMessageDelayed(MSG_GET_PICTURES_DONE, 500);
    }
}
