package com.kjs.skywalk.app_android;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.kjs.skywalk.control.kjsNumberPicker;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.kjs.skywalk.app_android.commonFun.getHouseTypeString;

public class Activity_Zushouweituo_Fangyuanxinxi extends SKBaseActivity {

    private PopupWindowZhuangxiuSelector mZhuangxiuSelector = null;
    private ScrollView mContainer = null;

    private String mRoomString = "";
    private String mLoungeString = "";
    private String mBathString = "";
    private int mRoomIndex= -1;
    private int mLoungeIndex = -1;
    private int mBathIndex = -1;

    private int mPropertyId = -1;
    private int mDecorate = -1;
    private int mBuyYear = 0;
    private int mBuyMonth = 0;
    private int mBuyDay = 0;

    ClassDefine.HouseTypeSelector mHouseTypeSelector = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__zushouweituo__fangyuanxinxi);
        TextView titleText = (TextView)findViewById(R.id.textViewActivityTitle);
        titleText.setText("租售委托-房源信息");
        ImageView closeButton = (ImageView)findViewById(R.id.imageViewActivityClose);
        closeButton.setVisibility(View.INVISIBLE);
        mContainer = (ScrollView)findViewById(R.id.scrollViewContainer);
    }

    public void onClickResponse(View v) {
        switch (v.getId()) {
            case R.id.imageViewActivityBack:
            {
                finish();
            }
            break;
            case R.id.imageViewActivityClose:
            {
                Intent intent =new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                finish();
            }
            break;

            case R.id.tv_huxing_selector:
            {
                selectHouseType();
            }
            break;

            case R.id.tv_zhuangxiu_selector:
            {
                if(mZhuangxiuSelector == null) {
                    mZhuangxiuSelector = new PopupWindowZhuangxiuSelector(this);
                }

                mZhuangxiuSelector.showAtLocation(mContainer, Gravity.BOTTOM, 0, 0);
                break;
            }

            case R.id.tv_date_selector:
            {
                showDateSelectorDlg();
            }
            break;

            case R.id.tv_next:
            {
                if(!collectData()){
                    return;
                }
                startActivity(new Intent(Activity_Zushouweituo_Fangyuanxinxi.this, Activity_Zushouweituo_Jiagesheding.class));
            }
            break;
            case R.id.textViewSelectBlock: {
                startActivityForResult(new Intent(Activity_Zushouweituo_Fangyuanxinxi.this, Activity_Search_House.class), 0);
                break;
            }
            case R.id.textViewMaopi:
            case R.id.textViewJianzhuang:
            case R.id.textViewZhongdeng:
            case R.id.textViewJingzhuang:
            case R.id.textViewHaohua:{
                TextView tmp = (TextView)v;
                mDecorate = Integer.valueOf(tmp.getTag().toString());
                TextView button = (TextView)findViewById(R.id.tv_zhuangxiu_selector);
                button.setText(tmp.getText().toString());
                mZhuangxiuSelector.dismiss();
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0) { //request from Activity_Search_House
            if(resultCode != 0) {
                TextView view = (TextView)findViewById(R.id.textViewSelectBlock);
                String name = data.getStringExtra("name");
                view.setText(name);
                mPropertyId = data.getIntExtra("id", -1);
            }
        }
    }

    private boolean collectData() {
        TextView viewPropertyName = (TextView)findViewById(R.id.textViewSelectBlock);
        String propertyName = viewPropertyName.getText().toString();
        if(propertyName.isEmpty()) {
            commonFun.showToast_info(this, viewPropertyName, "小区名称不能为空");
            return false;
        }

        ClassDefine.HouseInfoForCommit.propertyName = propertyName;
        ClassDefine.HouseInfoForCommit.propertyId = mPropertyId;

        EditText viewNumber = (EditText)findViewById(R.id.editTextPropertyNumber);
        String number = viewNumber.getText().toString();
        if(number.isEmpty()) {
            commonFun.showToast_info(this, viewPropertyName, "楼栋号不能为空");
            return false;
        }

        ClassDefine.HouseInfoForCommit.buildingNo = number;

        EditText viewFloor = (EditText)findViewById(R.id.editTextFloor);
        String floor = viewFloor.getText().toString();
        EditText viewFloorTotal = (EditText)findViewById(R.id.editTextTotalFloor);
        String floorTotal = viewFloorTotal.getText().toString();

        int nFloor;
        int nFloorTotal;
        try {
            nFloor = Integer.valueOf(floor);
            if(nFloor <= 0) {
                commonFun.showToast_info(this, viewPropertyName, "楼层输入不正确");
                return false;
            }
        } catch (NumberFormatException e) {
            commonFun.showToast_info(this, viewPropertyName, "楼层输入不正确");
            return false;
        }
        try {
            nFloorTotal = Integer.valueOf(floorTotal);
            if(nFloorTotal <= 0) {
                commonFun.showToast_info(this, viewPropertyName, "总楼层数输入不正确");
                return false;
            }
        } catch (NumberFormatException e) {
            commonFun.showToast_info(this, viewPropertyName, "总楼层数输入不正确");
            return false;
        }

        if(nFloor > nFloorTotal){
            commonFun.showToast_info(this, viewPropertyName, "楼层应小于总楼层数");
            return false;
        }

        ClassDefine.HouseInfoForCommit.floor = nFloor;
        ClassDefine.HouseInfoForCommit.totalFloor = nFloorTotal;

        EditText viewRoomNumber = (EditText)findViewById(R.id.editTextRoomNumber);
        String roomNumber = viewRoomNumber.getText().toString();
        if(roomNumber.isEmpty()) {
            commonFun.showToast_info(this, viewRoomNumber, "门牌号不能为空");
            return false;
        }

        ClassDefine.HouseInfoForCommit.roomNo = roomNumber;

        EditText viewArea = (EditText)findViewById(R.id.editTextArea);
        String area = viewArea.getText().toString();
        double nArea;
        try {
            nArea = Double.valueOf(area);
            if(nArea <= 0) {
                commonFun.showToast_info(this, viewPropertyName, "面积输入不正确");
                return false;
            }
        } catch (NumberFormatException e) {
            commonFun.showToast_info(this, viewPropertyName, "面积输入不正确");
            return false;
        }

        ClassDefine.HouseInfoForCommit.area = (int)(nArea * 100);

        TextView viewHuxing = (TextView)findViewById(R.id.tv_huxing_selector);
        String huxing = viewHuxing.getText().toString();
        if(huxing.isEmpty()) {
            commonFun.showToast_info(this, viewPropertyName, "请选择户型");
            return false;
        }

        ClassDefine.HouseInfoForCommit.bedRooms = mRoomIndex + 1;
        ClassDefine.HouseInfoForCommit.livingRooms = mLoungeIndex + 1;
        ClassDefine.HouseInfoForCommit.bathRooms = mBathIndex + 1;

        TextView viewZhuangxiu = (TextView)findViewById(R.id.tv_zhuangxiu_selector);
        String zhuangxiu = viewZhuangxiu.getText().toString();
        if(zhuangxiu.isEmpty()) {
            commonFun.showToast_info(this, viewPropertyName, "请选择装修程度");
            return false;
        }

        ClassDefine.HouseInfoForCommit.decorate = mDecorate;
        ClassDefine.HouseInfoForCommit.decorateDescription = zhuangxiu;

        TextView viewDate = (TextView)findViewById(R.id.tv_date_selector);
        String date = viewDate.getText().toString();
        if(date.isEmpty()) {
            commonFun.showToast_info(this, viewPropertyName, "请选择购买日期");
            return false;
        }

        ClassDefine.HouseInfoForCommit.buyYear = mBuyYear;
        ClassDefine.HouseInfoForCommit.buyMonth = mBuyMonth;
        ClassDefine.HouseInfoForCommit.buyDay = mBuyDay;

        ToggleButton hasLoan = (ToggleButton)findViewById(R.id.toggleButtonLoan);
        ClassDefine.HouseInfoForCommit.hasLoan = hasLoan.isChecked() ? 1 : 0;

        ToggleButton unique = (ToggleButton)findViewById(R.id.toggleButtonUnique);
        ClassDefine.HouseInfoForCommit.unique = unique.isChecked() ? 1 : 0;

        return true;
    }

    private AlertDialog mDateSelectorDlg;
    private DatePicker mDatePicker;
    private void showDateSelectorDlg() {
        if (mDateSelectorDlg == null) {
            mDateSelectorDlg = new AlertDialog.Builder(this).create();
        }
        mDateSelectorDlg.show();
        mDateSelectorDlg.setContentView(R.layout.dialog_fangyuanxinxi_dateselector);

        mDatePicker = (DatePicker)mDateSelectorDlg.findViewById(R.id.datePicker2);

        TextView tvBack = (TextView) mDateSelectorDlg.findViewById(R.id.tv_back);
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDateSelectorDlg.dismiss();
            }
        });

        TextView tvConfirm = (TextView) mDateSelectorDlg.findViewById(R.id.tv_confirm);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBuyYear = mDatePicker.getYear();
                mBuyMonth = mDatePicker.getMonth();
                mBuyDay = mDatePicker.getDayOfMonth();

                Calendar calendar = Calendar.getInstance();
                calendar.set(mBuyYear, mBuyMonth, mBuyDay);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String date = df.format(calendar.getTime());
                TextView tmp = (TextView)findViewById(R.id.tv_date_selector);
                tmp.setText(date);

                mDateSelectorDlg.dismiss();
            }
        });
    }

    private void selectHouseType() {
        DialogInterface.OnDismissListener listener = new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(mHouseTypeSelector.mDirty) {
                    mRoomIndex = mHouseTypeSelector.mRoomIndex;
                    mBathIndex = mHouseTypeSelector.mBathIndex;
                    mLoungeIndex = mHouseTypeSelector.mLoungeIndex;

                    TextView type = (TextView) findViewById(R.id.tv_huxing_selector);
                    String strType = getHouseTypeString(mRoomIndex + 1, mLoungeIndex + 1, mBathIndex + 1);
                    type.setText(strType);
                }
            }
        };

        mHouseTypeSelector = new ClassDefine.HouseTypeSelector(this);
        mHouseTypeSelector.setHouseType(1, 1, 1);
        mHouseTypeSelector.show(listener);
    }
}
