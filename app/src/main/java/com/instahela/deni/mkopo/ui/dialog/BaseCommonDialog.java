package com.instahela.deni.mkopo.ui.dialog;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;

public class BaseCommonDialog extends Dialog {

    public BaseCommonDialog(@NonNull Context context, @StyleRes int themeStyleRes) {
        super(context, themeStyleRes);
    }

    protected OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    public static abstract class OnItemClickListener {
        public abstract void onClickAgree();

        public void onClickCancel(){

        }
    }
}
