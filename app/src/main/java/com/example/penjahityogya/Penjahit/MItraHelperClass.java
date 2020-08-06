package com.example.penjahityogya.Penjahit;
import android.widget.TextView;

public class MItraHelperClass {
    String userId, usaha, email, Telp, password, Alamat;

    public MItraHelperClass() {
    }

    public MItraHelperClass(String userId, String usaha, String email, String Telp, String password, String Alamat) {
        this.userId= userId;
        this.usaha = usaha;
        this.email = email;
        this.Telp = Telp;
        this.password = password;
        this.Alamat = Alamat;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsaha() {
        return usaha;
    }

    public void setUsaha(String usaha) {
        this.usaha = usaha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelp() {
        return Telp;
    }

    public void setTelp(String telp) {
        Telp = telp;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAlamat() {
        return Alamat;
    }

    public void setAlamat(String alamat) {
        Alamat = alamat;
    }
}
