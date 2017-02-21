package com.shobhit.pooltool;

/**
 * Created by Shobhit-pc on 1/14/2017.
 */

public class ManageGroupObject {

    String name, mobileNo , admin;
    int image;

    public ManageGroupObject(String admin , String name , int image , String mobileNo){
        this.admin = admin;
        this.name = name;
        this.mobileNo = mobileNo;
        this.image = image;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }
}
