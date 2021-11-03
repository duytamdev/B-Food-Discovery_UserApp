package com.fpoly.pro1121.userapp.activities;

import static android.widget.Toast.LENGTH_SHORT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fpoly.pro1121.userapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText edtEmail,edtPassword;
    Button btnLogin;
    TextView tvSignup;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        initUI();
        events();
    }



    private void initUI() {
        edtEmail = findViewById(R.id.edt_email_login);
        edtPassword =  findViewById(R.id.edt_password_login);
        btnLogin =  findViewById(R.id.btnLogin);
        tvSignup = findViewById(R.id.tvSignup);
    }
    private void events() {
        tvSignup.setOnClickListener(view -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
        btnLogin.setOnClickListener(view -> {
           try {
               String email = edtEmail.getText().toString();
               String  password = edtPassword.getText().toString();
               actionSignIn(email,password);

           }catch (IllegalArgumentException illegalArgumentException){
               Toast.makeText(LoginActivity.this,"Vui lòng điền thông tin",LENGTH_SHORT).show();
           }catch(Exception e){
               Toast.makeText(LoginActivity.this,e.getMessage(),LENGTH_SHORT).show();
           }
        });
    }

    private void actionSignIn(String email,String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công .",
                                    LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    LENGTH_SHORT).show();
                        }
                    }
                });
    }
}