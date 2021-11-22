package com.fpoly.pro1121.userapp.activities;

import static android.widget.Toast.LENGTH_SHORT;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.fpoly.pro1121.userapp.R;
import com.fpoly.pro1121.userapp.utils.Utils;
import com.fpoly.pro1121.userapp.model.User;

import com.google.android.material.textfield.TextInputLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;


public class RegisterActivity extends AppCompatActivity {
    TextInputLayout tilName, tilEmail, tilPassword, tilPhone, tilLocation;
    EditText edtEmail, edtPassword, edtFullName, edtPhone, edtLocation;
    Button btnRegister;
    Toolbar toolbar;
    TextView tvHaveAnAccount;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_register);
        initUI();
        initToolbar();
        events();

    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar_register_account);
        toolbar.setTitle("Đăng kí tài khoản");
        toolbar.setNavigationIcon(R.drawable.ic_baseline_keyboard_backspace_24);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }


    private void initUI() {
        tilEmail = findViewById(R.id.til_email_register);
        tilPassword = findViewById(R.id.til_password_register);
        tilName = findViewById(R.id.til_name_register);
        tilPhone = findViewById(R.id.til_phone_register);
        tilLocation = findViewById(R.id.til_location_register);
        edtEmail = findViewById(R.id.edt_email_register);
        edtPassword = findViewById(R.id.edt_password_register);
        edtFullName = findViewById(R.id.edt_name_register);
        edtPhone = findViewById(R.id.edt_phone_number_register);
        edtLocation = findViewById(R.id.edt_location_register);

        btnRegister = findViewById(R.id.btnRegister);
        tvHaveAnAccount = findViewById(R.id.tvHaveAnAccount);
    }

    private void events() {
        Utils.addTextChangedListener(edtFullName, tilName, false);
        Utils.addTextChangedListener(edtEmail, tilEmail, true);
        Utils.addTextChangedListenerPass(edtPassword, tilPassword);
        Utils.addTextChangedListener(edtPhone, tilPhone, false);
        Utils.addTextChangedListener(edtLocation, tilLocation, false);


        btnRegister.setOnClickListener(view -> {
            try {
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                String fullName = edtFullName.getText().toString().trim();
                String phoneNumber = edtPhone.getText().toString().trim();
                String location = edtLocation.getText().toString().trim();
                // check validation
                if (tilName.getError() != null || tilEmail.getError() != null || tilPassword.getError() != null || tilPhone.getError() != null || tilLocation.getError() != null) {
                    return;
                }
                if (email.isEmpty() || password.isEmpty() || fullName.isEmpty() || phoneNumber.isEmpty() || location.isEmpty()) {
                    return;
                }
                actionRegister(email, password, fullName, phoneNumber, location);
            } catch (IllegalArgumentException argumentException) {
                Toast.makeText(RegisterActivity.this, "Vui lòng điền thông tin", LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(RegisterActivity.this, e.getMessage(), LENGTH_SHORT).show();
            }

        });
        tvHaveAnAccount.setOnClickListener(view -> onBackPressed());
    }


    private void actionRegister(String email, String password, String name, String phoneNumber, String location) {
        // khi đăng kí auth thành công tiến thành tạo 1 user có collection id, idUser trùng với uidUser đã tạo trên db
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                User user = new User(userID, name, location, phoneNumber, "", false);
                addUserToFireBase(user);
            } else {
                new AlertDialog.Builder(RegisterActivity.this)
                        .setTitle("Đăng kí thất bại")
                        .setMessage("Email này được đăng kí")
                        .setPositiveButton("Thoát đăng kí", (dialogInterface, i) -> onBackPressed())
                        .setNegativeButton("Thử lại   ", null)
                        .show();
            }
        });
    }

    private void addUserToFireBase(User user) {
        final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        db.collection("users").document(user.getId()).set(user).addOnSuccessListener(unused -> {
            progressDialog.dismiss();
            Toast.makeText(RegisterActivity.this, "Tạo tài khoản thành công", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }).addOnFailureListener(e -> Log.w("error", "Lỗi hệ thống vui lòng thử lại"));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        finish();
    }
}