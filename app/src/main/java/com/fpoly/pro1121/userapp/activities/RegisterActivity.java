package com.fpoly.pro1121.userapp.activities;

import static android.widget.Toast.LENGTH_SHORT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fpoly.pro1121.userapp.R;
import com.fpoly.pro1121.userapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {
    EditText edtEmail,edtPassword,edtFullName;
    Button btnRegister;
    TextView tvHaveAnAccount;
    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_register);
        initUI();
        events();

    }


    private void initUI() {
        edtEmail = findViewById(R.id.edt_email_register);
        edtPassword =  findViewById(R.id.edt_password_register);
        edtFullName =  findViewById(R.id.edt_name_register);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        tvHaveAnAccount = (TextView) findViewById(R.id.tvHaveAnAccount);
    }

    private void events() {
        btnRegister.setOnClickListener(view -> {
            try {
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();
                String fullName = edtFullName.getText().toString();
                actionRegister(email,password,fullName);
            }catch(IllegalArgumentException argumentException){
                Toast.makeText(RegisterActivity.this,"Vui lòng điền thông tin",LENGTH_SHORT).show();
            }catch(Exception e) {
                Toast.makeText(RegisterActivity.this,e.getMessage(),LENGTH_SHORT).show();
            }

        });
    }

    private void actionRegister(String email, String password,String name) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            User user = new User(mAuth.getCurrentUser().getUid(),email,password,name,"","",false);
                            addUserToFireBase(user);
                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void addUserToFireBase(User user) {
        db.collection("users").document(user.getId())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(RegisterActivity.this, "Tạo tài khoản thành công",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("---->", "Error writing document", e);
                    }
                });

    }

}