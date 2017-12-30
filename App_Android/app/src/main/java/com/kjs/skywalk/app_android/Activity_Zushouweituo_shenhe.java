package com.kjs.skywalk.app_android;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.midi.MidiManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kjs.skywalk.communicationlibrary.CmdExecRes;
import com.kjs.skywalk.communicationlibrary.CommandManager;
import com.kjs.skywalk.communicationlibrary.CommunicationError;
import com.kjs.skywalk.communicationlibrary.CommunicationInterface;
import com.kjs.skywalk.communicationlibrary.IApiResults;
import com.kjs.skywalk.control.kjsNumberPicker;


import org.w3c.dom.Text;

import java.text.SimpleDateFormat;

import static com.kjs.skywalk.app_android.commonFun.getHouseTypeString;
import static com.kjs.skywalk.communicationlibrary.CommunicationInterface.CmdID.*;

public class Activity_Zushouweituo_shenhe extends SKBaseActivity {

    private boolean mModifyMode = false;
    private RelativeLayout mRootLayout = null;
    private TextView mTextViewPropertyName = null;
    private TextView mTextViewRoom = null;

    private ClassDefine.HouseInfo mHouseInfo = null;
    private int mRooms = 0;
    private int mLounges = 0;
    private int mBaths = 0;

    private String mBuildingNo = "";
    private String mRoomNo = "";

    private int mCurrentFloor = 0;
    private int mTotalFloor = 0;

    private int mArea = 0;
    private int mPropertyId = 0;
    private String mBuyDate = "";
    private boolean mPassed = false;

    private String mPhoneNumber = "";
    private String mLandlordName = "";

    ClassDefine.HouseTypeSelector mHouseTypeSelector = null;
    ClassDefine.ConfirmDialog mConfirmDialog = null;

    private PopupWindowWaitingUnclickable mWaitingWindow = null;

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

