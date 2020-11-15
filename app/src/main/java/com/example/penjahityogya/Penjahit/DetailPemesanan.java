package com.example.penjahityogya.Penjahit;

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
import com.example.penjahityogya.activities.Home;
import com.example.penjahityogya.activities.Pemesanan;
import com.example.penjahityogya.helpers.LocationTrack;
import com.example.penjahityogya.models.AgendaModel;
import com.example.penjahityogya.models.Cart;
import com.example.penjahityogya.models.Products;
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
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class DetailPemesanan extends AppCompatActivity implements OnMapReadyCallback {
    String idUser;
    private AgendaModel resAlamat = new AgendaModel();
    private GoogleMap googleMap;
    private AgendaModel detailAgenda;
    private boolean valEdit;
    private LatLng currentLocation;
    private LocationTrack gps;
    FirebaseAuth mAuth;
    Button terima, tolak, antar, selesai;
    FirebaseUser currentUser;
    DatabaseReference reference, reff2;
    RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private TextView txtTotalAmount;
    private int overTotalPrice = 0, max, overTotalQuantity = 0;


    TextView alamat, nama, telp;

    FirebaseDatabase database;
    DatabaseReference reff;
    UserHelperPesan member;

    private ElegantNumberButton numberButton;
    private TextView productPrice, productDescriptuon, productName;

    String _alamat, _nama, _telp, _latitude, _longitude;
    private String productID = "", productID2, idMitra = "", category = "", date, time, hargabahan, pname, price, status, productPriceS, quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pemesanan);
        Intent intent = getIntent();
        idUser = intent.getStringExtra("idUser");

        recyclerView = findViewById(R.id.recycle_menu_pemesanan);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        txtTotalAmount = (TextView) findViewById(R.id.total_pemesanan);

        alamat = findViewById(R.id.tvAlamat);
        nama = findViewById(R.id.pemesanan_nama);
        telp = findViewById(R.id.pemesanan_telp);

        terima = findViewById(R.id.btnTerima);
        tolak = findViewById(R.id.btnTolak);
        antar = findViewById(R.id.btnAntar);
        selesai = findViewById(R.id.btn_selesai);
        reff = database.getInstance().getReference().child("Orderan");
        member = new UserHelperPesan();
//        cekStatus();


        gps = new LocationTrack(this);
        currentLocation = new LatLng(gps.getLatitude(), gps.getLongitude());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map1);
        mapFragment.getMapAsync((OnMapReadyCallback) this);

//        _alamat = intent.getStringExtra("alamat");
        _nama = intent.getStringExtra("nama");
        txtTotalAmount.setText("Total = Rp. " + (intent.getStringExtra("total")));
//        _latitude = intent.getStringExtra("latitude");
//        _longitude = intent.getStringExtra("longitude");

//        productID = getIntent().getStringExtra("pid");
//        idMitra = getIntent().getStringExtra("idMitra");
//        category = getIntent().getStringExtra("category");

