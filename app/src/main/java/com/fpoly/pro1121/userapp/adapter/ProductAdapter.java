package com.fpoly.pro1121.userapp.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fpoly.pro1121.userapp.R;
import com.fpoly.pro1121.userapp.Utils;
import com.fpoly.pro1121.userapp.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    List<Product> list = new ArrayList<>();
    List<Product> clones = new ArrayList<>();
    IClickProductListener iClickProductListener;

    public ProductAdapter(IClickProductListener iClickProductListener) {
        this.iClickProductListener = iClickProductListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Product> list) {
        this.list = list;
        if (this.list != null) {
            clones.addAll(this.list);
        }
        notifyDataSetChanged();
    }

    public void filter(String text) {
        list.clear();
        if (text.isEmpty()) {
            list.addAll(clones);
        } else {
            text = text.toLowerCase();
            Log.d("-", "filter: " + text);
            for (Product item : clones) {
                if (item.getName().toLowerCase().contains(text)) {
                    list.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = list.get(position);
        if (product == null) return;
        holder.tvName.setText(product.getName());
        holder.tvPrice.setText(Utils.getFormatNumber(product.getPrice()));
        Glide
                .with(holder.itemView.getContext())
                .load(product.getUrlImage())
                .centerCrop()
                .into(holder.image);
        holder.itemView.setOnClickListener(view -> iClickProductListener.clickShowDetail(product));
    }

    @Override
    public int getItemCount() {
        if (list != null) return list.size();
        return 0;
    }

    public interface IClickProductListener {
        void clickShowDetail(Product product);
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView tvName, tvPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.iv_product_item);
            tvName = itemView.findViewById(R.id.tv_name_product_item);
            tvPrice = itemView.findViewById(R.id.tv_price_product_item);
        }
    }
}
