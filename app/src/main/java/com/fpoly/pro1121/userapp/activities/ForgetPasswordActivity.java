package com.fpoly.pro1121.userapp.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.fpoly.pro1121.userapp.R;
import com.fpoly.pro1121.userapp.Utils;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextInputLayout tilEmail;
    EditText edtEmail;
    Button btnReset;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        initUI();
        initToolbar();
        events();
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar_forget_password);
        toolbar.setTitle("Reset Password");
        toolbar.setNavigationIcon(R.drawable.ic_baseline_keyboard_backspace_24);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

    }

    private void events() {
        Utils.addTextChangedListener(edtEmail, tilEmail, true);
        btnReset.setOnClickListener(view -> {
            String emailReset = edtEmail.getText().toString().trim();
            if (emailReset.isEmpty() || tilEmail.getError() != null) {
                return;
            }
            resetPassword(emailReset);
        });
    }

    private void resetPassword(String emailReset) {
        mAuth.sendPasswordResetEmail(emailReset)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        new AlertDialog.Builder(ForgetPasswordActivity.this)
                                .setMessage("Chúng tôi đã gửi đường đẫn thay đổi mật khẩu đến hộp thư của bạn !")
                                .setPositiveButton("OK", (dialogInterface, i) -> {
                                    finish();
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                                })
                                .show();
                    } else {
                        new AlertDialog.Builder(ForgetPasswordActivity.this)
                                .setMessage("Email của bạn không đúng hoặc chưa đăng kí")
                                .setPositiveButton("Thử lại", null)
                                .show();
                    }
                });
    }

    private void initUI() {
        tilEmail = findViewById(R.id.til_email_reset);
        edtEmail = findViewById(R.id.edt_email_reset);
        btnReset = findViewById(R.id.btn_reset_password);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
}