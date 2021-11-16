package com.instahela.deni.mkopo.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class SignInResponseBean implements Parcelable {
    private String accountId;

    private boolean hasContact;

    private boolean hasOther;

    private boolean hasProfile;

    private String mobile;

    private String token;

    protected SignInResponseBean(Parcel in) {
        accountId = in.readString();
        hasContact = in.readByte() != 0;
        hasOther = in.readByte() != 0;
        hasProfile = in.readByte() != 0;
        mobile = in.readString();
        token = in.readString();
    }

    public static final Creator<SignInResponseBean> CREATOR = new Creator<SignInResponseBean>() {
        @Override
        public SignInResponseBean createFromParcel(Parcel in) {
            return new SignInResponseBean(in);
        }

        @Override
        public SignInResponseBean[] newArray(int size) {
            return new SignInResponseBean[size];
        }
    };

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(accountId);
        dest.writeByte((byte) (hasContact ? 1 : 0));
        dest.writeByte((byte) (hasOther ? 1 : 0));
        dest.writeByte((byte) (hasProfile ? 1 : 0));
        dest.writeString(mobile);
        dest.writeString(token);
    }
}
