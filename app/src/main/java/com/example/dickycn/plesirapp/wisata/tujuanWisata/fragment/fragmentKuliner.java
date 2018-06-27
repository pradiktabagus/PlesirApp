package com.example.dickycn.plesirapp.wisata.tujuanWisata.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dickycn.plesirapp.R;


/**
 * Created by diktabagus on 27/09/2017.
 */

public class fragmentKuliner extends Fragment {
    public fragmentKuliner(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kuliner, container, false);
        return view;
    }
}
