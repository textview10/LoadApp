package com.instahela.deni.mkopo.ui.signup;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.blankj.utilcode.util.BarUtils;
import com.instahela.deni.mkopo.R;
import com.instahela.deni.mkopo.base.BaseActivity;
import com.instahela.deni.mkopo.base.BaseFragment;

public class SignUpActivity extends BaseActivity {

    private static final int TYPE_VERIFICATION = 111;

    private FrameLayout flContainer;
    private TextView tvTitle;
    private ImageView ivBack;
    private ViewGroup flTopContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setStatusBarColor(this, getResources().getColor(R.color.bg_color));
        setContentView(R.layout.activity_sign_up);
        initialView();
        initialFragment(TYPE_VERIFICATION);
    }

    private void initialView() {
        flContainer = findViewById(R.id.fl_signup_container);
        tvTitle = findViewById(R.id.tv_sign_up_title);
        ivBack = findViewById(R.id.iv_sign_up_back);
        flTopContainer = findViewById(R.id.fl_container_sign_up);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishInternal();
            }
        });
    }

    private void initialFragment(int type) {
       AgreeTermFragment agreeTermFragment = new AgreeTermFragment();
       toFragment(agreeTermFragment);
    }

    public void toVerificationFragment(){
        VerificationFragment fragment = new VerificationFragment();
        toFragment(fragment);
    }

    public void toSetPasswordFragment(String phoneNum) {
        if (tvTitle != null){
            tvTitle.setText(R.string.sign_up_set_password_title);
        }
        SetPassWordFragment fragment = new SetPassWordFragment();
        fragment.setPhoneNum(phoneNum);
        toFragment(fragment);
    }

    public void showOrHideTopContainer(boolean showFlag){
        if (flTopContainer != null){
            flTopContainer.setVisibility(showFlag ? View.VISIBLE : View.GONE);
        }
    }

    private void finishInternal() {
        finish();
    }

    @Override
    protected int getFragmentContainerRes() {
        return R.id.fl_signup_container;
    }
}
