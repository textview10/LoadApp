package com.instahela.deni.mkopo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.BarUtils;
import com.instahela.deni.mkopo.R;
import com.instahela.deni.mkopo.base.BaseActivity;
import com.instahela.deni.mkopo.ui.signin.SignInActivity;
import com.instahela.deni.mkopo.ui.signup.SignUpActivity;

public class WelcomeActivity extends BaseActivity {

    private TextView tvSignIn;
    private TextView tvSignUp;
    private TextView tvVersion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setStatusBarColor(this, getResources().getColor(R.color.white));
        setContentView(R.layout.activity_welcome);
        initializeView();
    }

    private void initializeView() {
        tvSignIn = findViewById(R.id.tv_welcome_sign_in);
        tvSignUp = findViewById(R.id.tv_welcome_sign_up);
        tvVersion = findViewById(R.id.tv_welcome_version);

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        tvVersion.setText(AppUtils.getAppVersionName());
    }
}
