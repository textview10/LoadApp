package com.instahela.deni.mkopo.ui.load.loadcomfirmation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.instahela.deni.mkopo.R;
import com.instahela.deni.mkopo.bean.additionalprofile.TrialResponse;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public abstract class BaseLoadAdapter extends RecyclerView.Adapter<LoadConfirmationHolder> {
    public BaseLoadAdapter() {

    }

    @NonNull
    @NotNull
    @Override
    public LoadConfirmationHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new LoadConfirmationHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_load_confirmation, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull LoadConfirmationHolder holder, int position) {
        String[] showDetail = getShowDetail(position);
        if (showTitle()){
            holder.tvTitle.setText(showDetail[0]);
            holder.view.setVisibility(View.VISIBLE);
            holder.tvTitle.setVisibility(View.VISIBLE);
        } else {
            holder.tvTitle.setVisibility(View.GONE);
            holder.view.setVisibility(View.GONE);
        }
        holder.tvDueDate.setText(showDetail[1]);
        holder.tvServiceFee.setText(appendPrefix(showDetail[2]));
        holder.tvInterest.setText(appendPrefix(showDetail[3]));
        holder.tvReviewFree.setText(appendPrefix(showDetail[4]));
        holder.tvTotalPayment.setText(appendPrefix(showDetail[5]));

    }

    private String appendPrefix(String content){
        return content + "ksh";
    }

    @Override
    public int getItemCount() {
       return getItemCountInternal();
    }

    public abstract boolean showTitle();

    public abstract int getItemCountInternal();

    public abstract String[] getShowDetail(int position);


}
