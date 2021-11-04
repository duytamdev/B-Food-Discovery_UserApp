package com.fpoly.pro1121.userapp.adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fpoly.pro1121.userapp.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>{

    List<Product> list = new ArrayList<>();

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Product> list){
        this.list = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View mView = LayoutInflater.from(parent.getContext()).inflate("",parent,false);
//          return new ProductViewHolder(mView);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = list.get(position);
        if(product==null) return;
    }

    @Override
    public int getItemCount() {
        if(list!=null) return list.size();
        return 0;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
