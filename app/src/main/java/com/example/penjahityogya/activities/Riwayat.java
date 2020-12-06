package com.example.penjahityogya.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.penjahityogya.Penjahit.DetailPemesanan;
import com.example.penjahityogya.Penjahit.Home_mitra;
import com.example.penjahityogya.R;
import com.example.penjahityogya.ViewHolder.RiwayatViewHolder;
import com.example.penjahityogya.ViewHolder.StatusViewHolder;
import com.example.penjahityogya.models.Cart;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Riwayat extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    FirebaseAuth mAuth;
    FirebaseUser currentUser ;
    private DatabaseReference OrderRef,reference;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    String idMitra;
    private long backPressedTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat);

        Intent in = getIntent();
        idMitra = in.getStringExtra("idMitra");
        //tampilan daftar orderan
        recyclerView = findViewById(R.id.recycle_menu_list_riwayat);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        OrderRef = FirebaseDatabase.getInstance().getReference().child("Riwayat").child(currentUser.getUid());

    }
    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(OrderRef, Cart.class)
                        .build();


        FirebaseRecyclerAdapter<Cart, RiwayatViewHolder> adapter =
                new FirebaseRecyclerAdapter<Cart, RiwayatViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull RiwayatViewHolder holder, int posisition, @NonNull Cart cart) {
                        holder.txtBarang.setText("Nama Barang : "+ cart.getPname());//
                        holder.txtTotal.setText("Total : "+ cart.getTotal());
                        holder.txtStatus.setText("Status : "+ cart.getStatus());
                        holder.txtCategory.setText("Category : "+ cart.getCategory());
                        holder.txtTanggal.setText("Tanggal : "+ cart.getDate());
                        holder.txtJumlah.setText("Jumlah : "+ cart.getQuantity());
                    }

                    int oneTypeProductPrice;

                    @NonNull
                    @Override
                    public RiwayatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_riwayat, parent, false);
                        RiwayatViewHolder holder = new RiwayatViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
    @Override
    public void onBackPressed() {
        if (backPressedTime + 1000 > System.currentTimeMillis()) {
            Intent intent = new Intent(Riwayat.this, Home.class);
            startActivity(intent);
        }
        backPressedTime = System.currentTimeMillis();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
}
