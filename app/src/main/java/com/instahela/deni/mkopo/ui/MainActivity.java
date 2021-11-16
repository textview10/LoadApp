package com.instahela.deni.mkopo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.instahela.deni.mkopo.R;
import com.instahela.deni.mkopo.api.Api;
import com.instahela.deni.mkopo.base.BaseActivity;
import com.instahela.deni.mkopo.bean.main.LoanDetailResponse;
import com.instahela.deni.mkopo.data.CollectDataManager;
import com.instahela.deni.mkopo.data.DataManager;
import com.instahela.deni.mkopo.data.FireBaseMgr;
import com.instahela.deni.mkopo.event.RefreshEvent;
import com.instahela.deni.mkopo.event.ToStartLoadEvent;
import com.instahela.deni.mkopo.global.Constant;
import com.instahela.deni.mkopo.notify.NotifyManager;
import com.instahela.deni.mkopo.ui.home.LoadStatus;
import com.instahela.deni.mkopo.ui.home.loadresult.LoadResultFragment;
import com.instahela.deni.mkopo.ui.home.loadstatus.LoadStatusFragment;
import com.instahela.deni.mkopo.ui.home.startload.StartLoadFragment;
import com.instahela.deni.mkopo.ui.load.personprofile.AdditionalProfileFragment;
import com.instahela.deni.mkopo.ui.load.personprofile.PersonProfileFragment;
import com.instahela.deni.mkopo.ui.setting.SettingActivity;
import com.instahela.deni.mkopo.util.CheckClickFastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    private TextView tvTitle;
    private ProgressBar pbLoading;

    private static final int REQUEST_CODE_SETTING = 112;

    private final Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setStatusBarColor(this, getResources().getColor(R.color.bg_color));
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        setContentView(R.layout.activity_main);
        initErrorPage();
        initializeView();
        initialData();

    }

    @Override
    protected void clickErrorRetry() {
        requestLoadDetail();
    }

    private void initializeView() {
        ImageView ivSetting = findViewById(R.id.iv_main_setting);
        pbLoading = findViewById(R.id.pb_main_loading);
        tvTitle = findViewById(R.id.tv_main_title);

        ivSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SETTING);
            }
        });

        Handler handler = new Handler();
        if (Constant.mLoginDetail != null ){
            CheckClickFastUtils.setRequestMillion();
            updateStatusByLoginDetail(Constant.mLoginDetail);
            Constant.mLoginDetail = null;
        } else {
            handler.postDelayed(() -> {
                requestLoadDetail();
            }, 1500);
        }
        handler.postDelayed(() -> {
            DataManager.getInstance().requestInfo();

        }, 1000);
    }

    private void initialData() {
        FireBaseMgr.getInstance().reportFcmToken();
    }

    @Override
    protected int getFragmentContainerRes() {
        return R.id.fl_main_container;
    }

    /**
     * 开始请求状态
     */
    public void requestLoadDetail() {
        if (pbLoading != null) {
            pbLoading.setVisibility(View.VISIBLE);
        }
        OkGo.<String>post(Api.LOAN_DETAIL_URL).tag(TAG).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                if (pbLoading != null) {
                    pbLoading.setVisibility(View.GONE);
                }
               hideErrorPage();
                LoanDetailResponse loanDetailResponse = checkResponseSuccess(response, LoanDetailResponse.class);
                if (loanDetailResponse == null) {
                    showErrorPage();
                    return;
                }
                CheckClickFastUtils.setRequestMillion();
                updateStatusByLoginDetail(loanDetailResponse);
//                test(loanDetailResponse.getOrderId());
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                if (pbLoading != null) {
                    pbLoading.setVisibility(View.GONE);
                }
                showErrorPage();
                Log.e(TAG, "on error ...");
            }
        });
    }

    private void updateStatusByLoginDetail(LoanDetailResponse loanDetailResponse) {
        if (loanDetailResponse.isCanApply()){   //canApply话,直接去开始借贷界面
            toStartLoanFragment();
            return;
        }
        if (TextUtils.isEmpty(loanDetailResponse.getStatus())){ // status字段为null
            showErrorPage();
            return;
        }
        switch (loanDetailResponse.getStatus()) {
            case "1":   //已申请,待审核 ;
                if (tvTitle != null) {
                    tvTitle.setText(R.string.load_result_title_repayment);
                }
                toLoadStatusFragment(loanDetailResponse.getReason(), LoadStatus.TYPE_APPLY_START, loanDetailResponse.isCanApply());
                break;
            case "2":   //审核通过,放款成功
                if (tvTitle != null) {
                    tvTitle.setText(R.string.load_result_title_repayment);
                }
                toLoadResultFragment(loanDetailResponse.getStageList(), LoadStatus.TYPE_APPLY_SUCCESS,
                        loanDetailResponse.getOrderId(), loanDetailResponse.getTotalAmount());
                if (!TextUtils.isEmpty(loanDetailResponse.getOrderId())){
                    if (!NotifyManager.hasHandleOrder(loanDetailResponse.getOrderId())){
                        NotifyManager.setHandleOrder(loanDetailResponse.getOrderId());
                        if (loanDetailResponse.getStageList() != null && loanDetailResponse.getStageList().size() > 0){
                            LoanDetailResponse.Stage stage = loanDetailResponse.getStageList().get(0);
                            NotifyManager.sendLoanSuccess(stage.getAmount(), stage.getTotalAmount(),stage.getRepayDate(),false);
                        }
                    }
                }
                break;
            case "3":   //还款成功
                if (tvTitle != null) {
                    tvTitle.setText(R.string.load_processing_success_title);
                }
                toLoadStatusFragment(loanDetailResponse.getReason(), LoadStatus.TYPE_REPAY_SUCCESS, loanDetailResponse.isCanApply());
                break;
            case "4":   //逾期
                if (tvTitle != null) {
                    tvTitle.setText(R.string.load_processing_success_title);
                }
                toLoadResultFragment(loanDetailResponse.getStageList(), LoadStatus.TYPE_REPAY_TIME_OUT,
                        loanDetailResponse.getOrderId(), loanDetailResponse.getTotalAmount());
                break;
            case "5":   //贷款被拒绝
                if (tvTitle != null) {
                    tvTitle.setText(R.string.load_result_timeout);
                }
                toLoadStatusFragment(loanDetailResponse.getReason(), LoadStatus.TYPE_APPLY_FAILURE, loanDetailResponse.isCanApply());
                if (!TextUtils.isEmpty(loanDetailResponse.getOrderId())){
                    if (!NotifyManager.hasHandleOrder(loanDetailResponse.getOrderId())){
                        NotifyManager.setHandleOrder(loanDetailResponse.getOrderId());
                        if (loanDetailResponse.getStageList() != null && loanDetailResponse.getStageList().size() > 0){
                            LoanDetailResponse.Stage stage = loanDetailResponse.getStageList().get(0);
                            NotifyManager.sendLoanFailure(stage.getAmount(), stage.getTotalAmount(),stage.getRepayDate(),false);
                        }
                    }
                }
                break;
            case "6":   //还款中
                if (tvTitle != null) {
                    tvTitle.setText(R.string.load_result_title_in_progress);
                }
                toLoadStatusFragment(loanDetailResponse.getReason(), LoadStatus.TYPE_REPAYING, loanDetailResponse.isCanApply());
                break;
        }
    }

    public void toStartLoanFragment() {  //开始贷款
        if (tvTitle != null) {
            tvTitle.setText(R.string.start_load_welcome_instahela);
        }
        StartLoadFragment fragment = new StartLoadFragment();
        toFragment(fragment);
    }

    private void toPersonProfileFragment(){
        if (tvTitle != null) {
            tvTitle.setText(R.string.load_person_profile_title);
        }
        PersonProfileFragment fragment = new PersonProfileFragment();
        toFragment(fragment);
    }

    public void toAdditionalProfile(){
        tvTitle.setText(R.string.load_additional_profile_title);
        AdditionalProfileFragment additionalProfileFragment = new AdditionalProfileFragment();
        toFragment(additionalProfileFragment);
    }

    private void toLoadResultFragment(ArrayList<LoanDetailResponse.Stage> stageList, @LoadStatus.LoanStatus String status
            , String order, float amount) {
        LoadResultFragment fragment = new LoadResultFragment();
        fragment.setLoanDetail(stageList, status, order);
        toFragment(fragment);
    }

    private void toLoadStatusFragment(String desc, @LoadStatus.LoanStatus String status, boolean canApply) {
        LoadStatusFragment fragment = new LoadStatusFragment();
        fragment.setLoanDetail(desc, status, canApply);
        toFragment(fragment);
    }

    @Override
    protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
        if (mHandler != null){
            mHandler.removeCallbacksAndMessages(null);
        }
        OkGo.getInstance().cancelTag(TAG);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE_SETTING:
                if (data == null){
                    break;
                }
                int type = data.getIntExtra(SettingActivity.KEY_RESULT_TYPE, -1);
                if (type == SettingActivity.TYPE_MY_LOAN){
                    requestLoadDetail();
                } else if (type == SettingActivity.TYPE_MY_INFORMATION){
                    toPersonProfileFragment();
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(RefreshEvent event) {
        long delayMillions = System.currentTimeMillis() - CheckClickFastUtils.clickStartLoanMillions;
        if (delayMillions >= 5000){
            if (pbLoading != null){
                pbLoading.setVisibility(View.GONE);
            }
            requestLoadDetail();
        } else {
            if (pbLoading != null) {
                pbLoading.setVisibility(View.VISIBLE);
            }
            mHandler.postDelayed(() -> {
                if (pbLoading != null){
                    pbLoading.setVisibility(View.GONE);
                }
                requestLoadDetail();
            }, 5000 - delayMillions);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = false)
    public void onEvent(ToStartLoadEvent event) {
        long delayMillions = System.currentTimeMillis() - CheckClickFastUtils.clickStartLoanMillions;
        if (delayMillions >= 5000){
            if (pbLoading != null){
                pbLoading.setVisibility(View.GONE);
            }
            toStartLoanFragment();
        } else {
            if (pbLoading != null){
                pbLoading.setVisibility(View.VISIBLE);
            }
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (pbLoading != null){
                        pbLoading.setVisibility(View.GONE);
                    }
                    toStartLoanFragment();
                }
            }, 5000 - delayMillions);
        }
    }

    @VisibleForTesting
    private void test(String orderId){
        ThreadUtils.executeByCached(new ThreadUtils.SimpleTask<Object>() {
            @Override
            public Object doInBackground() throws Throwable {
                CollectDataManager.getInstance().collectAuthData(MainActivity.this, orderId,null);
                return null;
            }

            @Override
            public void onSuccess(Object result) {

            }
        });
    }

    @Override
    public void setTitle(String title) {
        if (tvTitle != null){
            tvTitle.setText(title);
        }
    }
}