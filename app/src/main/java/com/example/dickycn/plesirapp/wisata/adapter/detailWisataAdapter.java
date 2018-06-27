package com.example.dickycn.plesirapp.wisata.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.dickycn.plesirapp.R;
import com.example.dickycn.plesirapp.rekomendasi.wisata;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

/**
 * Created by diktabagus on 22/09/2017.
 */

public class detailWisataAdapter extends PagerAdapter {
    wisata a=new wisata();
   /* ;*/
    private HashMap<String,String> map;
    private Context ctx;
    private LayoutInflater layoutInflater;

    public detailWisataAdapter (Context ctx , HashMap<String,String> map){
        this.ctx = ctx;
        this.map=map;
    }

    @Override
    public int getCount() {
        return map.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == (LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view = layoutInflater.inflate(R.layout.slidding, container, false);
        ImageView imageView = (ImageView) item_view.findViewById(R.id.slider_image);

        String[] image_resource =new String[30];
//gambar
        for(int i=0;i<map.size();i++){
            String no=Integer.toString(i);
            image_resource[i]=map.get(no);
        }
        Picasso.with(ctx).load(image_resource[position])
                .placeholder(R.drawable.img_default) // optional
                .error(R.drawable.img_default).into(imageView);


        //imageView.setImageResource(image_resource[position]);
        container.addView(item_view);

        return item_view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
