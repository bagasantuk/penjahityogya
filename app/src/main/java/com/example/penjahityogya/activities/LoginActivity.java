package com.example.penjahityogya.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.penjahityogya.Penjahit.LoginMitra;
import com.example.penjahityogya.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    private EditText edtEmail, edtPassword;
    private Button btnLogin;
    private TextView btnMitra;
    private TextView btnRegister;
    private FirebaseAuth auth, mAuth;
    FirebaseUser currentUser;
    SessionManager sessionManager;
    String _email;

    private Object firebaseAnalytics;
    private Object firebaseAuth;
    private Object progressDialog;
    private Object authStateListener;
    private Intent notifService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sessionManager = new SessionManager(LoginActivity.this, SessionManager.SESSION_USER); //deklarasi sessionManager NB: SESSION_USER sesi untuk user, SESSION_JASA sesi untuk jasa

        initView();
        FirebaseConnect();
        login();

    }

    private void FirebaseConnect() {
//        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
//        firebaseAuth = FirebaseAuth.getInstance();
//        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        HashMap<String, String> userLogin = sessionManager.getUserLoginFromSession(); //deklarasi HashMap ambil data dari sessionManager
        _email = userLogin.get(SessionManager.KEY_EMAIL); // contoh ambil data dari HashMap
        if (currentUser != null &&  _email!= null) {
//            startService(notifService);
            startActivity(new Intent(LoginActivity.this, Pilih_Lokasi_Pengguna.class));
            finish();
        }else if(currentUser != null){
            startActivity(new Intent(LoginActivity.this, LoginMitra.class));
            finish();
        }else
            FirebaseAuth.getInstance().signOut();

//        authStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                if (firebaseAuth.getCurrentUser() != null) {
//                    startService(notifService);
//                    startActivity(new Intent(LoginActivity.this, Home.class));
//                    finish();
//                }
//
//            }
//        };
    }

    private void login() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegistrasiActivity.class));
            }
        });
        btnMitra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, LoginMitra.class));
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //menampung imputan user
                final String emailUser = edtEmail.getText().toString().trim();
                final String passwordUser = edtPassword.getText().toString().trim();

                //validasi email dan password
                // jika email kosong
                if (emailUser.isEmpty()) {
                    edtEmail.setError("Email tidak boleh kosong");
                }
                // jika email not valid
                else if (!Patterns.EMAIL_ADDRESS.matcher(emailUser).matches()) {
                    edtEmail.setError("Email tidak valid");
                }
                // jika password kosong
                else if (passwordUser.isEmpty()) {
                    edtPassword.setError("Password tidak boleh kosong");
                }
                //jika password kurang dari 6 karakter
                else if (passwordUser.length() < 6) {
                    edtPassword.setError("Password minimal terdiri dari 6 karakter");
                } else {
                    auth.signInWithEmailAndPassword(emailUser, passwordUser)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // ketika gagal locin maka akan do something
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this,
                                                "Gagal login karena " + task.getException().getMessage()
                                                , Toast.LENGTH_LONG).show();
                                    } else {
                                        Bundle bundle = new Bundle();
                                        bundle.putString("email", emailUser);
                                        bundle.putString("pass", passwordUser);
                                        currentUser = mAuth.getCurrentUser();
                                        sessionManager.createLoginSession(passwordUser, emailUser,currentUser.getUid()); //contoh save data ke sessionManager

//                                        if (!emailUser.equals("admin@gmail.com") && !passwordUser.equals("admin123")) {
                                            startActivity(new Intent(LoginActivity.this, Pilih_Lokasi_Pengguna.class)
                                                    .putExtra("emailpass", bundle));
                                            finish();
//                                        } else {
//                                        startActivity(new Intent(LoginActivity.this, PenjahitActivity.class)
//                                                .putExtra("emailpass", bundle));
//                                        finish();
//
//
//                                        }

                                    }
                                }
                            });
                }
            }
        });
    }

    private void initView() {
        edtEmail = findViewById(R.id.logEmail);
        edtPassword = findViewById(R.id.logPassword);
        btnLogin = findViewById(R.id.logBtn);
        btnRegister = findViewById(R.id.logBtnReg);
        btnMitra = findViewById(R.id.logBtnMitra);
        auth = FirebaseAuth.getInstance();
    }


}
