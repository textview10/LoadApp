package com.instahela.deni.mkopo.ui.signup;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.instahela.deni.mkopo.R;
import com.instahela.deni.mkopo.api.Api;
import com.instahela.deni.mkopo.base.BaseActivity;
import com.instahela.deni.mkopo.base.BaseFragment;
import com.instahela.deni.mkopo.bean.BaseResponseBean;
import com.instahela.deni.mkopo.bean.SignInResponseBean;
import com.instahela.deni.mkopo.global.Constant;
import com.instahela.deni.mkopo.ui.MainActivity;
import com.instahela.deni.mkopo.ui.signin.SignInFragment;
import com.instahela.deni.mkopo.widget.EditTextContainer;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.Response;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

public class SetPassWordFragment extends BaseFragment {
    private static final String TAG = "SetPassWordFragment";

    private EditTextContainer etCreatePassCode;
    private EditTextContainer etSetPassword;

    private TextView tvSetPassword;

    private String mPhoneNum;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setpassword, container, false);
        initializeView(view);
        initializeData(view);
        return view;
    }

    private void initializeView(View view) {
        etCreatePassCode = view.findViewById(R.id.edit_text_create_pass_code);
        etSetPassword = view.findViewById(R.id.edit_text_repeat_pass_code);
        tvSetPassword = view.findViewById(R.id.tv_set_password_next);
    }

    private void initializeData(View view) {
        etCreatePassCode.setPassWordMode(true);
        etSetPassword.setPassWordMode(true);
        tvSetPassword.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                String password1 = etCreatePassCode.getText().toString();
                String password2 = etSetPassword.getText().toString();
                if (TextUtils.isEmpty(password1) ) {
                    return;
                }
                if (TextUtils.isEmpty(password2) ) {
                    return;
                }
                if (!TextUtils.equals(password1, password2)){
                    ToastUtils.showShort(" two passwords not equal");
                    etSetPassword.setEditTextAndSelection("");
                    return;
                }
                boolean hasPermission = PermissionUtils.isGranted(Manifest.permission.READ_PHONE_STATE);
                if (hasPermission) {
                    requestRegisterUrl(mPhoneNum, password2, PhoneUtils.getIMEI(), "",
                            PhoneUtils.getDeviceId(), 0, ""
                            , "", "",
                            String.valueOf(AppUtils.getAppVersionCode()), "");
                } else {
                    PermissionUtils.permission(Manifest.permission.READ_PHONE_STATE).callback(new PermissionUtils.SimpleCallback() {
                        @Override
                        public void onGranted() {
                            requestRegisterUrl(mPhoneNum, password2, PhoneUtils.getIMEI(), "",
                                    PhoneUtils.getDeviceId(), 0, ""
                                    , "", "",
                                    String.valueOf(AppUtils.getAppVersionCode()), "");
                        }

                        @Override
                        public void onDenied() {
                            requestRegisterUrl(mPhoneNum, password2, "", "",
                                    "", 0, ""
                                    , "", "",
                                    String.valueOf(AppUtils.getAppVersionCode()), "");
                        }
                    }).request();
                }
            }
        });

    }

    public void setPhoneNum(String phoneNum) {
        mPhoneNum = phoneNum;
    }

    /**
     * @param phoneNum
     * @param passWord
     * @param imei             手机序列号
     * @param getuicid         个推cid
     * @param deviceId         设备ID
     * @param platform         平台: 0 android
     * @param channel          注册渠道
     * @param utmSource        广告来源
     * @param utmMedium        广告渠道商
     * @param appVersion       app版本号
     * @param innerVersionCode 内部版本号
     */
    private void requestRegisterUrl(String phoneNum, String passWord, String imei, String getuicid, String deviceId
            , int platform, String channel, String utmSource, String utmMedium, String appVersion, String innerVersionCode) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", phoneNum);
            jsonObject.put("password", passWord);
            jsonObject.put("imei", imei);
            jsonObject.put("getuicid", getuicid);
            jsonObject.put("deviceId", deviceId);
            jsonObject.put("platform", platform);
            jsonObject.put("channel", channel);
            jsonObject.put("utmSource", utmSource);
            jsonObject.put("utmMedium", utmMedium);
            jsonObject.put("appVersion", appVersion);
            jsonObject.put("innerVersionCode", innerVersionCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<String>post(Api.REGISTER_URL).tag(TAG).upJson(jsonObject).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                SignInResponseBean signInResponseBean = checkResponseSuccess(response, SignInResponseBean.class);
                if (signInResponseBean == null) {
                    return;
                }
                registerSuccessAndSignIn(signInResponseBean, phoneNum, passWord);
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                Log.e(TAG, "on error ...");
            }
        });

    }

    private void registerSuccessAndSignIn(SignInResponseBean signInResponseBean, String phoneNum, String password) {
        if (isDetached()){
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", phoneNum);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<String>post(Api.SIGN_IN_URL).tag(TAG).upJson(jsonObject).execute(new StringCallback() {

            @Override
            public void onSuccess(Response<String> response) {
                String body = response.body();
                if (TextUtils.isEmpty(body)) {
                    return;
                }
                Gson gson = new Gson();
                BaseResponseBean baseResponseBean = gson.fromJson(body, BaseResponseBean.class);
                String errorMsg = "sign in failure.";
                if (baseResponseBean == null || !baseResponseBean.isRequestSuccess()) {
                    if (baseResponseBean != null && baseResponseBean.getHead() != null &&
                            !TextUtils.isEmpty(baseResponseBean.getHead().getMsg())) {
                        errorMsg = baseResponseBean.getHead().getMsg();
                    }
                    ToastUtils.showShort(errorMsg);
                    return;
                }
                ToastUtils.showShort("register success and sign in");
                SPUtils.getInstance().put(SignInFragment.KEY_PHONE_NUM, phoneNum);
                SPUtils.getInstance().put(SignInFragment.KEY_PASS_CODE, password);
                SignInResponseBean signInResponseBean = gson.fromJson(baseResponseBean.getBody().toString(), SignInResponseBean.class);
                toMainActivity(signInResponseBean);
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                ToastUtils.showShort("register success , but sign in failure.");
                SignInFragment signInFragment = new SignInFragment();
                BaseActivity baseActivity = (BaseActivity) getActivity();
                baseActivity.toFragment(signInFragment);
                Log.e(TAG, "on error ...");
            }
        });
    }

    private void toMainActivity(SignInResponseBean signInResponseBean) {
        Constant.mAccountId = signInResponseBean.getAccountId();
        Constant.mPhoneNum = signInResponseBean.getMobile();
        Constant.mToken = signInResponseBean.getToken();

        SPUtils.getInstance().put(Constant.KEY_SP_ACCOUNT_ID, Constant.mAccountId);
        SPUtils.getInstance().put(Constant.KEY_SP_PHONE_NUM, Constant.mPhoneNum);
        SPUtils.getInstance().put(Constant.KEY_SP_TOKEN, Constant.mToken);
        ToastUtils.showShort("login success.");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.put("token", Constant.mToken);
        OkGo.getInstance().addCommonHeaders(httpHeaders);

        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onDestroy() {
        OkGo.getInstance().cancelTag(TAG);
        super.onDestroy();
    }
}
