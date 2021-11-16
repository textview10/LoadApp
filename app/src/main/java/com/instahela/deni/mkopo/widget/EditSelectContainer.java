package com.instahela.deni.mkopo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import com.instahela.deni.mkopo.R;

public class EditSelectContainer extends FrameLayout {

    private String title, desc;
    private @DrawableRes
    int drawableRes;

    private TextView tvTitle;
    private AppCompatEditText editText;
    private ImageView ivIcon;

    public EditSelectContainer(@NonNull Context context) {
        super(context);
    }

    public EditSelectContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.select_view);
        title = typedArray.getString(R.styleable.select_view_select_view_title);
        desc = typedArray.getString(R.styleable.select_view_select_view_desc);
        drawableRes = typedArray.getResourceId(R.styleable.select_view_select_view_icon, R.drawable.ic_bottom_arrow);
        initializeView();
    }

    private void initializeView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_edit_select_text, this, false);
        tvTitle = view.findViewById(R.id.tv_loan_select_title);
        editText = view.findViewById(R.id.et_loan_select_desc);
        ivIcon = view.findViewById(R.id.iv_loan_select_icon);

        tvTitle.setText(title);
        editText.setHint(desc);
        if (drawableRes != 0) {
            ivIcon.setImageResource(drawableRes);
        }
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.onClick();
                }
            }
        });
        addView(view);
    }

    public boolean isEmptyText() {
        if (editText == null) {
            return true;
        }
        return TextUtils.isEmpty(getText());
    }

    public String getText(){
        if (editText == null) {
            return "";
        }
        return editText.getText().toString();
    }

    public void setEditTextAndSelection(String editTextStr){
        if (editText != null && !TextUtils.isEmpty(editTextStr)){
            editText.setText(editTextStr);
            editText.setSelection(editTextStr.length());
        }
    }

    private OnBgClickListener mListener;

    public void setOnBgClickListener(OnBgClickListener listener){
        this.mListener = listener;
    }

    public interface OnBgClickListener {
        void onClick();
    }
}
