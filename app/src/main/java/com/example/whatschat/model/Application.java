package com.example.whatschat.model;

import com.google.firebase.auth.FirebaseUser;

public class Application {

    private static Application instance;
    private FirebaseUser currentUser;

    private Application(){

    }

    public static Application getInstance()
    {
        if (instance == null)
        {
            instance = new Application();
        }
        return instance;
    }

    public void setCurrentUser(FirebaseUser currentUser) {
        this.currentUser = currentUser;
    }

    public FirebaseUser getCurrentUser(){
        return this.currentUser;
    }
}
