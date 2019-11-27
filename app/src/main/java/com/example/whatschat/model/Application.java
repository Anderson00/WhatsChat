package com.example.whatschat.model;

public class Application {

    private static Application instance;
    private User currentUser;

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

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public User getCurrentUser(){
        return this.currentUser;
    }
}
