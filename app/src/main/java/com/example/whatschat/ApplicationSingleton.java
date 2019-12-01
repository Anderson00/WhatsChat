package com.example.whatschat;

import com.google.firebase.auth.FirebaseUser;

public class ApplicationSingleton {

    private static ApplicationSingleton singleton = new ApplicationSingleton();

    private FirebaseUser user;

    private ApplicationSingleton(){

    }

    public static ApplicationSingleton getInstance(){
        return singleton;
    }

    public void setUser(FirebaseUser user) {
        this.user = user;
    }

    public FirebaseUser getUser() {
        return user;
    }
}
