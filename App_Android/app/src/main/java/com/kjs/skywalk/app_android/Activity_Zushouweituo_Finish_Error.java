package com.kjs.skywalk.app_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by admin on 2017/3/22.
 */

public class Activity_Zushouweituo_Finish_Error extends SKBaseActivity {

    private int mErrorCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__zushouweituo__finish_error);
        TextView titleText = (TextView)findViewById(R.id.textViewActivityTitle);
        titleText.setText("租售委托-完成");
        ImageView closeButton = (ImageView)findViewById(R.id.imageViewActivityClose);
        closeButton.setVisibility(View.INVISIBLE);

        mErrorCode = getIntent().getIntExtra(ClassDefine.IntentExtraKeyValue.KEY_ERROR_CODE, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();

        TextView view = (TextView)findViewById(R.id.textViewHouseLocation);
        view.setText(mHouseLocation);

        TextView viewReason = (TextView)findViewById(R.id.textViewReason);
        String error = commonFun.getErrorDescriptionByErrorCode(mErrorCode);
        viewReason.setText(error);
    }

    private void  doFinish() {
        Intent intent =new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        finish();
    }

    public void onClickResponse(View v) {
        int id = v.getId();
        switch (v.getId()) {
            case R.id.textViewBack:
            {
                finish();
                break;
            }
            case R.id.textViewClose:
            {
                doFinish();
                break;
            }
            case R.id.improveContainer: {
                Intent intent = new Intent(Activity_Zushouweituo_Finish_Error.this, Activity_fangyuan_guanli.class);
                intent.putExtra(ClassDefine.IntentExtraKeyValue.KEY_HOUSE_ID, mHouseId);
                intent.putExtra(ClassDefine.IntentExtraKeyValue.KEY_HOUSE_LOCATION, mHouseLocation);
                startActivityForResult(intent, 0);
                break;
            }
            case R.id.imageViewActivityBack: {
                finish();
                break;
            }
            case R.id.imageViewActivityClose: {
                doFinish();
                break;
            }
        }
    }
}
