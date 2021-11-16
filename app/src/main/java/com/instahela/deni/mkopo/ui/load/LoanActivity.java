package com.instahela.deni.mkopo.ui.load;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.BarUtils;
import com.instahela.deni.mkopo.R;
import com.instahela.deni.mkopo.base.BaseActivity;
import com.instahela.deni.mkopo.global.LoanConstant;
import com.instahela.deni.mkopo.ui.load.personprofile.AdditionalProfileFragment;
import com.instahela.deni.mkopo.ui.load.loadcomfirmation.LoadConfirmationFragment;
import com.instahela.deni.mkopo.ui.load.personprofile.PersonProfileFragment;

public class LoanActivity extends BaseActivity {

    private ImageView ivLoadBack;
    private TextView tvTitle;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setStatusBarColor(this, getResources().getColor(R.color.bg_color));
        setContentView(R.layout.activity_loan);
        initializeView();
        initializeData();
    }

    private void initializeView() {
        ivLoadBack = findViewById(R.id.iv_load_back);
        tvTitle = findViewById(R.id.tv_loan_title);
        ivLoadBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressedInternal();
            }
        });
    }

    private void initializeData() {
        if (LoanConstant.mDetailProfile != null && LoanConstant.mDetailProfile.isBvnChecked()){
            toLoadConfirmation();
        } else {
            toPersonProfileFragment();
        }
    }

    @Override
    public void setTitle(String title) {
        if (tvTitle != null){
            tvTitle.setText(title);
        }
    }

    public void toPersonProfileFragment(){
        tvTitle.setText(R.string.load_person_profile_title);
        PersonProfileFragment personProfileFragment = new PersonProfileFragment();
        toFragment(personProfileFragment);
    }

    public void toAdditionalProfile(){
        tvTitle.setText(R.string.load_additional_profile_title);
        AdditionalProfileFragment additionalProfileFragment = new AdditionalProfileFragment();
        toFragment(additionalProfileFragment);
    }

    public void toLoadConfirmation(){
        tvTitle.setText(R.string.load_confirmation_submit_title);
        LoadConfirmationFragment loadConfirmationFragment = new LoadConfirmationFragment();
        addFragment(loadConfirmationFragment,"load");
    }

    @Override
    protected int getFragmentContainerRes() {
        return R.id.fl_loan_container;
    }

    @Override
    public void onBackPressed() {
        onBackPressedInternal();
    }

    private void onBackPressedInternal() {
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount > 1) {
            if (tvTitle != null){
                tvTitle.setText(R.string.load_confirmation_submit_title);
            }
            getSupportFragmentManager().popBackStackImmediate();
        } else {
            finish();
        }
    }
}
