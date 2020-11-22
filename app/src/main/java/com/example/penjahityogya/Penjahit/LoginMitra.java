package com.example.penjahityogya.Penjahit;

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

import com.example.penjahityogya.R;
import com.example.penjahityogya.activities.PenjahitActivity;
import com.example.penjahityogya.activities.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

public class LoginMitra extends AppCompatActivity {
    private EditText edtEmail, edtPassword;
    private Button btnMitraLogin;
    private TextView btnMItraRegister;
    private FirebaseAuth auth, mAuth;
    FirebaseUser currentUser;
    String _email;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_mitra);
        sessionManager = new SessionManager(LoginMitra.this, SessionManager.SESSION_JASA); //deklarasi sessionManager NB: SESSION_USER sesi untuk user, SESSION_JASA sesi untuk jasa

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
        HashMap<String, String> jasaLogin = sessionManager.getJasaLoginFromSession(); //deklarasi HashMap ambil data dari sessionManager
        _email = jasaLogin.get(SessionManager.KEY_EMAIL); // contoh ambil data dari HashMap
        if (currentUser != null && _email != null) {
//            startService(notifService);
            startActivity(new Intent(LoginMitra.this, Home_mitra.class));
            finish();
        }

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
//        }
    }

    private void login() {
        btnMItraRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginMitra.this, RegistrasiMitra.class));
            }
        });
        btnMitraLogin.setOnClickListener(new View.OnClickListener() {
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
                            .addOnCompleteListener(LoginMitra.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // ketika gagal locin maka akan do something
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(LoginMitra.this,
                                                "Gagal login karena " + task.getException().getMessage()
                                                , Toast.LENGTH_LONG).show();
                                    } else {
                                        Bundle bundle = new Bundle();
                                        bundle.putString("email", emailUser);
                                        bundle.putString("pass", passwordUser);
                                        sessionManager.createLoginJasaSession(passwordUser, emailUser); //contoh save data ke sessionManager

//                                        if (!emailUser.equals("admin@gmail.com")&&!passwordUser.equals("admin123")){
                                            startActivity(new Intent(LoginMitra.this, Home_mitra.class)
                                                    .putExtra("emailpass", bundle));
                                            finish();
//                                        }else{
//                                            startActivity(new Intent(LoginMitra.this, PenjahitActivity.class)
//                                                    .putExtra("emailpass", bundle));
//                                            finish();
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
        edtEmail = findViewById(R.id.logmitraEmail);
        edtPassword = findViewById(R.id.logmitraPassword);
        btnMitraLogin = findViewById(R.id.logmitraBtn);
        btnMItraRegister = findViewById(R.id.logBtnRegMitra);
        auth = FirebaseAuth.getInstance();
    }
}
