package com.example.dickycn.plesirapp.rekomendasi;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.dickycn.plesirapp.ApiVolley;
import com.example.dickycn.plesirapp.R;
import com.example.dickycn.plesirapp.webserviceURL;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by diktabagus on 12/08/2017.
 */

public class RekomendasiTab extends Fragment {
    public RekomendasiTab() {
        // Required empty public constructor
    }
    SearchView searchView;
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    private FusedLocationProviderClient mFusedLocationClient;
    ListView list;
    CustomListAdapter adapter;
    private ProgressDialog pDialog;
    private List<wisata> wisataList = new ArrayList<wisata>();
    ImageView bingkai;
    public Double myLat=1.0,myLong=1.0,jarak;
    LatLng MyLoc,loc;
    TextView addre;
    public View footer;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        pDialog = new ProgressDialog(getActivity());
        final View v= inflater.inflate(R.layout.fragment_rekomendasi, container, false);

        //Search
        /*searchView=(SearchView) v.findViewById(R.id.search);
        searchView.setQueryHint("Cari Wisata di Blora");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(final String query) {
                list.removeFooterView(footer);
                wisataList.clear();
                if(query!="")
                {
                    search_list(query,"all");
                }
                else
                {
                    rekomendasi_list();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                list.removeFooterView(footer);
                wisataList.clear();
                if(newText!="")
                {
                    search_list(newText,"all");
                }
                else{
                    rekomendasi_list();
                }
                //Toast.makeText(getActivity().getBaseContext(), newText, Toast.LENGTH_LONG).show();
                return false;
            }
        });*/
        //endsearch
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
                   // Toast.makeText(getActivity(), "My Positions saya : "+myLat+","+myLong, Toast.LENGTH_SHORT).show();

                }else {
                    //Toast.makeText(getActivity(), "location null", Toast.LENGTH_SHORT).show();
                }
            }
        });


        adapter = new CustomListAdapter(getActivity(), wisataList);
        list=(ListView)v.findViewById(R.id.list);
        list.setAdapter(adapter);

        //adapter = new CustomListAdapter(getActivity(), wisataList);
        //list=(ListView)v.findViewById(R.id.list);
        //footer = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer, null, false);
        //list.setAdapter(adapter);

        /*footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if((adapter.num)*5 < wisataList.size())
                    adapter.num = adapter.num +1;
                adapter.notifyDataSetChanged();
            }
        });*/

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                // TODO Auto-generated method stub
                wisata a = wisataList.get(position);
                int id=a.getid();
                String jud =a.getTitle();
                String img =a.getThumbnailUrl();
                String almt =a.getalamat();
                String desk=a.getdeskrip();
                int rating =a.getrating();
                Double lat=a.getlat();
                Double longi=a.getlong();
                String kat=a.getkategori();
                int trans=a.gettransport();
                int idkat=a.getidkat();
                loc=new LatLng(lat,longi);
                Double jarak=CalculationByDistance(MyLoc,loc);

                String im = webserviceURL.lokasi_gbr_full+img;
                Intent in = new Intent(getActivity().getApplicationContext(),com.example.dickycn.plesirapp.wisata.detailWisataActivity.class);
                in.putExtra("id_wis",id);
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
                //Toast.makeText(getActivity().getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();
            }
        });


        //getdata
        rekomendasi_list();
        //endgetdata


//Swipe
        /*mSwipeRefreshLayout =(SwipeRefreshLayout) v.findViewById(R.id.swipe1);
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
                            //t.makeText(getActivity(), "My Positions saya : "+myLat+","+myLong, Toast.LENGTH_SHORT).show();

                        }else {
                            //Toast.makeText(getActivity(), "location null", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                //list.removeFooterView(footer);
                wisataList.clear();
                //getdata
                rekomendasi_list();
                //endgetdata
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });*/

        return  v;
    }

    public void rekomendasi_list()
    {
        wisataList.clear();
        JSONObject json = new JSONObject();
        ApiVolley req = new ApiVolley(getContext(), json, "GET", webserviceURL.get_rekomendasi, "", "", 0, new ApiVolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {

                Log.d("cekk",result.toString());
                hidePDialog();
                // Important Note : need to use try catch when parsing JSONObject, no need when parsing string

                try {
                    JSONObject responseAPI = new JSONObject(result);
                    JSONArray arr = responseAPI.getJSONArray("response");
                    String status = responseAPI.getJSONObject("metadata").getString("status");
                    responseAPI = null;

                    for(int i=0;i<arr.length();i++){
                        JSONObject ar = arr.getJSONObject(i);
                        Double lat=Double.parseDouble(ar.getString("latitude"));
                        Double longi=Double.parseDouble(ar.getString("longtitude"));
                        int kategori=Integer.parseInt(ar.getString("id_kategori"));

                        wisata a= new wisata();
                        a.setid(ar.getInt("id_wisata"));
                        a.setTitle(ar.getString("nama_wisata"));
                        a.setThumbnailUrl(ar.getString("gambar"));
                        a.setdeskrip(ar.getString("deskripsi"));
                        a.setalamat(ar.getString("alamat"));
                        a.setkategori(ar.getString("nama_kategori"));
                        a.setlat(lat);
                        a.setlong(longi);
                        a.setlat1(myLat);
                        a.setlong1(myLong);
                        a.setrating(ar.getInt("rating"));
                        a.settransport(ar.getInt("transportasi"));
                        a.setidkat(kategori);
                        a.setnomer(i);
                        wisataList.add(a);
                    }
                    //list.addFooterView(footer);
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
    }

    public void search_list(String cari,String id_kategori)
    {
        JSONObject json = new JSONObject();
        try{
            json.put("nama",cari);
            json.put("id_kategori",id_kategori);
        }
        catch (Exception e){e.printStackTrace();}
        ApiVolley req = new ApiVolley(getContext(), json, "POST", webserviceURL.get_search, "", "", 0, new ApiVolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                Log.d("cekk",result.toString());
                hidePDialog();
                // Important Note : need to use try catch when parsing JSONObject, no need when parsing string

                try {
                    JSONObject responseAPI = new JSONObject(result);
                    JSONArray arr = responseAPI.getJSONArray("response");
                    String status = responseAPI.getJSONObject("metadata").getString("status");
                    responseAPI = null;

                    for(int i=0;i<arr.length();i++){
                        JSONObject ar = arr.getJSONObject(i);
                        Double lat=Double.parseDouble(ar.getString("latitude"));
                        Double longi=Double.parseDouble(ar.getString("longtitude"));
                        int kategori=Integer.parseInt(ar.getString("id_kategori"));

                        wisata a= new wisata();
                        a.setid(ar.getInt("id_wisata"));
                        a.setTitle(ar.getString("nama_wisata"));
                        a.setThumbnailUrl(ar.getString("gambar"));
                        a.setdeskrip(ar.getString("deskripsi"));
                        a.setalamat(ar.getString("alamat"));
                        a.setkategori(ar.getString("nama_kategori"));
                        a.setlat(lat);
                        a.setlong(longi);
                        a.setlat1(myLat);
                        a.setlong1(myLong);
                        a.setrating(ar.getInt("rating"));
                        a.settransport(ar.getInt("transportasi"));
                        a.setidkat(kategori);
                        wisataList.add(a);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }


    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
}
