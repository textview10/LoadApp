package com.instahela.deni.mkopo.ui.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.blankj.utilcode.util.BarUtils;
import com.instahela.deni.mkopo.R;
import com.instahela.deni.mkopo.base.BaseActivity;
import com.instahela.deni.mkopo.base.BaseFragment;
import com.instahela.deni.mkopo.ui.signin.SignInActivity;

public class SettingActivity extends BaseActivity {

    public static final String KEY_RESULT_TYPE = "key_result_type";
    public static final int TYPE_MY_LOAN = 111;
    public static final int TYPE_MY_INFORMATION = 112;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setStatusBarColor(this, getResources().getColor(R.color.bg_color));
        setContentView(R.layout.activity_setting);
        initializeView();
    }

    private void initializeView() {
        initTitle();
        setTitle(getString(R.string.setting_menu));
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressedInternal();
            }
        });
        MenuFragment menuFragment = new MenuFragment();
        addFragment(menuFragment, "menu");
    }


    public void toLoginActivity(){
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        onBackPressedInternal();
    }

    private void onBackPressedInternal() {
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount > 1) {
            getSupportFragmentManager().popBackStackImmediate();
        } else {
            finish();
        }
    }

    @Override
    protected int getFragmentContainerRes() {
        return R.id.fl_setting_container;
    }
}
