package com.kjs.skywalk.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import com.kjs.skywalk.app_android.kjsLogUtil;
import com.kjs.skywalk.communicationlibrary.CommandManager;
import com.kjs.skywalk.communicationlibrary.CommunicationError;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface;
import com.kjs.skywalk.communicationlibrary.IApiResults;

import java.util.HashMap;

import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.CMD_GET_SYSTEM_MSG_LST;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class UpdateIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String  BROADACTION_NEW_MESSAGE_COUNT ="com.kjs.skywalk.broadcast.newMsgCount";

    private static final String ACTION_FOO = "com.kjs.skywalk.service.action.MsgUpdate";
    private static final String ACTION_BAZ = "com.kjs.skywalk.service.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.kjs.skywalk.service.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.kjs.skywalk.service.extra.PARAM2";

    private int mMsgCount = 0;

    public UpdateIntentService() {
        super("UpdateIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionMsgUpdate(Context context, String param1, String param2) {
        Intent intent = new Intent(context, UpdateIntentService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, UpdateIntentService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionMsgUpdate(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionMsgUpdate(String param1, String param2) {
        // TODO: Handle action Foo
        while (true) {
            kjsLogUtil.i("service run ===>");
            try {
                int newMsgCount = getMessageCountSync(false, true);
                kjsLogUtil.i("newMsgCount: " + newMsgCount);
                if (newMsgCount != mMsgCount) {
                    Intent intent = new Intent(BROADACTION_NEW_MESSAGE_COUNT);
                    intent.putExtra("new_msg_count", newMsgCount);
                    sendBroadcast(intent);
                    kjsLogUtil.i("sendBroadcast --- newMsgCount: " + newMsgCount);
                }
                mMsgCount = newMsgCount;

                Thread.sleep(60 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }

    boolean mIsCmdFinished = false;
    int mTmpMsgCount = 0;
    private int getMessageCountSync(boolean ido, boolean nmo) {
        mIsCmdFinished = false;
        CommandManager.getCmdMgrInstance(getApplicationContext(), new CommunicationInterface.CICommandListener() {
            @Override
            public void onCommandFinished(int command, IApiResults.ICommon iResult) {
                if (null == iResult) {
                    kjsLogUtil.w("result is null");
                    mIsCmdFinished = true;
                    return;
                }
                kjsLogUtil.i(String.format("[command: %d(%s)] --- %s" , command, CommunicationInterface.CmdID.GetCmdDesc(command), iResult.DebugString()));
                if (CommunicationError.CE_ERROR_NO_ERROR != iResult.GetErrCode()) {
                    kjsLogUtil.e("Command:" + command + " finished with error: " + iResult.GetErrDesc());
                    mIsCmdFinished = true;
                    return;
                }

                if (command == CMD_GET_SYSTEM_MSG_LST) {
                    IApiResults.IResultList resultList = (IApiResults.IResultList) iResult;
                    int nFetch = resultList.GetFetchedNumber();
                    kjsLogUtil.i("nFetch: " + nFetch);
                    if (nFetch == -1) {
                        mTmpMsgCount = resultList.GetTotalNumber();
                        mIsCmdFinished = true;
                        kjsLogUtil.i("mIsCmdFinished: " + mIsCmdFinished);
                    }
                }
            }
        }, mProgreessListener).GetSysMsgList(0, 0 , ido, nmo);

        kjsLogUtil.i("mIsCmdFinished: " + mIsCmdFinished);
        while (!mIsCmdFinished) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        kjsLogUtil.i("mTmpMsgCount: " + mTmpMsgCount);
        return mTmpMsgCount;
    }

    CommunicationInterface.CIProgressListener mProgreessListener = new CommunicationInterface.CIProgressListener() {
        @Override
        public void onProgressChanged(int i, String s, HashMap<String, String> hashMap) {
        }
    };

}
