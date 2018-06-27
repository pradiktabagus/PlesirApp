package com.example.dickycn.plesirapp.wisata;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dickycn.plesirapp.ApiVolley;
import com.example.dickycn.plesirapp.R;
import com.example.dickycn.plesirapp.rekomendasi.wisata;
import com.example.dickycn.plesirapp.webserviceURL;
import com.example.dickycn.plesirapp.wisata.adapter.detailWisataAdapter;
import com.example.dickycn.plesirapp.wisata.tujuanWisata.activityTujuan;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.dickycn.plesirapp.R.id.kategori;

public class detailWisataActivity extends AppCompatActivity {

    ViewPager viewPager;
    detailWisataAdapter adapter;
    ImageButton imageButton;
    Button more,less;
    TextView jud,deskr,dist;
    Context ctx;
    ImageView transport,medan,jarak;
    RelativeLayout tombol;
    Toolbar bar;
    ImageView label;
    ImageButton plesir;
    private List<wisata> wisataList = new ArrayList<wisata>();

    String nama,email,uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_wisata);
        jud=(TextView)findViewById(R.id.judul);
        deskr=(TextView) findViewById(R.id.deskripsi);
        transport=(ImageView)findViewById(R.id.transportasi);
        medan=(ImageView)findViewById(R.id.medanJalan);
        jarak=(ImageView)findViewById(R.id.gambarjarak);
        medan=(ImageView)findViewById(R.id.medanJalan);
        more=(Button) findViewById(R.id.selanjutnya);
        dist=(TextView)findViewById(R.id.jarak);
        tombol=(RelativeLayout)findViewById(R.id.desk_layout);
        bar=(Toolbar)findViewById(R.id.toolbar);
        label=(ImageView)findViewById(R.id.label);
        plesir=(ImageButton)findViewById(R.id.plesir);



        //inisial
        Intent i=getIntent();
        final int id=i.getIntExtra("id_wis",0);
        final String Judul=i.getStringExtra("judul");
        final String image=i.getStringExtra("im");
        //String alamat=i.getStringExtra("almt");
        String desk=i.getStringExtra("desk");
        //int rating=Integer.getInteger(i.getStringExtra("rating"));
        final Double lat=i.getDoubleExtra("lat",0);
        final Double longi=i.getDoubleExtra("longi",0);
        //String kat=i.getStringExtra("kat");
        int idkat=i.getIntExtra("idkat",0);
        int trans=i.getIntExtra("trans",0);
        String jar=Double.toString(i.getDoubleExtra("jarak",0));
        final String po=Integer.toString(id);
        wisata a= new wisata();
        a.setid(id);
        a.setTitle(Judul);
        a.setlat(lat);
        a.setlong(longi);
        a.setidkat(kategori);
        wisataList.add(a);

        //Toast.makeText(detailWisataActivity.this,po,Toast.LENGTH_SHORT).show();
       // Toast.makeText(detailWisataActivity.this,uid,Toast.LENGTH_SHORT).show();



        jud.setText(Judul);
        deskr.setText(desk);
        dist.setText(jar+" KM");
        deskr.setMaxLines(8);

        if(idkat==4){label.setImageResource(R.drawable.label_alam);}
        if(idkat==5){label.setImageResource(R.drawable.label_budaya);}
        if(idkat==6){label.setImageResource(R.drawable.label_sejarah);}
        if(idkat==7){label.setImageResource(R.drawable.label_religi);}
        if(idkat==8){label.setImageResource(R.drawable.label_hiburan);}

        if(trans==1){
            transport.setImageResource(R.drawable.ic_jalan);
            medan.setImageResource(R.drawable.ic_setapak);
        }
        if(trans==4){
            transport.setImageResource(R.drawable.ic_motor);
            medan.setImageResource(R.drawable.ic_setapak);
        }
        if(trans==3){
            transport.setImageResource(R.drawable.ic_mobil);
            medan.setImageResource(R.drawable.ic_aspal);
        }
        if(trans==2){
            transport.setImageResource(R.drawable.ic_bus);
            medan.setImageResource(R.drawable.ic_aspal);

        }

        more.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                more.setVisibility(View.INVISIBLE);
                //less.setVisibility(View.VISIBLE);
               deskr.setMaxLines(Integer.MAX_VALUE);
            }
        });
        deskr.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                more.setVisibility(View.VISIBLE);
                //less.setVisibility(View.INVISIBLE);
                deskr.setMaxLines(8);
            }
        });


        //imageButton = (ImageButton)findViewById(R.id.plesir);
        plesir.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(detailWisataActivity.this,
                        activityTujuan.class);
                intent.putExtra("title",Judul);
                intent.putExtra("id_wis",id);
                intent.putExtra("lat",lat);
                intent.putExtra("longi",longi);
                startActivity(intent);
            }
        });

        viewPager = (ViewPager)findViewById(R.id.slider);

        //image

        final HashMap<String,String> map= new HashMap<>();
        //String id_w=Integer.toString(id);
        JSONObject json = new JSONObject();
        ApiVolley req = new ApiVolley(this, json, "GET", webserviceURL.get_gambar+id, "", "", 0, new ApiVolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                Log.d("cekk",result.toString());

                // Important Note : need to use try catch when parsing JSONObject, no need when parsing string

                try {
                    JSONObject responseAPI = new JSONObject(result);
                    JSONArray arr = responseAPI.getJSONArray("response");
                    String status = responseAPI.getJSONObject("metadata").getString("status");
                    responseAPI = null;

                    for(int i=0;i<arr.length();i++){
                        JSONObject ar = arr.getJSONObject(i);
                        String no=Integer.toString(i);
                        //Log.d("cek url",ar.getString("url"));
                        String url=webserviceURL.lokasi_gbr_thumb+ar.getString("url");
                        map.put(no,url);
                    }
                    //Log.d("cek isi",ar.getString("judul_acara"));

                } catch (Exception e) {

                    e.printStackTrace();
//                    Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                    //Toast.makeText(getActivity(), "Terjadi kesalahan saat memuat data", Toast.LENGTH_LONG).show();
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String result) {
                Log.d("cek","eror");
            }
        });

        adapter = new detailWisataAdapter(detailWisataActivity.this,map);
        //end image

        viewPager.setAdapter(adapter);
    }


}
