package com.kjs.skywalk.app_android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kjs.skywalk.communicationlibrary.CommandManager;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface;
import com.kjs.skywalk.communicationlibrary.IApiResults;

import java.util.HashMap;

import static com.kjs.skywalk.communicationlibrary.CommunicationError.CE_ERROR_NO_ERROR;
import com.kjs.skywalk.app_android.ClassDefine.IntentExtraKeyValue;

/**
 * Created by admin on 2017/3/16.
 */

public class Activity_Weituoqueren extends SKBaseActivity implements CommunicationInterface.CIProgressListener{
    private final String TAG = "Weiguoqueren";
    private WebView mWebView = null;
    private ProgressBar mProgress = null;
    private RelativeLayout mProgressContainer = null;
    //private String mURL = "http://www.baidu.com/";
    private String mURL = "file:////sdcard/test.html";
    private String mErrorPage = "file:////sdcard/error.html";
    private final int MSG_HIDE_PROGRESS_BAR = 0;
    private final int MSG_HOUSE_INFO_COMMIT_DONE = 1;
    private final int MSG_HOUSE_PRICE_COMMIT_DONE = 2;
    private final int MSG_HOUSE_INFO_COMMIT_DONE_WITH_ERROR = 3;
    private int mHouseId = 0;
    private boolean mHouseInfoCommitted = false;
    private boolean mPriceInfoCommitted = false;
    private int mErrorCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tijiaoweituo);
        TextView titleText = (TextView)findViewById(R.id.textViewActivityTitle);
        titleText.setText("业主房屋委托确认书");
        ImageView closeButton = (ImageView)findViewById(R.id.imageViewActivityClose);
        closeButton.setVisibility(View.INVISIBLE);

        mProgress = (ProgressBar)findViewById(R.id.progressBar);
        mProgressContainer = (RelativeLayout)findViewById(R.id.progressContainer);
        mWebView = (WebView)findViewById(R.id.webView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean 	shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return false;
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                mProgress.setProgress(progress);
                if(progress == 100) {
                    myHandler.sendEmptyMessageDelayed(MSG_HIDE_PROGRESS_BAR, 500);
                }
                else
                {
                    mProgressContainer.setVisibility(View.VISIBLE);
                }
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.e(TAG, description);
                mWebView.removeAllViews();
                //mWebView.loadUrl(mErrorPage);
                TextView reload = (TextView)findViewById(R.id.clickReloadView);
                reload.setVisibility(View.VISIBLE);
            }

