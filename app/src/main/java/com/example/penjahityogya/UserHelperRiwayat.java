package com.example.penjahityogya;

public class UserHelperRiwayat {

    private String category;
    private String date;
    private String pname;
    private String quantity;
    private String status;
    private String total;

    public UserHelperRiwayat(){

    }

    public UserHelperRiwayat(String category,String date,String pname,String quantity,String status,String total){
        this.category = category;
        this.date = date;
        this.pname = pname;
        this.quantity = quantity;
        this.status = status;
        this.total = total;
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

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
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

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

}
