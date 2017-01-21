package com.kjs.skywalk.communicationlibrary;

import java.util.HashMap;

/**
 * Created by Jackie on 2017/1/20.
 */

public class CommunicationInterface {
    public interface CICommandListener {
        void onCommandFinished(final String command, final String returnCode, final String description, final HashMap<String, String> map);
    }

    public interface CIProgressListener {
        void onProgressChanged(final String command, final String percent, HashMap<String, String> map);
    }
}
