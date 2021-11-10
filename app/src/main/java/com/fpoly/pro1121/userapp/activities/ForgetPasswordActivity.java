package com.fpoly.pro1121.userapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.fpoly.pro1121.userapp.R;
import com.fpoly.pro1121.userapp.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {
    TextInputLayout tilEmail;
    EditText edtEmail;
    Button btnReset;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        initUI();
        events();
    }

    private void events() {
        Utils.addTextChangedListener(edtEmail,tilEmail,true);
        btnReset.setOnClickListener(view->{
            String emailReset = edtEmail.getText().toString().trim();
            if(emailReset.isEmpty()||tilEmail.getError()!=null){
                return;
            }
            resetPassword(emailReset);
        });
    }

    private void resetPassword(String emailReset) {
        mAuth.sendPasswordResetEmail(emailReset)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                           new AlertDialog.Builder(ForgetPasswordActivity.this)
                                   .setMessage("Chúng tôi đã gửi đường đẫn thay đỗi mật khẩu đến hộp thư của bạn !")
                                   .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                       @Override
                                       public void onClick(DialogInterface dialogInterface, int i) {
                                            finish();
                                            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
                                       }
                                   })
                                   .show();
                        }
                    }
                });
    }

    private void initUI() {
        tilEmail = findViewById(R.id.til_email_reset);
        edtEmail = findViewById(R.id.edt_email_reset);
        btnReset = findViewById(R.id.btn_reset_password);
    }
}