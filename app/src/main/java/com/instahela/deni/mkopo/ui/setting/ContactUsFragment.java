package com.instahela.deni.mkopo.ui.setting;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.instahela.deni.mkopo.R;
import com.instahela.deni.mkopo.base.BaseFragment;

public class ContactUsFragment extends BaseSettingFragment {

    private LinearLayout llTalkUs, llCallCustomer, llEmail;

    private static final String CALL_CUSTOMER = "254720660270";
    private static final String WHAT_APP = "254720660270";
    private static final String EMAIL = "support@instahela.com";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        initializeView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(R.string.setting_contact_us);
    }

    private void initializeView(View view) {
        llTalkUs = view.findViewById(R.id.ll_setting_talk_us);
        llCallCustomer = view.findViewById(R.id.ll_setting_call_customer);
        llEmail = view.findViewById(R.id.ll_setting_email);

        llTalkUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAppInstall(getContext(), "com.whatsapp")) { //判断是否装了whatsAPP
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, WHAT_APP);
                    sendIntent.setType("text/plain");
                    sendIntent.setPackage("com.whatsapp");//区别别的应用包名
                    startActivity(sendIntent);
                }else {
                    ToastUtils.showShort("not install WhatsApp");
                }
            }
        });
        llCallCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionUtils.permission(Manifest.permission.CALL_PHONE).callback(new PermissionUtils.SimpleCallback() {
                    @Override
                    public void onGranted() {
                        //call动作为直接拨打电话(需要加CALL权限)
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + CALL_CUSTOMER));
                        //dial动作为调用拨号盘
                        startActivity(intent);
                    }

                    @Override
                    public void onDenied() {

                    }
                }).request();
            }
        });
        llEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 调用系统发邮件
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                // 设置文本格式
                emailIntent.setType("text/plain");
                // 设置对方邮件地址
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { EMAIL});
                // 设置标题内容
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "mail");
                // 设置邮件文本内容
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(emailIntent, "send mail"));
            }
        });
    }

    private boolean isAppInstall(Context context, String appName) {
        PackageInfo packageInfo = null ;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(appName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo==null?false:true;
    }
}
