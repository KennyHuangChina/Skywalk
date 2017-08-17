package com.kjs.skywalk.app_android;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kjs.skywalk.app_android.Server.GetHouseInfo;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;

import static com.kjs.skywalk.app_android.commonFun.getHouseTypeString;

public class Activity_Zushouweituo_shenhe extends SKBaseActivity implements GetHouseInfo.TaskFinished{

    private boolean mModifyMode = false;
    private RelativeLayout mRootLayout = null;
    private TextView mTextViewPropertyName = null;
    private TextView mTextViewRoom = null;

    private ClassDefine.HouseInfo mHouseInfo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__zushouweituo_shenhe);
        mRootLayout = (RelativeLayout)findViewById(R.id.activity__zushouweituo_shenhe);

        mTextViewPropertyName = (TextView)findViewById(R.id.textViewPropertyName);
        mTextViewRoom = (TextView)findViewById(R.id.textViewRoomNo);

        mTextViewPropertyName.setText(mPropertyName);
//        String strRoom = mBuildingNo + "栋" + mRoomNo + "室";
//        mTextViewRoom.setText(strRoom);

        GetHouseInfo houseInfo = new GetHouseInfo(this, this);
        houseInfo.execute(mHouseId, 1);
    }

    private void update() {
        if(mHouseInfo == null) {
            return;
        }

        if(mHouseInfo.buildingNo != null && !mHouseInfo.buildingNo.isEmpty() &&
                mHouseInfo.roomNo != null && !mHouseInfo.roomNo.isEmpty()) {
            String strRoom = mHouseInfo.buildingNo + "栋" + mHouseInfo.roomNo + "室";
            mTextViewRoom.setText(strRoom);
        } else {
            mTextViewRoom.setText("未知");
        }

        TextView floor = (TextView)findViewById(R.id.textViewFloor);
        floor.setText("" + mHouseInfo.floor + "/" + mHouseInfo.totalFloor + "F");

        TextView area = (TextView)findViewById(R.id.textViewArea);
        double acreage = (double)mHouseInfo.area / 100.0;
        area.setText(String.format("%.02f", acreage) + "㎡");

        TextView type = (TextView)findViewById(R.id.textViewHouseType);
        String strType = getHouseTypeString(mHouseInfo.bedRooms, mHouseInfo.livingRooms, mHouseInfo.bathRooms);
        type.setText(strType);

        TextView submitTime = (TextView)findViewById(R.id.textViewSubmitTime);
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdformat.format(mHouseInfo.submitTime);
        submitTime.setText(time);
    }

    @Override
    public void onTaskFinished(final ClassDefine.HouseInfo houseInfo) {
        if(houseInfo == null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    commonFun.showToast_info(getApplicationContext(), mRootLayout, "获取房屋信息失败");
                }
            });
            return;
        } else {
            mHouseInfo = houseInfo;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    update();
                }
            });
        }
    }

    public void onClickResponse(View v) {
        switch (v.getId()) {
            case R.id.tv_back: {
                finish();
            }
            break;
            case R.id.textViewPropertyName: {
                startActivityForResult(new Intent(Activity_Zushouweituo_shenhe.this, Activity_Search_House.class), 0);
                break;
            }
            case R.id.textViewModify: {
                mModifyMode = !mModifyMode;
                TextView button = (TextView)v;
                TextView floor = (TextView)findViewById(R.id.textViewFloor);
                TextView propertyName = (TextView)findViewById(R.id.textViewPropertyName);
                TextView room = (TextView)findViewById(R.id.textViewRoomNo);
                TextView area  = (TextView)findViewById(R.id.textViewArea);
                TextView type = (TextView)findViewById(R.id.textViewHouseType);
                if(mModifyMode) {
                    Drawable drawable = ContextCompat.getDrawable(this, R.drawable.right_n);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    floor.setCompoundDrawablePadding(15);
                    floor.setCompoundDrawables(null, null, drawable, null);
                    propertyName.setCompoundDrawablePadding(15);
                    propertyName.setCompoundDrawables(null, null, drawable, null);
                    room.setCompoundDrawablePadding(15);
                    room.setCompoundDrawables(null, null, drawable, null);
                    area.setCompoundDrawablePadding(15);
                    area.setCompoundDrawables(null, null, drawable, null);
                    type.setCompoundDrawablePadding(15);
                    type.setCompoundDrawables(null, null, drawable, null);

                    int color = ContextCompat.getColor(this, R.color.colorButtonTextHighlight);
                    floor.setTextColor(color);
                    propertyName.setTextColor(color);
                    room.setTextColor(color);
                    area.setTextColor(color);
                    type.setTextColor(color);

                    floor.setEnabled(true);
                    propertyName.setEnabled(true);
                    room.setEnabled(true);
                    area.setEnabled(true);
                    type.setEnabled(true);

                    button.setText("完成");
                } else {
                    floor.setCompoundDrawables(null, null, null, null);
                    propertyName.setCompoundDrawables(null, null, null, null);
                    room.setCompoundDrawables(null, null, null, null);
                    area.setCompoundDrawables(null, null, null, null);
                    type.setCompoundDrawables(null, null, null, null);
                    int color = ContextCompat.getColor(this, R.color.colorTextNormal);
                    floor.setTextColor(color);
                    propertyName.setTextColor(color);
                    room.setTextColor(color);
                    area.setTextColor(color);
                    type.setTextColor(color);

                    floor.setEnabled(false);
                    propertyName.setEnabled(false);
                    room.setEnabled(false);
                    area.setEnabled(false);
                    type.setEnabled(false);
                    button.setText("修改");
                }

                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0) { //request from Activity_Search_House
            if(resultCode != 0) {
                TextView view = (TextView)findViewById(R.id.textViewPropertyName);
                String name = data.getStringExtra("name");
                view.setText(name);
            }
        }

    }

}
