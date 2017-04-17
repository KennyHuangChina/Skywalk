package com.kjs.skywalk.app_android;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class Activity_HouseholdAppliances extends AppCompatActivity {

    // test data
    private final static ArrayList<listitem_adapter_household_appliance.ApplianceItem> mHouseAppliances = new ArrayList<listitem_adapter_household_appliance.ApplianceItem>(
            Arrays.asList(
                    new listitem_adapter_household_appliance.ApplianceItem(R.drawable.sofa_n, "家具：沙发+茶几", "咖啡色真皮沙发2个，茶几1个，靠垫4个",1),
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
        listitem_adapter_household_appliance adapter = new listitem_adapter_household_appliance(this);
        adapter.updateApplianceItemList(mHouseAppliances);
        lvHouseApp.setAdapter(adapter);
        lvHouseApp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showApplianceEdtDlg(i);
            }
        });
    }

    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.tv_apartment_name:
            {
                finish();
            }
            break;
            case R.id.ib_new:
            {
                finish();
            }
            break;
        }
    }

    private AlertDialog mApplianceNewDlg;
    private AlertDialog mApplianceEdtDlg;
    private void showApplianceEdtDlg(int appl_item_index) {
        listitem_adapter_household_appliance.ApplianceItem applItem = mHouseAppliances.get(appl_item_index);

        if (mApplianceEdtDlg == null) {
            mApplianceEdtDlg = new AlertDialog.Builder(this).create();
        }
        mApplianceEdtDlg.show();
        mApplianceEdtDlg.setContentView(R.layout.dialog_appliances_editor);

        TextView tvName = (TextView) mApplianceEdtDlg.findViewById(R.id.tv_appl_name);
        tvName.setText(applItem.mName);
        Drawable drawable = getResources().getDrawable(applItem.mIcon, null);
        tvName.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);

        // tod0
    }
}
