package com.kjs.skywalk.app_android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by admin on 2017/3/22.
 */

public class Activity_Zushouweituo_Finish extends SKBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__zushouweituo__finish);
        TextView titleText = (TextView)findViewById(R.id.textViewActivityTitle);
        titleText.setText("租售委托-完成");
        ImageView closeButton = (ImageView)findViewById(R.id.imageViewActivityClose);
        closeButton.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        TextView view = (TextView)findViewById(R.id.textViewHouseLocation);
        String property = ClassDefine.HouseInfoForCommit.propertyName;
        String buildingNo = ClassDefine.HouseInfoForCommit.buildingNo;
        String room = ClassDefine.HouseInfoForCommit.roomNo;

        String location = property + buildingNo + "栋" + room + "室";
        view.setText(location);

        TextView rental = (TextView)findViewById(R.id.textViewRent);
        TextView sale = (TextView)findViewById(R.id.textViewSale);
        if(ClassDefine.HouseInfoForCommit.forRental()) {
            rental.setSelected(true);
        } else {
            rental.setSelected(false);
        }

        if(ClassDefine.HouseInfoForCommit.forSale()) {
            sale.setSelected(true);
        } else {
            sale.setSelected(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        doFinish();
    }

    private void  doFinish() {
        Intent intent =new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        finish();
    }

    public void onClickResponse(View v) {
        switch (v.getId()) {
            case R.id.tv_close:
            {
                doFinish();
                break;
            }
            case R.id.improveContainer: {
                startActivityForResult(new Intent(Activity_Zushouweituo_Finish.this, Activity_fangyuan_guanli.class), 0);
                break;
            }
            case R.id.uploadContainer: {
                startActivityForResult(new Intent(Activity_Zushouweituo_Finish.this, Activity_fangyuan_zhaopian.class), 1);
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
