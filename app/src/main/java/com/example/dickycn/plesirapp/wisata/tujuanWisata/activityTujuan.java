package com.example.dickycn.plesirapp.wisata.tujuanWisata;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.example.dickycn.plesirapp.ApiVolley;
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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.example.dickycn.plesirapp.R.id.map;

//
//import com.roughike.bottombar.BottomBar;
//import com.roughike.bottombar.OnTabSelectListener;

/**
 * Created by diktabagus on 26/09/2017.
 */

public class activityTujuan extends AppCompatActivity {
    //    private BottomBar bottomBar;
    private MapView mMapView;
    private BottomSheetBehavior mBottomSheetBehavior;
    Context ctx;
    private GoogleMap googleMap;
    public Double myLat,myLong;
    public LatLng MyLoc;
    private LocationManager locationManager;
    private LocationListener locationListener;
    public Marker myLocation;
    public TextView judul;
    private String[] colors = {"#7fff7272", "#7f31c7c5", "#7fff8a00"};
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private FusedLocationProviderClient mFusedLocationClient;
    ProgressBar progressBar;

    Marker kulinerMarker;

    private Typeface slim_joe = null;
    TextView infoKuliner;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tujuan);
        FirebaseApp.initializeApp(activityTujuan.this);
        final View bottomSheet = findViewById( R.id.bottom_sheet );
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setPeekHeight(250);


        slim_joe = Typeface.createFromAsset(getAssets(), "Slim_Joe.otf");
        infoKuliner = (TextView)findViewById(R.id.info_kuliner);
        infoKuliner.setTypeface(slim_joe);

        final Double lat=getIntent().getDoubleExtra("lat",0);
        final Double longi=getIntent().getDoubleExtra("longi",0);
        final int idwis=getIntent().getIntExtra("id_wis",0);
        final String jud=getIntent().getStringExtra("title");
        String wiss=Double.toString(lat);

        progressBar=(ProgressBar)findViewById(R.id.progress_bar);
        //Toast.makeText(activityTujuan.this,"latitude : "+Double.toString(longi),Toast.LENGTH_SHORT).show();
        //Toast.makeText(activityTujuan.this,"id wisata : "+ idwis,Toast.LENGTH_SHORT).show();

        judul=(TextView)findViewById(R.id.judul);
        judul.setText(jud);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                } else {
                   // Toast.makeText(activityTujuan.this, "user kosong", Toast.LENGTH_SHORT).show();
                }
               // Toast.makeText(activityTujuan.this, "username diluar = "+user.getDisplayName(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(activityTujuan.this, "id diluar = "+user.getUid(), Toast.LENGTH_SHORT).show();
                String idu=user.getUid();
                JSONObject json1 = new JSONObject();
                try{
                    json1.put("id_wisata",idwis);
                    json1.put("id_user",idu);
                }

                catch (Exception e){e.printStackTrace();}
                ApiVolley req1 = new ApiVolley(activityTujuan.this, json1, "POST", webserviceURL.insert_pengunjung, "", "", 0, new ApiVolley.VolleyCallback() {

                    @Override
                    public void onSuccess(String result) {
                        Log.d("cekk",result.toString());

                        //Toast.makeText(activityTujuan.this, "sukksesss upload", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String result) {
                        Log.d("cek","eror");
                        //Toast.makeText(activityTujuan.this, "erorrrr", Toast.LENGTH_SHORT).show();
                    }
                });
            }


        };
        //insert kuliner


        //insert plesir

        mMapView = (MapView) findViewById(map);
        mMapView.onCreate(savedInstanceState);


        mMapView.onResume();
        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap1) {
                googleMap = googleMap1;

                if (ActivityCompat.checkSelfPermission(activityTujuan.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activityTujuan.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.INTERNET
                        }, 10);
                    }
                    // TODO: Consider calling
                    return;
                } else {
                }
                googleMap.setMyLocationEnabled(true);

                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activityTujuan.this);

                mFusedLocationClient.getLastLocation().addOnSuccessListener(activityTujuan.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if(location != null){
                            MyLoc = new LatLng(location.getLatitude(),location.getLongitude());
                            myLat = location.getLatitude();
                            myLong = location.getLongitude();

                            CameraPosition newCamPos = new CameraPosition(new LatLng(myLat,myLong),
                                    15.5f,
                                    googleMap.getCameraPosition().tilt, //use old tilt
                                    googleMap.getCameraPosition().bearing); //use old bearing
                            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(newCamPos), 1000, new GoogleMap.CancelableCallback() {
                                @Override
                                public void onFinish() {

                                }

                                @Override
                                public void onCancel() {

                                }
                            });

                            //Toast.makeText(activityTujuan.this, "My Positions saya aktiviti : "+lat+","+longi, Toast.LENGTH_SHORT).show();

                            String serverKey = "AIzaSyBEeKpdUkoRmhnRrHCKMMRWOvquEgQkxDc";
                            final LatLng origin = new LatLng(myLat, myLong);
                            final LatLng destination = new LatLng(lat, longi);


                            GoogleDirection.withServerKey(serverKey)
                                    .from(origin)
                                    .to(destination)
                                    .execute(new DirectionCallback() {
                                        @Override
                                        public void onDirectionSuccess(Direction direction, String rawBody) {
                                            if (direction.isOK()) {
                                                int marker=R.drawable.pin_hiburan;
                                                int marker1=R.drawable.pin_alam;
                                                googleMap.addMarker(new MarkerOptions().position(destination).icon(BitmapDescriptorFactory.fromResource(marker1)));

                                                for (int i = 0; i < direction.getRouteList().size(); i++) {
                                                    Route route = direction.getRouteList().get(i);
                                                    String color = colors[i % colors.length];
                                                    ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();
                                                    googleMap.addPolyline(DirectionConverter.createPolyline(activityTujuan.this, directionPositionList, 5, Color.parseColor(color)));
                                                }

                                                //btnRequestDirection.setVisibility(View.GONE);
                                            }
                                        }

                                        @Override
                                        public void onDirectionFailure(Throwable t) {
                                            // Do something here
                                        }
                                    });

                        }else {
                            //Toast.makeText(activityTujuan.this, "location null", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        MyLoc = new LatLng(location.getLatitude(),location.getLongitude());
                        if(myLocation != null){
                            myLocation.remove();


                        }else {

                        }
                    }

                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) {

                    }

                    @Override
                    public void onProviderEnabled(String s) {

                    }

                    @Override
                    public void onProviderDisabled(String s) {
                        Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(i);

                    }
                };
                locationManager.requestLocationUpdates("gps",1000,1,locationListener);



                googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {


                    @Override
                    public View getInfoWindow(Marker marker) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker marker) {
                        progressBar.setVisibility(View.VISIBLE);
                        LatLng ll = new LatLng(0,0);
//
                        TextView namaKuliner = (TextView) bottomSheet.findViewById(R.id.judul_mak);

                        ll = marker.getPosition();
                        namaKuliner.setText(marker.getTitle());
                        String idKulinerTampil = marker.getSnippet();

                        System.out.println("id kuliner sini= " +idKulinerTampil);


                        JSONObject json1 = new JSONObject();
                        JSONObject json2 = new JSONObject();
                        JSONObject json3 = new JSONObject();
                        ApiVolley req = new ApiVolley(activityTujuan.this, json1, "GET", webserviceURL.get_kuliner2+idKulinerTampil, "", "", 0, new ApiVolley.VolleyCallback() {
                            @Override
                            public void onSuccess(String result) {
                                Log.d("cekk", result.toString());
                                // Important Note : need to use try catch when parsing JSONObject, no need when parsing string

                                try {
                                    JSONObject responseAPI = new JSONObject(result);
                                    JSONArray arr = responseAPI.getJSONArray("response");
                                    String status = responseAPI.getJSONObject("metadata").getString("status");
                                    responseAPI = null;
                                    for (int i = 0; i < arr.length(); i++) {
                                        TextView jamBuka = (TextView) bottomSheet.findViewById(R.id.jam);
                                        JSONObject ar = arr.getJSONObject(i);
                                        jamBuka.setText("Buka : "+ar.getString("jam_buka"));


                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(String result) {

                            }
                        });

                        ApiVolley reqMenuKuliner = new ApiVolley(activityTujuan.this, json3, "GET", webserviceURL.get_MenuKuliner+idKulinerTampil, "", "", 0, new ApiVolley.VolleyCallback() {
                            @Override
                            public void onSuccess(String result) {
                                Log.d("cekk", result.toString());
                                // Important Note : need to use try catch when parsing JSONObject, no need when parsing string

                                try {
                                    JSONObject responseAPI = new JSONObject(result);
                                    JSONArray arr = responseAPI.getJSONArray("response");
                                    String status = responseAPI.getJSONObject("metadata").getString("status");
                                    responseAPI = null;

                                    final LinearLayout relaView = (LinearLayout)findViewById(R.id.menu);
                                    relaView.removeAllViews();
                                    for (int i = 0; i < arr.length(); i++) {



                                        JSONObject ar = arr.getJSONObject(i);

                                        final TextView[] myTextViewsNama = new TextView[ar.length()]; // create an empty array;
                                        final TextView[] myTextViewsHarga = new TextView[ar.length()];

                                        // create a new textview
                                        myTextViewsNama[i] = new TextView(activityTujuan.this);
                                        myTextViewsHarga[i] = new TextView(activityTujuan.this);

                                        // set some properties of rowTextView or something
                                        myTextViewsNama[i].setText("+ "+ar.getString("nama_menu"));
                                        myTextViewsNama[i].setTextSize(10);
                                        myTextViewsHarga[i].setText("   Rp. "+ar.getString("harga_menu")+",-");
                                        myTextViewsHarga[i].setTextSize(10);

                                        // add the textview to the linearlayout
                                        relaView.addView(myTextViewsNama[i]);
                                        relaView.setOrientation(LinearLayout.HORIZONTAL);
                                        relaView.addView(myTextViewsHarga[i]);
                                        relaView.setOrientation(LinearLayout.VERTICAL);
                                        if(i<arr.length()-1){progressBar.setVisibility(View.INVISIBLE);}

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(String result) {

                            }
                        });

                        ApiVolley req1 = new ApiVolley(activityTujuan.this, json2, "GET", webserviceURL.get_GMBkuliner+idKulinerTampil, "", "", 0, new ApiVolley.VolleyCallback() {
                            @Override
                            public void onSuccess(String result) {
                                Log.d("cekk", result.toString());
                                // Important Note : need to use try catch when parsing JSONObject, no need when parsing string

                                try {
                                    JSONObject responseAPI = new JSONObject(result);
                                    JSONArray arr = responseAPI.getJSONArray("response");
                                    String status = responseAPI.getJSONObject("metadata").getString("status");
                                    responseAPI = null;
                                    for (int i = 0; i < arr.length(); i++) {
                                        ImageView imageKuliner = (ImageView) bottomSheet.findViewById(R.id.imageView6);
                                        JSONObject ar = arr.getJSONObject(i);
                                        Toast.makeText(activityTujuan.this, "idGambar : "+ar.getString("idg"), Toast.LENGTH_SHORT).show();
                                        String url=webserviceURL.lokasi_gbr_kuliner+ar.getString("gambar");

                                        Picasso.with(activityTujuan.this).load(url)
                                                .placeholder(R.drawable.img_default) // optional
                                                .error(R.drawable.img_default).into(imageKuliner);

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(String result) {

                            }
                        });




                        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                            @Override
                            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                                    mBottomSheetBehavior.setPeekHeight(250);

                                    //fareDetail.setVisibility(View.GONE);
                                } else {
//                                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                                }
                                //fareDetail.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                            }
                        });
//
                        return null;
                    }
                });

            }
        });

        //mark
        JSONObject json = new JSONObject();
        ApiVolley req = new ApiVolley(activityTujuan.this, json, "GET", webserviceURL.get_kuliner, "", "", 0, new ApiVolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                Log.d("cekk", result.toString());
                // Important Note : need to use try catch when parsing JSONObject, no need when parsing string

                try {
                    JSONObject responseAPI = new JSONObject(result);
                    JSONArray arr = responseAPI.getJSONArray("response");
                    String status = responseAPI.getJSONObject("metadata").getString("status");
                    responseAPI = null;
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject ar = arr.getJSONObject(i);
                        final String namaKuliner = ar.getString("nama_kuliner");
                        final String alamatKuliner = ar.getString("alamat_kuliner");
                        final String jamBuka1232 = ar.getString("jam_buka");

                        final String idKuliner = ar.getString("id_kuliner");
                        System.out.println("id kuliner = " +idKuliner);
                        Double locationWisataLat = Double.parseDouble(ar.getString("latitude")),
                                locationWisataLong=Double.parseDouble(ar.getString("longitude"));

                        LatLng locWisata = new LatLng(locationWisataLat,locationWisataLong);
                        int marker;
                        marker=R.drawable.pin_kuliner;

                        setKulinerMarker(namaKuliner, jamBuka1232, locWisata, marker, idKuliner);



                    }
                    //Log.d("cek isi",ar.getString("judul_acara"));

                } catch (Exception e) {

                    e.printStackTrace();
//                    Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(String result) {

            }
        });

    }

    private void setKulinerMarker(String namaKuliner, String jambuka, LatLng locWisata, int marker, String id) {
        kulinerMarker = googleMap.addMarker(new MarkerOptions().position(locWisata)
                .title(namaKuliner)
                .snippet(id)
                .icon(BitmapDescriptorFactory.fromResource(marker)));
    }


    private void setUserData(FirebaseUser user) {
        //Toast.makeText(this, "user name : " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, "id user : " + user.getUid(), Toast.LENGTH_SHORT).show();



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
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }


}