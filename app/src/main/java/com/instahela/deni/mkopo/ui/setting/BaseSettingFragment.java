package com.instahela.deni.mkopo.ui.setting;

import androidx.annotation.StringRes;

import com.instahela.deni.mkopo.base.BaseFragment;

public class BaseSettingFragment extends BaseFragment {

    public void setTitle(@StringRes int textRes){
        if (!(getActivity() instanceof SettingActivity)) {
            return;
        }
        SettingActivity settingActivity = (SettingActivity) getActivity();
        if (settingActivity.isFinishing() || settingActivity.isDestroyed()){
            return;
        }
        settingActivity.setTitle(getString(textRes));
    }
}
