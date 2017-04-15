package com.kjs.skywalk.app_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class Activity_HouseholdAppliances extends AppCompatActivity {

    // test data
    private final static ArrayList<listitem_adapter_household_appliance.ApplianceItem> mHouseAppliances = new ArrayList<listitem_adapter_household_appliance.ApplianceItem>(
            Arrays.asList(
                    new listitem_adapter_household_appliance.ApplianceItem(R.drawable.deliverable_damenyaoshi, "家具：沙发+茶几", "咖啡色真皮沙发2个，茶几1个，靠垫4个",1),
                    new listitem_adapter_household_appliance.ApplianceItem(R.drawable.deliverable_menjinka, "电器：电视机", "康佳29寸纯屏", 1),
                    new listitem_adapter_household_appliance.ApplianceItem(R.drawable.deliverable_shuidianka, "厨具：灶具/油烟机", "林内灶具x1 美帝DT310油烟机", 1)
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
        lvHouseApp.setAdapter(adapter);
    }

    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.tv_apartment_name:
            {
                finish();
            }
            break;
        }
    }
}
