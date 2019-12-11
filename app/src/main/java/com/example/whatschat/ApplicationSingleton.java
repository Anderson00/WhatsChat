package com.example.whatschat;

import com.example.whatschat.model.Usuario;
import com.google.firebase.auth.FirebaseUser;

public class ApplicationSingleton {

    private static ApplicationSingleton singleton = new ApplicationSingleton();

    private FirebaseUser user;
    private Usuario usuario;

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

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }
}
