package com.shobhit.pooltool;

/**
 * Created by Shobhit-pc on 1/12/2017.
 */

public class SettleUpObject {
    String userName , amount, userMobileNo;
    int image;

    public SettleUpObject(String userName , String amount , int image , String userMobileNo){
        this.userName = userName;
        this.amount = amount;
        this.image = image;
        this.userMobileNo = userMobileNo;
    }

    public String getUserName() {
        return userName;
    }

    public String getAmount() {
        return amount;
    }

    public int getImage() {
        return image;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getUserMobileNo() {
        return userMobileNo;
    }

    public void setUserMobileNo(String userMobileNo) {
        this.userMobileNo = userMobileNo;
    }
}
