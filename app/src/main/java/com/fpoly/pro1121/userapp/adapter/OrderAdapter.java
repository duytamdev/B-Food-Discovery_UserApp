package com.fpoly.pro1121.userapp.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fpoly.pro1121.userapp.R;
import com.fpoly.pro1121.userapp.utils.Utils;
import com.fpoly.pro1121.userapp.model.Order;
import com.fpoly.pro1121.userapp.model.ProductOrder;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    List<Order> list = new ArrayList<>();
    IOrderListener iOrderListener;

    public OrderAdapter(IOrderListener iOrderListener) {
        this.iOrderListener = iOrderListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Order> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = list.get(position);
        if (order == null) return;
        holder.tvUnitPrice.setText(Utils.getFormatNumber(order.getUnitPrice()));
        holder.tvDate.setText(Utils.DateToStringMonth(order.getDate()));
        holder.itemView.setOnClickListener(view -> iOrderListener.clickShowDetail(order.getProductOrderList()));
    }

    @Override
    public int getItemCount() {
        if (list != null) return list.size();
        return 0;
    }

    public interface IOrderListener {
        void clickShowDetail(List<ProductOrder> list);
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvUnitPrice;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_date_order);
            tvUnitPrice = itemView.findViewById(R.id.tv_unit_price_order);
        }
    }
}
