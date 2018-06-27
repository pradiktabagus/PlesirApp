package com.example.dickycn.plesirapp.profil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dickycn.plesirapp.ApiVolley;
import com.example.dickycn.plesirapp.LoginActivity;
import com.example.dickycn.plesirapp.R;
import com.example.dickycn.plesirapp.webserviceURL;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;


public class profilTab extends Fragment implements GoogleApiClient.OnConnectionFailedListener{


    private ImageView photoImageView;
    private TextView nameTextView, emailTextView, idTextView;

    private ImageButton logOut, addWisata;

    private GoogleApiClient googleApiClient;


    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    profilAdapter adapter;
    GridView grid;
    TextView title;
    private ProgressDialog pDialog;
    private List<profil> proflist = new ArrayList<profil>();
    private FloatingActionButton fab;
    String idu;

    //    private OnFragmentInteractionListener mListener;
    public View v;

    public profilTab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profilTab.
     */
    // TODO: Rename and change types and number of parameters
    public static profilTab newInstance(String param1, String param2) {
        profilTab fragment = new profilTab();
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

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    setUserData(user);
                } else {
                    goLogInScreen();
                }
            }
        };
//

//
//        revoke.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                revoke(v);
//            }
//        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_profil, container, false);

        photoImageView = (ImageView) v.findViewById(R.id.prof_pict);
        nameTextView = (TextView)v.findViewById(R.id.name);
        emailTextView = (TextView)v.findViewById(R.id.email);

        logOut = (ImageButton) v.findViewById(R.id.bn_logout);
        addWisata = (ImageButton)v.findViewById(R.id.bn_addWisata);

//        revoke = (Button)v.findViewById(R.id.bn_revoke);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut(v);
            }
        });

        addWisata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goAddWisata();
            }
        });

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(v.getContext());

        FirebaseApp.initializeApp(v.getContext());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        if(googleApiClient == null || !googleApiClient.isConnected()){
            try {
                googleApiClient = new GoogleApiClient.Builder(getActivity())
                        .enableAutoManage(getActivity(),this)
                        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                        .build();
                googleApiClient.connect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        adapter = new profilAdapter(v.getContext(), proflist);
        grid = (GridView) v.findViewById(R.id.grid_view);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });


       histori("92nM12P9FndOet0vfHk27LkrTtB2");

        return v;
    }

    public void histori(String id)
    {
        proflist.clear();
        JSONObject json = new JSONObject();
        ApiVolley req = new ApiVolley(getContext(), json, "GET", webserviceURL.get_history+id, "", "", 0, new ApiVolley.VolleyCallback() {
            @Override
            public void onSuccess(String result) {

                Log.d("cekk",result.toString());

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
                        profil p=new profil();
                        p.setJudul(ar.getString("nama_wisata"));
                        p.setImg(ar.getString("url"));
                        p.settanggal(ar.getString("tanggal"));
                       /* wisata a= new wisata();
                        a.setid(ar.getInt("id_wisata"));
                        a.setTitle(ar.getString("nama_wisata"));
                        a.setThumbnailUrl(ar.getString("gambar"));
                        a.setdeskrip(ar.getString("deskripsi"));
                        a.setalamat(ar.getString("alamat"));
                        a.setkategori(ar.getString("nama_kategori"));
                        a.setlat(lat);
                        a.setlong(longi);
                       // a.setlat1(myLat);
                       // a.setlong1(myLong);
                        a.setrating(ar.getInt("rating"));
                        a.settransport(ar.getInt("transportasi"));
                        a.setidkat(kategori);
                        a.setnomer(i);*/
                        proflist.add(p);
                        //Toast.makeText(getContext(),ar.getString("url"),Toast.LENGTH_SHORT).show();
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

//
//    // TODO: Rename method, update argument and hook method into UI event


    private void setUserData(FirebaseUser user) {
        nameTextView.setText(user.getDisplayName());
        emailTextView.setText(user.getEmail());
        idu=user.getUid();
//        idTextView.setText(user.getUid());
        Glide.with(v.getContext()).load(user.getPhotoUrl()).into(photoImageView);
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.stopAutoManage(getActivity());
            googleApiClient.disconnect();
        }
    }

    private void goLogInScreen() {
        Intent intent1 = new Intent(v.getContext(), LoginActivity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent1);
    }


    private void goAddWisata() {
        Intent intent1 = new Intent(v.getContext().getApplicationContext(), BuatWisata.class);
//        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent1);
    }


    public void logOut(View view) {
        firebaseAuth.signOut();
        LoginManager.getInstance().logOut();
        goLogInScreen();

//        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
//            @Override
//            public void onResult(@NonNull Status status) {
//                if (status.isSuccess()) {
//                    goLogInScreen();
//                } else {
//                    Toast.makeText(getApplicationContext(), "not success", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }

//    public void revoke(View view) {
//        firebaseAuth.signOut();
//
//        Auth.GoogleSignInApi.revokeAccess(googleApiClient).setResultCallback(new ResultCallback<Status>() {
//            @Override
//            public void onResult(@NonNull Status status) {
//                if (status.isSuccess()) {
//                    goLogInScreen();
//                } else {
//                    Toast.makeText(getApplicationContext(), "not succes to revoke", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onStop() {
        super.onStop();

        if (firebaseAuthListener != null) {
            firebaseAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }


}
