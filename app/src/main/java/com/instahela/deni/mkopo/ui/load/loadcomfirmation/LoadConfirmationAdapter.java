package com.instahela.deni.mkopo.ui.load.loadcomfirmation;

import android.view.View;

import androidx.annotation.NonNull;

import com.instahela.deni.mkopo.bean.additionalprofile.TrialResponse;

import java.util.ArrayList;

public class LoadConfirmationAdapter extends BaseLoadAdapter {

    private ArrayList<TrialResponse.Trial> mList;

    public LoadConfirmationAdapter(ArrayList<TrialResponse.Trial> list) {
        this.mList = list;
    }

    @Override
    public int getItemCountInternal() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public String[] getShowDetail(int position) {
        TrialResponse.Trial trial = mList.get(position);
        String[] str = new String[6];
        str[0] = trial.title;
        str[1] = trial.getDisburseTime();
        str[2] = trial.getFee();
        str[3] = trial.getInterest();
        str[4] = trial.getDisburseAmount();
        str[5] = trial.getTotalAmount();
        return str;
    }

    @Override
    public void onBindViewHolder(@NonNull LoadConfirmationHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (position == 0){
            holder.flDesc4.setVisibility(View.VISIBLE);
        } else {
            holder.flDesc4.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean showTitle() {
        return true;
    }
}
