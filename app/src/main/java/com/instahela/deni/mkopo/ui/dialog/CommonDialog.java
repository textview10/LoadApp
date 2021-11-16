package com.instahela.deni.mkopo.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ScreenUtils;
import com.instahela.deni.mkopo.R;

public class CommonDialog extends BaseCommonDialog {

    public CommonDialog(@NonNull Context context, String title, String desc) {
        super(context,  R.style.DialogTheme);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = (int)(ScreenUtils.getAppScreenWidth() * 3 / 4); //设置宽度
        getWindow().setAttributes(lp);

        setContentView(R.layout.dialog_common);
        TextView tvTitle = findViewById(R.id.tv_dialog_title);
        TextView tvDesc = findViewById(R.id.tv_dialog_desc);
        TextView tvNo = findViewById(R.id.tv_dialog_no);
        TextView tvYes = findViewById(R.id.tv_dialog_yes);

        tvTitle.setText(title);
        tvDesc.setText(desc);
        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.onClickAgree();
                }
                dismiss();
            }
        });

        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.onClickCancel();
                }
                dismiss();
            }
        });
    }


}
