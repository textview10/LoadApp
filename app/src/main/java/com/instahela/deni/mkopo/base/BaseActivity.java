package com.instahela.deni.mkopo.base;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.instahela.deni.mkopo.R;
import com.instahela.deni.mkopo.bean.BaseResponseBean;
import com.instahela.deni.mkopo.global.AppManager;
import com.lzy.okgo.model.Response;

public abstract class BaseActivity extends AppCompatActivity {

    protected final Gson gson = new Gson();

    protected ImageView ivBack;
    private TextView tvTitle;

    private FrameLayout flError;
    private TextView tvRetry;

    protected void initErrorPage(){
        flError = findViewById(R.id.fl_error_page);
        tvRetry = findViewById(R.id.tv_main_retry);
        tvRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickErrorRetry();
            }
        });
    }

    protected void clickErrorRetry(){

    }

    protected void showErrorPage(){
        if (flError != null){
            flError.setVisibility(View.VISIBLE);
        }
    }

    protected void hideErrorPage(){
        if (flError != null){
            flError.setVisibility(View.GONE);
        }
    }

    public void toFragment(BaseFragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();   // 开启一个事务
        transaction.replace(getFragmentContainerRes(), fragment);
        transaction.commitAllowingStateLoss();
    }

    public void addFragment(BaseFragment fragment, String tag){
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        beginTransaction.replace(getFragmentContainerRes(), fragment, tag);
        beginTransaction.addToBackStack(tag);
        beginTransaction.commitAllowingStateLoss();
    }

    protected void initTitle(){
        ivBack = findViewById(R.id.iv_back);
        tvTitle = findViewById(R.id.tv_title);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void setTitle(String title){
        if (tvTitle != null){
            tvTitle.setText(title);
        }
    }

    protected <T> T checkResponseSuccess(Response<String> response, Class<T> clazz) {
        BaseResponseBean responseBean = gson.fromJson(response.body().toString(), BaseResponseBean.class);
        if (responseBean == null) {
            ToastUtils.showShort("request failure.");
            return null;
        }
        if (!responseBean.isRequestSuccess()) {
            ToastUtils.showShort(responseBean.getHead().getMsg());
            return null;
        }
        if (responseBean.getBody() == null){
            ToastUtils.showShort("request failure 2.");
            return null;
        }
        return gson.fromJson(responseBean.getBodyStr(), clazz);

    }

    protected @IdRes int getFragmentContainerRes(){
        return R.id.fl_main_container;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppManager.getAppManager().addActivity(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        AppManager.getAppManager().finishActivity(this);
        super.onDestroy();
    }
}
