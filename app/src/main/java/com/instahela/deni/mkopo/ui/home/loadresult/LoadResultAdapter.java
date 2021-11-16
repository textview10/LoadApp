package com.instahela.deni.mkopo.ui.home.loadresult;

import android.view.View;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.CollectionUtils;
import com.instahela.deni.mkopo.R;
import com.instahela.deni.mkopo.bean.main.LoanDetailResponse;
import com.instahela.deni.mkopo.ui.load.loadcomfirmation.BaseLoadAdapter;
import com.instahela.deni.mkopo.ui.load.loadcomfirmation.LoadConfirmationHolder;

import java.util.ArrayList;

public class LoadResultAdapter extends BaseLoadAdapter {

    private ArrayList<LoanDetailResponse.Stage> mList;

    public LoadResultAdapter(ArrayList<LoanDetailResponse.Stage> list) {
        this.mList = list;
    }

    @Override
    public int getItemCountInternal() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public String[] getShowDetail(int position) {
        LoanDetailResponse.Stage stage = mList.get(position);
        String[] str = new String[6];
        switch (position){
            case 0:
                str[0] = "1st";
                break;
            case 1:
                str[0] = "2nd";
                break;
            default:
                str[0] = (position + 1) + "st";
                break;
        }

        str[1] = stage.getDisburseTime();
        str[2] = String.valueOf(stage.getFee());
        str[3] = String.valueOf(stage.getInterest());
        str[4] = String.valueOf(stage.getAmount());
        str[5] = String.valueOf(stage.getTotalAmount());
        return str;
    }

    @Override
    public void onBindViewHolder(@NonNull LoadConfirmationHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        LoanDetailResponse.Stage stage = mList.get(position);
        if (position == 0) {
            holder.tvRepay.setText(R.string.load_processing_btn_repayment);
            holder.tvRepay.setVisibility(View.VISIBLE);
            holder.tvRepay.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onItemClick(stage, position, false);
                }
            });
        } else {
            holder.tvRepay.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean showTitle() {
        return true;
    }

    public interface OnItemClickListener {
        void onItemClick(LoanDetailResponse.Stage stage, int pos, boolean isLast);
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }
}
