package com.instahela.deni.mkopo.ui.load.loadcomfirmation;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.instahela.deni.mkopo.R;
import com.instahela.deni.mkopo.api.Api;
import com.instahela.deni.mkopo.base.BaseActivity;
import com.instahela.deni.mkopo.bean.additionalprofile.CheckBeanResponse;
import com.instahela.deni.mkopo.bean.additionalprofile.LoanApplyResponse;
import com.instahela.deni.mkopo.bean.additionalprofile.TrialResponse;
import com.instahela.deni.mkopo.bean.authupload.AuthUploadBean;
import com.instahela.deni.mkopo.data.CollectDataManager;
import com.instahela.deni.mkopo.event.RefreshEvent;
import com.instahela.deni.mkopo.global.Constant;
import com.instahela.deni.mkopo.notify.NotifyManager;
import com.instahela.deni.mkopo.ui.load.personprofile.BaseProfileFragment;
import com.instahela.deni.mkopo.ui.webview.WebViewFragment;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoadConfirmationFragment extends BaseProfileFragment {

    private static final String TAG = "LoadConfirmation";
    private RecyclerView recyclerView;

    private final ArrayList<TrialResponse.Trial> mList = new ArrayList<>();
    private LoadConfirmationAdapter mAdapter;
    private TextView tvApply;
    private ImageView checkAgree;

    private boolean isAgree = true;
    private TextView tvTerms;

    private WebViewFragment webViewFragment;

    private ViewGroup llTermContainer;
    private ProgressBar pbLoading;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_load_confirmation, container, false);
        initializeView(view);
        initializeData();
        return view;
    }

    private void initializeView(View view) {
        recyclerView = view.findViewById(R.id.rv_load_confirmation);
        checkAgree = view.findViewById(R.id.iv_load_confirmation_agree);
        tvTerms = view.findViewById(R.id.tv_load_confirmation_term);

        llTermContainer = view.findViewById(R.id.ll_term_container);
        pbLoading = view.findViewById(R.id.pv_load_confirmation);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new LoadConfirmationAdapter(mList);
        recyclerView.setAdapter(mAdapter);

        checkAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAgree = !isAgree;
                updateAgreeState();
            }
        });

        tvTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseActivity baseActivity = (BaseActivity) getActivity();
                baseActivity.setTitle(getString(R.string.load_confirmation_term_2));
                if (webViewFragment == null){
                    webViewFragment = new WebViewFragment();
                }
                webViewFragment.setUrl(Api.WEB_VIEW_POLICY);
                baseActivity.addFragment(webViewFragment, "term");
            }
        });

        tvApply = view.findViewById(R.id.tv_load_confirmation_apply);
        tvApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAgree){
                    ToastUtils.showShort(" must agree terms");
                    return;
                }
                if (checkClickFast()){
                    return;
                }
                requestCheckUrl();
            }
        });
        updateAgreeState();
    }

    private void initializeData() {
        requestTrialUrl();
    }

    private void requestTrialUrl() {
        if (pbLoading != null){
            pbLoading.setVisibility(View.VISIBLE);
        }
        if (llTermContainer != null){
            llTermContainer.setVisibility(View.GONE);
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("prodId", Constant.proId);
            jsonObject.put("loanAmount", Constant.mLoadAmount);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<String>post(Api.LOAN_PROFILE_TRIAL_URL).tag(TAG).
                params("prodId", Constant.proId).
                params("loanAmount", Constant.mLoadAmount).
                upJson(jsonObject).execute(new StringCallback() {

            @Override
            public void onSuccess(Response<String> response) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (pbLoading != null){
                            pbLoading.setVisibility(View.GONE);
                        }
                        if (llTermContainer != null){
                            llTermContainer.setVisibility(View.VISIBLE);
                        }
                    }
                }, 300);

                TrialResponse trialResponse = checkResponseSuccess(response, TrialResponse.class);
                if (trialResponse == null || trialResponse.getTrial() == null ||
                        trialResponse.getTrial().size() == 0) {
                    Log.e(TAG, "" + response.body().toString());
                    ToastUtils.showShort("request trial failure.");
                    return;
                }

                for (int i = 0; i < trialResponse.getTrial().size(); i++) {
                    TrialResponse.Trial trial = trialResponse.getTrial().get(i);
                    trial.title = String.format(getString(R.string.load_confirmation_submit_item), i + 1);
                }
                TrialResponse.Trial trial = new TrialResponse.Trial();
                trial.title = getString(R.string.load_confirmation_submit_summary);
                trial.setDisburseTime(trialResponse.getRepayDate());
                trial.setFee(String.valueOf(trialResponse.getTotalFeeAmount()));
                trial.setInterest(String.valueOf(trialResponse.getTotalInterestAmount()));
                trial.setDisburseAmount(String.valueOf(trialResponse.getTotalDisburseAmount()));
                trial.setTotalAmount(String.valueOf(trialResponse.getTotalRepaymentAmount()));
                trialResponse.getTrial().add(0, trial);

                mList.clear();
                mList.addAll(trialResponse.getTrial());
                if (mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                if (pbLoading != null){
                    pbLoading.setVisibility(View.GONE);
                }
                if (llTermContainer != null){
                    llTermContainer.setVisibility(View.VISIBLE);
                }
                Log.e(TAG, "on error ...");
            }
        });
    }

    //请求申请贷款接口
    private void requestApplyUrl(String orderId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("accountId", Constant.mAccountId);
            jsonObject.put("orderId",orderId);
            jsonObject.put("amount", Constant.mLoadAmount);
            jsonObject.put("prodId", Constant.proId);
            jsonObject.put("period", Constant.mPeriod);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<String>post(Api.LOAN_PROFILE_APPLY_URL).headers("channel", "googleplay").tag(TAG).
                upJson(jsonObject).execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e(TAG, "" + response.body());
                        if (pbLoading != null){
                            pbLoading.setVisibility(View.GONE);
                        }
                        LoanApplyResponse loanApplyResponse = checkResponseSuccess(response, LoanApplyResponse.class);
                        if (loanApplyResponse == null){
                            return;
                        }
                        if (!TextUtils.equals("1", loanApplyResponse.getStatus())){
                            ToastUtils.showShort("apply loan error. status = " + loanApplyResponse.getStatus());
                            return;
                        }
                        ToastUtils.showShort("request success.");
                        getActivity().finish();
                        EventBus.getDefault().post(new RefreshEvent());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        if (pbLoading != null){
                            pbLoading.setVisibility(View.GONE);
                        }
                    }
        });
    }

    //请求检查接口
    private void requestCheckUrl(){
        if (pbLoading != null){
            pbLoading.setVisibility(View.VISIBLE);
        }
        OkGo.<String>post(Api.LOAN_PROFILE_CHECK_URL).tag(TAG)
                .execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                CheckBeanResponse checkBeanResponse = checkResponseSuccess(response, CheckBeanResponse.class);
                if (checkBeanResponse == null){
                    showOrHideLoading(false);
                    return;
                }
                if (TextUtils.isEmpty(checkBeanResponse.getOrderId())){
                    showOrHideLoading(false);
                    return;
                }
                CollectDataManager.getInstance().collectAuthData(getContext().getApplicationContext(), checkBeanResponse.getOrderId(), new CollectDataManager.Observer() {
                    @Override
                    public void success(Response<String> response) {
                        AuthUploadBean authLoad= checkResponseSuccess(response, AuthUploadBean.class);
                        if (authLoad == null){
                            showOrHideLoading(false);
                            return;
                        }
                        if (!authLoad.isHasUpload()){
                            ToastUtils.showShort(authLoad.getFailReason());
                            showOrHideLoading(false);
                            return;
                        }
                        requestApplyUrl(checkBeanResponse.getOrderId());
                    }

                    @Override
                    public void failure(Response<String> response) {
                        showOrHideLoading(false);
                    }

                });
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                if (pbLoading != null){
                    pbLoading.setVisibility(View.GONE);
                }
            }
        });
    }

    private void updateAgreeState(){
        if (checkAgree != null){
            checkAgree.setImageResource(isAgree ? R.drawable.ic_selected : R.drawable.ic_unselected);
        }
    }

    private void showOrHideLoading(boolean showFlag){
        if (Looper.myLooper() == Looper.getMainLooper()){
            if (pbLoading != null){
                pbLoading.setVisibility(showFlag ? View.VISIBLE : View.GONE);
            }
        } else {
            if (getActivity().isFinishing() || getActivity().isDestroyed()){
                return;
            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (pbLoading != null){
                        pbLoading.setVisibility(showFlag ? View.VISIBLE : View.GONE);
                    }
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        OkGo.getInstance().cancelTag(TAG);
        super.onDestroy();
    }
}
