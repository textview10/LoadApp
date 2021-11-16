package com.instahela.deni.mkopo.ui.home.startload;

import com.instahela.deni.mkopo.bean.main.ProductBean;

import java.util.ArrayList;

public class PeriodAdapter extends BaseHomeAdapter {

    private ArrayList<ProductBean.Item> mPeriodList;

    public PeriodAdapter(ArrayList<ProductBean.Item> list, int pos) {
        super(pos);
        mPeriodList = list;
    }

    @Override
    public String getStringByPos(int pos) {
        return mPeriodList.get(pos).getPeriod() + "d";
    }

    @Override
    public int getItemCountInternal() {
        return mPeriodList == null ? 0 : mPeriodList.size();
    }
}
