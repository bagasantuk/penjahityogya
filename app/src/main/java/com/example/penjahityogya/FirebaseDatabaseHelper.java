package com.example.penjahityogya;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.penjahityogya.activities.SessionManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//kelas adapter recyclerview

public class FirebaseDatabaseHelper {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceBooks;
    private List<Pesan> books = new ArrayList<>();
//    private SessionManager sessionManager;
    private FirebaseAuth auth, mAuth;
    FirebaseUser currentUser;

    public interface DataStatus{
       void DataIsLoaded(List<Pesan> books, List<String> keys);
       void DataIsInserted();
       void DataIsUpdate();
       void DataIsDeleted();
    }

    public FirebaseDatabaseHelper() {
        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
//        sessionManager = new SessionManager(Con, SessionManager.SESSION_USER); //deklarasi sessionManager NB: SESSION_USER sesi untuk user, SESSION_JASA sesi untuk jasa

//    sessionManager.createPesanSession(variabel yang akan disimpan1, variabel yang akan disimpan2); //contoh save data ke sessionManager
//
//        HashMap<String, String> usersDetails = sessionManager.getUserLoginFromSession(); //deklarasi HashMap ambil data dari sessionManager

        String idUser = currentUser.getUid(); // contoh ambil data dari HashMap

//        sessionManager.logoutUserFromSession(); //Hapus data yang ada di sessionManager (untuk Logout)
        mReferenceBooks = mDatabase.getReference("Orderan").child("Temp").child(idUser);
    }

    public void readBooks(final DataStatus dataStatus){
        mReferenceBooks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                books.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    Pesan book = keyNode.getValue(Pesan.class);
                    books.add(book);
                }
                dataStatus.DataIsLoaded(books, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
