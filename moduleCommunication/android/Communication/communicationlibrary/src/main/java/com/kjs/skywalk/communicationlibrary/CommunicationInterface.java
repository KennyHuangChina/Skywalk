package com.kjs.skywalk.communicationlibrary;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Jackie on 2017/1/20.
 */

public class CommunicationInterface {
    public interface CICommandListener {
        void onCommandFinished(final String command, final IApiResults.ICommon res);
    }

    public interface CIProgressListener {
        void onProgressChanged(final String command, final String percent, HashMap<String, String> map);
    }
}