//        numberButton = (ElegantNumberButton) findViewById(R.id.number_btn);
        productDescriptuon = (TextView) findViewById(R.id.product_description_detail);
        productName = (TextView) findViewById(R.id.product_name_detail);
        productPrice = (TextView) findViewById(R.id.product_price_detail);


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        DatabaseReference productsref2 = FirebaseDatabase.getInstance().getReference().child("users").child(idUser);
        productsref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    telp.setText(dataSnapshot.child("phoneNo").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        DatabaseReference productsref = FirebaseDatabase.getInstance().getReference().child("tglOrder").child(currentUser.getUid()).child(idUser).child("0");
//        productsref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    pid=dataSnapshot.child("pid").getValue().toString();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

//        DatabaseReference productsref3 = FirebaseDatabase.getInstance().getReference().child("Cart List").child("Mitra View").child(currentUser.getUid()).child(idUser).child("Products").child("BahanPenjahit").child(pid);
//        productsref3.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    category=dataSnapshot.child("category").getValue().toString();
//                    date=dataSnapshot.child("date").getValue().toString();
//                    hargabahan=dataSnapshot.child("hargabahan").getValue().toString();
//                    pname=dataSnapshot.child("pname").getValue().toString();
//                    time=dataSnapshot.child("time").getValue().toString();
//                    status=dataSnapshot.child("status").getValue().toString();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        showAllUserData();

        terima.setOnClickListener(new View.OnClickListener() {
            int a;

            @Override
            public void onClick(View view) {
                gantiStatus("sudah", "Pesanan Diterima");

//                for (a = 0; a <= max; a++) {
//                    reference = FirebaseDatabase.getInstance().getReference()
//                            .child("tglOrder")
//                            .child(idMitra)
//                            .child(idUser)
//                            .child(String.valueOf(a));
////                            .child(String.valueOf(a));
//                    reference.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            productID2 = dataSnapshot.child("pid").getValue(String.class);
////                            showMessage("asdf");
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//                    if (productID2 != null) {
//                        ambilData(productID2);
//                        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Orderan");
//                        if (quantity != null
//                                && pname != null
//                                && productPriceS != null
//                                && date != null
//                                && time != null
//                                && idMitra != null
//                                && category != null
//                                && hargabahan != null
//                        ) {
//                            final HashMap<String, Object> cartMap = new HashMap<>();
//                            cartMap.put("pid", productID2);
//                            cartMap.put("pname", pname);
//                            cartMap.put("price", productPriceS);
//                            cartMap.put("date", date);
//                            cartMap.put("time", time);
//                            cartMap.put("quantity", quantity);
//                            cartMap.put("mitraId", idMitra);
//                            cartMap.put("category", category);
//                            cartMap.put("hargabahan", hargabahan);
//                            cartMap.put("status", "sudah");
//
//                            //cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhoneNo())
////                    if (productID2 != null)
//                            cartListRef.child("User View").child(idUser)
//                                    .child(productID2)
//                                    .updateChildren(cartMap)
//                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//                                            if (task.isSuccessful()) {
//                                                //cartListRef.child("Mitra View").child(Prevalent.currentOnlineUser.getPhoneNo())
//                                                cartListRef.child("Mitra View").child(idMitra).child(idUser)
//                                                        .child(productID2)
//                                                        .updateChildren(cartMap)
//                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                            @Override
//                                                            public void onComplete(@NonNull Task<Void> task) {
//                                                                if (task.isSuccessful()) {
//                                                                    Toast.makeText(DetailPemesanan.this, "Pesanan Diterima", Toast.LENGTH_SHORT).show();
//
//                                                                }
//                                                            }
//                                                        });
//
//                                            }
//                                        }
//                                    });
//                        }
//                    }
//                }
//                Intent intent = new Intent(DetailPemesanan.this, Home.class);
//                startActivity(intent);
//                finish();
            }
        });

        tolak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gantiStatus("ditolak", "Pesanan Berhasil ditolak");
            }
        });

        antar.setOnClickListener(new View.OnClickListener() {
            int a;

            @Override
            public void onClick(View v) {
                gantiStatus("antar", "Pesanan Diantar");

//                for (a = 0; a <= max; a++) {
//                    reference = FirebaseDatabase.getInstance().getReference()
//                            .child("tglOrder")
//                            .child(idMitra)
//                            .child(idUser)
//                            .child(String.valueOf(a));
//                    reference.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            productID2 = dataSnapshot.child("pid").getValue().toString();
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//                    if (productID2 != null) {
//                        ambilData(productID2);
//                        if (quantity != null
//                                && pname != null
//                                && productPriceS != null
//                                && date != null
//                                && time != null
//                                && idMitra != null
//                                && category != null
//                                && hargabahan != null
//                        ) {
//                            final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Orderan");
//
//                            final HashMap<String, Object> cartMap = new HashMap<>();
//                            cartMap.put("pid", productID2);
//                            cartMap.put("pname", pname);
//                            cartMap.put("price", productPriceS);
//                            cartMap.put("date", date);
//                            cartMap.put("time", time);
//                            cartMap.put("quantity", quantity);
//                            cartMap.put("mitraId", idMitra);
//                            cartMap.put("category", category);
//                            cartMap.put("hargabahan", hargabahan);
//                            cartMap.put("status", "antar");
//
//                            //cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhoneNo())
//                            cartListRef.child("User View").child(idUser)
//                                    .child(productID2)
//                                    .updateChildren(cartMap)
//                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//                                            if (task.isSuccessful()) {
//                                                //cartListRef.child("Mitra View").child(Prevalent.currentOnlineUser.getPhoneNo())
//                                                cartListRef.child("Mitra View").child(idMitra).child(idUser)
//                                                        .child(productID2)
//                                                        .updateChildren(cartMap)
//                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                            @Override
//                                                            public void onComplete(@NonNull Task<Void> task) {
//                                                                if (task.isSuccessful()) {
//                                                                    Toast.makeText(DetailPemesanan.this, "Pesanan Berhasil Ditolak", Toast.LENGTH_SHORT).show();
//
//                                                                }
//                                                            }
//                                                        });
//
//                                            }
//                                        }
//                                    });
//                        }
//                    }
//                }
            }
        });

        selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gantiStatus("selesai", "Pesanan selesai");
                Intent intent = new Intent(DetailPemesanan.this, Home_mitra.class);
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

    private void gantiStatus(String stat, String toas) {
        int a;
        for (a = 0; a <= max; a++) {
            reference = FirebaseDatabase.getInstance().getReference()
                    .child("tglOrder")
                    .child(idMitra)
                    .child(idUser)
                    .child(String.valueOf(a));
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    productID2 = dataSnapshot.child("pid").getValue().toString();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            if (productID2 != null) {
                ambilData(productID2);
                if (quantity != null
                        && pname != null
                        && productPriceS != null
                        && date != null
                        && time != null
                        && idMitra != null
                        && category != null
                        && hargabahan != null
                ) {
                    final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Orderan");

                    final HashMap<String, Object> cartMap = new HashMap<>();
                    cartMap.put("pid", productID2);
                    cartMap.put("pname", pname);
                    cartMap.put("price", productPriceS);
                    cartMap.put("date", date);
                    cartMap.put("time", time);
                    cartMap.put("quantity", quantity);
                    cartMap.put("mitraId", idMitra);
                    cartMap.put("category", category);
                    cartMap.put("hargabahan", hargabahan);
                    cartMap.put("status", stat);

                    //cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhoneNo())
                    cartListRef.child("User View").child(idUser)
                            .child(productID2)
                            .updateChildren(cartMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        //cartListRef.child("Mitra View").child(Prevalent.currentOnlineUser.getPhoneNo())
                                        cartListRef.child("Mitra View").child(idMitra).child(idUser)
                                                .child(productID2)
                                                .updateChildren(cartMap)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(DetailPemesanan.this, toas, Toast.LENGTH_SHORT).show();

                                                        }
                                                    }
                                                });

                                    }
                                }
                            });
                }
            }
        }
    }

    private void  cekStatus(String pid){
        reff2 = FirebaseDatabase.getInstance().getReference()
                .child("Orderan")
                .child("Mitra View")
                .child(idMitra)
                .child(idUser)
                .child(pid);
        reff2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("status").getValue() != null) {
                    status = dataSnapshot.child("status").getValue().toString();
                    if (status != null) {
                            if (status.equals("sudah")) {
                            terima.setVisibility(View.INVISIBLE);
                            tolak.setVisibility(View.INVISIBLE);
                            antar.setVisibility(View.VISIBLE);
                            selesai.setVisibility(View.INVISIBLE);
                        } else if (status.equals("belum")) {
                            terima.setVisibility(View.VISIBLE);
                            tolak.setVisibility(View.VISIBLE);
                            antar.setVisibility(View.INVISIBLE);
                            selesai.setVisibility(View.INVISIBLE);
                        } else if (status.equals("antar")) {
                            terima.setVisibility(View.INVISIBLE);
                            tolak.setVisibility(View.INVISIBLE);
                            antar.setVisibility(View.INVISIBLE);
                            selesai.setVisibility(View.VISIBLE);
                        } else if (status.equals("ditolak")) {
                            terima.setVisibility(View.INVISIBLE);
                            tolak.setVisibility(View.INVISIBLE);
                            antar.setVisibility(View.INVISIBLE);
                            selesai.setVisibility(View.VISIBLE);
                        } else if (status.equals("selesai")) {
                            terima.setVisibility(View.INVISIBLE);
                            tolak.setVisibility(View.INVISIBLE);
                            antar.setVisibility(View.INVISIBLE);
                            selesai.setVisibility(View.INVISIBLE);
                        }
                    }else{
                        terima.setVisibility(View.VISIBLE);
                        tolak.setVisibility(View.VISIBLE);
                        antar.setVisibility(View.INVISIBLE);
                        selesai.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void ambilData(String pid) {
        reff2 = FirebaseDatabase.getInstance().getReference()
                .child("Orderan")
                .child("Mitra View")
                .child(idMitra)
                .child(idUser)
                .child(pid);
        reff2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("category").getValue() != null
                        && dataSnapshot.child("date").getValue() != null
                        && dataSnapshot.child("hargabahan").getValue() != null
                        && dataSnapshot.child("pname").getValue() != null
                        && dataSnapshot.child("time").getValue() != null
                        && dataSnapshot.child("price").getValue() != null
                        && dataSnapshot.child("quantity").getValue() != null
                        && dataSnapshot.child("total").getValue() != null
                        && dataSnapshot.child("status").getValue() != null
                ) {
                    category = dataSnapshot.child("category").getValue().toString();
                    date = dataSnapshot.child("date").getValue().toString();
                    hargabahan = dataSnapshot.child("hargabahan").getValue().toString();
                    pname = dataSnapshot.child("pname").getValue().toString();
                    time = dataSnapshot.child("time").getValue().toString();
                    //status = dataSnapshot.child("status").getValue().toString();
                    productPriceS = dataSnapshot.child("price").getValue().toString();
                    quantity = dataSnapshot.child("quantity").getValue().toString();
                    status = dataSnapshot.child("status").getValue().toString();
                    txtTotalAmount.setText("Total = Rp. " + (dataSnapshot.child("total").getValue().toString()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onMapReady(GoogleMap map) {
        DatabaseReference productsref = FirebaseDatabase.getInstance().getReference().child("Data").child(idUser);
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

        //googleMap.setMyLocationEnabled(true);


    }

    private void showMessage(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();


        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Orderan");

        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(cartListRef.child("Mitra View")
                                        .child(currentUser.getUid())
                                        .child(idUser)
//                                .child(productID)
                                , Cart.class)
                        .build();
//         FirebaseRecyclerOptions<Cart> options =
//                 new FirebaseRecyclerOptions.Builder<Cart>()
//                 .setQuery(cartListRef, Cart.class).build();


        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter
                = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            int counter;
            int oneTypeProductPrice, oneTypeQuantity;

            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int posisition, @NonNull Cart model) {
                quantity = model.getQuantity();
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
                productID = model.getPid();
                cekStatus(productID);
                if (model.getPid() != null) {
                    counter++;
                } else
                    max = counter;

                oneTypeQuantity = Integer.valueOf(model.getQuantity());
                overTotalQuantity = overTotalQuantity + oneTypeQuantity;
//                DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child("ambilId").child(idMitra).child(currentUser.getUid());
//                member.setIdUser(currentUser.getUid());
//                member.setTotOrder(String.valueOf(max));
//                member.setNamaUser(_nama);
//                member.setQuantity(String.valueOf(overTotalQuantity));
//                member.setTotal(String.valueOf(overTotalPrice));
//                reff.setValue(member);
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
