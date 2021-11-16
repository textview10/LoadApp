package com.instahela.deni.mkopo.ui.home;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class LoadStatus {
    public static final String TYPE_APPLY_START = "1";    // //已申请,待审核
    public static final String TYPE_APPLY_SUCCESS = "2";      //审核通过,放款成功
    public static final String TYPE_REPAY_SUCCESS = "3";        //还款成功
    public static final String TYPE_REPAY_TIME_OUT = "4";        //逾期
    public static final String TYPE_APPLY_FAILURE = "5";        //贷款被拒绝
    public static final String TYPE_REPAYING = "6";        //还款中

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({TYPE_APPLY_START, TYPE_APPLY_SUCCESS, TYPE_REPAY_SUCCESS, TYPE_REPAY_TIME_OUT, TYPE_APPLY_FAILURE, TYPE_REPAYING})
    public @interface LoanStatus {

    }
}

