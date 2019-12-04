package com.example.whatschat.model;

public class Usuario {

    private String uuid;
    private String username;
    private String email;
    private String profileIconURI;

    public Usuario(){

    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileIconURI() {
        return profileIconURI;
    }

    public void setProfileIconURI(String profileIconURI) {
        this.profileIconURI = profileIconURI;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
