package com.shobhit.pooltool.model;

/**
 * Created by Shobhit-pc on 12/26/2016.
 */

public class Group {
    private String groupName,groupId,date;
    int image;

    public Group() {
    }

    public Group(String group , String groupid , int image , String date) {
        this.groupName = group;
        this.groupId = groupid;
        this.image = image;
        this.date = date;
    }

    public String getgroupName() {
        return groupName;
    }

    public void setgroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
