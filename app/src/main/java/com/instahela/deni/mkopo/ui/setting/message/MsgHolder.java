package com.instahela.deni.mkopo.ui.setting.message;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.instahela.deni.mkopo.R;

import org.jetbrains.annotations.NotNull;

public class MsgHolder extends RecyclerView.ViewHolder {

    public ImageView ivPic;
    public TextView tvTitle, tvCalendar, tvDesc;

    public MsgHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        ivPic = itemView.findViewById(R.id.iv_item_msg_pic);
        tvTitle = itemView.findViewById(R.id.tv_item_msg_title);
        tvCalendar = itemView.findViewById(R.id.tv_item_msg_calendar);
        tvDesc = itemView.findViewById(R.id.tv_item_msg_desc);
    }
}
