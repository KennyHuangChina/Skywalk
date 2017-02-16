package com.kjs.skywalk.communicationlibrary;

/**
 * Created by kenny on 2017/2/15.
 */

public class NativeCall {
    private native byte[] GeneratePassword(String pass, String salt, String rand, int ver);
}
