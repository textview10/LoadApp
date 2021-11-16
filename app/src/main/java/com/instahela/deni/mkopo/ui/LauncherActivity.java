package com.instahela.deni.mkopo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.instahela.deni.mkopo.R;
import com.instahela.deni.mkopo.api.Api;
import com.instahela.deni.mkopo.base.BaseActivity;
import com.instahela.deni.mkopo.bean.BaseResponseBean;
import com.instahela.deni.mkopo.bean.main.LoanDetailResponse;
import com.instahela.deni.mkopo.global.Constant;
import com.instahela.deni.mkopo.ui.guide.GuideActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.Response;

public class LauncherActivity extends BaseActivity {

    private static final String KEY_GUIDE = "key_guide";

    private static final String TAG = "LauncherActivity";

    private static final int TO_WELCOME_PAGE = 111;

    private static final int TO_MAIN_PAGE = 112;
    private long requestTime;

    private Handler mHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what){
                case TO_WELCOME_PAGE:
                    if (mHandler != null){
                        mHandler.removeCallbacksAndMessages(null);
                    }
                    OkGo.getInstance().cancelTag(TAG);
                    Intent welcomeIntent = new Intent(LauncherActivity.this,  WelcomeActivity.class);
                    startActivity(welcomeIntent);
                    finish();
                    break;
                case TO_MAIN_PAGE:
                    Intent mainIntent = new Intent(LauncherActivity.this,  MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setStatusBarColor(this, getResources().getColor(R.color.white));
        setContentView(R.layout.activity_launcher);
//        boolean openGuide = SPUtils.getInstance().getBoolean(KEY_GUIDE, true);
//        boolean openGuide = false;
//        if (openGuide) {
//            SPUtils.getInstance().put(KEY_GUIDE, false);
//        }
        String accountId = SPUtils.getInstance().getString(Constant.KEY_SP_ACCOUNT_ID);
        String phoneNum = SPUtils.getInstance().getString(Constant.KEY_SP_PHONE_NUM);
        String token = SPUtils.getInstance().getString(Constant.KEY_SP_TOKEN);
        if (TextUtils.isEmpty(accountId) || TextUtils.isEmpty(phoneNum) || TextUtils.isEmpty(token)){
            if (mHandler != null){
                mHandler.sendEmptyMessageDelayed(TO_WELCOME_PAGE, 1000);
            }
        } else {
            Constant.mAccountId = accountId;
            Constant.mPhoneNum = phoneNum;
            Constant.mToken = token;
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.put("token", Constant.mToken);
            OkGo.getInstance().addCommonHeaders(httpHeaders);
            requestDetail();
            if (mHandler != null){
                mHandler.sendEmptyMessageDelayed(TO_WELCOME_PAGE, 3000);
            }
        }
    }

    private void requestDetail() {
        requestTime = System.currentTimeMillis();
        OkGo.<String>post(Api.LOAN_DETAIL_URL).tag(TAG).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                if (isFinishing() || isDestroyed()){
                    return;
                }
                boolean successEnter = false;
                String body = response.body();
                if (!TextUtils.isEmpty(body)) {
                    BaseResponseBean responseBean = gson.fromJson(body, BaseResponseBean.class);
                    if (responseBean != null && responseBean.isRequestSuccess() && !TextUtils.isEmpty(responseBean.getBodyStr())) {
                        LoanDetailResponse loanDetail = gson.fromJson(responseBean.getBodyStr(), LoanDetailResponse.class);
                        if (loanDetail != null && (loanDetail.isCanApply() || !TextUtils.isEmpty(loanDetail.getStatus()))) {
                            Constant.mLoginDetail = loanDetail;
                            Constant.mToken = Constant.mLoginDetail.getToken();
                            successEnter = true;
                        }
                    }
                }
                if (mHandler != null){
                    mHandler.sendEmptyMessage(successEnter ? TO_MAIN_PAGE : TO_WELCOME_PAGE);
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                if (isFinishing() || isDestroyed()){
                    return;
                }
                if (mHandler != null){
                    mHandler.sendEmptyMessage(TO_WELCOME_PAGE);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (mHandler != null){
            mHandler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }
}
