package com.fpoly.pro1121.userapp.activities;

import static android.widget.Toast.LENGTH_SHORT;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fpoly.pro1121.userapp.R;
import com.fpoly.pro1121.userapp.Utils;
import com.fpoly.pro1121.userapp.model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {
    TextInputLayout tilName,tilEmail,tilPassword,tilPhone,tilLocation;
    EditText edtEmail,edtPassword,edtFullName,edtPhone,edtLocation;
    Button btnRegister;
    TextView tvHaveAnAccount;
    ImageView ivPre;
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
        tilEmail = findViewById(R.id.til_email_register);
        tilPassword = findViewById(R.id.til_password_register);
        tilName = findViewById(R.id.til_name_register);
        tilPhone = findViewById(R.id.til_phone_register);
        tilLocation = findViewById(R.id.til_location_register);
        ivPre = findViewById(R.id.iv_pre_register);
        edtEmail = findViewById(R.id.edt_email_register);
        edtPassword =  findViewById(R.id.edt_password_register);
        edtFullName =  findViewById(R.id.edt_name_register);
        edtPhone = findViewById(R.id.edt_phone_number_register);
        edtLocation = findViewById(R.id.edt_location_register);

        btnRegister =  findViewById(R.id.btnRegister);
        tvHaveAnAccount = findViewById(R.id.tvHaveAnAccount);
    }

    private void events() {
        Utils.addTextChangedListener(edtFullName,tilName,false);
        Utils.addTextChangedListener(edtEmail,tilEmail,true);
        Utils.addTextChangedListener(edtPassword,tilPassword,false);
        Utils.addTextChangedListener(edtPhone,tilPhone,false);
        Utils.addTextChangedListener(edtLocation,tilLocation,false);


        btnRegister.setOnClickListener(view -> {
            try {
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                String fullName = edtFullName.getText().toString().trim();
                String phoneNumber = edtPhone.getText().toString().trim();
                String location = edtLocation.getText().toString().trim();
                // check validation
                if(tilName.getError()!=null||tilEmail.getError()!=null||tilPassword.getError()!=null||tilPhone.getError()!=null|| tilLocation.getError()!=null) {
                    return;
                }
                if(email.isEmpty()||password.isEmpty()||fullName.isEmpty()||phoneNumber.isEmpty()||location.isEmpty()) {
                    return;
                }
                actionRegister(email,password,fullName,phoneNumber,location);
            }catch(IllegalArgumentException argumentException){
                Toast.makeText(RegisterActivity.this,"Vui lòng điền thông tin",LENGTH_SHORT).show();
            }catch(Exception e) {
                Toast.makeText(RegisterActivity.this,e.getMessage(),LENGTH_SHORT).show();
            }

        });
        ivPre.setOnClickListener(view->{
            startMyActivity(LoginActivity.class);
        });
        tvHaveAnAccount.setOnClickListener(view -> {
            startMyActivity(LoginActivity.class);
        });
    }
    private void startMyActivity(Class <?> cls){
        Intent intent = new Intent(RegisterActivity.this,cls);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
    }

    private void actionRegister(String email, String password,String name,String phoneNumber,String location) {
      // khi đăng kí auth thành công tiến thành tạo 1 user có collection id, idUser trùng với uidUser đã tạo trên db
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String userID = mAuth.getCurrentUser().getUid();
                            User user = new User(userID,name,location,phoneNumber,"",false);
                            addUserToFireBase(user);
                        } else {
                            new AlertDialog.Builder(RegisterActivity.this)
                                    .setTitle("Đăng kí thất bại")
                                    .setMessage("Email này được đăng kí")
                                    .setPositiveButton("Thoát đăng kí", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            startMyActivity(LoginActivity.class);
                                        }
                                    })
                                    .setNegativeButton("Thử lại   ",null)
                                    .show();
                        }
                    }
                });
    }
    private void addUserToFireBase(User user) {
        final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        db.collection("users").document(user.getId()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Tạo tài khoản thành công",Toast.LENGTH_SHORT).show();
                        startMyActivity(LoginActivity.class);
                    }
                }).addOnFailureListener(e -> Log.w("error","Lỗi hệ thống vui lòng thử lại"));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
        finish();
    }
}