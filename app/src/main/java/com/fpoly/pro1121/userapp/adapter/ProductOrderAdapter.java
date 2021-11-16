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
import com.fpoly.pro1121.userapp.Utils;
import com.fpoly.pro1121.userapp.model.Product;
import com.fpoly.pro1121.userapp.model.ProductOrder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductOrderAdapter extends RecyclerView.Adapter<ProductOrderAdapter.ProductOrderViewHolder> {

    List<ProductOrder> list = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    IClickProductListener iClickProductListener;
    boolean isOrderHistory;
    public ProductOrderAdapter(ProductOrderAdapter.IClickProductListener iClickProductListener, boolean isOrderHistory) {
        this.iClickProductListener = iClickProductListener;
        this.isOrderHistory = isOrderHistory;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<ProductOrder> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_in_cart, parent, false);
        return new ProductOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductOrderViewHolder holder, int position) {
        ProductOrder productOrder = list.get(position);
        if (productOrder == null) return;

        db.collection("products").document(productOrder.getIdProduct())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    assert document != null;
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        String id = (String) data.get("id");
                        String name = (String) data.get("name");
                        int price = ((Long) data.get("price")).intValue();
                        String categoryID = (String) data.get("categoryID");
                        String urlImage = (String) data.get("urlImage");
                        String description = (String) data.get("description");
                        Product product = new Product(id, urlImage, name, price, description, categoryID);


                        Glide.with(holder.itemView.getContext())
                                .load(urlImage)
                                .centerCrop()
                                .into(holder.ivImage);
                        holder.tvName.setText(name);
                        holder.tvName.setOnClickListener(view -> iClickProductListener.clickShowDetail(product));
                        holder.ivImage.setOnClickListener(view -> iClickProductListener.clickShowDetail(product));
                    }
                }
            }
        });
        holder.tvUnitPrice.setText(Utils.getFormatNumber(productOrder.getUnitPrice()));
        if (isOrderHistory) {
            holder.tvQuantity.setText("Số lượng mua: " + productOrder.getQuantity());
            holder.ivAddQuantity.setVisibility(View.GONE);
            holder.ivMinusQuantity.setVisibility(View.GONE);
        } else {
            holder.tvQuantity.setText("" + productOrder.getQuantity());
            holder.ivAddQuantity.setOnClickListener(view -> iClickProductListener.clickUpdateQuantity(true, productOrder));
            holder.ivMinusQuantity.setOnClickListener(view -> {
                if (productOrder.getQuantity() <= 1) {
                    iClickProductListener.clickDelete(productOrder.getId());
                } else {
                    iClickProductListener.clickUpdateQuantity(false, productOrder);
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        if (list != null) return list.size();
        return 0;
    }

    public interface IClickProductListener {
        void clickUpdateQuantity(boolean isAdd, ProductOrder productOrder);

        void clickDelete(int idProductOrder);

        void clickShowDetail(Product product);
    }

    public class ProductOrderViewHolder extends RecyclerView.ViewHolder {
        CircleImageView ivImage;
        TextView tvName, tvUnitPrice, tvQuantity;
        ImageView ivMinusQuantity, ivAddQuantity;

        public ProductOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_product_in_cart);
            tvName = itemView.findViewById(R.id.tv_name_product_in_cart);
            tvUnitPrice = itemView.findViewById(R.id.tv_price_product_in_cart);
            tvQuantity = itemView.findViewById(R.id.tv_quantity_product_in_cart);
            ivMinusQuantity = itemView.findViewById(R.id.iv_minus_quantity_in_cart);
            ivAddQuantity = itemView.findViewById(R.id.iv_add_quantity_in_cart);
        }
    }
}
