package com.fpoly.pro1121.userapp.activities;

import static android.widget.Toast.LENGTH_SHORT;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.fpoly.pro1121.userapp.R;
import com.fpoly.pro1121.userapp.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText edtEmail,edtPassword;
    Button btnLogin,btnLoginGoogle,btnLoginFacebook;
    TextView tvSignup;
    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    GoogleSignInClient googleSignInClient;
    CallbackManager callbackManager;
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
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
        btnLoginFacebook.setOnClickListener(view -> {
            LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email"));
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Loading ...");
        progressDialog.show();
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser userFb = mAuth.getCurrentUser();
                            User user = new User(userFb.getUid(),userFb.getDisplayName(),"","","",false);

                            db.collection("users")
                                    .document(user.getId())
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {
                                                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công .",
                                                            LENGTH_SHORT).show();
                                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
                                                    finish();
                                                } else {
                                                    addUserToDatabase(user);
                                                }
                                                progressDialog.dismiss();
                                            } else {
                                                Log.d("-->", "Failed with: ", task.getException());
                                            }
                                        }
                                    });


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("-->", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void initUI() {
        edtEmail = findViewById(R.id.edt_email_login);
        edtPassword =  findViewById(R.id.edt_password_login);
        btnLogin =  findViewById(R.id.btnLogin);
        tvSignup = findViewById(R.id.tvSignup);
        btnLoginGoogle = findViewById(R.id.btn_login_google);
        btnLoginFacebook = findViewById(R.id.btn_login_facebook);
    }
    private void events() {
        tvSignup.setOnClickListener(view -> {
            startActivity(new Intent(this, RegisterActivity.class));
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
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
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Loading ...");
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công .",
                                    LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void loginGoogle(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient   = GoogleSignIn.getClient(this,gso);
        btnLoginGoogle.setOnClickListener(view->{
            Intent intent = googleSignInClient.getSignInIntent();
            googleResultLauncher.launch(intent);
        });
    }
   public final ActivityResultLauncher<Intent> googleResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
       @Override
       public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK){
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    firebaseAuthWithGoogle(account.getIdToken());
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
       }
   });
    private void firebaseAuthWithGoogle(String idToken) {
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Loading ...");
        progressDialog.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser userGG  = mAuth.getCurrentUser();
                            User user = new User(userGG.getUid(),userGG.getDisplayName(),"","","",false);
                            db.collection("users")
                                    .document(user.getId())
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {
                                                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công .",
                                                            LENGTH_SHORT).show();
                                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
                                                    finish();
                                                } else {
                                                   addUserToDatabase(user);
                                                }
                                                progressDialog.dismiss();
                                            } else {
                                                Log.d("-->", "Failed with: ", task.getException());
                                            }
                                        }
                                    });



//
                        } else {
                            // If sign in fails, display a message to the user.
                        }
                    }
                });
    }
    private void addUserToDatabase(User user) {
        db.collection("users")
                .document(user.getId())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công .",
                                LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
                        finish();
                    }
                });
    }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}