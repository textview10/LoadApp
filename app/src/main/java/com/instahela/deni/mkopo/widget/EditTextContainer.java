package com.instahela.deni.mkopo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.instahela.deni.mkopo.R;

public class EditTextContainer extends FrameLayout {

    private TextView tvTitle;
    private EditText editText;
    private ImageView ivClear;
    private String title, desc;

    public EditTextContainer(Context context) {
        super(context);
        initializeView();
    }

    public EditTextContainer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.select_view);
        title = typedArray.getString(R.styleable.select_view_select_view_title);
        desc = typedArray.getString(R.styleable.select_view_select_view_desc);
        initializeView();
    }

    private void initializeView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_edit_text, this, false);
        tvTitle = view.findViewById(R.id.edit_view_title);
        editText = view.findViewById(R.id.edit_view_edit_text);
        ivClear = view.findViewById(R.id.edit_view_clear);

        tvTitle.setText(title);
        editText.setHint(desc);

        ivClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText != null) {
                    editText.setText("");
                }
            }
        });
        editText.addTextChangedListener(mTextWatcher);
        addView(view);
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!TextUtils.isEmpty(s) && s.length() > 0) {
                if (ivClear != null) {
                    ivClear.setVisibility(View.VISIBLE);
                }
            } else {
                if (ivClear != null) {
                    ivClear.setVisibility(View.INVISIBLE);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

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
        if (editText != null){
            editText.setText(editTextStr);
            editText.setSelection(editTextStr.length());
        }
    }

    public void setPassWordMode(boolean isPasswordMode){
        if (editText != null){
            editText.setTransformationMethod(isPasswordMode ? PasswordTransformationMethod.getInstance()
                    : HideReturnsTransformationMethod.getInstance());
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (editText != null) {
            editText.addTextChangedListener(mTextWatcher);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (editText != null) {
            editText.removeTextChangedListener(mTextWatcher);
        }
    }
}
