package com.example.dickycn.plesirapp.profil;

/**
 * Created by diktabagus on 24/08/2017.
 */

public class profil {
    private String nama, judul, img, id_user,tanggal;

    public profil(){

    }
    public profil(String tanggal,String nama, String judul, String img, String id_user){
        this.id_user = id_user;
        this.tanggal=tanggal;
        this.nama = nama;
        this.judul = judul;
        this.img = img;
    }
    public String getTanggal(){
        return tanggal;
    }
    public void settanggal(String tanggal){
        this.tanggal = tanggal;
    }
    public String getId_user(){
        return id_user;
    }
    public void setId_user(String id_user){
        this.id_user = id_user;
    }
    public String getNama(){
        return nama;
    }
    public void setNama(String nama){
        this.nama = nama;
    }
    public String getJudul(){
        return judul;
    }
    public void setJudul(String judul){
        this.judul = judul;
    }
    public String getImg(){
        return img;
    }
    public void setImg(String img){
        this.img = img;
    }
}
