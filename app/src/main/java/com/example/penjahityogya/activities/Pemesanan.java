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
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.penjahityogya.R;
import com.example.penjahityogya.UserHelperPesan;
import com.example.penjahityogya.ViewHolder.CartViewHolder;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Pemesanan extends AppCompatActivity implements OnMapReadyCallback {

    private AgendaModel resAlamat = new AgendaModel();
    private GoogleMap googleMap;
    private AgendaModel detailAgenda;
    private boolean valEdit;
    private LatLng currentLocation;
    private LocationTrack gps;
    FirebaseAuth mAuth;
    Button pesan;
    FirebaseUser currentUser;
    DatabaseReference reference, reff2;
    RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private TextView txtTotalAmount;
    private int overTotalPrice = 0, max, overTotalQuantity=0;

    String _alamat, _nama, _telp, _latitude, _longitude;
    TextView alamat, nama, telp;

    FirebaseDatabase database;
    DatabaseReference reff;
    UserHelperPesan member;

    private ElegantNumberButton numberButton;
    private TextView productPrice, productDescriptuon, productName;
    private String productID = "", idMitra = "", category = "", date, time, productNameS, productPriceS, quantity, hargabahan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemesanan);

        recyclerView = findViewById(R.id.recycle_menu_pemesanan);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalAmount = (TextView) findViewById(R.id.total_pemesanan);

        alamat = findViewById(R.id.tvAlamat);
        nama = findViewById(R.id.pemesanan_nama);
        telp = findViewById(R.id.pemesanan_telp);

        pesan = findViewById(R.id.btnPesan);
        reff = database.getInstance().getReference().child("Orderan");
        member = new UserHelperPesan();

        gps = new LocationTrack(this);
        currentLocation = new LatLng(gps.getLatitude(), gps.getLongitude());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);

        Intent intent = getIntent();
        _alamat = intent.getStringExtra("alamat");
        _nama = intent.getStringExtra("nama");
        _telp = intent.getStringExtra("telp");
        _latitude = intent.getStringExtra("latitude");
        _longitude = intent.getStringExtra("longitude");

        productID = getIntent().getStringExtra("pid");
        idMitra = getIntent().getStringExtra("idMitra");
        category = getIntent().getStringExtra("category");


        numberButton = (ElegantNumberButton) findViewById(R.id.number_btn);
        productDescriptuon = (TextView) findViewById(R.id.product_description_detail);
        productName = (TextView) findViewById(R.id.product_name_detail);
        productPrice = (TextView) findViewById(R.id.product_price_detail);


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        showAllUserData();


        pesan.setOnClickListener(new View.OnClickListener() {
            int a;

            @Override
            public void onClick(View view) {

                for (a = 0; a <= max; a++) {
                    reference = FirebaseDatabase.getInstance().getReference()
                            .child("tglOrder")
                            .child(idMitra)
                            .child(currentUser.getUid())
                            .child(String.valueOf(a));
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (true) {
                                String pid = dataSnapshot.child("pid").getValue().toString();

                                reff2 = FirebaseDatabase.getInstance().getReference()
                                        .child("Cart List")
                                        .child("User View")
                                        .child(currentUser.getUid())
                                        .child("Products")
                                        .child(pid);
                                reff2.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.child("category").getValue() != null && dataSnapshot.child("date").getValue() != null) {
                                            category = dataSnapshot.child("category").getValue().toString();
                                            date = dataSnapshot.child("date").getValue().toString();
                                            idMitra = dataSnapshot.child("mitraId").getValue().toString();
                                            productID = dataSnapshot.child("pid").getValue().toString();
                                            productNameS = dataSnapshot.child("pname").getValue().toString();
                                            productPriceS = dataSnapshot.child("price").getValue().toString();
                                            quantity = dataSnapshot.child("quantity").getValue().toString();
                                            time = dataSnapshot.child("time").getValue().toString();
                                            hargabahan = dataSnapshot.child("hargabahan").getValue().toString();

                                            final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Orderan");

                                            final HashMap<String, Object> cartMap = new HashMap<>();
                                            cartMap.put("pid", productID);
                                            cartMap.put("pname", productNameS);
                                            cartMap.put("price", productPriceS);
                                            cartMap.put("date", date);
                                            cartMap.put("time", time);
                                            cartMap.put("quantity", quantity);
                                            cartMap.put("mitraId", idMitra);
                                            cartMap.put("category", category);
                                            cartMap.put("hargabahan", hargabahan);
                                            cartMap.put("status", "belum");
                                            cartMap.put("total", String.valueOf(overTotalPrice));

                                            //cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhoneNo())
                                            if (productID != null)
                                                cartListRef.child("User View").child(currentUser.getUid())
                                                        .child(productID)
                                                        .updateChildren(cartMap)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    //cartListRef.child("Mitra View").child(Prevalent.currentOnlineUser.getPhoneNo())
                                                                    cartListRef.child("Mitra View").child(idMitra).child(currentUser.getUid())
                                                                            .child(productID)
                                                                            .updateChildren(cartMap)
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if (task.isSuccessful()) {
                                                                                        Toast.makeText(Pemesanan.this, "Add to Cart List." + a, Toast.LENGTH_SHORT).show();

                                                                                    }
                                                                                }
                                                                            });

                                                                }
                                                            }
                                                        });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                Intent intent = new Intent(Pemesanan.this, Home.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private void showAllUserData() {
        alamat.setText(_alamat);
        nama.setText(_nama);
        telp.setText(_telp);
    }
    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        // For showing a move to my location button
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LatLng posisi = new LatLng(Double.parseDouble(_latitude), Double.parseDouble(_longitude));
        googleMap.addMarker(new MarkerOptions().position(posisi)
                .title(_alamat));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posisi, 14));
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        //googleMap.setMyLocationEnabled(true);


    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(cartListRef.child("User View")
                                .child(currentUser.getUid())
                                .child("Products"), Cart.class)
                        .build();
//         FirebaseRecyclerOptions<Cart> options =
//                 new FirebaseRecyclerOptions.Builder<Cart>()
//                 .setQuery(cartListRef, Cart.class).build();


        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter
                = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            int counter = 0;
            int oneTypeProductPrice,oneTypeQuantity;

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
                txtTotalAmount.setText("Total = Rp. " + String.valueOf(overTotalPrice));
                idMitra = model.getMitraId();
                productID = model.getPid();
                //addTodatabase
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference()
                        .child("tglOrder")
                        .child(model.getMitraId())
                        .child(currentUser.getUid());
                Cart user = new Cart(model.getPid());
                mDatabase.child(String.valueOf(counter)).setValue(user);
                if (model != null) {
                    counter++;
                } else
                    max = counter;

                oneTypeQuantity = Integer.valueOf(model.getQuantity());
                overTotalQuantity = overTotalQuantity + oneTypeQuantity;
                DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("ambilId").child(idMitra).child(currentUser.getUid());
                member.setIdUser(currentUser.getUid());
                member.setTotOrder(String.valueOf(max));
                member.setNamaUser(_nama);
                member.setQuantity(String.valueOf(overTotalQuantity));
                member.setTotal(String.valueOf(overTotalPrice));
                reff.setValue(member);
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
