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

    public byte[] EncryptReloginSession(String suer, String rand, String sid, int ver)
    {
        return GenerateReloginSession(suer, rand, sid, ver);
    }

    // How to generate the JNI header file
    // 1. enter \Skywalk\moduleCommunication\android\Communication\communicationlibrary\src\main
    // 2. javah -d jni -classpath C:\Users\kenny\AppData\Local\Android\sdk\platforms\android-24\android.jar;..\..\build\intermediates\classes\debug com.kjs.skywalk.communicationlibrary.NativeCall
    private native byte[] GeneratePassword(String pass, String salt, String rand, int ver);
    private native byte[] GenerateReloginSession(String user, String rand, String sid, int ver);
}
