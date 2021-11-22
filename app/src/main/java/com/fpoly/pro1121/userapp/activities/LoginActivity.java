package com.fpoly.pro1121.userapp.activities;

import static android.widget.Toast.LENGTH_SHORT;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.fpoly.pro1121.userapp.utils.MySharePreference;
import com.fpoly.pro1121.userapp.R;
import com.fpoly.pro1121.userapp.utils.Utils;
import com.fpoly.pro1121.userapp.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout tilEmail, tilPassword;
    EditText edtEmail, edtPassword;
    Button btnLogin, btnLoginGoogle, btnLoginFacebook;
    TextView tvSignup, tvForgetPassword;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    GoogleSignInClient googleSignInClient;
    CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    public final ActivityResultLauncher<Intent> googleResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });

    @SuppressLint("PackageManagerGetSignatures")
    // hàm để lấy keyHash Facebook
    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (android.content.pm.Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        callbackManager = CallbackManager.Factory.create();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        initUI();
        events();
        loginGoogle();
        loginFacebook();
        //printKeyHash(this);
    }

    private void loginFacebook() {
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("-->", "facebook:onSuccess:" + loginResult);
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(@NonNull FacebookException exception) {
                        // App code
                    }
                });
        btnLoginFacebook.setOnClickListener(view -> LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Collections.singletonList("email")));
    }

    private void handleFacebookAccessToken(AccessToken token) {
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Loading ...");
        progressDialog.show();
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser userFb = mAuth.getCurrentUser();
                        assert userFb != null;
                        User user = new User(userFb.getUid(), userFb.getDisplayName(), "", "", "", false);

                        db.collection("users")
                                .document(user.getId())
                                .get()
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        DocumentSnapshot document = task1.getResult();
                                        if (document.exists()) {
                                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công .",
                                                    LENGTH_SHORT).show();
                                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                                            finish();
                                        } else {
                                            addUserToDatabase(user);
                                        }
                                        progressDialog.dismiss();
                                    } else {
                                        Log.d("-->", "Failed with: ", task1.getException());
                                    }
                                });


                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("-->", "signInWithCredential:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initUI() {
        tvForgetPassword = findViewById(R.id.tv_forget_password);
        edtEmail = findViewById(R.id.edt_email_login);
        edtPassword = findViewById(R.id.edt_password_login);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignup = findViewById(R.id.tvSignup);
        btnLoginGoogle = findViewById(R.id.btn_login_google);
        btnLoginFacebook = findViewById(R.id.btn_login_facebook);
        tilEmail = findViewById(R.id.til_email);
        tilPassword = findViewById(R.id.til_password);
    }

    private void events() {
        Utils.addTextChangedListener(edtEmail, tilEmail, true);
        Utils.addTextChangedListenerPass(edtPassword, tilPassword);
        tvSignup.setOnClickListener(view -> {
            startActivity(new Intent(this, RegisterActivity.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        });
        btnLogin.setOnClickListener(view -> {
            try {
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();
                // if validate không cho phép đăng nhập
                if (email.trim().isEmpty() || password.trim().isEmpty()) {
                    return;
                }
                if ((tilEmail.getError() != null) || (tilPassword.getError() != null)) {
                    return;
                }
                // login email/password
                actionSignIn(email, password);

            } catch (IllegalArgumentException illegalArgumentException) {
                Toast.makeText(LoginActivity.this, "Vui lòng điền thông tin", LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(LoginActivity.this, e.getMessage(), LENGTH_SHORT).show();
            }
        });
        tvForgetPassword.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        });
    }
    // login bằng email và password
    private void actionSignIn(String email, String password) {
        // đăng nhập thành công -> MainActivity
        // đăng nhập thất bại -> Toast Lỗi

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                progressDialog.dismiss();
                MySharePreference.getInstance(this).putString("emailCurrent",email);
                Toast.makeText(LoginActivity.this,"Đăng nhập thành công",LENGTH_SHORT).show();
                Intent i = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
                finish();
            } else {
                progressDialog.dismiss();
                new AlertDialog.Builder(LoginActivity.this)
                        .setMessage("Email của bạn không đúng hoặc Password chưa chính xác")
                        .setPositiveButton("Thử lại", null)
                        .show();
            }
        });

    }

    private void loginGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        btnLoginGoogle.setOnClickListener(view -> {
            Intent intent = googleSignInClient.getSignInIntent();
            googleResultLauncher.launch(intent);
        });
    }

    private void firebaseAuthWithGoogle(String idToken) {
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Loading ...");
        progressDialog.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser userGG = mAuth.getCurrentUser();
                        assert userGG != null;
                        User user = new User(userGG.getUid(), userGG.getDisplayName(), "", "", "", false);
                        db.collection("users")
                                .document(user.getId())
                                .get()
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        DocumentSnapshot document = task1.getResult();
                                        if (document.exists()) {
                                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công .",
                                                    LENGTH_SHORT).show();
                                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                                            finish();
                                        } else {
                                            addUserToDatabase(user);
                                        }
                                        progressDialog.dismiss();
                                    } else {
                                        Log.d("-->", "Failed with: ", task1.getException());
                                    }
                                });

                    }

                });
    }

    private void addUserToDatabase(User user) {
        db.collection("users")
                .document(user.getId())
                .set(user)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công .",
                            LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                    finish();
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String email = MySharePreference.getInstance(LoginActivity.this).getString("emailCurrent");
        if(email != null){
            edtEmail.setText(email);
        }
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Nhấn thoát một lần nữa", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(() -> doubleBackToExitPressedOnce=false, 2000);
    }
}