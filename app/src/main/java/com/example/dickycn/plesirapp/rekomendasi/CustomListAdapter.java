package com.example.dickycn.plesirapp.rekomendasi;

import android.app.Activity;
import android.location.Geocoder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dickycn.plesirapp.R;
import com.example.dickycn.plesirapp.webserviceURL;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by rejak on 8/22/2017.
 */

public class CustomListAdapter extends BaseAdapter {
    Geocoder geocoder;


    public LatLng MyLoc,loc;
    public Double dist;
    private ImageView bingkai;
    private Activity context;
    private List<wisata> listwisata;
    private TextView alamat,jarak,kat,rating,add;
    private ImageView emblem;
    public CustomListAdapter(Activity context, List<wisata> listwisata) {
        this.context = context;
        this.listwisata = listwisata;
    }
    public String title;
    int num=1;

    @Override
    public int getCount() {
        if(num*10 > listwisata.size()){
            return listwisata.size();
        }else{
            return num*10;
        }
    }

    @Override
    public Object getItem(int i) {
        return listwisata.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.wisata_list, null,true);
        final wisata m = listwisata.get(position);
        Log.d("judul",m.getTitle());
        //image
        MyLoc=new LatLng(m.getlat1(),m.getlong1());
        loc=new LatLng(m.getlat(),m.getlong());
        dist=CalculationByDistance(MyLoc,loc);
        Double lat=m.getlat1();
        Double longi=m.getlong1();
        ImageView gbr=(ImageView) rowView.findViewById(R.id.coverImageView);
        TextView Judul1 = (TextView) rowView.findViewById(R.id.titleTextView);
        bingkai=(ImageView)rowView.findViewById(R.id.bingkai);
        alamat=(TextView)rowView.findViewById(R.id.alamat);
        jarak=(TextView)rowView.findViewById(R.id.jarak);
        kat=(TextView)rowView.findViewById(R.id.tit_bingkai);
        rating=(TextView)rowView.findViewById(R.id.rating);
        emblem=(ImageView)rowView.findViewById(R.id.emblem);


        if(m.getnomer()==0){emblem.setImageResource(R.drawable.ic_no1);}
        else if(m.getnomer()==1){emblem.setImageResource(R.drawable.ic_no2);}
        else if(m.getnomer()==2){emblem.setImageResource(R.drawable.ic_no3);}
        else {emblem.setVisibility(View.INVISIBLE);}

        if(m.getidkat()==4){bingkai.setImageResource(R.drawable.bing_alam);}
        if(m.getidkat()==5){bingkai.setImageResource(R.drawable.bing_bdya);}
        if(m.getidkat()==6){bingkai.setImageResource(R.drawable.bing_sejarah);}
        if(m.getidkat()==7){bingkai.setImageResource(R.drawable.bing_religi);}
        if(m.getidkat()==8){bingkai.setImageResource(R.drawable.bing_hiburan);}

        String image_url= webserviceURL.lokasi_gbr_thumb+m.getThumbnailUrl();
        Picasso.with(context).load(image_url)
                .placeholder(R.drawable.img_default) // optional
                .error(R.drawable.img_default).into(gbr);
        alamat.setText(m.getalamat());
        Judul1.setText(m.getTitle());
        kat.setText(m.getkategori());
        rating.setText("X "+m.getrating());
        jarak.setText(dist+" KM");
        return rowView;

    }
    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return kmInDec;
    }
    ;
}