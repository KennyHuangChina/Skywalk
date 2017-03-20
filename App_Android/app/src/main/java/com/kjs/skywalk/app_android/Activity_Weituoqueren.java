package com.kjs.skywalk.app_android;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * Created by admin on 2017/3/16.
 */

public class Activity_Weituoqueren extends Activity {
    private WebView mWebView = null;
    private ProgressBar mProgress = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tijiaoweituo);

        mProgress = (ProgressBar)findViewById(R.id.progressBar);
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
                    mProgress.setVisibility(View.GONE);
                }
                else
                {
                    mProgress.setVisibility(View.VISIBLE);
                }
            }
        });

        //mWebView.loadUrl("http://www.baidu.com/");
        mWebView.loadUrl("file:////sdcard/test.html");
    }

    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.textViewCommit:
            {
                finish();
            }
            break;
        }
    }
}
