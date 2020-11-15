package com.example.penjahityogya.Penjahit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.penjahityogya.PlacePickerActivity;
import com.example.penjahityogya.R;
import com.example.penjahityogya.UserHelperMap;
import com.example.penjahityogya.activities.Home;
import com.example.penjahityogya.helpers.Gxon;
import com.example.penjahityogya.helpers.LocationTrack;
import com.example.penjahityogya.models.AgendaModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.penjahityogya.PlacePickerActivity.REQUEST_PLACE_PICKER;

public class pilih_lokasi_mitra extends AppCompatActivity implements OnMapReadyCallback
{
    TextView tvAlamat;
    NavigationView navigationView;
    Toolbar toolbar;
    Menu menu;
    TextView textView;
    private long backPressedTime;
    Button btnselesai, btnPickLokasi;

    FirebaseDatabase database;
    DatabaseReference reff;
    int maxid = 0;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    UserHelperMap member;


    //spinner
    private ConstraintLayout constraintLayout;
    private AgendaModel detailAgenda;
    private GoogleMap googleMap;
    private LatLng currentLocation;
    private LocationTrack gps;
    private boolean valEdit;
    private AgendaModel resAlamat = new AgendaModel();
    private Object View;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_lokasi_mitra);

        gps = new LocationTrack(this);
        currentLocation = new LatLng(gps.getLatitude(), gps.getLongitude());
        //Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //getLastID();
        if (getIntent().hasExtra("data") && getIntent().getStringExtra("data") != null) {
            detailAgenda = Gxon.from(getIntent().getStringExtra("data"), AgendaModel.class);
            tampilData(true);
            valEdit = true;
        }
//        Toolbar toolbar = findViewById(R.id.toolbar);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        /*Hooks*/
        tvAlamat = findViewById(R.id.tvAlamat);
        navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        btnPickLokasi = findViewById(R.id.btnPilihLokasi);
        btnselesai = findViewById(R.id.btnselesai);

        reff = database.getInstance().getReference().child("Data").child(currentUser.getUid());
        member = new UserHelperMap();

        /*Tool Bar*/
        setSupportActionBar(toolbar);
        btnPickLokasi.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                Intent i = new Intent();
                i.setClass(pilih_lokasi_mitra.this, PlacePickerActivity.class);
                startActivityForResult(i, REQUEST_PLACE_PICKER);
            }
        });

        //menyimpan data kedalam database

        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    maxid = (int) dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnselesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                if(resAlamat.getLatitude()!= null&& resAlamat.getLongitude()!=null){
                    member.setLatitude(resAlamat.getLatitude());
                    reff.setValue(member);
                    member.setLongitude(resAlamat.getLongitude());
                    reff.setValue(member);
                    member.setAlamat(tvAlamat.getText().toString());
                    reff.setValue(member);
                }

                Intent intent = new Intent(getApplicationContext(), Home.class);

                intent.putExtra("latitude",resAlamat.getLatitude());
                intent.putExtra("longitude",resAlamat.getLongitude());
                intent.putExtra("alamat", tvAlamat.getText().toString());

                AgendaModel data = new AgendaModel();
                data.setAlamat(tvAlamat.getText().toString());
                data.setLatitude(String.valueOf(resAlamat.getLatitude()));
                data.setLongitude(String.valueOf(resAlamat.getLongitude()));
                intent.putExtra("resAlamat", Gxon.to(data));
                setResult(AppCompatActivity.RESULT_OK, intent);
                finish();

                startActivity(intent);
//                Intent intent = new Intent(Pilih_Lokasi_Pengguna.this, Home.class);
//                startActivity(intent);

            }
        });

    }


    private void tampilData(boolean valEdit)
    {
        tvAlamat.setText(detailAgenda.getAlamat());
        resAlamat.setLatitude(detailAgenda.getLatitude());
        resAlamat.setLongitude(detailAgenda.getLongitude());
        addMarker(detailAgenda);

        tvAlamat.setEnabled(valEdit);
        //btnPickLokasi.setVisibility(valEdit ? View.VISIBLE:View.GONE);
        this.valEdit = valEdit;
    }
    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    private void addMarker (AgendaModel data){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(Double.parseDouble(data.getLatitude()), Double.parseDouble(data.getLongitude())));
        markerOptions.title(data.getAlamat());
        googleMap.clear();
        googleMap.addMarker(markerOptions);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(data.getLatitude()), Double.parseDouble(data.getLongitude())), 12));
    }


    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        // For showing a move to my location button
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setMyLocationEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 14));
        if (detailAgenda != null) addMarker(detailAgenda);
    }
    public void onActivityResult ( int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PLACE_PICKER && resultCode == RESULT_OK) {
            resAlamat = Gxon.from(data.getStringExtra("resAlamat"), AgendaModel.class);
            tvAlamat.setText(resAlamat.getAlamat());
//                Location locationA = new Location("Location A");
//
//                locationA.setLatitude(-7.792789540441245);
//
//                locationA.setLongitude(110.40796615183355);
//
//                Location locationB = new Location("Location B");
//
//                locationB.setLatitude(-7.803148829355984);
//
//                locationB.setLongitude(110.4178574681282);
//
//                tvAlamat.setText("Distance : " + new DecimalFormat("##.##").format(locationA.distanceTo(locationB)) + "m");

            addMarker(resAlamat);
        }
    }
}
