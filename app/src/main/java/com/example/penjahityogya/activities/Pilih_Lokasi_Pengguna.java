package com.example.penjahityogya.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.example.penjahityogya.PlacePickerActivity;
import com.example.penjahityogya.R;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.penjahityogya.PlacePickerActivity.REQUEST_PLACE_PICKER;

public class Pilih_Lokasi_Pengguna extends AppCompatActivity
        implements OnMapReadyCallback {

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
    FirebaseUser currentUser ;


    //spinner
    private ConstraintLayout constraintLayout;
    private AgendaModel detailAgenda;
    private GoogleMap googleMap;
    private LatLng currentLocation;
    private LocationTrack gps;
    private boolean valEdit;
    private AgendaModel resAlamat = new AgendaModel();
    private Object View;


    //@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_lokasi_pengguna);

        gps = new LocationTrack(this);
        currentLocation = new LatLng(gps.getLatitude(), gps.getLongitude());
        //Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //getLastID();
        if(getIntent().hasExtra("data") && getIntent().getStringExtra("data") != null){
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

        reff = database.getInstance().getReference().child("Data ");

        /*Tool Bar*/
        setSupportActionBar(toolbar);
        btnselesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                Intent intent = new Intent(Pilih_Lokasi_Pengguna.this, Home.class);
                startActivity(intent);

            }
        });

        btnPickLokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                Intent i = new Intent();
               i.setClass(Pilih_Lokasi_Pengguna.this, PlacePickerActivity.class);
               startActivityForResult(i, REQUEST_PLACE_PICKER);
            }
        });

//        btnPickLokasi.setOnClickListener(v -> {
//            Intent i = new Intent();
//            i.setClass(this, PlacePickerActivity.class);
//            startActivityForResult(i, REQUEST_PLACE_PICKER);
//        });
}






    private void tampilData(boolean valEdit) {
        tvAlamat.setText(detailAgenda.getAlamat());
        resAlamat.setLatitude(detailAgenda.getLatitude());
        resAlamat.setLongitude(detailAgenda.getLongitude());
        addMarker(detailAgenda);

        tvAlamat.setEnabled(valEdit);
        //btnPickLokasi.setVisibility(valEdit ? View.VISIBLE:View.GONE);
        this.valEdit = valEdit;
    }

    private void addMarker(AgendaModel data) {
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
        if(detailAgenda!=null)addMarker(detailAgenda);

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PLACE_PICKER && resultCode == RESULT_OK) {
            resAlamat = Gxon.from(data.getStringExtra("resAlamat"), AgendaModel.class);
            tvAlamat.setText(resAlamat.getAlamat());
            addMarker(resAlamat);
        }
    }
}