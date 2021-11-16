package com.instahela.deni.mkopo.bean.additionalprofile;

public class AdditionalProfileResponse {

    private boolean hasProfile;

    private boolean hasProfileOther;

    private String token;

    public boolean isHasProfile() {
        return hasProfile;
    }

    public void setHasProfile(boolean hasProfile) {
        this.hasProfile = hasProfile;
    }

    public boolean isHasProfileOther() {
        return hasProfileOther;
    }

    public void setHasProfileOther(boolean hasProfileOther) {
        this.hasProfileOther = hasProfileOther;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
