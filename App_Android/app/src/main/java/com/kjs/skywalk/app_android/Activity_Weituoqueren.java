package com.kjs.skywalk.app_android;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by admin on 2017/3/16.
 */

public class Activity_Weituoqueren extends Activity {
    private final String TAG = "Weiguoqueren";
    private WebView mWebView = null;
    private ProgressBar mProgress = null;
    private RelativeLayout mProgressContainer = null;
    //private String mURL = "http://www.baidu.com/";
    private String mURL = "file:////sdcard/test.html";
    private String mErrorPage = "file:////sdcard/error.html";
    private final int MSG_HIDE_PROGRESS_BAR = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tijiaoweituo);

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

    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.textViewCommit:
            {
                finish();
            }
            break;
            case R.id.clickReloadView:
            {
                TextView reload = (TextView)findViewById(R.id.clickReloadView);
                reload.setVisibility(View.INVISIBLE);
                mWebView.loadUrl(mURL);
            }
        }
    }

    protected void onResume() {
        super.onResume();
        mWebView.removeAllViews();
        mWebView.loadUrl(mURL);
    }

    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_HIDE_PROGRESS_BAR:
                    mProgressContainer.setVisibility(View.INVISIBLE);
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
