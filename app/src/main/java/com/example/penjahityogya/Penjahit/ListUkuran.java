package com.example.penjahityogya.Penjahit;

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

import com.example.penjahityogya.R;
import com.example.penjahityogya.ViewHolder.OrderViewHolder;
import com.example.penjahityogya.ViewHolder.RiwayatViewHolder;
import com.example.penjahityogya.models.Cart;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ListUkuran extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private DatabaseReference OrderRef, reference;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference Ukuran;
    String nama;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_ukuran);

        Intent in = getIntent();
//        idMitra = in.getStringExtra("idMitra");
        //tampilan daftar orderan
        recyclerView = findViewById(R.id.recycle_menu_list_ukuran);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


        Ukuran = FirebaseDatabase.getInstance().getReference().child("tmpUkuran").child(currentUser.getUid());
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(Ukuran, Cart.class)
                        .build();


        FirebaseRecyclerAdapter<Cart, OrderViewHolder> adapter =
                new FirebaseRecyclerAdapter<Cart, OrderViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull OrderViewHolder holder, int posisition, @NonNull Cart cart) {
//                            reference = FirebaseDatabase.getInstance().getReference()
//                                    .child("Users")
//                                    .child(cart.getIdUser());
//                            reference.addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                    if (dataSnapshot.child("name").getValue() != null
//                                    ) {
//                                        nama = dataSnapshot.child("name").getValue().toString();
//                                    }
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                }
//                            });
                        holder.txtnama.setText(cart.getNamaUser());
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(ListUkuran.this, Menampilkan_ukuran.class);
                                intent.putExtra("idUser", cart.getIdUser());
                                startActivity(intent);


                            }
                        });
                    }


                    @NonNull
                    @Override
                    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_ukuran_pelanggan, parent, false);
                        OrderViewHolder holder = new OrderViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
}
