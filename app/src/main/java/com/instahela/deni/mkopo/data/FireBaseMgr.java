package com.instahela.deni.mkopo.data;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.instahela.deni.mkopo.api.Api;
import com.instahela.deni.mkopo.global.Constant;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

public class FireBaseMgr {

    private static final String TAG = "FireBaseMgr";

    private static FireBaseMgr mManager;

    private FireBaseMgr() {

    }

    public static FireBaseMgr getInstance() {
        if (mManager == null) {
            synchronized (LocationMgr.class) {
                if (mManager == null) {
                    mManager = new FireBaseMgr();
                }
            }
        }
        return mManager;
    }

    public void getToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();
                        Constant.mFirebaseToken = token;
                    }
                });
    }

    public void reportFcmToken() {
        if (TextUtils.isEmpty(Constant.mFirebaseToken)){
            return;
        }
        if (!Constant.isNewToken){
            return;
        }
        OkGo.<String>post(Api.REPORT_FCM_TOKEN).tag(TAG).headers("token", Constant.mToken).params("fcmToken", Constant.mFirebaseToken)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e(TAG, " report fcm token success = " + response.body().toString());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e(TAG, " report fcm token failure = ");
                    }
                });

    }
}
