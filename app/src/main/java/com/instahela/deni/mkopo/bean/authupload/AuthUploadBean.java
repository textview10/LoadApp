package com.instahela.deni.mkopo.bean.authupload;

public class AuthUploadBean {
    private String failReason;
    private boolean hasUpload;

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public boolean isHasUpload() {
        return hasUpload;
    }

    public void setHasUpload(boolean hasUpload) {
        this.hasUpload = hasUpload;
    }
}
