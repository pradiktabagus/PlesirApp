package com.example.dickycn.plesirapp.profil;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dickycn.plesirapp.R;
import com.google.android.gms.internal.lv;

import java.io.File;
import java.util.ArrayList;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;

public class InputGambarWisata extends AppCompatActivity {

    Button simpanGambar;
    ListView lv;
    int MY_PERMISSIONS_REQUEST_READ_CONTACTS=10;
    ArrayList<String> filePaths= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_gambar_wisata);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lv= (ListView) findViewById(R.id.lv);
        simpanGambar = (Button)findViewById(R.id.uploadGaleri);

        simpanGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                lv.setAdapter(new CustomAdapter(InputGambarWisata.this,getData()));
                filePaths.clear();
                FilePickerBuilder.getInstance().setMaxCount(5)
                        .setSelectedFiles(filePaths)
                        .setActivityTheme(R.style.FilePickerTheme)
                        .pickPhoto(InputGambarWisata.this);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, 10);
            // TODO: Consider calling
        }

        switch (requestCode){
            case FilePickerConst.REQUEST_CODE:
                if(resultCode == RESULT_OK && data != null){
                    filePaths = data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_PHOTOS);
                    Spacecraft s;
                    ArrayList<Spacecraft> spacecrafts=new ArrayList<>();

                    try {
                        for (String path:filePaths){
                            s=new Spacecraft();
                            s.setName(path.substring(path.lastIndexOf("/")+1));

                            s.setUri(Uri.fromFile(new File(path)));
                            spacecrafts.add(s);
                        }
                        lv.setAdapter(new CustomAdapter(this,spacecrafts));
                        Toast.makeText(this, "Total = "+String.valueOf(spacecrafts.size()), Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        e.printStackTrace();

                    }
                }
        }

    }

    //    private ArrayList<Spacecraft> getData()
//    {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(new String[]{
//                    Manifest.permission.READ_EXTERNAL_STORAGE
//            }, 10);
//            // TODO: Consider calling
//            ArrayList<Spacecraft> spacecrafts=new ArrayList<>();
//            //TARGET FOLDER
//            File downloadsFolder= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//            Spacecraft s;
//            if(downloadsFolder.exists())
//            {
//                //GET ALL FILES IN DOWNLOAD FOLDER
//                File[] files=downloadsFolder.listFiles();
//                //LOOP THRU THOSE FILES GETTING NAME AND URI
//                for (int i=0;i<files.length;i++)
//                {
//                    File file=files[i];
//                    s=new Spacecraft();
//                    s.setName(file.getName());
//                    s.setUri(Uri.fromFile(file));
//                    spacecrafts.add(s);
//                }
//            }
//            return spacecrafts;
//
//
//        }else{
//            ArrayList<Spacecraft> spacecrafts=new ArrayList<>();
//            //TARGET FOLDER
//            File downloadsFolder= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//            Spacecraft s;
//            if(downloadsFolder.exists())
//            {
//                //GET ALL FILES IN DOWNLOAD FOLDER
//                File[] files=downloadsFolder.listFiles();
//                //LOOP THRU THOSE FILES GETTING NAME AND URI
//                for (int i=0;i<files.length;i++)
//                {
//                    File file=files[i];
//                    s=new Spacecraft();
//                    s.setName(file.getName());
//                    s.setUri(Uri.fromFile(file));
//                    spacecrafts.add(s);
//                }
//            }
//            return spacecrafts;
//        }
//
//    }


}
