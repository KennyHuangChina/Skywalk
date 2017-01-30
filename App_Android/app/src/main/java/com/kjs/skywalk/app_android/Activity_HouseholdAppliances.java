package com.kjs.skywalk.app_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.widget.ListView;
import android.widget.TextView;

public class Activity_HouseholdAppliances extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__household_appliances);

        SpannableString styleText = new SpannableString("世贸蝶湖湾175栋2202室");
        int i = styleText.length();
        String s = "世贸蝶湖湾175栋2202室";
        int l = s.length();
        styleText.setSpan(new TextAppearanceSpan(this, R.style.textstyle_large), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        styleText.setSpan(new TextAppearanceSpan(this, R.style.textstyle_small), 5, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ((TextView)findViewById(R.id.tv_apartment_name)).setText(styleText, TextView.BufferType.SPANNABLE);

        ListView lvHouseApp = (ListView)findViewById(R.id.lvHouseholdAppliance);
        listitem_adapter_household_appliance adapter = new listitem_adapter_household_appliance(this);
        lvHouseApp.setAdapter(adapter);
    }
}
