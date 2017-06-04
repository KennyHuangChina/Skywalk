package com.kjs.skywalk.app_android;


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
        public int propertyId;
        public String buildingNo;
        public int floor;
        public int totalFloor;
        public String roomNo;
        public int area;
        public int bedRooms;
        public int livingRooms;
        public int bathRooms;
        public int decorate;
        public int decorateDescription;
        public int buyYear;
        public int buyMonth;
        public int buyDay;
        public int hasLoan;
        public int unique;
    }
}
