package com.example.penjahityogya.Penjahit;

public class HelperUkuran {
    String pinggang, pinggul, paha, selangkangan, lutut, pergelangan, panjang;
    String bahu, dada, leher, ketiak, perut, panjangtangan;
    String idUser,namaUser;

    public HelperUkuran(){

    }
    public HelperUkuran(String idUser, String namaUser){
        this.idUser = idUser;
        this.namaUser = namaUser;
    }
    public HelperUkuran ( String bahu, String dada, String leher, String ketiak, String perut, String pinggul, String pergelangan, String panjangtangan, String panjang) {
        this.bahu = bahu;
        this.dada = dada;
        this.leher = leher;
        this.ketiak = ketiak;
        this.perut = perut;
        this.pinggul = pinggul;
        this.pergelangan = pergelangan;
        this.panjangtangan = panjangtangan;
        this.panjang = panjang;
    }
    public HelperUkuran ( String pinggang, String pinggul, String paha, String selangkangan, String lutut, String pergelangan, String panjang){
        this.pinggang = pinggang;
        this.pinggul = pinggul;
        this.paha = paha;
        this.selangkangan = selangkangan;
        this.lutut = lutut;
        this.pergelangan = pergelangan;
        this.panjang = panjang;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getNamaUser() {
        return namaUser;
    }

    public void setNamaUser(String namaUser) {
        this.namaUser = namaUser;
    }

    public String getPinggang() {
        return pinggang;
    }

    public void setPinggang(String pinggang) {
        this.pinggang = pinggang;
    }

    public String getPinggul() {
        return pinggul;
    }

    public void setPinggul(String pinggul) {
        this.pinggul = pinggul;
    }

    public String getPaha() {
        return paha;
    }

    public void setPaha(String paha) {
        this.paha = paha;
    }

    public String getSelangkangan() {
        return selangkangan;
    }

    public void setSelangkangan(String selangkangan) {
        this.selangkangan = selangkangan;
    }

    public String getLutut() {
        return lutut;
    }

    public void setLutut(String lutut) {
        this.lutut = lutut;
    }

    public String getPergelangan() {
        return pergelangan;
    }

    public void setPergelangan(String pergelangan) {
        this.pergelangan = pergelangan;
    }

    public String getPanjang() {
        return panjang;
    }

    public void setPanjang(String panjang) {
        this.panjang = panjang;
    }

    public String getBahu() {
        return bahu;
    }

    public void setBahu(String bahu) {
        this.bahu = bahu;
    }

    public String getDada() {
        return dada;
    }

    public void setDada(String dada) {
        this.dada = dada;
    }

    public String getLeher() {
        return leher;
    }

    public void setLeher(String leher) {
        this.leher = leher;
    }

    public String getKetiak() {
        return ketiak;
    }

    public void setKetiak(String ketiak) {
        this.ketiak = ketiak;
    }

    public String getPerut() {
        return perut;
    }

    public void setPerut(String perut) {
        this.perut = perut;
    }

    public String getPanjangtangan() {
        return panjangtangan;
    }

    public void setPanjangtangan(String panjangtangan) {
        this.panjangtangan = panjangtangan;
    }
}
