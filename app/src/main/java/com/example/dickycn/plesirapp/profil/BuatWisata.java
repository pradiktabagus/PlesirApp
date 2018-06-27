package com.example.dickycn.plesirapp.profil;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dickycn.plesirapp.ApiVolley;
import com.example.dickycn.plesirapp.MainActivity;
import com.example.dickycn.plesirapp.R;
import com.example.dickycn.plesirapp.webserviceURL;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;

import static com.example.dickycn.plesirapp.R.drawable.posisi_kita;

public class BuatWisata extends AppCompatActivity {






    private MapView mMapView;
    private GoogleMap googleMap;
    private Button save,MarkMe;
    private Spinner kategori;
    private TextView nameIMG;
    private RadioGroup RgTrans;
    private RadioButton RbTrans;
    public Marker myLocation;
    public Double myLat,myLong;
    public LatLng MyLoc;
    Bitmap bitmap;


    private String[] lv_arr;
    ArrayAdapter<CharSequence> adapterKategori;

    private LocationManager locationManager;
    private LocationListener locationListener;

    Button simpanGambar;
    ListView lv;
    int MY_PERMISSIONS_REQUEST_READ_CONTACTS=10;
    ArrayList<String> filePaths= new ArrayList<>();

    ArrayAdapter<CharSequence> adapterKat;
    int id_kat;


    String transporasi;
    TextView namaWisata1, alamat,desk,pic;
    JSONObject jbody,jbody2;
    JSONArray Jarray1, Jarray2;

    String nama, deskrip, nomor, alamatWisata;


    private ProgressBar progressBar;
    private FrameLayout frameLayout;

    ViewPager image;
    buatWisataAdapter buatWisataAdapter;


    private FusedLocationProviderClient mFusedLocationClient;
    public static int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE =10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buat_wisata);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//
