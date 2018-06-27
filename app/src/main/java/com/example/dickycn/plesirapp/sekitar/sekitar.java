package com.example.dickycn.plesirapp.sekitar;

/**
 * Created by diktabagus on 26/08/2017.
 */

public class sekitar {
    private String namaWisata,img, kategori,alamat,deskrip;
    private Double latitude, longtitude;
    private int id_wisata,rating,transport;
    int idw;
    public sekitar(){

    }
    public sekitar(int idw,String namaWisata,String img, String kategori, String deskripsi,
                   String alamat, Double latitude, Double longtitude, int rating,int id_wisata,int transport){
        this.id_wisata = id_wisata;
        this.namaWisata = namaWisata;
        this.kategori = kategori;
        this.img = img;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.idw=idw;
        this.deskrip=deskripsi;
        this.alamat=alamat;
        this.rating=rating;
        this.transport=transport;
    }



    public String getNamaWisata() {
        return namaWisata;
    }

    public void setNamaWisata(String namaWisata) {
        this.namaWisata = namaWisata;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(Double longtitude) {
        this.longtitude = longtitude;
    }

    public int getId_wisata() {
        return id_wisata;
    }

    public void setId_wisata(int id_wisata) {
        this.id_wisata = id_wisata;
    }

    public int getid_wis(){return idw;}

    public void setIdw(int idw) {this.idw=idw;}

    public String getalamat(){
        return alamat;
    }

    public void setalamat(String alamat){
        this.alamat = alamat;
    }

    public String getdeskrip(){
        return deskrip;
    }

    public void setdeskrip(String deskrip){
        this.deskrip = deskrip;
    }

    public int getrating(){
        return rating;
    }

    public void setrating(int rating){
        this.rating = rating;
    }

    public int gettransport(){
        return transport;
    }

    public void settransport(int transport){
        this.transport = transport;
    }
}
