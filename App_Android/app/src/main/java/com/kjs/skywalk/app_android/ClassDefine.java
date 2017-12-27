package com.kjs.skywalk.app_android;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.kjs.skywalk.communicationlibrary.IApiResults;
import com.kjs.skywalk.control.kjsNumberPicker;

import java.util.ArrayList;
import java.util.Date;

import static com.kjs.skywalk.communicationlibrary.IApiArgs.*;

/**
 * Created by sailor.zhou on 2017/2/2.
 */

public class ClassDefine {
    static class PicList {
        String mDesc;
        String mPath;
        int    mDrawable;
        boolean mIsChecked;
        boolean mIsLocal;
        int mId;

        public PicList(String desc, String path, int drawable, boolean isChecked, boolean isLocal, int id) {
            mId = id;
            mDesc = desc;
            mPath = path;
            mDrawable = drawable;
            mIsChecked = isChecked;
            mIsLocal = isLocal;
        }
    }

    static class PicGroup {
        String mName;
        ArrayList<PicList> mList;

        public PicGroup(String name, ArrayList<PicList> list) {
            mName = name;
            mList = list;
        }
    }

    static class Property {
        public String mName;
        public String mAddress;
    }

    static class Agent {
        public String mName;
        public String mSex;
        public String mIDCard;
        public String mProfessional;
        public String mAttitude;
        public String mYears;
        public String mID;
        public String mPortrait;
    }

    public static class HouseTag {
        public int tagId;
        public String tagName;

        public HouseTag(int id, String name) {
            tagId = id;
            tagName = name;
        }
    }

    public static class HouseDigest {
        public int houseId;
        public String property;
        public String addr;
        public int Bedrooms;
        public int Livingrooms;
        public int Bathrooms;
        public double Acreage;
        public double Rental;
        public double Pricing;
        public int CoverImage;
        public boolean includePropertyFee;
        public String CoverImageUrlS;
        public String CoverImageUrlM;
        public ArrayList<HouseTag> houseTags;
    }

    public static class HouseInfo {
        public static String propertyName = "";
        public static int propertyId = -1;
        public static String buildingNo = "";
        public static int floor = 0;
        public static int totalFloor = 0;
        public static String roomNo = "";
        public static int area = 0;
        public static int bedRooms = 0;
        public static int livingRooms = 0;
        public static int bathRooms = 0;
        public static int decorate = 0;
        public static String decorateDescription = "";
        public static int buyYear = 0;
        public static int buyMonth = 0;
        public static int buyDay = 0;
        public static int hasLoan = 0;
        public static int unique = 0;

        public static int rental = 0;
        public static int minRental = 0;
        public static int includePropertyFee = 0;
        public static int propertyFee = 0;
        public static int price = 0;
        public static int minPrice = 0;

        public static int autoAgent = 1;
        public static String agentId = "0";

        public static int service = -1;

        public static int landlordId = -1;
        public static Date submitTime = null;

        public static boolean forSale() {
            return price > 0 ? true : false;
        }

        public static boolean forRental() {
            return rental > 0 ? true : false;
        }

        public static String dateToString() {
            String month = String.format("%02d", ClassDefine.HouseInfoForCommit.buyMonth);
            String day = String.format("%02d", ClassDefine.HouseInfoForCommit.buyDay);
            String date = "" + ClassDefine.HouseInfoForCommit.buyYear + "-" + month + "-" + day;

            return date;
        }

        public static String getHouseLocation() {
            String location = propertyName + buildingNo + "栋" + roomNo + "室";

            return location;
        }

        public void printContent() {

        }
    }

    public static class HouseInfoForCommit {
        public static String propertyName = "";
        public static int propertyId = -1;
        public static String buildingNo = "";
        public static int floor = 0;
        public static int totalFloor = 0;
        public static String roomNo = "";
        public static int area = 0;
        public static int bedRooms = 0;
        public static int livingRooms = 0;
        public static int bathRooms = 0;
        public static int decorate = 0;
        public static String decorateDescription = "";
        public static int buyYear = 0;
        public static int buyMonth = 0;
        public static int buyDay = 0;
        public static int hasLoan = 0;
        public static int unique = 0;

        public static int rental = 0;
        public static int minRental = 0;
        public static int includePropertyFee = 0;
        public static int propertyFee = 0;
        public static int price = 0;
        public static int minPrice = 0;

