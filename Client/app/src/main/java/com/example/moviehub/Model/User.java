package com.example.moviehub.Model;

public class User {

    private String username;
    private String password;
    private Boolean logged;

    public User(String username, String password, Boolean logged) {
        this.username = username;
        this.password = password;
        this.logged = logged;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean isLogged() {
        return logged;
    }

    public void setLogged(Boolean logged) {
        this.logged = logged;
    }
}
