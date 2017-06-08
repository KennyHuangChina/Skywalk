package com.kjs.skywalk.app_android;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by sailor.zhou on 2017/2/2.
 */

public class ClassDefine {
    static class PicList {
        String mDesc;
        String mPath;
        int    mDrawable;
        boolean mIsChecked;

        public PicList(String desc, String path, int drawable, boolean isChecked) {
            mDesc = desc;
            mPath = path;
            mDrawable = drawable;
            mIsChecked = isChecked;
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
    }

    public static class HouseDigest {
        public int houseId;
        public String property;
        public String addr;
        public int Bedrooms;
        public int Livingrooms;
        public int Bathrooms;
        public int Acreage;
        public int Rental;
        public int Pricing;
        public int CoverImage;
        public ArrayList<HouseTag> houseTags;
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

        public void printContent() {

        }
    }
}
