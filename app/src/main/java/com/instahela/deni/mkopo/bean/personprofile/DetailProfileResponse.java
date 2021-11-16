package com.instahela.deni.mkopo.bean.personprofile;

public class DetailProfileResponse {
    /**
     * {"body":{"birthday":"1992-10-10",
     * "bvn":"1234","bvnChecked":true,
     * "edit":false,"email":"wang867103701@gmail.com",
     * "firstName":"xu","fullName":"XU XUN WANG","gender":1,
     * "genderLabel":"Male","homeAddress":"1234","homeArea":"Nairobi",
     * "homeAreaLabel":"","homeProvince":"-","homeProvinceLabel":"",
     * "homeState":"Nairobi","homeStateLabel":"","lastName":"wang",
     * "middleName":"xun","result":49152,"utime":"2021-07-18 06:54:21"},
     * "head":{"code":"200","msg":"SUCCESS"}}
     */
    private String birthday;
    private String bvn;

    private boolean bvnChecked;
    private boolean edit;

    private String email;
    private String firstName;
//    private String fullName;
    private int gender;
//    private String genderLabel;

    private String homeAddress;

    private String homeArea;
//    private String homeAreaLabel;
    private String homeProvince;
//    private String homeProvinceLabel;
    private String homeState;
//    private String homeStateLabel;
    private String lastName;
    private String middleName;

    private int result;
    private String utime;

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBvn() {
        return bvn;
    }

    public void setBvn(String bvn) {
        this.bvn = bvn;
    }

    public boolean isBvnChecked() {
        return bvnChecked;
    }

    public void setBvnChecked(boolean bvnChecked) {
        this.bvnChecked = bvnChecked;
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getHomeArea() {
        return homeArea;
    }

    public void setHomeArea(String homeArea) {
        this.homeArea = homeArea;
    }

    public String getHomeProvince() {
        return homeProvince;
    }

    public void setHomeProvince(String homeProvince) {
        this.homeProvince = homeProvince;
    }

    public String getHomeState() {
        return homeState;
    }

    public void setHomeState(String homeState) {
        this.homeState = homeState;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getUtime() {
        return utime;
    }

    public void setUtime(String utime) {
        this.utime = utime;
    }
}
