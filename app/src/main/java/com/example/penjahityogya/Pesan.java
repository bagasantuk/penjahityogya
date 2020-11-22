package com.example.penjahityogya;

import java.io.Serializable;

public class Pesan implements Serializable {
    String userId, usaha, email, Telp, password, Alamat, Jam, image, Jarak, longitude, latitude,mitraId,nama;


    public Pesan() {

    }

    //kelas adapter recyclerview


    public Pesan(String userId, String usaha, String email, String telp, String password, String alamat, String jam, String image, String jarak, String longitude, String latitude) {
        this.userId = userId;
        this.usaha = usaha;
        this.email = email;
        Telp = telp;
        this.password = password;
        Alamat = alamat;
        Jam = jam;
        this.image = image;
        Jarak = jarak;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getMitraId() {
        return mitraId;
    }

    public void setMitraId(String mitraId) {
        this.mitraId = mitraId;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getJarak() {
        return Jarak;
    }

    public void setJarak(String jarak) {
        Jarak = jarak;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