        public static int autoAgent = 1;
        public static String agentId = "0";

        public static int service = -1;

        public static boolean forSale() {
            return price > 0 ? true : false;
        }

        public static boolean forRental() {
            return rental > 0 ? true : false;
        }

        public static String dateToString() {
            String month = String.format("%02d", ClassDefine.HouseInfoForCommit.buyMonth);
            String day = String.format("%02d", ClassDefine.HouseInfoForCommit.buyDay);
            String date = "" + ClassDefine.HouseInfoForCommit.buyYear + "-" + month + "-" + day;

            return date;
        }

        public static String getHouseLocation() {
            String location = propertyName + buildingNo + "栋" + roomNo + "室";

            return location;
        }

        public void printContent() {

        }
    }

    public static class IntentExtraKeyValue {
        public static String KEY_HOUSE_ID       = "house_id";
        public static String KEY_HOUSE_LOCATION = "house_location";
        public static String KEY_PROPERTY_NAME  = "property_name";
        public static String KEY_PROPERTY_ID    = "property_id";
        public static String KEY_BUILDING_NO    = "building_no";
        public static String KEY_ROOM_NO        = "room_no";
        public static String KEY_USER_ID        = "user_id";
        public static String KEY_USER_NAME      = "user_name";
        public static String KEY_USER_PHONE     = "phone_no";
        public static String KEY_LOGIN_RESULT   = "login_result";
        public static String KEY_ERROR_CODE     = "error_code";
        public static String KEY_REFID          = "ref_id";
    }

    public static class ActivityResultValue {
        public static int RESULT_VALUE_LOGIN = 50;
    }

    public static class TextItem {
        public int mTextViewWidth;
        public TextView mView;
    }

    public static class ServerError {
        public static final int SERVER_NO_ERROR = 0;
        public static final int SERVER_CONNECTION_ERROR = 0x80001000;
        public static final int SERVER_NEED_LOGIN = 0x80002000;

        public static int getErrorType(int errorCode) {
            switch (errorCode) {
                case 1104:
                case 1106:
                    return SERVER_NEED_LOGIN;
            }

            return SERVER_NO_ERROR;
        }
    }

    public static class NetworkErrorDialog {
        private Context mContext = null;
        private static AlertDialog mDialog = null;
        public NetworkErrorDialog(Context context) {
            mContext = context;
            mDialog = new AlertDialog.Builder(context).create();
        }

