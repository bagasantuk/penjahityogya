package com.example.penjahityogya.models;

public class Cart
{
    private String pid, pname, price, quantity,mitraId,category, date, time,hargabahan,namaUser,totOrder,total,idUser,status, mitra, nama;

    public Cart()
    {

    }

    public Cart(String pid)
    {
        this.pid = pid;
    }

    public Cart(String pid, String pname, String price, String quantity, String category, String status, String mitra)
    {
        this.pid = pid;
        this.pname = pname;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.status = status;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getHargabahan() {
        return hargabahan;
    }

    public void setHargabahan(String hargabahan) {
        this.hargabahan = hargabahan;
    }

    public String getNamaUser() {
        return namaUser;
    }

    public void setNamaUser(String namaUser) {
        this.namaUser = namaUser;
    }

    public String getTotOrder() {
        return totOrder;
    }

    public void setTotOrder(String totOrder) {
        this.totOrder = totOrder;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getMitraId() {
        return mitraId;
    }

    public void setMitraId(String mitraId) {
        this.mitraId = mitraId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMitra() {
        return mitra;
    }

    public void setMitra(String mitra) {
        this.mitra = mitra;
    }
}
