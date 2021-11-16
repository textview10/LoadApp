package com.instahela.deni.mkopo.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.LocaleList;
import android.text.TextUtils;


import java.util.Locale;

public class GooglePlayUtils {

    public static final String GOOGLE_VENDING_PKG = "com.android.vending";
    //goole play评分地址前缀
    public static final String GOOLE_PLAY_MOBILE_DETAIL_PRE = "market://details?id=";
    public static final String GOOLE_PLAY_WEB_DETAIL_PRE = "https://play.google.com/store/apps/details?id=";

    public static void startWithUrl(Context context, String url, String pkg) {
        //跳转到浏览器
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            if (!TextUtils.isEmpty(pkg)){
                intent.setPackage(pkg);
            }
            context.startActivity(intent);
        }catch (Exception e){}
    }

    public static boolean isPkgInstalled(Context context, String packageName)
    {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
        }
        return packageInfo != null;
    }
}
