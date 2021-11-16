package com.instahela.deni.mkopo.bean.personprofile;

public class RequestProfileResponse {

    private boolean bvnChecked;

    private boolean hasContact;

    private boolean hasOther;

    private boolean hasProfile;

    private String token;

    public boolean isBvnChecked() {
        return bvnChecked;
    }

    public void setBvnChecked(boolean bvnChecked) {
        this.bvnChecked = bvnChecked;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
