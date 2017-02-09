package com.kjs.skywalk.app_android.Apartment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.kjs.skywalk.app_android.R;
import com.kjs.skywalk.app_android.commonFun;
import com.kjs.skywalk.app_android.kjsLogUtil;
import com.kjs.skywalk.control.SliderView;

import java.util.ArrayList;

//http://www.cnblogs.com/yangqiangyu/p/5143590.html
//https://github.com/singwhatiwanna/banner/tree/master/app/src/main
public class Activity_ApartmentDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__apartment_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SliderView sView = (SliderView) findViewById(R.id.sv_view);
        ArrayList<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");

        sView.setImages(list, mSvListener);
    }

    private SliderView.SliderViewListener mSvListener = new SliderView.SliderViewListener() {
        @Override
        public void onImageClick(int pos, View view) {
            commonFun.showToast_resIag(Activity_ApartmentDetail.this, view);
        }

        @Override
        public void onImageDisplay(final String imgUrl, final ImageView imageView) {

            String tag = (String) imageView.getTag();
            int resId = getResources().getIdentifier("decorate" + tag, "drawable", getPackageName());
            kjsLogUtil.i(String.format("tag is %s, resId is %#x", tag, resId));

            imageView.setImageResource(resId);



//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    final Bitmap bitmap = commonFun.loadImageFromUrl(imgUrl);
//                    if (bitmap != null) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                imageView.setImageBitmap(bitmap);
//                            }
//                        });
//                    }
//                }
//            }).start();
        }
    };
}
