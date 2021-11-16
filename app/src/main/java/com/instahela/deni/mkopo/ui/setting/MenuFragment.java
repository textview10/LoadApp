package com.instahela.deni.mkopo.ui.setting;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.instahela.deni.mkopo.R;
import com.instahela.deni.mkopo.TestActivity;
import com.instahela.deni.mkopo.api.Api;
import com.instahela.deni.mkopo.base.BaseFragment;
import com.instahela.deni.mkopo.bean.setting.msg.UnReadMsgResponse;
import com.instahela.deni.mkopo.global.Constant;
import com.instahela.deni.mkopo.ui.dialog.BaseCommonDialog;
import com.instahela.deni.mkopo.ui.dialog.CommonDialog;
import com.instahela.deni.mkopo.ui.setting.message.MessageFragment;
import com.instahela.deni.mkopo.ui.webview.WebViewFragment;
import com.instahela.deni.mkopo.util.CheckClickFastUtils;
import com.instahela.deni.mkopo.util.GooglePlayUtils;
import com.instahela.deni.mkopo.widget.NotifyTextView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

public class MenuFragment extends BaseSettingFragment implements View.OnClickListener {
    private static final String TAG = "MenuFragment";

    private TextView tvName;
    private LinearLayout llMyLoan, llMyInformation, llMessage, llHelp, llAbout, llLogout, llComment, llTest;
    private NotifyTextView tvMessage;

    private WebViewFragment webViewFragment;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        initializeView(view);
        return view;
    }

    private void initializeView(View view) {
        tvName = view.findViewById(R.id.tv_setting_account_name);
        llMyLoan = view.findViewById(R.id.ll_setting_my_loan);
        llMyInformation = view.findViewById(R.id.ll_setting_my_information);
        llMessage = view.findViewById(R.id.ll_setting_message);
        llHelp = view.findViewById(R.id.ll_setting_help);
        llAbout = view.findViewById(R.id.ll_setting_about);
        llLogout = view.findViewById(R.id.ll_setting_logout);
        llComment = view.findViewById(R.id.ll_setting_post_comment);
        llTest = view.findViewById(R.id.ll_setting_test);

        llMyLoan.setOnClickListener(this);
        llMyInformation.setOnClickListener(this);
        llMessage.setOnClickListener(this);
        llHelp.setOnClickListener(this);
        llAbout.setOnClickListener(this);
        llLogout.setOnClickListener(this);
        llComment.setOnClickListener(this);
        llTest.setOnClickListener(this);
        requestMessageCount();

        llTest.setVisibility(Constant.SHOW_TEST_BUTTON ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(R.string.setting_menu);
    }

    @Override
    public void onClick(View v) {
        if ( !(getActivity() instanceof SettingActivity)){
            return;
        }
        SettingActivity settingActivity = (SettingActivity) getActivity();
        if (settingActivity.isFinishing() || settingActivity.isDestroyed()){
            return;
        }
        switch (v.getId()){
            case R.id.ll_setting_my_loan:
                if (CheckClickFastUtils.checkClickFast()){
                    return;
                }
                toActivityResult(SettingActivity.TYPE_MY_LOAN);
                break;
            case R.id.ll_setting_my_information:
                toActivityResult(SettingActivity.TYPE_MY_INFORMATION);
                break;
            case R.id.ll_setting_message:
                MessageFragment msgFragment = new MessageFragment();
                settingActivity.addFragment(msgFragment, "msg");
                break;
            case R.id.ll_setting_help:
                ContactUsFragment contactUsFragment = new ContactUsFragment();
                settingActivity.addFragment(contactUsFragment, "contactUsFragment");
                break;
            case R.id.ll_setting_about:
                AboutUsFragment aboutUsFragment = new AboutUsFragment();
                settingActivity.addFragment(aboutUsFragment, "aboutUs");
                break;
            case R.id.ll_setting_logout:
                CommonDialog commonDialog = new CommonDialog(getContext(), getString(R.string.dialog_logout_title),
                        getString(R.string.dialog_logout_desc));
                commonDialog.setOnItemClickListener(new BaseCommonDialog.OnItemClickListener() {
                    @Override
                    public void onClickAgree() {
                        requestLogout();
                    }
                });
                commonDialog.show();
                break;
            case R.id.ll_setting_post_comment:
                if (GooglePlayUtils.isPkgInstalled(getContext(), GooglePlayUtils.GOOGLE_VENDING_PKG)) {
                    // go to googleplay app
                    GooglePlayUtils.startWithUrl(getContext(), GooglePlayUtils.GOOLE_PLAY_MOBILE_DETAIL_PRE + Utils.getApp().getPackageName(), GooglePlayUtils.GOOGLE_VENDING_PKG);
                } else {
                    // go to web detail page
                    GooglePlayUtils.startWithUrl(getContext(), GooglePlayUtils.GOOLE_PLAY_WEB_DETAIL_PRE + Utils.getApp().getPackageName(), null);
                }
                break;
            case R.id.ll_setting_test:
                startActivity(new Intent(getContext(), TestActivity.class));
                break;
        }
    }

    private void toActivityResult(int type) {
        Intent intent = new Intent();
        intent.putExtra(SettingActivity.KEY_RESULT_TYPE, type);
        getActivity().setResult(RESULT_OK, intent);
        getActivity().finish();
    }

    private void toWebView(SettingActivity activity, String url) {
        if (webViewFragment == null){
            webViewFragment = new WebViewFragment();
        }
        webViewFragment.setUrl(url);
        activity.addFragment(webViewFragment, "webview");
    }

    private void requestLogout() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("accountId", Constant.mAccountId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<String>post(Api.LOG_OUT_URL).upJson(jsonObject).
                tag(TAG).execute(new StringCallback() {

            @Override
            public void onSuccess(Response<String> response) {
               Log.e(TAG, "" + response.body().toString());
               if (isRemoving() || isDetached()){
                   return;
               }
               if (getActivity() == null || getActivity().isFinishing() || getActivity().isDestroyed()){
                   return;
               }
               Constant.mPhoneNum = Constant.mAccountId = Constant.mToken = Constant.prodName = Constant.proId
                       = Constant.mLoadAmount = Constant.mPeriod = null;
                SPUtils.getInstance().put(Constant.KEY_SP_ACCOUNT_ID, "");
                SPUtils.getInstance().put(Constant.KEY_SP_PHONE_NUM, "");
                SPUtils.getInstance().put(Constant.KEY_SP_TOKEN, "");
               if (getActivity() instanceof SettingActivity){
                   ((SettingActivity) getActivity()).toLoginActivity();
               }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                ToastUtils.showShort("request logout failure");
                Log.e(TAG, "request logout  error ...");
            }
        });
    }

    private void requestMessageCount() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("accountId", Constant.mAccountId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<String>post(Api.UNREAD_MESSAGE_URL).params("accountId", Constant.mAccountId).
                tag(TAG).upJson(jsonObject).execute(new StringCallback() {

            @Override
            public void onSuccess(Response<String> response) {
                UnReadMsgResponse unReadMsgResponse = checkResponseSuccess(response, UnReadMsgResponse.class);
                if (unReadMsgResponse == null){
                    return;
                }
                int unreadCount = unReadMsgResponse.getUnreadCount();
                if (tvMessage != null){
                    tvMessage.setDrawNotify(unreadCount > 0);
                }
            }

            @Override
            public void onError(Response<String> response) {
                super.onError(response);
                Log.e(TAG, "request unread message  error ...");
            }
        });
    }

}
