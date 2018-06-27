package com.example.dickycn.plesirapp.profil;

/**
 * Created by diktabagus on 23/08/2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dickycn.plesirapp.R;
import com.example.dickycn.plesirapp.webserviceURL;
import com.squareup.picasso.Picasso;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.Date;
import java.util.List;

public class profilAdapter extends BaseAdapter{
    private Context mContext;
    private List<profil> profilList;



    //Constructor
    public profilAdapter(Context c, List<profil> proflist){
        mContext = c;
        this.profilList=proflist;
    }

    @Override
    public int getCount(){

        return profilList.size();
    }

    @Override
    public Object getItem (int position){
        profilList.get(position);
        return null;
    }

    @Override
    public long getItemId(int position){

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        //TODO Auto-generate method stub
        View grid;
        TextView nama_wisata,hari;
        ImageView img_thumbnail;
        Date date = null;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        JodaTimeAndroid.init(mContext);
        final profil p = profilList.get(position);
        if (convertView == null){
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.recent_profil, null);
            nama_wisata=(TextView)grid.findViewById(R.id.nama_wisata);
            img_thumbnail=(ImageView)grid.findViewById(R.id.img_thumbnail);
            TextView tanggal =(TextView)grid.findViewById(R.id.tanggal);
           // hari=(TextView)grid.findViewById(R.id.hari);
          /*  String dateStr=p.getTanggal();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date2 = Calendar.getInstance().getTime();
            try {
                date = sdf.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int days = Days.daysBetween(date, date2).getDays();*/
            nama_wisata.setText(p.getJudul());
            String tgl =p.getTanggal();
            String sub=tgl.substring(0,11);
            tanggal.setText("Tanggal : "+sub);
            String image_url = webserviceURL.lokasi_gbr_thumb+p.getImg();
            Picasso.with(mContext).load(image_url)
                    .placeholder(R.drawable.img_default).into(img_thumbnail);

        } else {
            grid = (View) convertView;
        }
        return grid;
    }
}
