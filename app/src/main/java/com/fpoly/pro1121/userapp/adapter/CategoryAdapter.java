package com.fpoly.pro1121.userapp.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fpoly.pro1121.userapp.R;
import com.fpoly.pro1121.userapp.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {


    List<Category> list = new ArrayList<>();
    IClickCategoryListener iClickCategoryListener;

    public CategoryAdapter(IClickCategoryListener iClickCategoryListener) {
        this.iClickCategoryListener = iClickCategoryListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Category> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = list.get(position);
        if (category == null) return;
        if (category.getUrlImage() != null) {
            Glide
                    .with(holder.itemView.getContext())
                    .load(category.getUrlImage())
                    .centerCrop()
                    .into(holder.image);
        }
        holder.name.setText(category.getName());
        holder.itemView.setOnClickListener(view -> iClickCategoryListener.clickCategory(category.getId()));

    }

    @Override
    public int getItemCount() {
        if (list != null) return list.size();
        return 0;
    }

    public interface IClickCategoryListener {
        void clickCategory(String idCategory);
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.iv_category);
            name = itemView.findViewById(R.id.tv_name_category);
        }
    }
}