//        int PERMISSION_ALL = 1;
//        String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
//
//        if(!hasPermissions(this, PERMISSIONS)){
//            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
//        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE
                }, 10);
            }
            // TODO: Consider calling
        }

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        kategori = (Spinner) findViewById(R.id.kategori);
        RgTrans = (RadioGroup) findViewById(R.id.rgRute);
        adapterKat = ArrayAdapter.createFromResource(this, R.array.kategori, android.R.layout.simple_spinner_item);
        adapterKat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kategori.setAdapter(adapterKat);
        kategori.setSelection(0);

        kategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_kat = position + 3;
                Toast.makeText(BuatWisata.this, "position = " + id_kat, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        image = (ViewPager) findViewById(R.id.slider);

        //Manipulasi Maps get Mark

        MarkMe = (Button) findViewById(R.id.clickMark);
        mMapView = (MapView) findViewById(R.id.mapViewMark);
        mMapView.onCreate(savedInstanceState);
//        lv = (ListView) findViewById(R.id.lv);
        simpanGambar = (Button) findViewById(R.id.upload_gbr);
        simpanGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                lv.setAdapter(new CustomAdapter(InputGambarWisata.this,getData()));
                filePaths.clear();
                FilePickerBuilder.getInstance().setMaxCount(5)
                        .setSelectedFiles(filePaths)
                        .setActivityTheme(R.style.FilePickerTheme)
                        .pickPhoto(BuatWisata.this);

            }
        });

        mMapView.onResume();
        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        save = (Button) findViewById(R.id.saveWisata);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MakeWisata();
                Intent i=new Intent(BuatWisata.this,com.example.dickycn.plesirapp.MainActivity.class);
                startActivity(i);
                Toast.makeText(BuatWisata.this, "Berhasil Menambahkan Wisata , Tunggu konfirmasi dari admin", Toast.LENGTH_SHORT).show();
            }
        });
        MarkMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMarking();
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            Intent intent = getIntent();
            finish(); // close this activity and return to preview activity (if there is any)
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        final HashMap<String,String> map= new HashMap<>();
        int PERMISSION_ALL = 1;

        switch (requestCode){
            case FilePickerConst.REQUEST_CODE:
                if(resultCode == RESULT_OK && data != null){
                    filePaths = data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_PHOTOS);
                    Spacecraft s;
                    ArrayList<Spacecraft> spacecrafts=new ArrayList<>();

                    nameIMG = (TextView) findViewById(R.id.nameTxt);


                    try {
                        for (String path:filePaths){
                            s=new Spacecraft();
                            s.setName(path.substring(path.lastIndexOf("/")+1));

                            s.setUri(Uri.fromFile(new File(path)));
                            //Toast.makeText(this, "Path = "+ path, Toast.LENGTH_SHORT).show();
                            spacecrafts.add(s);
                        }

//                        lv.setAdapter(new CustomAdapter(this,spacecrafts));
                        //Toast.makeText(this, "Total = "+String.valueOf(spacecrafts.size()), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(this, "jumlah : "+ spacecrafts.size(), Toast.LENGTH_SHORT).show();


                        Jarray2 = new JSONArray();

                        for(int i=0; i <spacecrafts.size();i++){

                            jbody2 = new JSONObject();

                            String no=Integer.toString(i);
                            //Log.d("cek url",ar.getString("url"));
                            String url=spacecrafts.get(i).getUri().toString();
//                            Toast.makeText(BuatWisata.this, "url buat adapter= "+url, Toast.LENGTH_SHORT).show();
                            map.put(no,url);

                            InputStream imageStream = null;
                            try {
                                imageStream = BuatWisata.this.getContentResolver().openInputStream(
                                        spacecrafts.get(i).getUri());
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }

                            Bitmap bmp = BitmapFactory.decodeStream(imageStream);

                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bmp.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                            byte[] byteArray = stream.toByteArray();
                            bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                            bitmap = scaleDown(bitmap, 500, true);
                            Toast.makeText(this, "Gambar ke "+i+" sukses", Toast.LENGTH_SHORT).show();

                            try {
                                stream.close();
                                stream = null;
                            } catch (IOException e) {

                                e.printStackTrace();
                            }

                            String image = getStringImage(bitmap);

//                            Toast.makeText(this, "bitmap String : "+image, Toast.LENGTH_SHORT).show();
                            jbody2.put("gambar",image);

                            Jarray2.put(i,jbody2);
//                            Toast.makeText(this, "sukses buat array gambar", Toast.LENGTH_SHORT).show();
                        }

                        buatWisataAdapter = new buatWisataAdapter(BuatWisata.this,map);
                        //end image

                        image.setAdapter(buatWisataAdapter);
                    }catch (Exception e){
                        e.printStackTrace();

                    }
                }
        }

    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    public void rbClik(View v){
        int radioButtoChoose = RgTrans.getCheckedRadioButtonId();
        RbTrans = (RadioButton)findViewById(radioButtoChoose);

        Toast.makeText(this, "choose : "+RbTrans.getText(), Toast.LENGTH_SHORT).show();

        if(RbTrans.getText().equals("Jalan Kaki")){
            Toast.makeText(this, "pilih 1 jalan kaki", Toast.LENGTH_SHORT).show();
            transporasi = "1";

        }else if (RbTrans.getText().equals("Motor")){
            Toast.makeText(this, "pilih 2 Motor", Toast.LENGTH_SHORT).show();
            transporasi = "2";

        }else if (RbTrans.getText().equals("Mobil")){
            Toast.makeText(this, "pilih 3 Mobil", Toast.LENGTH_SHORT).show();
            transporasi = "3";

        }else if (RbTrans.getText().equals("Bis")){
            Toast.makeText(this, "pilih 4 bis", Toast.LENGTH_SHORT).show();
            transporasi = "4";

        }else {
            Toast.makeText(this, "nothing", Toast.LENGTH_SHORT).show();
        }
    }

    private void getMarking() {
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap1) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(BuatWisata.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(BuatWisata.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.INTERNET,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        }, 10);
                        // TODO: Consider calling
                        return;
                    } else {

                    }
                }
                googleMap = googleMap1;
                googleMap.setMyLocationEnabled(true);

                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(BuatWisata.this);

                mFusedLocationClient.getLastLocation().addOnSuccessListener(BuatWisata.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if(location != null){
                            MyLoc = new LatLng(location.getLatitude(),location.getLongitude());
                            myLat = location.getLatitude();
                            myLong = location.getLongitude();
                            float zoomLevel = 13; //This goes up to 21
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MyLoc, zoomLevel));
                            Toast.makeText(BuatWisata.this, "My Position : "+myLat+","+myLong, Toast.LENGTH_SHORT).show();
                            myLocation = googleMap.addMarker(new MarkerOptions().position(MyLoc)
                                    .title("My Location")
                                    .snippet("Dicky CN")
                                    .icon(BitmapDescriptorFactory.fromResource(posisi_kita)));
                        }else {
                            Toast.makeText(BuatWisata.this, "", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                locationManager = (LocationManager)BuatWisata.this.getSystemService(Context.LOCATION_SERVICE);
//
            }
        });
    }

    private void MakeWisata() {

        namaWisata1 = (TextView) findViewById(R.id.namaWisata);
        alamat = (TextView) findViewById(R.id.alamat);
        desk = (TextView) findViewById(R.id.descWisata);
        pic = (TextView) findViewById(R.id.contact);

        nama = namaWisata1.getText().toString();
        deskrip = desk.getText().toString();
        nomor = pic.getText().toString();
        alamatWisata = alamat.getText().toString();

        jbody= new JSONObject();
        Jarray1 = new JSONArray();

        try{
            jbody.put("id_kategori",id_kat);
            jbody.put("nm_wisata",nama);
            jbody.put("deskripsi",deskrip);
            jbody.put("pic_wisata",nomor);
            jbody.put("pengunjung",null);
            jbody.put("id_trans",transporasi);
            jbody.put("alamat",alamatWisata);
            jbody.put("lat",myLat);
            jbody.put("long",myLong);
            Log.d("jason",jbody.toString());
            //Toast.makeText(getActivity(), "succes make json", Toast.LENGTH_SHORT).show();
        } catch (JSONException e){
            e.printStackTrace();
        }

        Jarray1.put(jbody);

        UploadData(Jarray1,Jarray2);

    }


    public void UploadData(JSONArray a, JSONArray b){

        progressBar.setVisibility(android.view.View.VISIBLE);

        JSONObject jbodyFInal= new JSONObject();
        try{
            jbodyFInal.put("data",a);
            jbodyFInal.put("data2",b);

            Log.d("jason",jbodyFInal.toString());
            //Toast.makeText(this, "succes make jsonedit profile", Toast.LENGTH_SHORT).show();
        } catch (JSONException e){
            e.printStackTrace();
        }

        ApiVolley request = new ApiVolley(this, jbodyFInal, "POST", webserviceURL.create_wisata, "", "", 0, new ApiVolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                JSONObject response;

                Toast.makeText(BuatWisata.this, "Success Create Event", Toast.LENGTH_SHORT).show();
                try {
                    response = new JSONObject(result);
                    String status = response.getJSONObject("metadata").getString("status");
                    //Toast.makeText(BuatWisata.this, "Status : " + status, Toast.LENGTH_SHORT).show();
//                    Intent intent = this.getActivity().getIntent();
//                    this.getActivity().finish();
//                    startActivity(intent);

                    progressBar.setVisibility(android.view.View.GONE);
                    goMainScreen();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String result) {

            }

        });

    }



    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;

    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

    private void goMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
