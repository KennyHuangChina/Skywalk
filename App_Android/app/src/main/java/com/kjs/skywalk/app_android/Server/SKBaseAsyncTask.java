package com.kjs.skywalk.app_android.Server;

import android.os.AsyncTask;

import com.kjs.skywalk.communicationlibrary.CommunicationInterface;

import java.util.HashMap;

/**
 * Created by admin on 2017/8/2.
 */

public class SKBaseAsyncTask extends AsyncTask<Integer, Void, Integer> {

    protected boolean mResultGot = false;

    @Override
    protected Integer doInBackground(Integer... params) {
        return null;
    }

    boolean waitResult(int nTimeoutMs) {
        if (nTimeoutMs < 100)
            return mResultGot;

        int wait_count = 0;
        while (wait_count < nTimeoutMs / 100) {
            if (mResultGot)
                return true;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            wait_count++;
        }

        return false;
    }

    CommunicationInterface.CIProgressListener mProgressListener = new CommunicationInterface.CIProgressListener() {
        @Override
        public void onProgressChanged(int i, String s, HashMap<String, String> hashMap) {
        }
    };
}
