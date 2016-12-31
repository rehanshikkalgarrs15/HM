package com.example.rehanr.hmcashew.Models;

/**
 * Created by rehan r on 31-12-2016.
 */
public class User {
    String email,password;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
