package com.instahela.deni.mkopo.notify;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.blankj.utilcode.util.NotificationUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.Utils;
import com.instahela.deni.mkopo.R;
import com.instahela.deni.mkopo.api.Api;
import com.instahela.deni.mkopo.global.Constant;
import com.instahela.deni.mkopo.ui.LauncherActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class NotifyManager {

    private static final String TAG = "NotifyManager";

    public static final String KEY_ORDER_ID = "orderId";

    public static boolean hasHandleOrder(String orderId){
        String result = SPUtils.getInstance().getString(KEY_ORDER_ID);
        if (result.contains(orderId)){
            return true;
        } else {
            return false;
        }
    }

    public static void setHandleOrder(String orderId){
        String result = SPUtils.getInstance().getString(KEY_ORDER_ID);
        SPUtils.getInstance().put(KEY_ORDER_ID, result + "," + orderId);
    }

// "{
//         ""accountId"": ""210521050100000189"",
//            ""mobile"": ""2341234432112"",
//            ""loanAmount"": 12000,
//            ""repayAmount"": 6000,
//            ""repayDate"": ""20210512"",
//            ""sendSMS"": ""1"",
//            ""sendStation"": """",
//     ""sendPush"": """"
//}"
    public static void sendLoanSuccess(double loanAmount, double repayAmount, String repayDate, boolean sendMessage) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("accountId", Constant.mAccountId);
            jsonObject.put("mobile", Constant.mPhoneNum);
            jsonObject.put("loanAmount", loanAmount);
            jsonObject.put("repayAmount", repayAmount);
            jsonObject.put("repayDate", repayDate);
            jsonObject.put("sendSMS", sendMessage ? "1" : "0");
            jsonObject.put("sendStation", "");
            jsonObject.put("sendPush", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkGo.<String>post(Api.LOAN_SUCCESS).tag(TAG).upJson(jsonObject)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e(TAG, " loan success = " + response.body().toString());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e(TAG, "sendLoanSuccess loan failure = " );
                    }
                });
    }

    public static void sendLoanFailure(double loanAmount, double repayAmount, String repayDate, boolean sendMessage) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("accountId", Constant.mAccountId);
            jsonObject.put("mobile", Constant.mPhoneNum);
            jsonObject.put("loanAmount", loanAmount);
            jsonObject.put("repayAmount", repayAmount);
            jsonObject.put("repayDate", repayDate);
            jsonObject.put("sendSMS", sendMessage ? "1" : "0");
            jsonObject.put("sendStation", "");
            jsonObject.put("sendPush", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkGo.<String>post(Api.LOAN_FAILURE).tag(TAG).upJson(jsonObject)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e(TAG, " loan success = " + response.body().toString());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e(TAG, "sendLoanFailure loan failure = ");
                    }
                });
    }

    public static void repayMessage(int loanAmount, int repayAmount, String repayDate, boolean sendMessage){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("accountId", Constant.mAccountId);
            jsonObject.put("mobile", Constant.mPhoneNum);
            jsonObject.put("loanAmount", loanAmount);
            jsonObject.put("repayAmount", repayAmount);
            jsonObject.put("repayDate", repayDate);
            jsonObject.put("sendSMS", sendMessage ? "1" : "0");
            jsonObject.put("sendStation", "");
            jsonObject.put("sendPush", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<String>post(Api.REPAY_SMS).tag(TAG).upJson(jsonObject)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e(TAG, " loan success = " + response.body().toString());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e(TAG, "repayMessage loan failure = ");
                    }
                });
    }

    public static void cancelAll(){
        OkGo.getInstance().cancelTag(TAG);
    }

    public static void sendNotifyMessage(int id, Context context) {
        NotificationUtils.notify(id, new Utils.Consumer<NotificationCompat.Builder>() {
            @Override
            public void accept(NotificationCompat.Builder builder) {
                Intent intent = new Intent(context, LauncherActivity.class);
                builder.setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("title")
                        .setContentText("content text: test")
                        .setContentIntent(PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT))
                        .setAutoCancel(true);
            }
        });
    }
}
