package com.shobhit.pooltool;

/**
 * Created by Shobhit-pc on 12/28/2016.
 */

public class ContactObject {
    private String contactName;
    private String contactNo;
    private int image;

    public ContactObject() {
    }

    public ContactObject(String contactName , String contactNo , int image) {
        this.contactName = contactName;
        this.contactNo = contactNo;
        this.image = image;
    }
    public String getName() {
        return contactName;
    }

    public void setName(String contactName) {
        this.contactName = contactName;
    }

    public String getNumber() {
        return contactNo;
    }

    public void setNumber(String contactNo) {
        this.contactNo = contactNo;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

}