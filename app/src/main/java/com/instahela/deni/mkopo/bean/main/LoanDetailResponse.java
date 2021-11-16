package com.instahela.deni.mkopo.bean.main;

import java.util.ArrayList;

public class LoanDetailResponse {

    private boolean canApply;

    private String token;

    private String orderId;

    private String reason;

    private String status;

    private long totalAmount;

    private ArrayList<Stage> stageList;

    public boolean isCanApply() {
        return canApply;
    }

    public void setCanApply(boolean canApply) {
        this.canApply = canApply;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public ArrayList<Stage> getStageList() {
        return stageList;
    }

    public void setStageList(ArrayList<Stage> stageList) {
        this.stageList = stageList;
    }

    public static class Stage {
        private double amount;
        private String disburseTime;
        private double fee;
        private double feePrePaid;
        private double interest;
        private double interestPrePaid;
        private String repayDate;
        private String stageNo;
        private double totalAmount;

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getDisburseTime() {
            return disburseTime;
        }

        public void setDisburseTime(String disburseTime) {
            this.disburseTime = disburseTime;
        }

        public double getFee() {
            return fee;
        }

        public void setFee(double fee) {
            this.fee = fee;
        }

        public double getFeePrePaid() {
            return feePrePaid;
        }

        public void setFeePrePaid(double feePrePaid) {
            this.feePrePaid = feePrePaid;
        }

        public double getInterest() {
            return interest;
        }

        public void setInterest(double interest) {
            this.interest = interest;
        }

        public double getInterestPrePaid() {
            return interestPrePaid;
        }

        public void setInterestPrePaid(double interestPrePaid) {
            this.interestPrePaid = interestPrePaid;
        }

        public String getRepayDate() {
            return repayDate;
        }

        public void setRepayDate(String repayDate) {
            this.repayDate = repayDate;
        }

        public String getStageNo() {
            return stageNo;
        }

        public void setStageNo(String stageNo) {
            this.stageNo = stageNo;
        }

        public double getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(double totalAmount) {
            this.totalAmount = totalAmount;
        }
    }
}
