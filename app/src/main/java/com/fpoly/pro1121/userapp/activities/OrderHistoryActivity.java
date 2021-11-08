package com.fpoly.pro1121.userapp.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fpoly.pro1121.userapp.R;
import com.fpoly.pro1121.userapp.adapter.ProductOrderAdapter;
import com.fpoly.pro1121.userapp.model.ProductOrder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderHistoryActivity extends AppCompatActivity {

    RecyclerView rvOrderHistory;
    List<ProductOrder> list;
    ProductOrderAdapter productOrderAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        initUI();
        readDataRealTime();
    }

    private void readDataRealTime() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        db
                .collection("orders")
                .whereEqualTo("userID", mAuth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.w("-->", "Listen failed.", error);
                            return;
                        }
                        if (value != null) {
                            try {
                                List<ProductOrder> clones= new ArrayList<>();
                                for (DocumentSnapshot document : value.getDocuments()) {
                                    Map<String, Object> data = document.getData();
                                    assert data != null;
                                    String idUser = (String) data.get("userID");

                                    // get list productOrder
                                    List<Map<String,Object>> productOrders = (List<Map<String, Object>>) data.get("productOrderList");
                                    assert productOrders != null;
                                    for(Map<String,Object> dataOfProductOrders : productOrders){
                                       int idProductOrder = ((Long) dataOfProductOrders.get("id")).intValue();
                                       String idProduct = (String) dataOfProductOrders.get("idProduct");
                                       int priceProduct = ((Long) dataOfProductOrders.get("priceProduct")).intValue();
                                       int quantity = ((Long) dataOfProductOrders.get("quantity")).intValue();
                                       int unitPrice = ((Long) dataOfProductOrders.get("unitPrice")).intValue();
                                       ProductOrder productOrder = new ProductOrder(idProductOrder,idUser,idProduct,priceProduct,quantity,unitPrice);
                                        clones.add(productOrder);
                                   }
                                }
                                list = new ArrayList<>();
                                list.addAll(clones);
                                productOrderAdapter.setData(list);
                                progressDialog.dismiss();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    private void initUI() {
        rvOrderHistory = findViewById(R.id.rv_order_history);
        productOrderAdapter = new ProductOrderAdapter(new ProductOrderAdapter.IClickProductListener() {
            @Override
            public void clickUpdateQuantity(boolean isAdd, ProductOrder productOrder) {

            }

            @Override
            public void clickDelete(int idProductOrder) {

            }
        },true);
        productOrderAdapter.setData(list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvOrderHistory.setLayoutManager(linearLayoutManager);
        rvOrderHistory.setAdapter(productOrderAdapter);
    }
}