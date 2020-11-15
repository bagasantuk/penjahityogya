package com.example.penjahityogya.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.penjahityogya.R;
import com.example.penjahityogya.ViewHolder.ProductViewHolder;
import com.example.penjahityogya.models.Products;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class BuatBaruActivity extends AppCompatActivity
{
    FirebaseAuth mAuth;
    FirebaseUser currentUser ;
    DatabaseReference reference;
    String idMitra,namaMitra, category;
    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buat_baru);

        Intent intent = getIntent();
        idMitra = intent.getStringExtra("idMitra");
        category = intent.getStringExtra("category");
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(category).child(idMitra);
        TextView profilnama = findViewById(R.id.detailMitraNama);

//        reference = FirebaseDatabase.getInstance().getReference("mitra").child(idMitra);
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.child("jam").getValue()!=null && dataSnapshot.child("alamat").getValue()!=null) {
//                    namaMitra = dataSnapshot.child("usaha").getValue().toString();
//                    profilnama.setText(namaMitra);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//        profilnama.setText(currentUser.getDisplayName());


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //tampilan produk
        recyclerView = findViewById(R.id.recycle_menuBuatBaru);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


    }

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductsRef, Products.class)
                        .build();


        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int posisition, @NonNull Products model)
                    {
                        holder.txtProductName.setText(model.getPname());
                        holder.txtProductDescription.setText(model.getDescription());
                        holder.txtProductPrice.setText("Harga = " +  "Rp." + model.getPrice() );
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                Intent intent = new Intent(BuatBaruActivity.this, ProductDetailActivity.class);
                                intent.putExtra("pid", model.getPid());
                                intent.putExtra("idMitra", idMitra);
                                intent.putExtra("category", category);
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
}
