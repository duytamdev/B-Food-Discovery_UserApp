package com.fpoly.pro1121.userapp.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.fpoly.pro1121.userapp.adapter.ProductOrderAdapter;
import com.fpoly.pro1121.userapp.database.ProductOrderDAO;
import com.fpoly.pro1121.userapp.model.ProductOrder;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CartFragment extends Fragment {

    View mView;
    List<ProductOrder> list;
    RecyclerView rvProductOrder;
    ProductOrderAdapter productOrderAdapter;
    TextView tvTotal;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String userIDExists = (Objects.requireNonNull(mAuth.getCurrentUser())).getUid();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_cart,container,false);
        initUI();
        return mView;

    }

    private void initUI() {
        tvTotal = mView.findViewById(R.id.tv_total_price);
        rvProductOrder = mView.findViewById(R.id.rv_cart_product);
        productOrderAdapter = new ProductOrderAdapter(new ProductOrderAdapter.IClickProductListener() {
            @Override
            public void clickDelete(int idProductOrder) {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Xác Nhận")
                        .setMessage("Xoá đồ ăn này ra khỏi giỏ hàng ? ")
                        .setPositiveButton("Xoá", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                boolean result =ProductOrderDAO.getInstance(requireContext()).deleteProductOrder(idProductOrder);
                                if(result) {
                                    Toast.makeText(requireContext(), "Product Order deleted", Toast.LENGTH_SHORT).show();
                                    reloadData();
                                }
                            }
                        })
                        .setNegativeButton("Huỷ",null)
                        .show();
            }
        });
        productOrderAdapter.setData(list);
        rvProductOrder.setAdapter(productOrderAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false);
        rvProductOrder.setLayoutManager(linearLayoutManager);
    }

    @SuppressLint("SetTextI18n")
    private void reloadData(){
        list = new ArrayList<>();
        list = ProductOrderDAO.getInstance(requireContext()).getAllProductOrder(userIDExists);
        productOrderAdapter.setData(list);
        tvTotal.setText("Tổng Tiền : "+ Utils.getFormatNumber(ProductOrderDAO.getInstance(requireContext()).getUnitPriceAllProductOrder(userIDExists)));
    }
    @Override
    public void onResume() {
        super.onResume();
        reloadData();
    }
}
