package com.instahela.deni.mkopo.ui.setting.message;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.instahela.deni.mkopo.R;
import com.instahela.deni.mkopo.bean.setting.msg.MsgResponse;
import com.instahela.deni.mkopo.ui.setting.BaseSettingFragment;

public class MessageContentFragment extends BaseSettingFragment {

    private String title;
    private String desc;
    private String time;

    private TextView tvTitle;
    private TextView tvDesc;
    private TextView tvTime;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_content, container, false);
        initializeView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(R.string.setting_message_content);
    }

    private void initializeView(View view) {
        tvTitle = view.findViewById(R.id.tv_message_content_title);
        tvDesc = view.findViewById(R.id.tv_message_content_desc);
        tvTime = view.findViewById(R.id.tv_message_content_time);
        updateContent();
    }

    public void setMsg(MsgResponse.Msg msg) {
        if (msg == null){
            return;
        }
        this.title = msg.getTitle();
        this.desc = msg.getContent();
        this.time = msg.getCtime();
        updateContent();
    }

    private void updateContent(){
        if (tvTitle != null && !TextUtils.isEmpty(title)){
            tvTitle.setText(title);
        }
        if (tvDesc != null && !TextUtils.isEmpty(desc)){
            tvDesc.setText(desc);
        }
        if (tvTime != null && !TextUtils.isEmpty(time)){
            tvTime.setText(time);
        }
    }
}
