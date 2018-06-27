package com.example.dickycn.plesirapp.profil;

import android.net.Uri;

/**
 * Created by dickyCN on 9/27/2017.
 */

public class Spacecraft {
    private String name;
    private Uri uri;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Uri getUri() {
        return uri;
    }
    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
