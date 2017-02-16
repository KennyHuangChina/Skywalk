package com.kjs.skywalk.communicationlibrary;

/**
 * Created by kenny on 2017/2/15.
 */

public class NativeCall {
    private static NativeCall mCaller = null;
    static {
        System.loadLibrary("communic");
    }
    public synchronized static NativeCall GetNativeCaller() {
        if(mCaller == null) {
            mCaller = new NativeCall();
        }

        return mCaller;
    }

    public byte[] EncryptPassword(String pass, String salt, String rand, int ver)
    {
        return GeneratePassword(pass, salt, rand, ver);
    }

    private native byte[] GeneratePassword(String pass, String salt, String rand, int ver);
}
