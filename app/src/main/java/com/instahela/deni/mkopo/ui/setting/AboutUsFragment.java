package com.instahela.deni.mkopo.ui.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.instahela.deni.mkopo.R;
import com.instahela.deni.mkopo.api.Api;
import com.instahela.deni.mkopo.base.BaseFragment;
import com.instahela.deni.mkopo.ui.webview.WebViewFragment;

public class AboutUsFragment extends BaseSettingFragment {

    private LinearLayout llPrivatePolicy, llTerms;
    private WebViewFragment webViewFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);
        initializeView(view);
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(R.string.setting_about);
    }

    private void initializeView(View view) {
        llPrivatePolicy = view.findViewById(R.id.ll_setting_private_policy);
        llTerms = view.findViewById(R.id.ll_setting_term);

        llPrivatePolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toWebView((SettingActivity) getActivity(), Api.WEB_VIEW_POLICY);
            }
        });
        llTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toWebView((SettingActivity) getActivity(), Api.WEB_VIEW_TERM);
            }
        });
    }

    private void toWebView(SettingActivity activity, String url) {
        if (webViewFragment == null){
            webViewFragment = new WebViewFragment();
        }
        webViewFragment.setUrl(url);
        activity.addFragment(webViewFragment, "webview");
    }
}
