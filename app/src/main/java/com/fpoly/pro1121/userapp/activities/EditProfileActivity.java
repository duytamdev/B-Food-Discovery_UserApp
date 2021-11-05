package com.fpoly.pro1121.userapp.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fpoly.pro1121.userapp.R;
import com.fpoly.pro1121.userapp.model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    CircleImageView imgAvt;
    EditText edtName,edtPhone,edtLocation;
    Button btnUpdate;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private User userCurrentUser;
    String urlImageSelected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getDataFireBase();
        initUI();
        actionUpdate();
    }

    private void actionUpdate() {
        imgAvt.setOnClickListener(view->{
            Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
            photoPickerIntent.setType("image/*");
            activityResultLauncher.launch(photoPickerIntent);
        });
        btnUpdate.setOnClickListener(view ->{
            try {
                String name = edtName.getText().toString();
                String phone = edtPhone.getText().toString();
                String location = edtLocation.getText().toString();
                String urlImage = urlImageSelected;
                userCurrentUser.setData(name,location,phone,urlImage);
                updateUserFireBase(userCurrentUser);
            }catch(Exception e) {
                Toast.makeText(EditProfileActivity.this,"Có lỗi "+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserFireBase(User userCurrentUser) {
        db.collection("users").document(userCurrentUser.getId())
                .update("name",userCurrentUser.getName(),
                        "phoneNumber",userCurrentUser.getPhoneNumber(),
                        "urlImage",userCurrentUser.getUrlImage(),
                        "location",userCurrentUser.getLocation())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(EditProfileActivity.this,"Cập Nhật Thành Công ",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
    }

    private void getDataFireBase() {
        db.collection("users").document(mAuth.getCurrentUser().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                       try {
                           Map<String,Object> data = document.getData();
                           assert data != null;
                           String id = (String) data.get("id");
                           String name = (String) data.get("name");
                           String urlImage = (String) data.get("urlImage");
                           String location = (String) data.get("location");
                           String phone = (String) data.get("phoneNumber");
                           if(urlImage.length()>0){
                               urlImageSelected = urlImage;
                           }
                           userCurrentUser = new User(id,name,location,phone,urlImage);
                           DOMUser(userCurrentUser);
                       }catch(Exception e) {
                           e.printStackTrace();
                       }
                    }
                }
            }
        });
    }

    private void initUI() {
        imgAvt = findViewById(R.id.img_EditProfile);
        edtName = findViewById(R.id.edt_name_edt_profile);
        edtPhone = findViewById(R.id.edt_edt_phone_profile);
        edtLocation = findViewById(R.id.edt_location_profile);
        btnUpdate = findViewById(R.id.btn_update_profile);
    }
    private void DOMUser(User user) {
        try {
            edtName.setText(user.getName());
            edtPhone.setText(user.getPhoneNumber());
            edtLocation.setText(user.getLocation());
            if(user.getUrlImage().length()>0){
                Glide.with(this)
                        .load(user.getUrlImage())
                        .centerCrop()
                        .into(imgAvt);
            }

        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode()==RESULT_OK){
                        Intent data = result.getData();
                        try {
                            Uri uriImage = data.getData();
                            imgAvt.setImageURI(uriImage); // dom
                            StorageReference ref  = FirebaseStorage.getInstance().getReference().child("imagesUser").child(UUID.randomUUID().toString());
                            UploadTask uploadTask = ref.putFile(uriImage);
                            ProgressDialog progressDialog = new ProgressDialog(EditProfileActivity.this);

                            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!task.isSuccessful()) {
                                        throw task.getException();
                                    }
                                    progressDialog.setMessage("loading....");
                                    progressDialog.show();
                                    return ref.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        urlImageSelected = task.getResult().toString();
                                        progressDialog.dismiss();
                                    }
                                }
                            });

                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );
}