package com.fpoly.pro1121.userapp.adapter;


import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fpoly.pro1121.userapp.R;
import com.fpoly.pro1121.userapp.Utils;
import com.fpoly.pro1121.userapp.model.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder>{

    List<Order> list = new ArrayList<>();

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Order> list){
        this.list = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_history,parent,false);
        return new OrderHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryViewHolder holder, int position) {
        Order order = list.get(position);
        if(order==null) return;
        holder.tvUnitPrice.setText(Utils.getFormatNumber(order.getUnitPrice()));
        holder.ivShowDetails.setOnClickListener(view ->{

        });
    }

    @Override
    public int getItemCount() {
        if(list!=null) return list.size();
        return 0;
    }

    public class OrderHistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvUnitPrice;
        ImageView ivShowDetails;
        public OrderHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUnitPrice = itemView.findViewById(R.id.tv_unit_price_order_history);
            ivShowDetails = itemView.findViewById(R.id.iv_arrow_show_detail_order);
        }
    }
}
