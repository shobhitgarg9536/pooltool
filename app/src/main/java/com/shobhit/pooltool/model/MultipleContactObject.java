package com.shobhit.pooltool.model;

/**
 * Created by Shobhit-pc on 12/29/2016.
 */

public class MultipleContactObject {

    private String contactName;
    private String contactNo;
    private String image;
    private boolean selected;

    MultipleContactObject(){

    }

    public MultipleContactObject(String contactName , String contactNo , boolean selected){
        this.contactName = contactName;
        this.contactNo = contactNo;
        this.selected = selected;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}