package com.instahela.deni.mkopo.ui.guide;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.instahela.deni.mkopo.R;
import com.instahela.deni.mkopo.ui.WelcomeActivity;
import com.instahela.deni.mkopo.base.BaseActivity;

public class GuideActivity extends BaseActivity {

    private static final String TAG = "GuideActivity";

    private ViewPager mViewPager;
    private TextView tvPreviousPage, tvNextPage, tvBegin;
    private FrameLayout flPointContent;
    private View indicateView;
    private int dp10;
    private int currentPos = 0;

    private static final int START_ANIM_POS_0 = 111;    //开始第0个位置的动画.....
    private static final int UPDATE_POINT_LOCATION = 112;   //更新点的位置

    private Handler mHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case START_ANIM_POS_0:
//                    mImageView = findImageViewByPos(0);
                    break;
                case UPDATE_POINT_LOCATION:
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) indicateView.getLayoutParams();
                    int leftMargin = msg.arg1;
                    layoutParams.leftMargin = leftMargin;
                    indicateView.setLayoutParams(layoutParams);
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setStatusBarColor(this, getResources().getColor(R.color.white));
        setContentView(R.layout.activity_guide);
        initializeView();
        initializeListener();
        initializePoint();
    }

    private void initializeView() {
        mViewPager = findViewById(R.id.view_pager_splash);
        tvPreviousPage = findViewById(R.id.tv_splash_previous_step);
        tvNextPage = findViewById(R.id.tv_splash_next_step);
        flPointContent = findViewById(R.id.ll_splash_indicate_point);
        tvBegin = findViewById(R.id.tv_splash_begin);

        dp10 = ConvertUtils.dp2px(10);
        GuideAdapter mAdapter = new GuideAdapter();
        mViewPager.setAdapter(mAdapter);
        mHandler.sendEmptyMessageDelayed(START_ANIM_POS_0, 1000);   //延时1.0s开始第一个界面的动画
    }

    private void initializeListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int pos, float percent, int i1) {
                mHandler.removeMessages(UPDATE_POINT_LOCATION);
                Message message = new Message();
                message.what = UPDATE_POINT_LOCATION;
                message.arg1 = dp10 + (int) ((dp10 * 3 * pos) + (dp10 * 3 * percent));
                mHandler.sendMessage(message);
            }

            @Override
            public void onPageSelected(int pos) {
                mHandler.removeMessages(START_ANIM_POS_0);  //切换时取消第一个界面的动画.
                currentPos = pos;
                switch (pos) {
                    case 0:
                        tvBegin.setVisibility(View.INVISIBLE);
                        tvPreviousPage.setVisibility(View.INVISIBLE);
                        tvNextPage.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        tvBegin.setVisibility(View.INVISIBLE);
                        tvPreviousPage.setVisibility(View.VISIBLE);
                        tvNextPage.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        tvBegin.setVisibility(View.VISIBLE);
                        tvPreviousPage.setVisibility(View.VISIBLE);
                        tvNextPage.setVisibility(View.INVISIBLE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        tvPreviousPage.setOnClickListener(v -> {
            if (currentPos - 1 < 0) {
                return;
            }
            mViewPager.setCurrentItem(currentPos - 1, true);
        });
        tvNextPage.setOnClickListener(v -> {
            if (currentPos + 1 > 2) {
                return;
            }
            mViewPager.setCurrentItem(currentPos + 1, true);
        });
        tvBegin.setOnClickListener(v -> {
            startActivity(new Intent(GuideActivity.this, WelcomeActivity.class));
            finish();
        });
    }

    //初始化指引点
    private void initializePoint() {
        for (int i = 0; i < 3; i++) {
            View view = new View(this);
            view.setBackgroundResource(R.drawable.shape_circle_grey);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(dp10, dp10);
            layoutParams.leftMargin = dp10 + (i * dp10 * 3);
            layoutParams.rightMargin = dp10;
            flPointContent.addView(view, layoutParams);
        }

        indicateView = new View(this);
        indicateView.setBackgroundResource(R.drawable.shape_circle_black);
        FrameLayout.LayoutParams indicateParams = new FrameLayout.LayoutParams(dp10, dp10);
        indicateParams.leftMargin = dp10;
        indicateParams.rightMargin = dp10;
        flPointContent.addView(indicateView, indicateParams);
    }

    @Override
    protected void onDestroy() {
        if (mHandler != null) {
            mHandler.removeMessages(START_ANIM_POS_0);
            mHandler.removeMessages(UPDATE_POINT_LOCATION);
        }
        super.onDestroy();
    }
}
