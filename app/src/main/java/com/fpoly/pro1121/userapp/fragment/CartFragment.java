package com.fpoly.pro1121.userapp.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fpoly.pro1121.userapp.R;
import com.fpoly.pro1121.userapp.Utils;
import com.fpoly.pro1121.userapp.activities.OrderComplete;
import com.fpoly.pro1121.userapp.adapter.ProductOrderAdapter;
import com.fpoly.pro1121.userapp.database.ProductOrderDAO;
import com.fpoly.pro1121.userapp.model.Order;
import com.fpoly.pro1121.userapp.model.ProductOrder;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class CartFragment extends Fragment {

    View mView;
    List<ProductOrder> list;
    RecyclerView rvProductOrder;
    ProductOrderAdapter productOrderAdapter;
    TextView tvTotal;
    Button btnOrder;
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userIDExists = (Objects.requireNonNull(mAuth.getCurrentUser())).getUid();
    int unitPrice;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_cart, container, false);
        initUI();
        actionOrder();
        return mView;

    }

    private void actionOrder() {
        btnOrder.setOnClickListener(view ->{
          try {
              UUID uuid = UUID.randomUUID();
              String id = uuid.toString();
              List<ProductOrder>productOrderList = list;
              if(productOrderList.size()<=0){
                  Toast.makeText(requireContext(),"Bạn chưa có gì trong giỏ hàng cả",Toast.LENGTH_SHORT).show();
                  return;
              }
              ProgressDialog progressDialog = new ProgressDialog(requireContext());
              progressDialog.setMessage("loading....");
              progressDialog.show();
              Order order = new Order(id,userIDExists,productOrderList,unitPrice,new Date());
                db.collection("orders")
                        .document(order.getId())
                        .set(order)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                progressDialog.dismiss();
                                startActivity(new Intent(requireContext(), OrderComplete.class));
                                requireActivity().overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
                                // clear cart
                                ProductOrderDAO.getInstance(requireContext()).deleteAllProductOrder(order.getUserID());
                            }
                        });
          }catch(Exception e){
              e.printStackTrace();
          }
        });
    }

    private void initUI() {
        btnOrder = mView.findViewById(R.id.btn_order_now);
        tvTotal = mView.findViewById(R.id.tv_total_price);
        rvProductOrder = mView.findViewById(R.id.rv_cart_product);
        productOrderAdapter = new ProductOrderAdapter(new ProductOrderAdapter.IClickProductListener() {
            @Override
            public void clickUpdateQuantity(boolean isAdd, ProductOrder productOrder) {
                if(isAdd) {
                    ProductOrderDAO.getInstance(requireContext()).clickUpdateQuantity(productOrder,true);
                }else{
                    ProductOrderDAO.getInstance(requireContext()).clickUpdateQuantity(productOrder,false);
                }
                reloadData();
            }

            @Override
            public void clickDelete(int idProductOrder) {
                deleteProductOrder(idProductOrder);
            }
        },false);
        productOrderAdapter.setData(list);
        rvProductOrder.setAdapter(productOrderAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        rvProductOrder.setLayoutManager(linearLayoutManager);
    }

    private void deleteProductOrder(int idProductOrder) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xác Nhận")
                .setMessage("Xoá đồ ăn này ra khỏi giỏ hàng ? ")
                .setPositiveButton("Xoá", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        boolean result = ProductOrderDAO.getInstance(requireContext()).deleteProductOrder(idProductOrder);
                        if (result) {
                            Toast.makeText(requireContext(), "Product Order deleted", Toast.LENGTH_SHORT).show();
                            reloadData();
                        }
                    }
                })
                .setNegativeButton("Huỷ", null)
                .show();
    }

    @SuppressLint("SetTextI18n")
    private void reloadData() {
        list = new ArrayList<>();
        list = ProductOrderDAO.getInstance(requireContext()).getAllProductOrder(userIDExists);
        productOrderAdapter.setData(list);
        unitPrice = ProductOrderDAO.getInstance(requireContext()).getUnitPriceAllProductOrder(userIDExists);
        tvTotal.setText("Tổng Tiền : " + Utils.getFormatNumber(unitPrice));
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadData();
    }
}
