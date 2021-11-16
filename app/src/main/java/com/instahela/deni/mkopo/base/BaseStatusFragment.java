package com.instahela.deni.mkopo.base;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.instahela.deni.mkopo.R;

public class BaseStatusFragment extends BaseFragment{

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
}