        getHouseInfo();
    }

    private void update() {
        if(mHouseInfo == null) {
            return;
        }

        if(mHouseInfo.buildingNo != null && !mHouseInfo.buildingNo.isEmpty() &&
                mHouseInfo.roomNo != null && !mHouseInfo.roomNo.isEmpty()) {
            mTextViewRoom.setText(String.format("%s栋 %s室" , mHouseInfo.buildingNo, mHouseInfo.roomNo));
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

        mBuildingNo = mHouseInfo.buildingNo;
        mRoomNo = mHouseInfo.roomNo;

        mCurrentFloor = mHouseInfo.floor;
        mTotalFloor = mHouseInfo.totalFloor;

        mArea = mHouseInfo.area;
    }

    @Override
    public void onCommandFinished(int command, int cmdSeq, IApiResults.ICommon iResult) {
        if (null == iResult) {
            kjsLogUtil.w("result is null");
            return;
        }
        CmdExecRes cmd = RetrieveCommand(cmdSeq);
        if (null == cmd) {  // result is not we wanted
            return;
        }
        kjsLogUtil.i(String.format("command: %d(%s), seq: %d %s", command, CommunicationInterface.CmdID.GetCmdDesc(command), cmdSeq, iResult.DebugString()));

        int nErrCode = iResult.GetErrCode();
        if (CommunicationInterface.CmdRes.CMD_RES_NOERROR != nErrCode) {
            kjsLogUtil.e("Command:" + command + " finished with error: " + nErrCode);
            super.onCommandFinished(command, cmdSeq, iResult);
            if(command == CMD_AMEND_HOUSE) {
                showWaiting(false);
            }
            return;
        }

        if (command == CommunicationInterface.CmdID.CMD_GET_USER_INFO) {
            IApiResults.IGetUserInfo info = (IApiResults.IGetUserInfo) iResult;
            mPhoneNumber = info.GetPhoneNo();
            mLandlordName = info.GetName();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateUserInfo();
                }
            });
        } else if (command == CommunicationInterface.CmdID.CMD_CERTIFY_HOUSE) {
            commonFun.showToast_info(getApplicationContext(), mRootLayout, "提交认证审核结果成功");
            showWaiting(false);
        } else if (command == CMD_AMEND_HOUSE) {
            showWaiting(false);
        } else if (command == CMD_GET_HOUSE_INFO) {     // GetHouseInfo
            mHouseInfo = new ClassDefine.HouseInfo();
            IApiResults.IGetHouseInfo info = (IApiResults.IGetHouseInfo) iResult;
            mHouseInfo.floor        = info.Floorthis();
            mHouseInfo.totalFloor   = info.FloorTotal();
            mHouseInfo.bedRooms     = info.Bedrooms();
            mHouseInfo.livingRooms  = info.Livingrooms();
            mHouseInfo.bathRooms    = info.Bathrooms();
            mHouseInfo.area         = info.Acreage();
            mHouseInfo.landlordId   = info.Landlord();
            mHouseInfo.submitTime   = info.SubmitTime();
            mHouseInfo.buildingNo   = info.BuildingNo();
            mHouseInfo.roomNo       = info.HouseNo();
            mHouseInfo.decorate     = info.Decorate();

            mPropertyId = info.ProId();
            mBuyDate    = info.BuyDate();
            mRooms      = info.Bedrooms();
            mLounges    = info.Livingrooms();
            mBaths      = info.Bathrooms();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    update();
                    getLandlordInfo();
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
            case R.id.textViewHouseType: {
                selectHouseType();
                break;
            }
            case R.id.textViewRoomNo: {
                modifyRoomNo();
                break;
            }
            case R.id.textViewFloor: {
                modifyFloor();
                break;
            }
            case R.id.textViewArea: {
                modifyArea();
                break;
            }
            case R.id.textViewNotPassed:
            case R.id.textViewPassed: {
                if(mModifyMode) {
                    commonFun.showToast_info(getApplicationContext(), mRootLayout, "请先点击完成，保存修改");
                    break;
                }
                EditText editText = (EditText)findViewById(R.id.editTextShenheShuomin);
                String text = editText.getText().toString();
                if(text == null || text.isEmpty()) {
                    commonFun.showToast_info(getApplicationContext(), mRootLayout, "请填写审核说明");
                    break;
                }

                if(mConfirmDialog == null) {
                    mConfirmDialog = new ClassDefine.ConfirmDialog(this);
                }

                DialogInterface.OnDismissListener listener = new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if(mConfirmDialog.getConfirmed()) {
                            doCertificateConfirm(mPassed);
                        } else {

                        }
                    }
                };
                if(v.getId() == R.id.textViewNotPassed) {
                    mConfirmDialog.setConfirmString("是否确认未通过审核？");
                    mPassed = false;
                } else {
                    mConfirmDialog.setConfirmString("是否确认通过审核？");
                    mPassed = true;
                }
                mConfirmDialog.setDismissListener(listener);
                mConfirmDialog.showDialog(true);
                break;
            }
            case R.id.textViewModify: {
                TextView button = (TextView)v;
                TextView floor = (TextView)findViewById(R.id.textViewFloor);
                TextView propertyName = (TextView)findViewById(R.id.textViewPropertyName);
                TextView room = (TextView)findViewById(R.id.textViewRoomNo);
                TextView area  = (TextView)findViewById(R.id.textViewArea);
                TextView type = (TextView)findViewById(R.id.textViewHouseType);
                if(!mModifyMode) {
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
                    mModifyMode = true;
                } else {
                    if(mConfirmDialog == null) {
                        mConfirmDialog = new ClassDefine.ConfirmDialog(this);
                    }

                    DialogInterface.OnDismissListener listener = new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            if(mConfirmDialog.getConfirmed()) {
                                doModifyConfirmed();
                                mModifyMode = false;
                            } else {

                            }
                        }
                    };
                    mConfirmDialog.setConfirmString("是否确认修改并提交？");
                    mConfirmDialog.setDismissListener(listener);
                    mConfirmDialog.showDialog(true);
                }

                break;
            }
        }
    }

    private void updateUserInfo() {
        String landlordInfo = mLandlordName + " " + mPhoneNumber;
        TextView v = (TextView)findViewById(R.id.textViewLandlord);
        v.setText(landlordInfo);

        kjsLogUtil.i("landlord info: " + landlordInfo);
    }

    private void getLandlordInfo() {
        if(mHouseInfo.landlordId <= 0) {
            return;
        }

        CmdExecRes res = CommandManager.getCmdMgrInstance(this).GetUserInfo(mHouseInfo.landlordId);
        if (res.mError != CommunicationError.CE_ERROR_NO_ERROR) {
            kjsLogUtil.e("Fail to send command GetUserInfo, err: " + res.mError);
            commonFun.showToast_info(getApplicationContext(), mRootLayout, "获取房东信息失败");
        } else {
            StoreCommand(res);
        }
    }

    private void doCertificateConfirm(boolean pass) {
        EditText textView = (EditText)findViewById(R.id.editTextShenheShuomin);
        String string = textView.getText().toString();

//        CommunicationInterface.CICommandListener listener = new CommunicationInterface.CICommandListener() {
//            @Override
//            public void onCommandFinished(int command, final int cmdSeq, IApiResults.ICommon iResult) {
//                if (null == iResult) {
//                    kjsLogUtil.w("result is null");
//                    return;
//                }
//
//                kjsLogUtil.i(String.format("[command: %d] --- %s" , command, iResult.DebugString()));
//                if (CommunicationError.CE_ERROR_NO_ERROR != iResult.GetErrCode()) {
//                    kjsLogUtil.e("Command:" + command + " finished with error: " + iResult.GetErrDesc());
//                    Activity_Zushouweituo_shenhe.super.onCommandFinished(command, cmdSeq, iResult);
//                    return;
//                }
//
//                if (command == CommunicationInterface.CmdID.CMD_CERTIFY_HOUSE) {
//                    commonFun.showToast_info(getApplicationContext(), mRootLayout, "提交认证审核结果成功");
//                    showWaiting(false);
//                }
//            }
//        };

        CommandManager manager = CommandManager.getCmdMgrInstance(this/*, listener, this*/);
        if (manager.CertificateHouse(mHouseId, pass, string).mError != CommunicationError.CE_ERROR_NO_ERROR) {
            commonFun.showToast_info(getApplicationContext(), mRootLayout, "提交认证审核结果失败");
        }
    }

    private void doModifyConfirmed() {
        TextView button = (TextView)findViewById(R.id.textViewModify);
        TextView floor = (TextView)findViewById(R.id.textViewFloor);
        TextView propertyName = (TextView)findViewById(R.id.textViewPropertyName);
        TextView room = (TextView)findViewById(R.id.textViewRoomNo);
        TextView area  = (TextView)findViewById(R.id.textViewArea);
        TextView type = (TextView)findViewById(R.id.textViewHouseType);

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

        commitModification();
    }

    private void commitModification() {
        CommunicationInterface.HouseInfo houseInfo =
                new CommunicationInterface.HouseInfo(mHouseId,
                                                     mPropertyId,
                                                     mBuildingNo,
                                                     mRoomNo,
                                                     mTotalFloor,
                                                     mCurrentFloor,
                                                     mLounges,
                                                     mRooms,
                                                     mBaths,
                                                     mArea,
                                                     false,
                                                     true,
                                                     mHouseInfo.decorate,
                                                     mBuyDate);

//        CommunicationInterface.CICommandListener listener = new CommunicationInterface.CICommandListener() {
//            @Override
//            public void onCommandFinished(int command, final int cmdSeq, IApiResults.ICommon iResult) {
//                if (CommunicationError.CE_ERROR_NO_ERROR != iResult.GetErrCode()) {
//                    kjsLogUtil.e("Command:" + command + " finished with error: " + iResult.GetErrDesc());
//                    Activity_Zushouweituo_shenhe.super.onCommandFinished(command, cmdSeq, iResult);
//                    showWaiting(false);
//                    return;
//                }
//
//                if(command == CMD_AMEND_HOUSE) {
//
//                    showWaiting(false);
//                }
//            }
//        };

        CommandManager manager = CommandManager.getCmdMgrInstance(this); //, listener, this);
        CmdExecRes result = manager.AmendHouse(houseInfo);
        if (result.mError != CommunicationError.CE_ERROR_NO_ERROR) {
            commonFun.showToast_info(getApplicationContext(), mRootLayout, "提交房屋信息失败");
        } else {
            showWaiting(true);
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

                mPropertyId = data.getIntExtra("id", 0);
            }
        }

    }

    private int getHouseInfo() {
        CmdExecRes result = CommandManager.getCmdMgrInstance(this).GetHouseInfo(mHouseId, true);
        if (result.mError != CommunicationError.CE_ERROR_NO_ERROR) {
            kjsLogUtil.e("Fail to send command GetHouseInfo, err:" + result.mError);
            commonFun.showToast_info(getApplicationContext(), mRootLayout, "获取房屋信息失败");
            return -1;
        } else {
            StoreCommand(result);
        }

        return 0;
    }

    private void selectHouseType() {
        DialogInterface.OnDismissListener listener = new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(mHouseTypeSelector.mDirty) {
                    mRooms = mHouseTypeSelector.mRoomIndex + 1;
                    mBaths =mHouseTypeSelector.mBathIndex + 1;
                    mLounges = mHouseTypeSelector.mLoungeIndex + 1;

                    TextView type = (TextView) findViewById(R.id.textViewHouseType);
                    String strType = getHouseTypeString(mRooms, mLounges, mBaths);
                    type.setText(strType);
                }
            }
        };
        mHouseTypeSelector = new ClassDefine.HouseTypeSelector(this);
        mHouseTypeSelector.setHouseType(mHouseInfo.bedRooms, mHouseInfo.livingRooms, mHouseInfo.bathRooms);
        mHouseTypeSelector.show(listener);
    }

    private AlertDialog mDialogRoomNo = null;
    private void modifyRoomNo() {
        if(mDialogRoomNo == null) {
            mDialogRoomNo = new AlertDialog.Builder(this).create();
        }

        mDialogRoomNo.show();
        mDialogRoomNo.setContentView(R.layout.dialog_change_room_no);
        mDialogRoomNo.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        TextView vBuildingNo = (TextView)mDialogRoomNo.findViewById(R.id.editTextBuildingNo);
        vBuildingNo.setText(mBuildingNo);
        TextView vRoomNo = (TextView)mDialogRoomNo.findViewById(R.id.editTextRoomNo);
        vRoomNo.setText(mRoomNo);

        mDialogRoomNo.findViewById(R.id.textViewCannel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonFun.hideSoftKeyboard(Activity_Zushouweituo_shenhe.this, v);
                mDialogRoomNo.dismiss();
            }
        });
        mDialogRoomNo.findViewById(R.id.textViewOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonFun.hideSoftKeyboard(Activity_Zushouweituo_shenhe.this, v);
                TextView vBuildingNo = (TextView)mDialogRoomNo.findViewById(R.id.editTextBuildingNo);
                String building = vBuildingNo.getText().toString();
                if(building.isEmpty()) {
                    commonFun.showToast_info(Activity_Zushouweituo_shenhe.this, vBuildingNo, "请输入正确的楼栋号");
                    return;
                }
                TextView vRoomNo = (TextView)mDialogRoomNo.findViewById(R.id.editTextRoomNo);
                String room = vRoomNo.getText().toString();
                if(room.isEmpty()) {
                    commonFun.showToast_info(Activity_Zushouweituo_shenhe.this, vRoomNo, "请输入正确的房间号");
                    return;
                }

                mBuildingNo = building;
                mRoomNo = room;
                mDialogRoomNo.dismiss();

                TextView roomView = (TextView)findViewById(R.id.textViewRoomNo);
                roomView.setText(mBuildingNo + "栋" + mRoomNo + "室");
            }
        });
    }

    private AlertDialog mDialogFloor = null;
    private void modifyFloor() {
        if(mDialogFloor == null) {
            mDialogFloor = new AlertDialog.Builder(this).create();
        }

        mDialogFloor.show();
        mDialogFloor.setContentView(R.layout.dialog_change_floor);
        mDialogFloor.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        EditText vFloor = (EditText)mDialogFloor.findViewById(R.id.editTextFloor);
        vFloor.setText(String.valueOf(mCurrentFloor));
        EditText vTotalFloor = (EditText)mDialogFloor.findViewById(R.id.editTextTotalFloor);
        vTotalFloor.setText(String.valueOf(mTotalFloor));

        mDialogFloor.findViewById(R.id.textViewCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonFun.hideSoftKeyboard(Activity_Zushouweituo_shenhe.this, v);
                mDialogFloor.dismiss();
            }
        });
        mDialogFloor.findViewById(R.id.textViewOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonFun.hideSoftKeyboard(Activity_Zushouweituo_shenhe.this, v);
                TextView vFloor = (TextView)mDialogFloor.findViewById(R.id.editTextFloor);
                String floor = vFloor.getText().toString();
                if(floor.isEmpty()) {
                    commonFun.showToast_info(Activity_Zushouweituo_shenhe.this, vFloor, "请输入正确的楼层");
                    return;
                }
                int nFloor = Integer.valueOf(floor);
                TextView vTotalFloor = (TextView)mDialogFloor.findViewById(R.id.editTextTotalFloor);
                String totalFloor = vTotalFloor.getText().toString();
                if(totalFloor.isEmpty()) {
                    commonFun.showToast_info(Activity_Zushouweituo_shenhe.this, vTotalFloor, "请输入正确的楼层");
                    return;
                }

                int nTotalFloor = Integer.valueOf(totalFloor);
                if(nFloor > nTotalFloor) {
                    commonFun.showToast_info(Activity_Zushouweituo_shenhe.this, vTotalFloor, "当前楼层不能大于总楼层");
                    return;
                }

                mCurrentFloor = nFloor;
                mTotalFloor = nTotalFloor;
                mDialogFloor.dismiss();

                TextView textViewFloor = (TextView)findViewById(R.id.textViewFloor);
                textViewFloor.setText("" + mCurrentFloor + "/" + mTotalFloor);
            }
        });
    }


    private AlertDialog mDialogArea = null;
    private void modifyArea() {
        if(mDialogArea == null) {
            mDialogArea = new AlertDialog.Builder(this).create();
        }

        mDialogArea.show();
        mDialogArea.setContentView(R.layout.dialog_change_area);
        mDialogArea.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        EditText vArea = (EditText)mDialogArea.findViewById(R.id.editTextArea);
        double acreage = (double)mArea / 100.0;
        String strArea = String.format("%.02f", acreage);
        vArea.setText(strArea);

        mDialogArea.findViewById(R.id.textViewCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonFun.hideSoftKeyboard(Activity_Zushouweituo_shenhe.this, v);
                mDialogArea.dismiss();
            }
        });
        mDialogArea.findViewById(R.id.textViewOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonFun.hideSoftKeyboard(Activity_Zushouweituo_shenhe.this, v);
                TextView vArea = (TextView)mDialogArea.findViewById(R.id.editTextArea);
                String area = vArea.getText().toString();
                if(area.isEmpty()) {
                    commonFun.showToast_info(Activity_Zushouweituo_shenhe.this, vArea, "请输入正确的面积");
                    return;
                }
                Double dArea = Double.valueOf(area);
                area = String.format("%.02f", dArea);
                dArea = Double.valueOf(area);
                mArea = (int)(dArea * 100);
                mDialogArea.dismiss();

                String strArea = String.format("%.02f", dArea);
                TextView textViewArea = (TextView)findViewById(R.id.textViewArea);
                textViewArea.setText( strArea + "㎡");
            }
        });
    }

    private void showWaiting(final boolean show) {
        if(mWaitingWindow == null) {
            mWaitingWindow = new PopupWindowWaitingUnclickable(this, mActScreenWidth, mActScreenHeight);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(show) {
                    mWaitingWindow.show(mRootLayout);
                } else {
                    mWaitingWindow.hide();
                }
            }
        });

    }
}
