package com.kjs.skywalk.app_android;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by admin on 2017/3/22.
 */

public class Activity_Search_Fangyuanliebiao extends Activity {
    private ListView mListView = null;

    private TextView mDisplayType = null;
    private AdapterFangyuanliebiao mAdapter = null;

    private static final int DISPLAY_GRID = 0;
    private static final int DISPLAY_LIST= 1;
    private int mDisplay = DISPLAY_LIST;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fangyuanliebiao);

        SearchView searchView = (SearchView)findViewById(R.id.search_view);
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) searchView.findViewById(id);
        textView.setTextSize(14);
        textView.setGravity(Gravity.BOTTOM);
        searchView.onActionViewCollapsed();

        mListView = (ListView)findViewById(R.id.listViewSearchResult);
        mAdapter = new AdapterFangyuanliebiao(this);
        mAdapter.setDisplayType(mDisplay);
        mListView.setAdapter(mAdapter);

        mDisplayType = (TextView)findViewById(R.id.textViewDisplayMode);
        mDisplayType.setOnClickListener(mClickListenerDisplayType);
    }

    private View.OnClickListener mClickListenerDisplayType = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(mDisplay == DISPLAY_LIST) {
                mDisplay = DISPLAY_GRID;
                Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.show_list);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                mDisplayType.setCompoundDrawables(drawable, null, null, null);
                mDisplayType.setText("小图模式");
            } else if(mDisplay == DISPLAY_GRID) {
                mDisplay = DISPLAY_LIST;
                Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.show_grid);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                mDisplayType.setCompoundDrawables(drawable, null, null, null);
                mDisplayType.setText("大图模式");
            }
            mAdapter.setDisplayType(mDisplay);
            mListView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
    };

    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.tv_info_title:
            {
                finish();
            }
            break;
        }
    }
}
