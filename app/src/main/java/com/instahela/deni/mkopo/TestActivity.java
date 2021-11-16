package com.instahela.deni.mkopo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.SPUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.instahela.deni.mkopo.global.Constant;
import com.instahela.deni.mkopo.notify.NotifyManager;
import com.instahela.deni.mkopo.test.Test2;
import com.instahela.deni.mkopo.ui.signin.SignInActivity;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initView();

//        Test test = new Test();
//        test.main();
        String s = "rp9v9muvsmh1nsd8";
        Test2 test2 = new Test2();
        try {
            test2.createKeyPair();

            String s1 = test2.encryptWithRSA(s);
            Log.e("Test", " s1 = " + s1);
            String s2 = test2.decryptWithRSA(s1);
            Log.e("Test", " s2 = " + s2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        findViewById(R.id.btn_test_success).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotifyManager.sendLoanSuccess(1000,1000,"20210928",false);
            }
        });

        findViewById(R.id.btn_test_fialure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotifyManager.sendLoanFailure(1000,1000,"20210928",false);
            }
        });

        findViewById(R.id.btn_repay_msg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotifyManager.repayMessage(1000,1000,"20210928",false);
            }
        });

        findViewById(R.id.btn_quit_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.mPhoneNum = Constant.mAccountId = Constant.mToken = Constant.prodName = Constant.proId
                        = Constant.mLoadAmount = Constant.mPeriod = null;
                SPUtils.getInstance().put(Constant.KEY_SP_ACCOUNT_ID, "");
                SPUtils.getInstance().put(Constant.KEY_SP_PHONE_NUM, "");
                SPUtils.getInstance().put(Constant.KEY_SP_TOKEN, "");
                startActivity(new Intent(TestActivity.this, SignInActivity.class));
                finish();
            }
        });

        findViewById(R.id.btn_notiy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotifyManager.sendNotifyMessage(1,TestActivity.this);
            }
        });

        findViewById(R.id.btn_get_firebase).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFirebase();
            }
        });
    }

    private void getFirebase() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("Test", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        Log.e("Test", "token = " + token);
                        Toast.makeText(TestActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
