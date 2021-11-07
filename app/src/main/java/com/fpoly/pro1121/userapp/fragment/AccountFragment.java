package com.fpoly.pro1121.userapp.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.fpoly.pro1121.userapp.R;
import com.fpoly.pro1121.userapp.activities.EditProfileActivity;
import com.fpoly.pro1121.userapp.activities.LoginActivity;
import com.fpoly.pro1121.userapp.activities.OrderHistoryActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountFragment extends Fragment {
    CircleImageView imgAvt;
    TextView tvNameUser;
    TextView tvEditProfile,tvOrderHistory,tvHelpContact,tvChatWithMe,tvLogOut;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    View mView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_account,container,false);
        initUI();
        actionClickCollections();
        return mView;

    }

    private void actionClickCollections() {
        tvEditProfile.setOnClickListener(view ->startMyActivity(EditProfileActivity.class));
        tvLogOut.setOnClickListener(view ->{
           new AlertDialog.Builder(requireContext())
                   .setTitle("Xác Nhận")
                   .setMessage("Bạn Thật Sự Muốn Đăng Xuất ?")
                   .setPositiveButton("Đăng Xuất", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {
                           mAuth.signOut();
                           startMyActivity(LoginActivity.class);
                       }
                   })
                   .setNegativeButton("Huỷ",null)
                   .show();
        });
        tvOrderHistory.setOnClickListener(view ->startMyActivity(OrderHistoryActivity.class));
    }
    private void startMyActivity(Class <?> cls){
        Intent intent = new Intent(requireContext(),cls);
        startActivity(intent);
    }

    private void loadDataUser() {
        db.collection("users").document(mAuth.getCurrentUser().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    assert document != null;
                    if(document.exists()){
                       try {
                           Map<String,Object> data = document.getData();
                           String urlImage = (String) Objects.requireNonNull(data).get("urlImage");
                           if(Objects.requireNonNull(urlImage).length()>0){
                               Glide.with(requireContext())
                                       .load(urlImage)
                                       .centerCrop()
                                       .into(imgAvt);
                           }
                           String name = (String) data.get("name");
                           tvNameUser.setText(name);
                       }catch(Exception e){
                           e.printStackTrace();
                       }
                    }
                }
            }
        });
    }

    private void initUI() {
        imgAvt = mView.findViewById(R.id.iv_img_account);
        tvNameUser = mView.findViewById(R.id.tv_name_user_account);
        tvEditProfile = mView.findViewById(R.id.tv_edit_profile_account);
        tvOrderHistory = mView.findViewById(R.id.tv_order_history_account);
        tvHelpContact = mView.findViewById(R.id.tv_help_contact_account);
        tvChatWithMe = mView.findViewById(R.id.tv_chat_with_me);
        tvLogOut = mView.findViewById(R.id.tv_log_out);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDataUser();
    }
}
