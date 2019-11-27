package com.example.whatschat.model;

import android.graphics.Bitmap;

import java.util.List;

public class User {

    private String name;
    private Bitmap profileImg;
    private String status;
    private String msgStatus;

    private List<HomeMessage> homeMsgs;

    public User(String name, Bitmap profileImg){
        this.name = name;
        this.profileImg = profileImg;
    }

    public Bitmap getProfileImg() {
        return profileImg;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getMsgStatus() {
        return msgStatus;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProfileImg(Bitmap profileImg) {
        this.profileImg = profileImg;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMsgStatus(String msgStatus) {
        this.msgStatus = msgStatus;
    }
}
