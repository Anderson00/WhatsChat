package com.example.whatschat.model;

import java.util.ArrayList;
import java.util.List;

public class Usuario {

    private String uuid;
    private String username;
    private String email;
    private String profileIconURI;

    private List<String> contatos;

    public Usuario(){
        this.contatos = new ArrayList<>();
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

    public List<String> getContatos() {
        return contatos;
    }

    public void setContatos(List<String> contatos) {
        this.contatos = contatos;
    }

    public void addContato(String uuid){
        this.contatos.add(uuid);
    }
}
