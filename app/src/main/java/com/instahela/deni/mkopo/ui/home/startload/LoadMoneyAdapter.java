package com.instahela.deni.mkopo.ui.home.startload;

import java.util.ArrayList;

public class LoadMoneyAdapter extends BaseHomeAdapter {
    private ArrayList<String> mLoadMoneyList;

    public LoadMoneyAdapter(ArrayList<String> list, int pos) {
        super(pos);
        this.mLoadMoneyList = list;
    }

    @Override
    public String getStringByPos(int pos) {
        return mLoadMoneyList.get(pos) + " Ksh";
    }

    @Override
    public int getItemCountInternal() {
        return mLoadMoneyList == null ? 0 : mLoadMoneyList.size();
    }
}
