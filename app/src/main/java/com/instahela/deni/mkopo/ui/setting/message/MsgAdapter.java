package com.instahela.deni.mkopo.ui.setting.message;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.instahela.deni.mkopo.R;
import com.instahela.deni.mkopo.bean.setting.msg.MsgResponse;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class MsgAdapter extends RecyclerView.Adapter<MsgHolder> {

    private ArrayList<MsgResponse.Msg> mList;

    public MsgAdapter(ArrayList<MsgResponse.Msg> list) {
        this.mList = list;
    }

    @NonNull
    @NotNull
    @Override
    public MsgHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new MsgHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_setting_msg, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MsgHolder holder, int position) {
        MsgResponse.Msg msg = mList.get(position);

        holder.tvTitle.setText(msg.getTitle());
        holder.tvCalendar.setText(msg.getCtime());
        holder.tvDesc.setText(msg.getContent());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null){
                    mListener.onItemClick(position, msg);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        mListener = onItemClickListener;
    }

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int pos, MsgResponse.Msg msg);
    }
}
