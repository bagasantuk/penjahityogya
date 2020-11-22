package com.example.penjahityogya;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.penjahityogya.activities.ListPemesananPelanggan;
import com.example.penjahityogya.activities.SessionManager;
import com.example.penjahityogya.activities.statuspemesananisimitra;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class RecyclerViewConfig {
    private Context mContext;
    private BooksAdapter mBooksAdapter;
    private Pesan model;
    private TextView mNama;
    SessionManager sessionManager;

    FirebaseDatabase database;
    DatabaseReference reff, reference;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    String status;
    String diambilOleh, nama, idMitra;

    //kelas adapter recyclerview


    public void setConfig(RecyclerView recyclerView, Context context, List<Pesan> books, List<String> keys) {
        mContext = context;
        mBooksAdapter = new BooksAdapter(books, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mBooksAdapter);
        sessionManager = new SessionManager(context, SessionManager.SESSION_USER);

    }

    class BookItemView extends RecyclerView.ViewHolder {


        private String key;

        public BookItemView(ViewGroup parent) {
            super(LayoutInflater.from(mContext).
                    inflate(R.layout.list_isi_statusmitra, parent, false));

            mNama = (TextView) itemView.findViewById(R.id.list_isimitra);
//            mJasa = (TextView) itemView.findViewById(R.id.tvJasa);
//            mUsername = (TextView) itemView.findViewById(R.id.tvUsername);
//            mStatus = (TextView) itemView.findViewById(R.id.tvStatus);

        }

        public void bind(Pesan book, String key) {
            mNama.setText("Mitra : " + book.getNama());
            this.key = key;
        }
    }

    class BooksAdapter extends RecyclerView.Adapter<BookItemView> {
        private List<Pesan> mBookList;
        private List<String> mKeys;

        public BooksAdapter(List<Pesan> mBookList, List<String> mKeys) {
            this.mBookList = mBookList;
            this.mKeys = mKeys;
        }

        @NonNull
        @Override
        public BookItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new BookItemView(parent);


        }

        @Override
        public void onBindViewHolder(@NonNull BookItemView holder, int position) {
            holder.bind(mBookList.get(position), mKeys.get(position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), ListPemesananPelanggan.class);
                    sessionManager.createidMitraSession(mKeys.get(position));
//                    i.putExtra("idMitra", mKeys.get(position));
                    v.getContext().startActivity(i);

                }
            });
        }

        @Override
        public int getItemCount() {
            return mBookList.size();
        }

    }

    private void showMessage(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }


}