//            public void onReceivedError(WebView viewWeb, WebResourceRequest request, WebResourceError error) {
//            }
        });
    }

    CommunicationInterface.CICommandListener mListener = new CommunicationInterface.CICommandListener() {
        @Override
        public void onCommandFinished(int i, IApiResults.ICommon iCommon) {
            if(i == CommunicationInterface.CmdID.CMD_COMMIT_HOUSE_BY_OWNER) {
                if(iCommon.GetErrCode() == CE_ERROR_NO_ERROR) {
                    mHouseId = ((IApiResults.IAddRes)iCommon).GetId();
                    myHandler.sendEmptyMessageDelayed(MSG_HOUSE_INFO_COMMIT_DONE, 0);
                    Log.i(TAG, "House Info Committed: " + mHouseId);
                } else {
                    Log.i(TAG, "Error Code: " + iCommon.GetErrCode() + " " + iCommon.GetErrDesc()) ;
                    //commonFun.showToast_info(getApplicationContext(), mWebView, "提交失败");
                    hideWaiting();

                    //0x451: duplicate
                    mErrorCode = iCommon.GetErrCode();
                    if(iCommon.GetErrCode() == 0x451) {
                        myHandler.sendEmptyMessageDelayed(MSG_HOUSE_INFO_COMMIT_DONE_WITH_ERROR, 0);
                    } else {
                        Activity_Weituoqueren.super.onCommandFinished(i, iCommon);
                        return;
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            } else if(i == CommunicationInterface.CmdID.CMD_SET_HOUSE_PRICE) {
                if(iCommon.GetErrCode() == CE_ERROR_NO_ERROR) {
                    myHandler.sendEmptyMessageDelayed(MSG_HOUSE_PRICE_COMMIT_DONE, 0);
                } else {
                    Log.i(TAG, iCommon.GetErrDesc()) ;
                    commonFun.showToast_info(getApplicationContext(), mWebView, "提交失败");
                    hideWaiting();
                }
            }
        }
    };

    private void commitPriceInfo() {
        CommandManager manager = CommandManager.getCmdMgrInstance(this, mListener, this);
        if(manager.SetHousePrice(mHouseId, ClassDefine.HouseInfoForCommit.rental, ClassDefine.HouseInfoForCommit.minRental,
                ClassDefine.HouseInfoForCommit.includePropertyFee == 0 ? false : true,
                ClassDefine.HouseInfoForCommit.price, ClassDefine.HouseInfoForCommit.minPrice) != CE_ERROR_NO_ERROR) {
            hideWaiting();
            commonFun.showToast_info(getApplicationContext(), mWebView, "提交失败");
        }
    }

    private void commitHouseInfo() {
        CommandManager manager = CommandManager.getCmdMgrInstance(this, mListener, this);
        boolean forSale = ClassDefine.HouseInfoForCommit.forSale();
        boolean forRent = ClassDefine.HouseInfoForCommit.forRental();

        String date = ClassDefine.HouseInfoForCommit.dateToString();

        CommunicationInterface.HouseInfo house = new CommunicationInterface.HouseInfo(
                0,
                ClassDefine.HouseInfoForCommit.propertyId,
                ClassDefine.HouseInfoForCommit.buildingNo,
                ClassDefine.HouseInfoForCommit.roomNo,
                ClassDefine.HouseInfoForCommit.totalFloor,
                ClassDefine.HouseInfoForCommit.floor,
                ClassDefine.HouseInfoForCommit.livingRooms,
                ClassDefine.HouseInfoForCommit.bedRooms,
                ClassDefine.HouseInfoForCommit.bathRooms,
                ClassDefine.HouseInfoForCommit.area,
                forSale, forRent,
                ClassDefine.HouseInfoForCommit.decorate,
                date
        );

        int agentId = Integer.valueOf(ClassDefine.HouseInfoForCommit.agentId);
        if(manager.CommitHouseByOwner(house, agentId) == CE_ERROR_NO_ERROR) {
            showWaiting(mWebView);
        } else {
            commonFun.showToast_info(getApplicationContext(), mWebView, "提交失败");
        }
    }

    public void onClickResponse(View v) {
        switch (v.getId()) {
            case R.id.textViewCommit:
            {
                CheckBox box = (CheckBox)findViewById(R.id.checkbox);
                if(!box.isChecked()) {
                    commonFun.showToast_info(this, box, "请仔细阅读委托确认书，理解并同意其条款");
                    return;
                }

                commitHouseInfo();
            }
            break;
            case R.id.clickReloadView:
            {
                TextView reload = (TextView)findViewById(R.id.clickReloadView);
                reload.setVisibility(View.INVISIBLE);
                mWebView.loadUrl(mURL);
                break;
            }
            case R.id.imageViewActivityBack: {
                finish();
                break;
            }
            case R.id.imageViewActivityClose: {
                Intent intent =new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                finish();
                break;
            }
        }
    }

    protected void onResume() {
        super.onResume();
        mWebView.removeAllViews();
        mWebView.loadUrl(mURL);
    }

    private void showErrorFinishedActivity() {
        Log.i(TAG, "提交失败");

        hideWaiting();
        Intent intent = new Intent(Activity_Weituoqueren.this, Activity_Zushouweituo_Finish_Error.class);
        intent.putExtra(IntentExtraKeyValue.KEY_HOUSE_ID, mHouseId);
        String location = ClassDefine.HouseInfoForCommit.getHouseLocation();
        intent.putExtra(IntentExtraKeyValue.KEY_HOUSE_LOCATION, location);
        intent.putExtra(IntentExtraKeyValue.KEY_ERROR_CODE, mErrorCode);
        startActivity(intent);
    }

    private void showFinishedActivity() {
        if(!mPriceInfoCommitted || !mHouseInfoCommitted) {
            return;
        }

        Log.i(TAG, "提交成功");

        hideWaiting();
        Intent intent = new Intent(Activity_Weituoqueren.this, Activity_Zushouweituo_Finish.class);
        intent.putExtra(IntentExtraKeyValue.KEY_HOUSE_ID, mHouseId);
        String location = ClassDefine.HouseInfoForCommit.getHouseLocation();
        intent.putExtra(IntentExtraKeyValue.KEY_HOUSE_LOCATION, location);
        startActivity(intent);
    }

    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_HIDE_PROGRESS_BAR:
                    mProgressContainer.setVisibility(View.INVISIBLE);
                    break;
                case MSG_HOUSE_INFO_COMMIT_DONE:
                    mHouseInfoCommitted = true;
                    commitPriceInfo();
                    break;
                case MSG_HOUSE_INFO_COMMIT_DONE_WITH_ERROR:
                    showErrorFinishedActivity();
                    break;
                case MSG_HOUSE_PRICE_COMMIT_DONE:
                    mPriceInfoCommitted = true;
                    showFinishedActivity();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onProgressChanged(int i, String s, HashMap<String, String> hashMap) {

    }
}
