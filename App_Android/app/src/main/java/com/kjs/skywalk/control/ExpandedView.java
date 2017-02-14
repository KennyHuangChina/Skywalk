package com.kjs.skywalk.control;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.kjs.skywalk.app_android.R;

/**
 * Created by sailor.zhou on 2017/2/13.
 */

// http://www.cnblogs.com/JczmDeveloper/p/3782586.html
public class ExpandedView extends FrameLayout {
    private Animation mExpandAnimation;
    private Animation mCollapseAnimation;

    public ExpandedView(Context context) {
        super(context);
        initView();
    }

    public ExpandedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ExpandedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.price_analysis, this, true);
        mExpandAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.view_expand);
        mExpandAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mCollapseAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.view_collapse);
        mCollapseAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void expand() {
        if (getVisibility() != View.VISIBLE) {
            clearAnimation();
            startAnimation(mExpandAnimation);
        }
    }

    public void collapse() {
        if (getVisibility() == View.VISIBLE) {
            clearAnimation();
            startAnimation(mCollapseAnimation);
        }
    }
}
