package com.instahela.deni.mkopo.bean.additionalprofile;

import java.util.ArrayList;

public class TrialResponse {

    private float totalFeeAmount;

    private String repayDate;

    private float totalInterestAmount;

    private float totalDisburseAmount;

    private float totalRepaymentAmount;

    private ArrayList<Trial> trial;

    public static class Trial {
        private String disburseAmount;
        private String disburseTime;
        private long disburseTimeMills;
        private String fee;
        private String feePrePaid;
        private String interest;
        private String interestPrePaid;
        private String loanAmount;
        private String repayDate;
        private long repayDateMills;
        private String stageNo;
        private String totalAmount;

        //添加用来ui显示
        public String title;

        public String getDisburseAmount() {
            return disburseAmount;
        }

        public void setDisburseAmount(String disburseAmount) {
            this.disburseAmount = disburseAmount;
        }

        public String getDisburseTime() {
            return disburseTime;
        }

        public void setDisburseTime(String disburseTime) {
            this.disburseTime = disburseTime;
        }

        public long getDisburseTimeMills() {
            return disburseTimeMills;
        }

        public void setDisburseTimeMills(long disburseTimeMills) {
            this.disburseTimeMills = disburseTimeMills;
        }

        public String getFee() {
            return fee;
        }

        public void setFee(String fee) {
            this.fee = fee;
        }

        public String getFeePrePaid() {
            return feePrePaid;
        }

        public void setFeePrePaid(String feePrePaid) {
            this.feePrePaid = feePrePaid;
        }

        public String getInterest() {
            return interest;
        }

        public void setInterest(String interest) {
            this.interest = interest;
        }

        public String getInterestPrePaid() {
            return interestPrePaid;
        }

        public void setInterestPrePaid(String interestPrePaid) {
            this.interestPrePaid = interestPrePaid;
        }

        public String getLoanAmount() {
            return loanAmount;
        }

        public void setLoanAmount(String loanAmount) {
            this.loanAmount = loanAmount;
        }

        public String getRepayDate() {
            return repayDate;
        }

        public void setRepayDate(String repayDate) {
            this.repayDate = repayDate;
        }

        public long getRepayDateMills() {
            return repayDateMills;
        }

        public void setRepayDateMills(long repayDateMills) {
            this.repayDateMills = repayDateMills;
        }

        public String getStageNo() {
            return stageNo;
        }

        public void setStageNo(String stageNo) {
            this.stageNo = stageNo;
        }

        public String getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(String totalAmount) {
            this.totalAmount = totalAmount;
        }
    }

    public float getTotalFeeAmount() {
        return totalFeeAmount;
    }

    public void setTotalFeeAmount(float totalFeeAmount) {
        this.totalFeeAmount = totalFeeAmount;
    }

    public String getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(String repayDate) {
        this.repayDate = repayDate;
    }

    public float getTotalInterestAmount() {
        return totalInterestAmount;
    }

    public void setTotalInterestAmount(float totalInterestAmount) {
        this.totalInterestAmount = totalInterestAmount;
    }

    public float getTotalDisburseAmount() {
        return totalDisburseAmount;
    }

    public void setTotalDisburseAmount(float totalDisburseAmount) {
        this.totalDisburseAmount = totalDisburseAmount;
    }

    public float getTotalRepaymentAmount() {
        return totalRepaymentAmount;
    }

    public void setTotalRepaymentAmount(float totalRepaymentAmount) {
        this.totalRepaymentAmount = totalRepaymentAmount;
    }

    public ArrayList<Trial> getTrial() {
        return trial;
    }

    public void setTrial(ArrayList<Trial> trial) {
        this.trial = trial;
    }
}
