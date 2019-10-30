package com.example.whatschat.model;

import android.widget.TextView;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeMessage implements Serializable {
    private String imgProfile; // File Directory
    private String nameProfile;
    private String msgProfile;
    private Date dateMsgProfile;
    private int msgQuant;

    public HomeMessage(String imgProfile, String nameProfile, String msgProfile, Date datMsgProfile){
        this.imgProfile = imgProfile;
        this.nameProfile = nameProfile;
        this.msgProfile = msgProfile;
        this.dateMsgProfile = datMsgProfile;
    }

    public String getImgProfile() {
        return imgProfile;
    }

    public void setImgProfile(String imgProfile) {
        this.imgProfile = imgProfile;
    }

    public String getNameProfile() {
        return nameProfile;
    }

    public void setNameProfile(String nameProfile) {
        this.nameProfile = nameProfile;
    }

    public String getMsgProfile() {
        return msgProfile;
    }

    public void setMsgProfile(String msgProfile) {
        this.msgProfile = msgProfile;
    }

    public String getDateMsgProfile() {
        SimpleDateFormat datFormat = new SimpleDateFormat("dd/MM/yyy");
        if(datFormat.format(dateMsgProfile).equals(datFormat.format(new Date()))){
            SimpleDateFormat datForm = new SimpleDateFormat("HH:mm");
            return datForm.format(dateMsgProfile);
        }
        return datFormat.format(dateMsgProfile);

    }

    public void setDateMsgProfile(Date dateMsgProfile) {
        this.dateMsgProfile = dateMsgProfile;
    }

    public int getMsgQuant() {
        return msgQuant;
    }

    public void setMsgQuant(int msgQuant) {
        this.msgQuant = msgQuant;
    }
}
