package com.example.dickycn.plesirapp.sekitar;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

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
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class sekitarTab extends Fragment{
    //swipe
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    GridView grid;
    TextView title;
    private ProgressDialog pDialog;
    private List<sekitar> seklist = new ArrayList<sekitar>();
    public ImageView full,less,peringatan,full1,less1;
    public View v;
    private MapView mMapView;
    private GoogleMap googleMap;
    public String provider;
    public LatLng userLocation;
    private LocationManager locationManager;
    private LocationListener locationListener;
    public Marker myLocation;
    public Double myLat,myLong;
    public LatLng MyLoc, wisata1;
    public RelativeLayout map_lay,list_lay;
    String kategori="";
    Button alam,budaya,sejarah,religi,hiburan,semua;
    TextView radius;
    int rad=50;
    SeekBar seekBar;
    CircleOptions circleOptions;
    LinearLayout layout_kat,map_lay1;
    ProgressBar linlaHeaderProgress;
    TextView addre,kilo,judul,pilih;
//    private OnFragmentInteractionListener mListener;

//    public double myLong, myLa;

    //percobaan ke dua
    private FusedLocationProviderClient mFusedLocationClient;


    public sekitarTab() {

        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static sekitarTab newInstance(String param1, String param2) {
        sekitarTab fragment = new sekitarTab();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(com.example.dickycn.plesirapp.R.layout.fragment_sekitar_tab, container, false);
         seekBar = (SeekBar) v.findViewById(R.id.seekbar);
        radius = (TextView)v.findViewById(R.id.radius);
        full=(ImageView)v.findViewById(R.id.full);
        less=(ImageView)v.findViewById(R.id.less);
        full1=(ImageView)v.findViewById(R.id.full1);
        less1=(ImageView)v.findViewById(R.id.less1);
        map_lay=(RelativeLayout)v.findViewById(R.id.map_lay);
        map_lay1=(LinearLayout)v.findViewById(R.id.map_lay1);
        list_lay=(RelativeLayout)v.findViewById(R.id.list_lay);
        peringatan=(ImageView)v.findViewById(R.id.peringatan);
        less.setVisibility(View.INVISIBLE);
        less1.setVisibility(View.INVISIBLE);
        mSwipeRefreshLayout  = (SwipeRefreshLayout) v.findViewById(R.id.swipeSekitar);
        layout_kat=(LinearLayout)v.findViewById(R.id.but_kat_layout);
        final LinearLayout.LayoutParams hilang=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0);
        final LinearLayout.LayoutParams muncul=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        pilih=(TextView)v.findViewById(R.id.pilih);
        addre=(TextView)v.findViewById(R.id.addr);
        kilo=(TextView)v.findViewById(R.id.kilo);
        judul=(TextView)v.findViewById(R.id.semua_wis);
        linlaHeaderProgress = (ProgressBar) v.findViewById(R.id.linlaHeaderProgress);

        linlaHeaderProgress.setVisibility(View.INVISIBLE);

        //get address
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mFusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    MyLoc = new LatLng(location.getLatitude(),location.getLongitude());
                    myLat = location.getLatitude();
                    myLong = location.getLongitude();
                    try {
                        Geocoder geocoder;
                        List<Address> addresses;

                        geocoder = new Geocoder(getActivity(), Locale.getDefault());
                        addresses = geocoder.getFromLocation(myLat, myLong, 1);

                        String address = addresses.get(0).getAddressLine(0);
                        String city = addresses.get(0).getAddressLine(1);
                        String country = addresses.get(0).getAddressLine(2);

                        if(address==null)
                            address="";
                        if(city==null)
                            city="";
                        if(country==null)
                            country="";

                        addre.setText(address+"-"+city+"-"+country);
                    } catch (Exception e) {
                    }

                }else {
                    //Toast.makeText(getActivity(), "location null", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //end get address

        //click
        layout_kat.setLayoutParams(hilang);
        //less.setVisibility(View.VISIBLE);
        full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pilih.setVisibility(View.INVISIBLE);
                less.setVisibility(View.VISIBLE);
                full.setVisibility(View.INVISIBLE);
                LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                map_lay1.setLayoutParams(params);
            }
        });

        less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pilih.setVisibility(View.INVISIBLE);
                layout_kat.setLayoutParams(hilang);
                less.setVisibility(View.INVISIBLE);
                full.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,700);
                map_lay1.setLayoutParams(params);
            }
        });

        full1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pilih.setVisibility(View.VISIBLE);
                layout_kat.setLayoutParams(muncul);
                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                less1.setVisibility(View.VISIBLE);
                full1.setVisibility(View.INVISIBLE);
                LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0);
                map_lay1.setLayoutParams(params);
            }
        });

        less1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pilih.setVisibility(View.INVISIBLE);
                layout_kat.setLayoutParams(hilang);
                less1.setVisibility(View.INVISIBLE);
                full1.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,700);
                map_lay1.setLayoutParams(params);
            }
        });

        mMapView = (MapView) v.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap1) {
                googleMap = googleMap1;
                if (ActivityCompat.checkSelfPermission(v.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(v.getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.INTERNET
                    }, 10);
                    // TODO: Consider calling
                    return;
                } else {
                }
                googleMap.setMyLocationEnabled(true);

                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(v.getContext());

                mFusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if(location != null){
                            MyLoc = new LatLng(location.getLatitude(),location.getLongitude());
                            myLat = location.getLatitude();
                            myLong = location.getLongitude();
                            float zoomLevel = 13; //This goes up to 21
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MyLoc, zoomLevel));
                        }else {
                            //Toast.makeText(getActivity(), "location null", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



                locationManager = (LocationManager)v.getContext().getSystemService(Context.LOCATION_SERVICE);
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
            }
        });

        //show wisata
        all_wisata_kat(kategori,25);

        //button kategori
        alam=(Button)v.findViewById(R.id.k_alam);
        alam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                judul.setText("Wisata Alam");
                kilo.setText("");
                kategori="4";
                googleMap.clear();
                all_wisata(kategori);
            }
        });
        budaya=(Button)v.findViewById(R.id.k_budaya);
        budaya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                judul.setText("Wisata Budaya");
                kilo.setText("");
                kategori="5";
                googleMap.clear();
                all_wisata(kategori);
            }
        });
        sejarah=(Button)v.findViewById(R.id.k_sejarah);
        sejarah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                judul.setText("Wisata Sejarah");
                kilo.setText("");
                kategori="6";
                googleMap.clear();
                all_wisata(kategori);
            }
        });
        religi=(Button)v.findViewById(R.id.k_religi);
        religi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                judul.setText("Wisata Religi");
                kilo.setText("");
                kategori="7";
                googleMap.clear();
                all_wisata(kategori);
            }
        });
        hiburan=(Button)v.findViewById(R.id.k_hiburan);
        hiburan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                judul.setText("Wisata Hiburan");
                kilo.setText("");
                kategori="8";
                googleMap.clear();
                all_wisata(kategori);

            }
        });
        semua=(Button)v.findViewById(R.id.k_semua);
        semua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                judul.setText("Semua Wisata");
                kilo.setText("");
                kategori="";
                googleMap.clear();
                all_wisata(kategori);

            }
        });



        //swipe
       // mSwipeRefreshLayout.setVisibility(View.INVISIBLE);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                if (ActivityCompat.checkSelfPermission(v.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(v.getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.INTERNET
                    }, 10);
                } else {
                }

                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
                mFusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if(location != null){
                            MyLoc = new LatLng(location.getLatitude(),location.getLongitude());
                            myLat = location.getLatitude();
                            myLong = location.getLongitude();
                            try {
                                Geocoder geocoder;
                                List<Address> addresses;

                                geocoder = new Geocoder(getActivity(), Locale.getDefault());
                                addresses = geocoder.getFromLocation(myLat, myLong, 1);

                                String address = addresses.get(0).getAddressLine(0);
                                String city = addresses.get(0).getAddressLine(1);
                                String country = addresses.get(0).getAddressLine(2);

                                if(address==null)
                                    address="";
                                if(city==null)
                                    city="";
                                if(country==null)
                                    country="";

                                addre.setText(address+"-"+city+"-"+country);
                            } catch (Exception e) {
                            }
                            //Toast.makeText(getActivity(), "My Positions saya : "+myLat+","+myLong, Toast.LENGTH_SHORT).show();

                        }else {
                            //Toast.makeText(getActivity(), "location null", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                peringatan.setVisibility(View.INVISIBLE);
                all_wisata_kat(kategori,rad);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int val = (progress * (seekBar.getWidth() - 2 * seekBar.getThumbOffset())) / seekBar.getMax();
                radius.setText("" + progress+" KM");
                radius.setX(seekBar.getX() + val + seekBar.getThumbOffset() / 2);
                rad=progress;

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                String r=Integer.toString(rad);
                judul.setText("Wisata Sekitar kurang dari ");
                kilo.setText(r+" KM");
                googleMap.clear();
                all_wisata_kat(kategori,rad);
            }
        });

        return v;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog(){
        if (pDialog !=null){
            pDialog.dismiss();
            pDialog = null;
        }
    }

    public void all_wisata_kat(String kat,final int bundar)
    {

        final sekitarAdapter adapter = new sekitarAdapter(v.getContext(), seklist);
        grid = (GridView)v.findViewById(R.id.grid_view);
        grid.setAdapter(adapter);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sekitar a = seklist.get(position);
                int id_w=a.getId_wisata();
                String jud =a.getNamaWisata();
                String img =a.getImg();
                String almt =a.getalamat();
                String desk=a.getdeskrip();
                int rating =a.getrating();
                Double lat=a.getLatitude();
                Double longi=a.getLongtitude();
                String kat=a.getKategori();
                int trans=a.gettransport();
                int idkat=a.getid_wis();
                LatLng loc=new LatLng(lat,longi);
                Double jarak=CalculationByDistance(MyLoc,loc);

                String im = webserviceURL.lokasi_gbr_full+img;
                Intent in = new Intent(getActivity().getApplicationContext(),com.example.dickycn.plesirapp.wisata.detailWisataActivity.class);
                in.putExtra("id_wis",id_w);
                in.putExtra("judul", jud);
                in.putExtra("gambar", im);
                in.putExtra("almt", almt);
                in.putExtra("desk", desk);
                in.putExtra("rating", rating);
                in.putExtra("lat",lat);
                in.putExtra("longi",longi);
                in.putExtra("kat",kat);
                in.putExtra("trans",trans);
                in.putExtra("idkat",idkat);
                in.putExtra("jarak",jarak);
                startActivity(in);
            }
        });
        String cob=Integer.toString(bundar);
        Log.d("radius",cob);
        seklist.clear();
        JSONObject json = new JSONObject();
        ApiVolley req = new ApiVolley(getContext(), json, "GET", webserviceURL.get_wisata_kat+kat, "", "", 0, new ApiVolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {

                Log.d("cekk", result.toString());
                hidePDialog();
                // Important Note : need to use try catch when parsing JSONObject, no need when parsing string

                try {
                    JSONObject responseAPI = new JSONObject(result);
                    JSONArray arr = responseAPI.getJSONArray("response");
                    String status = responseAPI.getJSONObject("metadata").getString("status");
                    responseAPI = null;
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject ar = arr.getJSONObject(i);
                        sekitar a = new sekitar();
                        String namaWisata = ar.getString("nama_wisata");
                        Double locationWisataLat = Double.parseDouble(ar.getString("latitude")),
                                locationWisataLong=Double.parseDouble(ar.getString("longtitude"));

                        LatLng locWisata = new LatLng(locationWisataLat,locationWisataLong);
                        if(CalculationByDistance(MyLoc,locWisata)<= bundar){
                        int marker=0;
                        if(ar.getInt("id_kategori")==4){marker=R.drawable.pin_alam;}
                        if(ar.getInt("id_kategori")==5){marker=R.drawable.pin_bud;}
                        if(ar.getInt("id_kategori")==6){marker=R.drawable.pin_sejarah;}
                        if(ar.getInt("id_kategori")==7){marker=R.drawable.pin_religi;}
                        if(ar.getInt("id_kategori")==8){marker=R.drawable.pin_hiburan;}

                        googleMap.addMarker(new MarkerOptions().position(locWisata)
                                .title(namaWisata)
                                .snippet(ar.getString("nama_kategori"))
                                .icon(BitmapDescriptorFactory.fromResource(marker)));
//


                            a.setId_wisata(ar.getInt("id_wisata"));
                            a.setKategori(ar.getString("nama_kategori"));
                            a.setNamaWisata(namaWisata);
                            a.setImg(ar.getString("gambar"));
                            a.setLatitude(locationWisataLat);
                            a.setLongtitude(locationWisataLong);
                            a.setIdw(ar.getInt("id_kategori"));
                            a.setalamat(ar.getString("alamat"));
                            a.setdeskrip(ar.getString("deskripsi"));
                            a.setrating(ar.getInt("rating"));
                            a.settransport(ar.getInt("transportasi"));
                            seklist.add(a);

                            //Toast.makeText(getActivity(), "distance = "+CalculationByDistance(MyLoc,locWisata), Toast.LENGTH_SHORT).show(); // 1

                        }
                       // Toast.makeText(getActivity(), "distance = "+CalculationByDistance(MyLoc,locWisata) +"rad:"+bundar, Toast.LENGTH_SHORT).show();

                        // 1
//                        Toast.makeText(getActivity(), "distance to blora= "+CalculationByDistance(MyLoc,wisata1), Toast.LENGTH_SHORT).show(); //519

                    }
                    //Log.d("cek isi",ar.getString("judul_acara"));

                } catch (Exception e) {

                    e.printStackTrace();
//

                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onError(String result) {

            }
        });
    }

    public void all_wisata(String kat)
    {
        linlaHeaderProgress.setVisibility(View.VISIBLE);
        final sekitarAdapter adapter = new sekitarAdapter(v.getContext(), seklist);
        grid = (GridView)v.findViewById(R.id.grid_view);
        grid.setAdapter(adapter);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sekitar a = seklist.get(position);
                int id_w=a.getId_wisata();
                String jud =a.getNamaWisata();
                String img =a.getImg();
                String almt =a.getalamat();
                String desk=a.getdeskrip();
                int rating =a.getrating();
                Double lat=a.getLatitude();
                Double longi=a.getLongtitude();
                String kat=a.getKategori();
                int trans=a.gettransport();
                int idkat=a.getid_wis();
                LatLng loc=new LatLng(lat,longi);
                Double jarak=CalculationByDistance(MyLoc,loc);

                String im = webserviceURL.lokasi_gbr_full+img;
                Intent in = new Intent(getActivity().getApplicationContext(),com.example.dickycn.plesirapp.wisata.detailWisataActivity.class);
                in.putExtra("id_wis",id_w);
                in.putExtra("judul", jud);
                in.putExtra("gambar", im);
                in.putExtra("almt", almt);
                in.putExtra("desk", desk);
                in.putExtra("rating", rating);
                in.putExtra("lat",lat);
                in.putExtra("longi",longi);
                in.putExtra("kat",kat);
                in.putExtra("trans",trans);
                in.putExtra("idkat",idkat);
                in.putExtra("jarak",jarak);
                startActivity(in);
            }
        });

        seklist.clear();
        JSONObject json = new JSONObject();
        ApiVolley req = new ApiVolley(getContext(), json, "GET", webserviceURL.get_wisata_kat+kat, "", "", 0, new ApiVolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {

                Log.d("cekk", result.toString());
                hidePDialog();

                try {
                    JSONObject responseAPI = new JSONObject(result);
                    JSONArray arr = responseAPI.getJSONArray("response");
                    String status = responseAPI.getJSONObject("metadata").getString("status");
                    responseAPI = null;
                    int c=0;
                    for (int i = 0; i < arr.length(); i++) {
                        if(i==arr.length()-1){linlaHeaderProgress.setVisibility(View.INVISIBLE);}
                        JSONObject ar = arr.getJSONObject(i);
                        sekitar a = new sekitar();
                        c++;
                        String namaWisata = ar.getString("nama_wisata");
                        Double locationWisataLat = Double.parseDouble(ar.getString("latitude")),
                                locationWisataLong=Double.parseDouble(ar.getString("longtitude"));
                        LatLng locWisata = new LatLng(locationWisataLat,locationWisataLong);
                        int marker=0;
                        if(ar.getInt("id_kategori")==4){marker=R.drawable.pin_alam;}
                        if(ar.getInt("id_kategori")==5){marker=R.drawable.pin_bud;}
                        if(ar.getInt("id_kategori")==6){marker=R.drawable.pin_sejarah;}
                        if(ar.getInt("id_kategori")==7){marker=R.drawable.pin_religi;}
                        if(ar.getInt("id_kategori")==8){marker=R.drawable.pin_hiburan;}

                        googleMap.addMarker(new MarkerOptions().position(locWisata)
                                .title(namaWisata)
                                .snippet(ar.getString("nama_kategori"))
                                .icon(BitmapDescriptorFactory.fromResource(marker)));

                            a.setId_wisata(ar.getInt("id_wisata"));
                            a.setKategori(ar.getString("nama_kategori"));
                            a.setNamaWisata(namaWisata);
                            a.setImg(ar.getString("gambar"));
                            a.setLatitude(locationWisataLat);
                            a.setLongtitude(locationWisataLong);
                            a.setIdw(ar.getInt("id_kategori"));
                            a.setalamat(ar.getString("alamat"));
                            a.setdeskrip(ar.getString("deskripsi"));
                            a.setrating(ar.getInt("rating"));
                            a.settransport(ar.getInt("transportasi"));
                            seklist.add(a);
                        String show = Integer.toString(arr.length());
                        Log.d("isi",show);
                        String show1 = Integer.toString(arr.length());
                        Log.d("isi1",show1);
                       // Toast.makeText(getContext(),show,Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {

                    e.printStackTrace();
//

                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onError(String result) {

            }
        });
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

//    protected void createLocationRequest() {
//        LocationRequest mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(10000);
//        mLocationRequest.setFastestInterval(5000);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        requestPermissions(new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.INTERNET
                        }, 10);
                        return;
                    }googleMap.setMyLocationEnabled(true);
//                locationManager.requestLocationUpdates("gps",5000,0,locationListener);
                return;
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
}
