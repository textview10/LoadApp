package com.instahela.deni.mkopo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.instahela.deni.mkopo.R;

public class SelectContainer extends FrameLayout {

    private String title, desc;
    private @DrawableRes int drawableRes;

    private TextView tvTitle, tvDesc;
    private ImageView ivIcon;

    public SelectContainer(@NonNull Context context) {
        super(context);
    }

    public SelectContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.select_view);
        title = typedArray.getString(R.styleable.select_view_select_view_title);
        desc = typedArray.getString(R.styleable.select_view_select_view_desc);
        drawableRes = typedArray.getResourceId(R.styleable.select_view_select_view_icon, R.drawable.ic_bottom_arrow);
        initializeView();
    }

    private void initializeView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_container_select, this, false);
        tvTitle = view.findViewById(R.id.tv_loan_select_title);
        tvDesc = view.findViewById(R.id.tv_loan_select_desc);
        ivIcon = view.findViewById(R.id.iv_loan_select_icon);

        tvTitle.setText(title);
        tvDesc.setText(desc);
        if (drawableRes != 0) {
            ivIcon.setImageResource(drawableRes);
        }

        addView(view);
    }


    public void setText(String text){
        if (tvDesc != null){
            tvDesc.setText(text);
        }
    }
}
