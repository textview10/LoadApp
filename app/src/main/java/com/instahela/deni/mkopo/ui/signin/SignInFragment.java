package com.instahela.deni.mkopo.ui.signin;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.instahela.deni.mkopo.BuildConfig;
import com.instahela.deni.mkopo.R;
import com.instahela.deni.mkopo.api.Api;
import com.instahela.deni.mkopo.base.BaseActivity;
import com.instahela.deni.mkopo.base.BaseFragment;
import com.instahela.deni.mkopo.bean.BaseResponseBean;
import com.instahela.deni.mkopo.bean.SignInResponseBean;
import com.instahela.deni.mkopo.global.Constant;
import com.instahela.deni.mkopo.presenter.PhoneNumPresenter;
import com.instahela.deni.mkopo.ui.MainActivity;
import com.instahela.deni.mkopo.ui.signup.SetPassWordFragment;
import com.instahela.deni.mkopo.widget.EditTextContainer;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.Response;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

public class SignInFragment extends BaseFragment {
    private static final String TAG = "SignInFragment";

    private EditText etPhoneNum;
    private EditTextContainer etPassCode;
    private TextView tvNext;
    private Spinner spinner;
    private ImageView ivPasswordShow;

    private boolean passwordMode;

    public static final String KEY_PHONE_NUM = "key_sign_in_phone_num";
    public static final String KEY_PASS_CODE = "key_sign_in_pass_code";

    private PhoneNumPresenter mPresenter;
    private ProgressBar pbLoading;


    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        initializeView(view);
        initialData();
        return view;
    }

    private void initializeView(View view) {
        etPhoneNum = view.findViewById(R.id.et_signin_phone_num);
        etPassCode = view.findViewById(R.id.et_sign_in_passcode);
        tvNext = view.findViewById(R.id.tv_sign_in_next);
        spinner = view.findViewById(R.id.spinner_signin);
        pbLoading = view.findViewById(R.id.pb_sign_in);
        ivPasswordShow = view.findViewById(R.id.iv_sign_in_password_show);
        TextView tvResetCode = view.findViewById(R.id.tv_sign_in_reset_code);

        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strPhoneNum = etPhoneNum.getText().toString();
                String strPassCode = etPassCode.getText().toString();
                requestSignIn(strPhoneNum, strPassCode);
            }
        });
        ivPasswordShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordMode = !passwordMode;
                if (etPassCode != null) {
                    etPassCode.setPassWordMode(passwordMode);
                }
                if (ivPasswordShow != null) {
                    ivPasswordShow.setImageResource(passwordMode ? R.drawable.ic_password_off
                            : R.drawable.ic_password_on);
                }
            }
        });
        tvResetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPhoneNum == null) {
                    return;
                }
                String phoneNum = etPhoneNum.getText().toString();
                if (TextUtils.isEmpty(phoneNum)) {
                    ToastUtils.showShort("phone num is null");
                    return;
                }
                SetPassWordFragment fragment = new SetPassWordFragment();
                fragment.setPhoneNum(phoneNum);
                BaseActivity baseActivity = (BaseActivity) getActivity();
                baseActivity.toFragment(fragment);
            }
        });

        mPresenter = new PhoneNumPresenter(getContext());
        mPresenter.initSpinner(spinner);
        passwordMode = true;
        etPassCode.setPassWordMode(passwordMode);
    }


    private void initialData() {
        String phoneNum = SPUtils.getInstance().getString(KEY_PHONE_NUM);
        String passCode = SPUtils.getInstance().getString(KEY_PASS_CODE);
//        if (BuildConfig.DEBUG && TextUtils.isEmpty(phoneNum)) {
//            phoneNum = "888888888";
//        }
//        if (BuildConfig.DEBUG && TextUtils.isEmpty(passCode)) {
//            passCode = "1234";
//        }
        if (!TextUtils.isEmpty(phoneNum)) {
            etPhoneNum.setText(phoneNum);
            etPhoneNum.setSelection(phoneNum.length() - 1);
        }
        if (!TextUtils.isEmpty(passCode)) {
            etPassCode.setEditTextAndSelection(passCode);
        }
    }

    private void requestSignIn(String phoneNum, String passCode) {
        if (pbLoading != null){
            pbLoading.setVisibility(View.VISIBLE);
        }
        String prefix = mPresenter.getSelectString(spinner.getSelectedItemPosition());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", prefix + phoneNum);
            jsonObject.put("password", passCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<String>post(Api.SIGN_IN_URL).tag(TAG).upJson(jsonObject).execute(new StringCallback() {

            @Override
            public void onSuccess(Response<String> response) {
                if (pbLoading != null){
                    pbLoading.setVisibility(View.GONE);
                }
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
                SPUtils.getInstance().put(KEY_PHONE_NUM, phoneNum);
                SPUtils.getInstance().put(KEY_PASS_CODE, passCode);
                SignInResponseBean signInResponseBean = gson.fromJson(baseResponseBean.getBody().toString(), SignInResponseBean.class);
                toMainActivity(signInResponseBean);
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                if (pbLoading != null){
                    pbLoading.setVisibility(View.GONE);
                }
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
