package com.kjs.skywalk.app_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

import me.iwf.photopicker.PhotoPicker;

public class Activity_fangyuan_renzheng_customer extends SKBaseActivity {
    int mPhotoPickerHostId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__fangyuan_renzheng_customer);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            List<String> photos = null;
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                onPhotoPickerReturn(photos);
            }
        }
    }

    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.tv_info_title: {
                finish();
            }
        }
    }

    public void onPhotoPickerClicked(View v) {
        switch (v.getId()) {
            case R.id.iv_photopicker_hpc:
            case R.id.iv_photopicker_idcard: {
                startPhotoPickerActivity(v);
            }
            break;

        }
    }

    private void startPhotoPickerActivity(View host_view) {
        mPhotoPickerHostId = host_view.getId();
        PhotoPicker.builder()
                .start(Activity_fangyuan_renzheng_customer.this);
    }

    private void onPhotoPickerReturn(List<String> photos) {
        switch (mPhotoPickerHostId) {
            case R.id.iv_photopicker_hpc:
            {
                ImageView ivZhengjian1 = (ImageView)findViewById(R.id.imageZhengjian1);
                ImageView ivZhengjian2 = (ImageView)findViewById(R.id.imageZhengjian2);
                ivZhengjian1.setVisibility(View.INVISIBLE);
                ivZhengjian2.setVisibility(View.INVISIBLE);

                int index = 0;
                for (String path : photos) {
                    if (index == 0) {
                        ivZhengjian1.setImageBitmap(commonFun.getScaleBitmapFromLocal(path, 320, 240));
                        ivZhengjian1.setVisibility(View.VISIBLE);
                    }
                    if (index == 1) {
                        ivZhengjian2.setImageBitmap(commonFun.getScaleBitmapFromLocal(path, 320, 240));
                        ivZhengjian2.setVisibility(View.VISIBLE);
                    }
                    index++;
                }

                break;
            }

            case R.id.iv_photopicker_idcard:
            {
                ImageView ivIdCard1 = (ImageView)findViewById(R.id.imageIdCard1);
                ImageView ivIdCard2 = (ImageView)findViewById(R.id.imageIdCard2);
                ivIdCard1.setVisibility(View.INVISIBLE);
                ivIdCard2.setVisibility(View.INVISIBLE);

                int index = 0;
                for (String path : photos) {
                    if (index == 0) {
                        ivIdCard1.setImageBitmap(commonFun.getScaleBitmapFromLocal(path, 320, 240));
                        ivIdCard1.setVisibility(View.VISIBLE);
                    }
                    if (index == 1) {
                        ivIdCard2.setImageBitmap(commonFun.getScaleBitmapFromLocal(path, 320, 240));
                        ivIdCard2.setVisibility(View.VISIBLE);
                    }
                    index++;
                }

                break;
            }
        }
    }
}
