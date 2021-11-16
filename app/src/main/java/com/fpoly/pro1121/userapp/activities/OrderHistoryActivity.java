package com.fpoly.pro1121.userapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fpoly.pro1121.userapp.R;
import com.fpoly.pro1121.userapp.adapter.OrderAdapter;
import com.fpoly.pro1121.userapp.model.Order;
import com.fpoly.pro1121.userapp.model.ProductOrder;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class OrderHistoryActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView rvOrderHistory;
    OrderAdapter orderAdapter;
    List<Order> list;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        initUI();
        initToolbar();
        readDataRealTime();
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar_order_history);
        toolbar.setTitle("Order history");
        toolbar.setNavigationIcon(R.drawable.ic_baseline_keyboard_backspace_24);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
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
                                List<Order> clones = new ArrayList<>();
                                for (DocumentSnapshot document : value.getDocuments()) {
                                    Map<String, Object> data = document.getData();
                                    assert data != null;
                                    String id = (String) data.get("id");
                                    String idUser = (String) data.get("userID");
                                    int unitPriceOrder = ((Long) Objects.requireNonNull(data.get("unitPrice"))).intValue();
                                    Timestamp stamp = (Timestamp) data.get("date");
                                    assert stamp != null;
                                    Date date = stamp.toDate();
                                    // get list productOrder
                                    List<ProductOrder> productOrderList = new ArrayList<>();
                                    List<Map<String, Object>> productOrders = (List<Map<String, Object>>) data.get("productOrderList");
                                    assert productOrders != null;
                                    for (Map<String, Object> dataOfProductOrders : productOrders) {
                                        int idProductOrder = ((Long) dataOfProductOrders.get("id")).intValue();
                                        String idProduct = (String) dataOfProductOrders.get("idProduct");
                                        int priceProduct = ((Long) dataOfProductOrders.get("priceProduct")).intValue();
                                        int quantity = ((Long) dataOfProductOrders.get("quantity")).intValue();
                                        int unitPrice = ((Long) dataOfProductOrders.get("unitPrice")).intValue();
                                        ProductOrder productOrder = new ProductOrder(idProductOrder, idUser, idProduct, priceProduct, quantity, unitPrice);
                                        productOrderList.add(productOrder);
                                    }
                                    Order order = new Order(id, idUser, productOrderList, unitPriceOrder, date);
                                    clones.add(order);
                                }
                                list = new ArrayList<>();
                                list.addAll(clones);
                                orderAdapter.setData(list);
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
        orderAdapter = new OrderAdapter(new OrderAdapter.IOrderListener() {
            @Override
            public void clickShowDetail(List<ProductOrder> list) {
                Intent intent = new Intent(OrderHistoryActivity.this, ShowDetailsProductsOrder.class);
                intent.putParcelableArrayListExtra("list_product_order_history", (ArrayList<? extends Parcelable>) list);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });
        orderAdapter.setData(list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvOrderHistory.setLayoutManager(linearLayoutManager);
        rvOrderHistory.setAdapter(orderAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
}