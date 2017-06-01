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

}
