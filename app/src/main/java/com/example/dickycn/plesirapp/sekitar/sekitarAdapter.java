package com.example.dickycn.plesirapp.sekitar;

/**
 * Created by diktabagus on 26/08/2017.
 */

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dickycn.plesirapp.R;
import com.example.dickycn.plesirapp.webserviceURL;
import com.squareup.picasso.Picasso;

import java.util.List;

public class sekitarAdapter extends BaseAdapter {
    private Context mContext;
    private List<sekitar> sekitarList;
    private RelativeLayout sekitarlay;

    //Constructor
    public sekitarAdapter(Context c, List<sekitar> seklist){
        mContext = c;
        this.sekitarList=seklist;
    }

    @Override
    public int getCount(){

        return sekitarList.size();
    }

    @Override
    public Object getItem (int position){
        sekitarList.get(position);
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
        final sekitar s = sekitarList.get(position);
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.wisata_sekitar, parent,false);
        } else {
            grid = convertView;
        }

        sekitarlay=(RelativeLayout)grid.findViewById(R.id.layout_sekitar);
        TextView namaWisata = (TextView) grid.findViewById(R.id.nama_wisata);
        ImageView gambar_kotak=(ImageView) grid.findViewById(R.id.img_thumbnail);
        namaWisata.setText(s.getNamaWisata());

        if(s.getid_wis()==4){sekitarlay.setBackgroundColor(Color.parseColor("#27ae60"));}
        if(s.getid_wis()==5){sekitarlay.setBackgroundColor(Color.parseColor("#f39c12"));}
        if(s.getid_wis()==6){sekitarlay.setBackgroundColor(Color.parseColor("#d35400"));}
        if(s.getid_wis()==7){sekitarlay.setBackgroundColor(Color.parseColor("#8e44ad"));}
        if(s.getid_wis()==8){sekitarlay.setBackgroundColor(Color.parseColor("#3498db"));}

        String image_url = webserviceURL.lokasi_gbr_thumb+s.getImg();
        Picasso.with(mContext).load(image_url)
                .placeholder(R.drawable.img_default)
                .error(R.drawable.img_default).into(gambar_kotak);

        return grid;
    }
}
