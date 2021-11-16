package com.instahela.deni.mkopo.ui.home.startload;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.instahela.deni.mkopo.R;

import org.jetbrains.annotations.NotNull;

public abstract class BaseHomeAdapter extends RecyclerView.Adapter<HomeHolder> {

    private int mSelectPos;

    public BaseHomeAdapter(int pos) {
        this.mSelectPos = pos;
    }

    @NonNull
    @NotNull
    @Override
    public HomeHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new HomeHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull HomeHolder holder, int position) {
        String text = getStringByPos(position);
        holder.tvHome.setText(text);
        holder.flHome.setBackgroundResource(mSelectPos == position ?
                R.drawable.home_corner_bg_select : R.drawable.home_corner_bg_unselect);
        holder.tvHome.setTextColor(holder.itemView.getContext().getResources().getColor(
                mSelectPos == position ? R.color.white : R.color.bg_color));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectPos == position){
                    return;
                }
                int oldPos = mSelectPos;
                mSelectPos = position;
                notifyItemChanged(oldPos);
                notifyItemChanged(mSelectPos);
                if (mListener != null){
                    mListener.onItemClick(position);
                }
            }
        });
    }

    public abstract String getStringByPos(int pos);

    public abstract int getItemCountInternal();
    @Override
    public int getItemCount() {
        return getItemCountInternal();
    }

    public int getSelectPos(){
        return mSelectPos;
    }

    public void setSelectPos(int pos){
        mSelectPos = pos;
    }

    private OnItemClickListener mListener;
    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int pos);
    }
}
