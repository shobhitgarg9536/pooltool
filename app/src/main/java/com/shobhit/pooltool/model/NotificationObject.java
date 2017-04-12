package com.shobhit.pooltool.model;

/**
 * Created by Shobhit-pc on 1/8/2017.
 */

public class NotificationObject {

    String notificationView;
    String date, groupId;
    int image;

    public NotificationObject(String notificationView , String date , int image, String groupId){
        this.notificationView = notificationView;
        this.date = date;
        this.image = image;
        this.groupId = groupId;
    }

    public String getNotificationView() {
        return notificationView;
    }

    public void setNotificationView(String notificationView) {
        this.notificationView = notificationView;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        image = image;
    }
}
