package com.example.penjahityogya.Penjahit;

public class MItraHelperClass {
    String userId, usaha, email, Telp, password, Alamat, Jam, latitude, longitude, image;

    public MItraHelperClass() {
    }

    public MItraHelperClass(String userId, String usaha, String email, String Telp, String password, String Alamat, String Jam, String longitude, String latitude) {
        this.userId= userId;
        this.usaha = usaha;
        this.email = email;
        this.Telp = Telp;
        this.password = password;
        this.Alamat = Alamat;
        this.Jam = Jam;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public MItraHelperClass(String userId, String usaha, String email, String Telp, String password, String Alamat, String Jam, String longitude, String latitude, String image) {
        this.userId= userId;
        this.usaha = usaha;
        this.email = email;
        this.Telp = Telp;
        this.password = password;
        this.Alamat = Alamat;
        this.Jam = Jam;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
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

    public String getJam() {
        return Jam;
    }

    public void setJam(String jam) {
        Jam = jam;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
