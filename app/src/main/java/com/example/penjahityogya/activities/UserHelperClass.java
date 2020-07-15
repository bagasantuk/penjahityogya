package com.example.penjahityogya.activities;

import android.widget.TextView;

public class UserHelperClass {

    String userId, name, email, Telp, password;

    public UserHelperClass() {
    }

    public UserHelperClass(String userId, String name, String email, String Telp, String password) {
        this.userId= userId;
        this.name = name;
        this.email = email;
        this.Telp = Telp;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return Telp;
    }

    public void setPhoneNo(String Telp) {
        this.Telp = Telp;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
