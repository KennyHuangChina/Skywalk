package com.kjs.skywalk.app_android;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class Activity_HouseholdDeliverables extends AppCompatActivity {
    private AlertDialog mDeliverableEdtDlg;
    // test data
    static class HouseholdDeliverable {
        int mIcon;
        String mDesc;
        int mNum;

        public HouseholdDeliverable(int icon, String description, int number) {
            mIcon = icon;
            mDesc = description;
            mNum = number;
        }
    }

    private final static ArrayList<HouseholdDeliverable> mHouseDeliverables = new ArrayList<HouseholdDeliverable>(
            Arrays.asList(
                    new HouseholdDeliverable(R.drawable.deliverable_damenyaoshi, "大门钥匙", 1),
                    new HouseholdDeliverable(R.drawable.deliverable_menjinka, "门禁卡", 2),
                    new HouseholdDeliverable(R.drawable.deliverable_shuidianka, "水电卡/存折", 1),
                    new HouseholdDeliverable(R.drawable.deliverable_youxiandianshi, "有线电视用户证", 1),
                    new HouseholdDeliverable(R.drawable.deliverable_shuibiaoxiangyaoshi, "水表箱钥匙", 1),
                    new HouseholdDeliverable(R.drawable.deliverable_dianbiaoxiangyaoshi, "电表箱钥匙", 3),
                    new HouseholdDeliverable(R.drawable.deliverable_xinbaoxiangyaoshi, "信报箱钥匙", 1),
                    new HouseholdDeliverable(R.drawable.deliverable_baoxianguiyaoshi, "保险柜钥匙", 1),
                    new HouseholdDeliverable(R.drawable.deliverable_ranqika, "燃气卡", 3)
            )
    );
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_household_deliverables);

        SpannableString styleText = new SpannableString("世贸蝶湖湾 175栋2202室");
        int i = styleText.length();
        String s = "世贸蝶湖湾 175栋2202室";
        int l = s.length();
        styleText.setSpan(new TextAppearanceSpan(this, R.style.textstyle_large), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        styleText.setSpan(new TextAppearanceSpan(this, R.style.textstyle_small), 5, 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ((TextView)findViewById(R.id.tv_apartment_name)).setText(styleText, TextView.BufferType.SPANNABLE);

        loadUI();

        // add button
        ImageButton ibNew = (ImageButton) findViewById(R.id.ib_new);
        ibNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeliverableNewDlg();
            }
        });
    }

    // http://blog.csdn.net/gao_chun/article/details/46008651
    private void loadUI() {
        LinearLayout llDeliverables = (LinearLayout) findViewById(R.id.ll_deliverables);

        int rows = mHouseDeliverables.size() / 3;
        loadItems(llDeliverables, rows);
    }

    private void loadItems(ViewGroup layout, int rows) {

        for (int i = 0; i < rows; i++) {
            // parent
            final LinearLayout linearLayout = new LinearLayout(this);
//            View view = View.inflate(this, R.layout.listitem_household_deliverable, linearLayout);

            // attachToRoot = false, return the view itself, otherwise return the root view
            View view = LayoutInflater.from(this).inflate(R.layout.listitem_household_deliverable, linearLayout, false);
            view.setId(i*3);
            view.setOnClickListener(mDeliverableItemClickedListener);
            ((ImageView)view.findViewById(R.id.iv_icon)).setImageResource(mHouseDeliverables.get(i*3).mIcon);
            ((TextView)view.findViewById(R.id.tv_description)).setText(mHouseDeliverables.get(i*3).mDesc);
            ((TextView)view.findViewById(R.id.tv_number)).setText("x" + mHouseDeliverables.get(i*3).mNum);
            linearLayout.addView(view);

//            view = View.inflate(this, R.layout.listitem_household_deliverable, linearLayout);
            view = LayoutInflater.from(this).inflate(R.layout.listitem_household_deliverable, linearLayout, false);
            view.setId(i*3+1);
            view.setOnClickListener(mDeliverableItemClickedListener);
            ((ImageView)view.findViewById(R.id.iv_icon)).setImageResource(mHouseDeliverables.get(i*3+1).mIcon);
            ((TextView)view.findViewById(R.id.tv_description)).setText(mHouseDeliverables.get(i*3+1).mDesc);
            ((TextView)view.findViewById(R.id.tv_number)).setText("x" + mHouseDeliverables.get(i*3+1).mNum);
            linearLayout.addView(view);

//            view = View.inflate(this, R.layout.listitem_household_deliverable, linearLayout);
            view = LayoutInflater.from(this).inflate(R.layout.listitem_household_deliverable, linearLayout, false);
            view.setId(i*3+2);
            view.setOnClickListener(mDeliverableItemClickedListener);
            ((ImageView)view.findViewById(R.id.iv_icon)).setImageResource(mHouseDeliverables.get(i*3+2).mIcon);
            ((TextView)view.findViewById(R.id.tv_description)).setText(mHouseDeliverables.get(i*3+2).mDesc);
            ((TextView)view.findViewById(R.id.tv_number)).setText("x" + mHouseDeliverables.get(i*3+2).mNum);
            linearLayout.addView(view);

            ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layout.addView(linearLayout, params);

        }
    }

    private void showDeliverableEditDlg(final View vDeliverable) {
        int itemIndex = vDeliverable.getId();
        HouseholdDeliverable deliverable = mHouseDeliverables.get(itemIndex);
        int curValue = deliverable.mNum;
        String name = deliverable.mDesc;

        if (mDeliverableEdtDlg == null) {
            mDeliverableEdtDlg = new AlertDialog.Builder(this).create();
        }
        mDeliverableEdtDlg.show();
        mDeliverableEdtDlg.setContentView(R.layout.dialog_deliverable_editor);

        TextView tvName = (TextView) mDeliverableEdtDlg.findViewById(R.id.tv_deliverable_name);
        tvName.setText(name);

        final NumberPicker npDeliverable = (NumberPicker) mDeliverableEdtDlg.findViewById(R.id.np_deliverable);
        npDeliverable.setMinValue(1);
        npDeliverable.setMaxValue(10);
        npDeliverable.setValue(curValue);
        npDeliverable.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//                Toast.makeText(Activity_HouseholdDeliverables.this, oldVal + " to " + newVal, Toast.LENGTH_SHORT).show();
            }
        });

        TextView tvBack = (TextView) mDeliverableEdtDlg.findViewById(R.id.tv_back);
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDeliverableEdtDlg.dismiss();
            }
        });

        TextView tvConfirm = (TextView) mDeliverableEdtDlg.findViewById(R.id.tv_confirm);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TextView)vDeliverable.findViewById(R.id.tv_number)).setText("x" + npDeliverable.getValue());
                mDeliverableEdtDlg.dismiss();
            }
        });

    }

    private void showDeliverableNewDlg() {
        int itemIndex = 0;  // for test
        HouseholdDeliverable deliverable = mHouseDeliverables.get(itemIndex);
        int curValue = deliverable.mNum;
        String name = deliverable.mDesc;

        if (mDeliverableEdtDlg == null) {
            mDeliverableEdtDlg = new AlertDialog.Builder(this).create();
        }
        mDeliverableEdtDlg.show();
        mDeliverableEdtDlg.setContentView(R.layout.dialog_deliverable_new);

        TextView tvName = (TextView) mDeliverableEdtDlg.findViewById(R.id.tv_deliverable_name);
        tvName.setText(name);

        final NumberPicker npDeliverable = (NumberPicker) mDeliverableEdtDlg.findViewById(R.id.np_deliverable);
        npDeliverable.setMinValue(1);
        npDeliverable.setMaxValue(10);
        npDeliverable.setValue(curValue);
        npDeliverable.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//                Toast.makeText(Activity_HouseholdDeliverables.this, oldVal + " to " + newVal, Toast.LENGTH_SHORT).show();
            }
        });

        TextView tvBack = (TextView) mDeliverableEdtDlg.findViewById(R.id.tv_back);
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDeliverableEdtDlg.dismiss();
            }
        });

        TextView tvConfirm = (TextView) mDeliverableEdtDlg.findViewById(R.id.tv_confirm);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDeliverableEdtDlg.dismiss();
            }
        });

    }

    private View.OnClickListener mDeliverableItemClickedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            commonFun.showToast_resId(Activity_HouseholdDeliverables.this, v);
            showDeliverableEditDlg(v);
        }
    };

}