        public static void showDialog(boolean show) {
            if(mDialog.isShowing()) {
                return;
            }
            mDialog.show();
            mDialog.setContentView(R.layout.dialog_network_error);

            mDialog.findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                }
            });
        }
    }

    public static class ListConvert {
        public static ArrayList<ClassDefine.HouseDigest> IHouseDigestListToHouseDigestList(ArrayList<Object> houseList) {
            ArrayList<ClassDefine.HouseDigest> list = new ArrayList<>();
            for (Object houseDigestObj : houseList) {
                ClassDefine.HouseDigest houseDigest = IHouseDigestToHouseDigest((IApiResults.IHouseDigest) houseDigestObj);
//                IApiResults.IHouseDigest houseDigestRes = (IApiResults.IHouseDigest) houseDigestObj;
//                ClassDefine.HouseDigest houseDigest = new ClassDefine.HouseDigest();
//                houseDigest.houseId = houseDigestRes.GetHouseId();
//                houseDigest.property = houseDigestRes.GetProperty();
//                houseDigest.addr = houseDigestRes.GetPropertyAddr();
//                houseDigest.Bedrooms = houseDigestRes.GetBedrooms();
//                houseDigest.Livingrooms = houseDigestRes.GetLivingrooms();
//                houseDigest.Bathrooms = houseDigestRes.GetBathrooms();
//                double acreage = (double) houseDigestRes.GetAcreage() / 100.0;
//                houseDigest.Acreage = Double.valueOf(String.format("%.02f", acreage));
//                double rental = (double) houseDigestRes.GetRental() / 100.0;
//                houseDigest.Rental = Double.valueOf(String.format("%.02f", rental));
//                double pricing = (double) houseDigestRes.GetPricing() / 100.0;
//                houseDigest.Pricing = Double.valueOf(String.format("%.02f", pricing));
//                houseDigest.CoverImage = houseDigestRes.GetCoverImage();
//                houseDigest.CoverImageUrlS = houseDigestRes.GetCoverImageUrlS();
//                houseDigest.CoverImageUrlM = houseDigestRes.GetCoverImageUrlM();
//                houseDigest.includePropertyFee = houseDigestRes.IsRentalIncPropFee();
//
//                ArrayList<Object> houseTags = ((IApiResults.IResultList) houseDigestRes).GetList();
//                houseDigest.houseTags = new ArrayList<>();
//                for (Object houseTagObj : houseTags) {
//                    IApiResults.IHouseTag tag = (IApiResults.IHouseTag) houseTagObj;
//                    ClassDefine.HouseTag houseTag = new ClassDefine.HouseTag(tag.GetTagId(), tag.GetName());
//                    houseDigest.houseTags.add(houseTag);
//                }

                list.add(houseDigest);
            }
            return list;
        }

        public static ClassDefine.HouseDigest IHouseDigestToHouseDigest(IApiResults.IHouseDigest apiHouseDigest) {
            ClassDefine.HouseDigest houseDigest = new ClassDefine.HouseDigest();
            houseDigest.houseId = apiHouseDigest.GetHouseId();
            houseDigest.property = apiHouseDigest.GetProperty();
            houseDigest.addr = apiHouseDigest.GetPropertyAddr();
            houseDigest.Bedrooms = apiHouseDigest.GetBedrooms();
            houseDigest.Livingrooms = apiHouseDigest.GetLivingrooms();
            houseDigest.Bathrooms = apiHouseDigest.GetBathrooms();
            double acreage = (double) apiHouseDigest.GetAcreage() / 100.0;
            houseDigest.Acreage = Double.valueOf(String.format("%.02f", acreage));
            double rental = (double) apiHouseDigest.GetRental() / 100.0;
            houseDigest.Rental = Double.valueOf(String.format("%.02f", rental));
            double pricing = (double) apiHouseDigest.GetPricing() / 100.0;
            houseDigest.Pricing = Double.valueOf(String.format("%.02f", pricing));
            houseDigest.CoverImage = apiHouseDigest.GetCoverImage();
            houseDigest.CoverImageUrlS = apiHouseDigest.GetCoverImageUrlS();
            houseDigest.CoverImageUrlM = apiHouseDigest.GetCoverImageUrlM();
            houseDigest.includePropertyFee = apiHouseDigest.IsRentalIncPropFee();

            ArrayList<Object> houseTags = ((IApiResults.IResultList) apiHouseDigest).GetList();
            houseDigest.houseTags = new ArrayList<>();
            for (Object houseTagObj : houseTags) {
                IApiResults.IHouseTag tag = (IApiResults.IHouseTag) houseTagObj;
                ClassDefine.HouseTag houseTag = new ClassDefine.HouseTag(tag.GetTagId(), tag.GetName());
                houseDigest.houseTags.add(houseTag);
            }

            return houseDigest;
        }
    }

    public static class HouseTypeSelector {
        private Context mContext = null;
        private AlertDialog mDialog = null;
        private int mRooms = 0;
        private int mBaths = 0;
        private int mLounges = 0;
        public int mRoomIndex = 0;
        public int mBathIndex = 0;
        public int mLoungeIndex = 0;
        public boolean mDirty = false;

        private static final String[] arrShi = {"1室", "2室", "3室", "4室", "5室"};
        private static final String[] arrTing = {"1厅", "2厅", "3厅", "4厅", "5厅"};
        private static final String[] arrWei = {"1卫", "2卫", "3卫", "4卫", "5卫"};

        public HouseTypeSelector(Context context) {
            mContext= context;
        }

        public void setHouseType(int rooms, int lounges, int bathRooms) {
            mRooms = rooms;
            mBaths = bathRooms;
            mLounges = lounges;
            mRoomIndex = rooms - 1;
            mBathIndex = bathRooms - 1;
            mLoungeIndex = lounges - 1;
        }

        public void show(DialogInterface.OnDismissListener listener) {
            if(((Activity)mContext).isFinishing()) {
                return;
            }
            if (mDialog == null) {
                mDialog = new AlertDialog.Builder(mContext).create();
            }
            mDialog.setOnDismissListener(listener);
            mDialog.show();
            mDialog.setContentView(R.layout.dialog_fangyuanxinxi_fangxing);

            kjsNumberPicker npShi = (kjsNumberPicker)mDialog.findViewById(R.id.np_unit_shi);
            kjsNumberPicker npTing = (kjsNumberPicker)mDialog.findViewById(R.id.np_unit_ting);
            kjsNumberPicker npWei = (kjsNumberPicker)mDialog.findViewById(R.id.np_unit_wei);

            npShi.setDisplayedValues(arrShi);
            npShi.setMinValue(0);
            npShi.setMaxValue(arrShi.length - 1);
            npShi.setDividerColor();

            npTing.setDisplayedValues(arrTing);
            npTing.setMinValue(0);
            npTing.setMaxValue(arrTing.length - 1);
            npTing.setDividerColor();

            npWei.setDisplayedValues(arrWei);
            npWei.setMinValue(0);
            npWei.setMaxValue(arrWei.length - 1);
            npWei.setDividerColor();

            npWei.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    mBathIndex = newVal;
                }
            });

            npTing.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    mLoungeIndex = newVal;
                }
            });

            npShi.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    mRoomIndex = newVal;
                }
            });

            TextView tvBack = (TextView) mDialog.findViewById(R.id.tv_back);
            tvBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDirty = false;
                    mDialog.dismiss();
                }
            });

            TextView tvConfirm = (TextView) mDialog.findViewById(R.id.tv_confirm);
            tvConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDirty = true;
                    mDialog.dismiss();
                }
            });
        }
    }

    public static class ConfirmDialog {
        private Context mContext = null;
        private static AlertDialog mDialog = null;
        private static String mContent = "是否确认";
        private static DialogInterface.OnDismissListener mDismissListener = null;
        private static boolean mConfirmed = false;
        public ConfirmDialog(Context context) {
            mContext = context;
            mDialog = new AlertDialog.Builder(context).create();
        }

        public void setConfirmString(String text) {
            mContent = text;
        }

        public void setDismissListener(DialogInterface.OnDismissListener listener) {
            mDismissListener = listener;
        }

        public boolean getConfirmed() {
            return mConfirmed;
        }

        public static void showDialog(boolean show) {
            if(mDialog.isShowing()) {
                return;
            }
            mConfirmed = false;

            mDialog.show();
            mDialog.setContentView(R.layout.dialog_confirm);
            if(mDismissListener != null) {
                mDialog.setOnDismissListener(mDismissListener);
            }

            TextView contentView = (TextView)mDialog.findViewById(R.id.textViewContent);
            contentView.setText(mContent);

            TextView viewOk = (TextView) mDialog.findViewById(R.id.textViewOk);
            viewOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mConfirmed = true;
                    mDialog.dismiss();
                }
            });

            TextView viewCancel = (TextView)mDialog.findViewById(R.id.textViewCancel);
            viewCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mConfirmed = false;
                    mDialog.dismiss();
                }
            });
        }
    }

    public static class PictureInfo {
        public int mId = 0;
        public String largePicUrl = "";
        public String middlePicUrl = "";
        public String smallPicUrl = "";
        public int mType = -1;

        public void print() {
            kjsLogUtil.i("ID: " + mId);
            kjsLogUtil.i("Type: " + mType);
            kjsLogUtil.i("Large Pic: " + largePicUrl);
            kjsLogUtil.i("Middle Pic: " + middlePicUrl);
            kjsLogUtil.i("Small Pic: " + smallPicUrl);
        }
    }

    public static class MessageInfo {
        public int mMsgId;
        public int mHouseId;
        public int mRefId;
        public int mMsgType;
        public String mCreate_time;
        public String mRead_time;
        public String mBody;
        public String mProperty;
        public String mBuilding_no;
        public String mHouse_no;

        public MessageInfo(int msgId, int houseId, int refId, int msgType, String create_time, String read_time, String body, String property, String building_no, String house_no) {
            mMsgId = msgId;
            mHouseId = houseId;
            mMsgType = msgType;
            mRefId = refId;
            mCreate_time = create_time;
            mRead_time = read_time;
            mBody = body;
            mProperty = property;
            mBuilding_no = building_no;
            mHouse_no = house_no;
        }
    }
}

