package com.instahela.deni.mkopo.ui.signup;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.instahela.deni.mkopo.BuildConfig;
import com.instahela.deni.mkopo.R;
import com.instahela.deni.mkopo.api.Api;
import com.instahela.deni.mkopo.base.BaseFragment;
import com.instahela.deni.mkopo.bean.CheckPhoneNumResponse;
import com.instahela.deni.mkopo.presenter.PhoneNumPresenter;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

public class VerificationFragment extends BaseFragment {

    private static final String TAG = "VerificationFragment";

    private static final boolean DEBUG_FLAG = true;

    private EditText etPhoneNum, etVerificationCode;
    private TextView tvVerify;

    private TextView tvNext;
    private Spinner mSpinner;

    private PhoneNumPresenter mPhoneNumPresenter;

    private String phoneNum;

    private static final int MAX_TIME = 60;
    private static final int TYPE_UPDATE_TIME = 1111;
    private boolean isTimer = false;    //正在倒计时
    private int time = 0;

    private Handler mHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case TYPE_UPDATE_TIME:
                    isTimer = true;
                    time--;
                    if (time <= 0){
                        isTimer = false;
                        time = 0;
                        endTimer();
                        break;
                    }
                    if (mHandler != null) {
                        mHandler.removeMessages(TYPE_UPDATE_TIME);
                        mHandler.sendEmptyMessageDelayed(TYPE_UPDATE_TIME, 1000);
                    }
                    if (tvVerify != null){
                        tvVerify.setText(String.valueOf(time));
                    }
                    break;
            }
            return false;
        }
    });

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_verification, container, false);
        initializeView(view);
        initializeData();
        return view;
    }

    private void initializeView(View view) {
        etPhoneNum = view.findViewById(R.id.et_verification_phone_num);
        etVerificationCode = view.findViewById(R.id.et_verification_code);

        tvVerify = view.findViewById(R.id.tv_verification_phone_num);

        tvNext = view.findViewById(R.id.tv_verification_next);
        mSpinner = view.findViewById(R.id.spinner_signup_verification);

        tvVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSpinner == null){
                    return;
                }
                if( isTimer){
                    ToastUtils.showShort("please click later");
                    return;
                }
                phoneNum = etPhoneNum.getText().toString();
                if (TextUtils.isEmpty(phoneNum)){
                    ToastUtils.showShort(" phone num is null");
                    etPhoneNum.setSelection(0);
                    return;
                }
                // 0720660270
                String prefix = mPhoneNumPresenter.getSelectString(mSpinner.getSelectedItemPosition());
                phoneNum = prefix + phoneNum;

                checkPhoneNum(phoneNum);
            }
        });
        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etVerificationCode == null){
                    return;
                }
                String verification = etVerificationCode.getText().toString();
                if (TextUtils.isEmpty(verification)){
                    ToastUtils.showShort(" verification is null.");
                    return;
                }
                requestCheckSmsNumAndCodeUrl(phoneNum, verification);
            }
        });
    }
    private boolean isTest(){
        return BuildConfig.DEBUG && TextUtils.equals(phoneNum, "888888888");
    }

    private void initializeData() {
        mPhoneNumPresenter = new PhoneNumPresenter(getContext());
        mPhoneNumPresenter.initSpinner(mSpinner);

        if (DEBUG_FLAG && BuildConfig.DEBUG){
            String debugPhoneNum = "888888888";
            etPhoneNum.setText(debugPhoneNum);
            etPhoneNum.setSelection(debugPhoneNum.length() - 1);
        }
    }

    private void requestPhoneNumSms(String phoneNum) {
        OkGo.<String>post(Api.SEND_SMS_URL).tag(TAG).
                params("captchaType", "sms").
                params("mobile", phoneNum).execute(new StringCallback() {

            @Override
            public void onSuccess(Response<String> response) {
                String body = checkResponseSuccess(response);
                if (TextUtils.isEmpty(body)){
                    return;
                }
                if (isTest() && etVerificationCode != null){
                    ToastUtils.showShort("debug状态下自动填充验证码");
                    String tempMsg = "999999";
                    etVerificationCode.setText(tempMsg);
                    etVerificationCode.setSelection(tempMsg.length());
                    etVerificationCode.requestFocus();
                }
                startTimer();
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                ToastUtils.showShort("net error");
                Log.e(TAG, "on error ...");
            }
        });
    }


    private void checkPhoneNum(String mobilePhone){
        OkGo.<String>post(Api.CHECK_PHONE_NUM_URL).
                params("mobile",mobilePhone).tag(TAG).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
               CheckPhoneNumResponse checkBean =  checkResponseSuccess(response, CheckPhoneNumResponse.class);
               if (checkBean == null){
                   return;
               }
               if (checkBean.isHasRegisted()){
                   ToastUtils.showShort(" phone num has registed");
                   return;
               }
                requestPhoneNumSms(phoneNum);
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                ToastUtils.showShort("net error");
            }
        });
    }

    //检查手机号和验证码
    private void requestCheckSmsNumAndCodeUrl(String phoneNum, String smsCode){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("captchaCode", smsCode);
            jsonObject.put("mobile", phoneNum);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<String>post(Api.CHECK_SMS_VERIFICATION).tag(TAG).upJson(jsonObject).execute(new StringCallback() {

            @Override
            public void onSuccess(Response<String> response) {
                String body = checkResponseSuccess(response);
                if (TextUtils.isEmpty(body)){
                Log.e(TAG, response.body().toString());
                    return;
                }
                ToastUtils.showShort(" sms verification success");
                if (getActivity() instanceof SignUpActivity){
                    ((SignUpActivity) getActivity()).toSetPasswordFragment(phoneNum);
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                Log.e(TAG, "on error ...");
                ToastUtils.showShort("net error");
            }
        });
    }

    @Override
    public void onDestroy() {
        if (mHandler != null){
            mHandler.removeCallbacksAndMessages(null);
        }
        OkGo.getInstance().cancelTag(TAG);
        super.onDestroy();
    }

    private void startTimer() {
        time = MAX_TIME;
        if (mHandler != null){
            mHandler.sendEmptyMessage(TYPE_UPDATE_TIME);
        }
        if (tvVerify != null){
            tvVerify.setBackgroundResource(R.drawable.disable_gray_corner_bg_select);
        }
    }

    private void endTimer() {
        if (isRemoving() || isDetached()){
            return;
        }
        if (tvVerify != null){
            tvVerify.setText(getString(R.string.sign_up_get));
            tvVerify.setBackgroundResource(R.drawable.yellow_corner_bg_select);
        }
    }

}
