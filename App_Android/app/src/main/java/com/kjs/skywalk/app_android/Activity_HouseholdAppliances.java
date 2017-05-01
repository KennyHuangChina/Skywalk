package com.kjs.skywalk.app_android;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class Activity_HouseholdAppliances extends AppCompatActivity {
    listitem_adapter_household_appliance mApplAdapter;
    // test data
    private final static ArrayList<listitem_adapter_household_appliance.ApplianceItem> mHouseAppliances = new ArrayList<listitem_adapter_household_appliance.ApplianceItem>(
            Arrays.asList(
                    new listitem_adapter_household_appliance.ApplianceItem(R.drawable.sofa_n, "家具：沙发+茶几", "咖啡色真皮沙发2个，茶几1个，靠垫4个", 1),
                    new listitem_adapter_household_appliance.ApplianceItem(R.drawable.tv3_n, "电器：电视机", "康佳29寸纯屏", 1),
                    new listitem_adapter_household_appliance.ApplianceItem(R.drawable.tableware_n, "厨具：灶具/油烟机", "林内灶具x1 美帝DT310油烟机", 1)
            )
    );
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__household_appliances);

        SpannableString styleText = new SpannableString("世贸蝶湖湾 175栋2202室");
        int i = styleText.length();
        String s = "世贸蝶湖湾 175栋2202室";
        int l = s.length();
        styleText.setSpan(new TextAppearanceSpan(this, R.style.textstyle_large), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        styleText.setSpan(new TextAppearanceSpan(this, R.style.textstyle_small), 5, 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ((TextView) findViewById(R.id.tv_apartment_name)).setText(styleText, TextView.BufferType.SPANNABLE);

        ListView lvHouseApp = (ListView) findViewById(R.id.lvHouseholdAppliance);
        mApplAdapter = new listitem_adapter_household_appliance(this);
        mApplAdapter.updateApplianceItemList(mHouseAppliances);
        lvHouseApp.setAdapter(mApplAdapter);
        lvHouseApp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showApplianceEdtDlg(i);
            }
        });
    }

    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.tv_apartment_name: {
                finish();
            }
            break;
            case R.id.ib_new: {
                showApplianceNewDlg();
            }
            break;
        }
    }

    private AlertDialog mApplianceEdtDlg;

    private void showApplianceEdtDlg(int appl_item_index) {
        final listitem_adapter_household_appliance.ApplianceItem applItem = mHouseAppliances.get(appl_item_index);

        if (mApplianceEdtDlg == null) {
            mApplianceEdtDlg = new AlertDialog.Builder(this).create();
        }
        mApplianceEdtDlg.show();
        mApplianceEdtDlg.setContentView(R.layout.dialog_appliances_editor);

        final TextView tvName = (TextView) mApplianceEdtDlg.findViewById(R.id.tv_appl_name);
        tvName.setText(applItem.mName);
        Drawable drawable = getResources().getDrawable(applItem.mIcon, null);
        tvName.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);

        final TextView tvNum = (TextView) mApplianceEdtDlg.findViewById(R.id.tv_num);
        tvNum.setText(String.valueOf(applItem.mNum));

        final int appl_num = applItem.mNum;
        TextView tvIncrease = (TextView) mApplianceEdtDlg.findViewById(R.id.tv_increase);   // -
        tvIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(tvNum.getText().toString());
                num++;
                tvNum.setText(String.valueOf(num));
            }
        });

        TextView tvDecrease = (TextView) mApplianceEdtDlg.findViewById(R.id.tv_decrease); // +
        tvDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(tvNum.getText().toString());
                if (num == 0)
                    return;
                num--;
                tvNum.setText(String.valueOf(num));
            }
        });

        TextView tvBack = (TextView) mApplianceEdtDlg.findViewById(R.id.tv_back); // 返回
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mApplianceEdtDlg.dismiss();
            }
        });

        TextView tvConfirm = (TextView) mApplianceEdtDlg.findViewById(R.id.tv_confirm); // 修改
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(tvNum.getText().toString());
                applItem.mNum = num;
                mApplAdapter.notifyDataSetChanged();
                mApplianceEdtDlg.dismiss();
            }
        });
    }

    private AlertDialog mApplianceNewDlg;
    private void showApplianceNewDlg() {
        if (mApplianceNewDlg == null) {
            mApplianceNewDlg = new AlertDialog.Builder(this).create();
        }
        mApplianceNewDlg.show();
        mApplianceNewDlg.setContentView(R.layout.dialog_appliances_new);
    }

    // SpinnerAdapter not use now, reserve for future use
    private class SpinnerAdapter extends ArrayAdapter<String> {
        Context context;
        String[] items = new String[]{};

        public SpinnerAdapter(final Context context,
                              final int textViewResourceId, final String[] objects) {
            super(context, textViewResourceId, objects);
            this.items = objects;
            this.context = context;
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(
                        android.R.layout.simple_spinner_item, parent, false);
            }

            TextView tv = (TextView) convertView
                    .findViewById(android.R.id.text1);
            tv.setText(items[position]);
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(Color.BLUE);
            tv.setTextSize(30);
            return convertView;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(
                        android.R.layout.simple_spinner_item, parent, false);
            }

            // android.R.id.text1 is default text view in resource of the android.
            // android.R.layout.simple_spinner_item is default layout in resources of android.

            TextView tv = (TextView) convertView
                    .findViewById(android.R.id.text1);
            tv.setText(items[position]);
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(Color.BLUE);
            tv.setTextSize(30);
            return convertView;
        }
    }
}