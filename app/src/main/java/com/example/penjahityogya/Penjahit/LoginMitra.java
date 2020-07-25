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

import com.example.penjahityogya.Penjahit.LoginMitra;
import com.example.penjahityogya.R;
import com.example.penjahityogya.Penjahit.HomeMitra;
import com.example.penjahityogya.Penjahit.LoginMitra;
import com.example.penjahityogya.activities.PenjahitActivity;
import com.example.penjahityogya.Penjahit.RegistrasiMitra;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.penjahityogya.R;

public class LoginMitra extends AppCompatActivity {
    private EditText edtEmail, edtPassword;
    private Button btnMitraLogin;
    private TextView btnMItraRegister;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_mitra);
        initView();
        login();
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
                                        if (!emailUser.equals("admin@gmail.com")&&!passwordUser.equals("admin123")){
                                            startActivity(new Intent(LoginMitra.this, HomeMitra.class)
                                                    .putExtra("emailpass", bundle));
                                            finish();
                                        }else{
                                            startActivity(new Intent(LoginMitra.this, PenjahitActivity.class)
                                                    .putExtra("emailpass", bundle));
                                            finish();


                                        }

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
