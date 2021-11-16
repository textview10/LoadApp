package com.instahela.deni.mkopo.base;

import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.instahela.deni.mkopo.R;
import com.instahela.deni.mkopo.bean.BaseResponseBean;
import com.lzy.okgo.model.Response;

public class BaseFragment extends Fragment {

    protected final Gson gson = new Gson();

    protected String checkResponseSuccess(Response<String> response) {
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
        return responseBean.getBodyStr();
    }

    protected <T> T checkResponseSuccess(Response<String> response, Class<T> clazz) {
        String body = checkResponseSuccess(response);
        if (TextUtils.isEmpty(body)){
            return null;
        }
        return gson.fromJson(body, clazz);
    }

    private FrameLayout flError;
    private TextView tvRetry;

    protected void initErrorPage(View view){
        flError = view.findViewById(R.id.fl_error_page);
        tvRetry = view.findViewById(R.id.tv_main_retry);
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

    private static long lastClickMillions;

    protected boolean checkClickFast(){
        long delay = (System.currentTimeMillis() - lastClickMillions);
        if ( delay < 4000){
            ToastUtils.showShort("click too fast. please wait a monment");
            return true;
        }
        lastClickMillions = System.currentTimeMillis();
        return false;
    }
}
