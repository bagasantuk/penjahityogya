package com.example.penjahityogya.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.penjahityogya.R;
import com.example.penjahityogya.UserHelperPesan;
import com.example.penjahityogya.ViewHolder.CartViewHolder;
import com.example.penjahityogya.ViewHolder.StatusViewHolder;
import com.example.penjahityogya.helpers.LocationTrack;
import com.example.penjahityogya.models.AgendaModel;
import com.example.penjahityogya.models.Cart;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StatusPemesanan extends AppCompatActivity implements OnMapReadyCallback {
    String idUser;
    private AgendaModel resAlamat = new AgendaModel();
    private GoogleMap googleMap;
    private AgendaModel detailAgenda;
    private boolean valEdit;
    private LatLng currentLocation;
    private LocationTrack gps;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference reference, reff2;
    RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private TextView txtTotalAmount;
    private int overTotalPrice = 0, max, overTotalQuantity = 0;
    Button batal;

    String _alamat;
    String _nama;
    String _telp;
    String _latitude;
    String _longitude, Status;
    TextView status;
    TextView alamat, nama, telp;

    FirebaseDatabase database;
    DatabaseReference reff;
    UserHelperPesan member;

    private ElegantNumberButton numberButton;
    private TextView productPrice, productDescriptuon, productName;
    private String productID, productID2, idMitra, category = "", date, time, productNameS, productPriceS, quantity, hargabahan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_pemesanan);

        recyclerView = findViewById(R.id.recycle_menu_pemesanan);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalAmount = (TextView) findViewById(R.id.total_pemesanan);

        alamat = findViewById(R.id.tvAlamat);
        nama = findViewById(R.id.pemesanan_nama);
        telp = findViewById(R.id.pemesanan_telp);
        status = (TextView) findViewById(R.id.status_pemesanan);

        reff = database.getInstance().getReference().child("Orderan");
        member = new UserHelperPesan();

        gps = new LocationTrack(this);
        currentLocation = new LatLng(gps.getLatitude(), gps.getLongitude());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        Intent intent = getIntent();
        _alamat = intent.getStringExtra("alamat");

        reference = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                _telp = dataSnapshot.child("phoneNo").getValue().toString();
                _nama = dataSnapshot.child("name").getValue().toString();
                nama.setText(_nama);
                telp.setText(_telp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


//        _nama = intent.getStringExtra("nama");
//        _telp = intent.getStringExtra("telp");
//        productID = intent.getStringExtra("telp");
        Status = intent.getStringExtra("status");
        _latitude = intent.getStringExtra("latitude");
        _longitude = intent.getStringExtra("longitude");

        productID = getIntent().getStringExtra("pid");
        idMitra = getIntent().getStringExtra("idMitra");
        category = getIntent().getStringExtra("category");

        numberButton = (ElegantNumberButton) findViewById(R.id.number_btn);
        productDescriptuon = (TextView) findViewById(R.id.product_description_detail);
        productName = (TextView) findViewById(R.id.product_name_detail);
        productPrice = (TextView) findViewById(R.id.product_price_detail);
        batal = (Button) findViewById(R.id.btnbatal);
//        if (productID != null) {
            reff2 = FirebaseDatabase.getInstance().getReference().child("Orderan").child("User View").child(currentUser.getUid()).child(productID);
            reff2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Status = dataSnapshot.child("status").getValue().toString();
                        idMitra = dataSnapshot.child("mitraId").getValue().toString();
                        txtTotalAmount.setText("Total = Rp. " +dataSnapshot.child("total").getValue().toString());
                        if (Status.equals("sudah")) {
                            batal.setVisibility(View.INVISIBLE);
                            Status ="Sedang Diproses...";
                        } else if (Status.equals("belum")) {
                            batal.setVisibility(View.VISIBLE);
                            Status ="Menunggu Konfirmasi";
                        }else if (Status.equals("antar")) {
                            batal.setVisibility(View.INVISIBLE);
                            Status ="Pesanan Sedang Diantar";
                        }else if (Status.equals("ditolak")) {
                            batal.setVisibility(View.INVISIBLE);
                            Status ="Pesanan Anda Ditolak";
                        }else if (Status.equals("selesai")) {
                            batal.setVisibility(View.INVISIBLE);
                            Status ="Pesanan Telah Selesai";
                        } else
                            batal.setVisibility(View.VISIBLE);
                        status.setText(Status);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
//        }



        showAllUserData();
        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference hapus = FirebaseDatabase.getInstance().getReference().child("Orderan");
                hapus.child("User View").child(currentUser.getUid()).child(productID).removeValue();
                hapus.child("Mitra View").child(idMitra).child(currentUser.getUid()).child(productID).removeValue();
                Intent intent = new Intent(StatusPemesanan.this, Home.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void showAllUserData() {
        alamat.setText(_alamat);
//        status.setText(Status);
//        nama.setText(_nama);
//        telp.setText(_telp);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        DatabaseReference productsref = FirebaseDatabase.getInstance().getReference().child("Data").child(currentUser.getUid());
        googleMap = map;
        // For showing a move to my location button
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        productsref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    alamat.setText(dataSnapshot.child("alamat").getValue().toString());
                    _latitude = dataSnapshot.child("latitude").getValue().toString();
                    _longitude = dataSnapshot.child("longitude").getValue().toString();

                    LatLng posisi = new LatLng(Double.parseDouble(_latitude), Double.parseDouble(_longitude));
                    googleMap.addMarker(new MarkerOptions().position(posisi)
                            .title(_alamat));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posisi, 14));
                    googleMap.getUiSettings().setZoomControlsEnabled(true);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Orderan");

        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(cartListRef.child("User View")
                                .child(currentUser.getUid())
                                , Cart.class)
                        .build();
//         FirebaseRecyclerOptions<Cart> options =
//                 new FirebaseRecyclerOptions.Builder<Cart>()
//                 .setQuery(cartListRef, Cart.class).build();


        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter
                = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            int counter = 0;
            int oneTypeProductPrice, oneTypeQuantity;

            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int posisition, @NonNull Cart model) {
                int hargabahan = (Integer.valueOf(model.getQuantity()) * Integer.valueOf(model.getHargabahan()));
                holder.txtProductQuantity.setText("= " + model.getQuantity());
                holder.txtProductPrice.setText("Harga = Rp. " + model.getPrice() + "+" + model.getHargabahan());
                holder.txtProductName.setText(model.getPname());
//menampilkan total price
                if (model.getPrice() != null)
                    oneTypeProductPrice = ((Integer.valueOf(model.getPrice()))) * Integer.valueOf(model.getQuantity());
                overTotalPrice = overTotalPrice + oneTypeProductPrice + hargabahan;
//                txtTotalAmount.setText("Total = Rp. " + String.valueOf(overTotalPrice));

                idMitra = model.getMitraId();
//                if (model.getPid() != null)
                    productID = model.getPid();
                //addTodatabase
                if (model != null) {
                    counter++;
                } else
                    max = counter;

                oneTypeQuantity = Integer.valueOf(model.getQuantity());
                overTotalQuantity = overTotalQuantity + oneTypeQuantity;

            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
}
