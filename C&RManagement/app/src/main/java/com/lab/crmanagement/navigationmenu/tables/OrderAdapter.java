package com.lab.crmanagement.navigationmenu.tables;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lab.crmanagement.R;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderHolder> {

    private ArrayList<String> itemNames;

    public OrderAdapter(ArrayList<String> itemNames) {
        this.itemNames = itemNames;
    }

    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View holder = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        return new OrderHolder(holder);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHolder holder, int position) {
        holder.getOrderText().setText(itemNames.get(position));
    }

    @Override
    public int getItemCount() {
        return itemNames.size();
    }

    public class OrderHolder extends RecyclerView.ViewHolder{
        private TextView orderText;
        public OrderHolder(@NonNull View itemView) {
            super(itemView);
            orderText = itemView.findViewById(R.id.order);
        }

        public TextView getOrderText() {
            return orderText;
        }
    }
}
