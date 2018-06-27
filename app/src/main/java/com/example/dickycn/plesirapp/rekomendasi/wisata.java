package com.example.dickycn.plesirapp.rekomendasi;

/**
 * Created by rejak on 8/22/2017.
 */

public class wisata {
    private String title, thumbnailUrl,deskrip,alamat,kategori;
    private Double lat,longi,jarak,lat1,longi1;
    int id,transport,kat,rating,nomer;
    public wisata(){

    }
    public wisata (int nomer,Double jarak,int kat,int id,String name, String thumbnailUrl,String deskrip,String alamat, Double lat, Double longi , Double lat1, Double longi1 , int rating, int transport ,String kategori){
        this.id=id;
        this.title = name;
        this.thumbnailUrl = thumbnailUrl;
        this.deskrip=deskrip;
        this.alamat =alamat;
        this.lat=lat;
        this.longi=longi;
        this.lat1=lat1;
        this.longi1=longi1;
        this.rating=rating;
        this.transport=transport;
        this.kategori=kategori;
        this.kat=kat;
        this.jarak=jarak;
        this.nomer=nomer;
    }

    public int getnomer(){return nomer;}
    public void setnomer(int nomer){this.nomer=nomer;}

    public int getid(){return id;}
    public void setid(int id){this.id=id;}

    public String getTitle(){
        return title;
    }

    public void setTitle(String name){
        this.title = name;
    }

    public String getThumbnailUrl(){
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl){
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getdeskrip(){
        return deskrip;
    }

    public void setdeskrip(String deskrip){
        this.deskrip = deskrip;
    }

    public String getalamat(){
        return alamat;
    }

    public void setalamat(String alamat){
        this.alamat = alamat;
    }

    public Double getlat(){
        return lat;
    }

    public void setlat(Double lat){
        this.lat = lat;
    }

    public Double getlong(){
        return longi;
    }

    public void setlong(Double longi){
        this.longi = longi;
    }

    public Double getlat1(){
        return lat1;
    }

    public void setlat1(Double lat1){
        this.lat1 = lat1;
    }

    public Double getlong1(){
        return longi1;
    }

    public void setlong1(Double longi1){
        this.longi1 = longi1;
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

    public String getkategori(){
        return kategori;
    }

    public void setkategori(String kategori){
        this.kategori = kategori;
    }

    public int getidkat(){
        return kat;
    }

    public void setidkat(int kat){
        this.kat = kat;
    }

    public Double getjarak(){
        return jarak;
    }

    public void setjarak(Double jarak){
        this.jarak = jarak;
    }
}

