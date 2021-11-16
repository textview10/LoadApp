package com.instahela.deni.mkopo.util;


import com.blankj.utilcode.util.ToastUtils;

public class CheckClickFastUtils {

    private static long lastClickMillions;

    public static boolean checkClickFast(){
        long delay = (System.currentTimeMillis() - lastClickMillions);
        if ( delay < 5000){
            try {
                int delayS = (int) (Math.ceil((5000L - delay) / 1000));
                if (delayS <= 0){
                    delayS = 1;
                }
                ToastUtils.showShort("click too fast. please wait " + delayS + " s");
            } catch (Exception e){

            }
            return true;
        }
        lastClickMillions = System.currentTimeMillis();
        return false;
    }

    public static void setRequestMillion(){
        lastClickMillions = System.currentTimeMillis();
    }

    // startLoad接口也不能请求过快。
    public static long clickStartLoanMillions;

}
