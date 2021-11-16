package com.instahela.deni.mkopo.bean.additionalprofile;

public class CheckBeanResponse {

    private boolean accountChecked;
    private boolean bvnChecked;
    private boolean cardChecked;
    private boolean hasContact;
    private boolean hasOther;
    private boolean hasProfile;
    private String orderId;

    public boolean isAccountChecked() {
        return accountChecked;
    }

    public void setAccountChecked(boolean accountChecked) {
        this.accountChecked = accountChecked;
    }

    public boolean isBvnChecked() {
        return bvnChecked;
    }

    public void setBvnChecked(boolean bvnChecked) {
        this.bvnChecked = bvnChecked;
    }

    public boolean isCardChecked() {
        return cardChecked;
    }

    public void setCardChecked(boolean cardChecked) {
        this.cardChecked = cardChecked;
    }

    public boolean isHasContact() {
        return hasContact;
    }

    public void setHasContact(boolean hasContact) {
        this.hasContact = hasContact;
    }

    public boolean isHasOther() {
        return hasOther;
    }

    public void setHasOther(boolean hasOther) {
        this.hasOther = hasOther;
    }

    public boolean isHasProfile() {
        return hasProfile;
    }

    public void setHasProfile(boolean hasProfile) {
        this.hasProfile = hasProfile;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
