package com.instahela.deni.mkopo.ui.signin;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.instahela.deni.mkopo.BuildConfig;
import com.instahela.deni.mkopo.ui.MainActivity;
import com.instahela.deni.mkopo.R;
import com.instahela.deni.mkopo.ui.WelcomeActivity;
import com.instahela.deni.mkopo.api.Api;
import com.instahela.deni.mkopo.base.BaseActivity;
import com.instahela.deni.mkopo.bean.BaseResponseBean;
import com.instahela.deni.mkopo.bean.SignInResponseBean;
import com.instahela.deni.mkopo.global.Constant;
import com.instahela.deni.mkopo.presenter.PhoneNumPresenter;
import com.instahela.deni.mkopo.widget.EditTextContainer;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class SignInActivity extends BaseActivity {

    private static final String TAG = "SignInActivity";
    private ImageView ivBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setStatusBarColor(this, getResources().getColor(R.color.bg_color));
        setContentView(R.layout.activity_login);
        initialView();
        initialData();
    }

    private void initialView() {
        ivBack = findViewById(R.id.iv_sigin_in_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressInternal();
            }
        });
    }

    private void initialData() {
        SignInFragment signInFragment = new SignInFragment();
        toFragment(signInFragment);
    }

    @Override
    public void onBackPressed() {
        onBackPressInternal();
    }

    private void onBackPressInternal(){
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected int getFragmentContainerRes() {
        return R.id.fl_container_sign_in;
    }

}
