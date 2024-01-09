package com.lab.crmanagement.navigationmenu.itemmenu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lab.crmanagement.R;
import com.lab.crmanagement.backend.data.menu.MenuItem;


import java.util.List;

public class MenuItemsAdapter extends RecyclerView.Adapter<MenuItemsAdapter.MenuItemHolder> {
    private List<MenuItem> items;

    public MenuItemsAdapter(List<MenuItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public MenuItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);
        return new MenuItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuItemHolder holder, int position) {
        MenuItem item = items.get(position);
        holder.itemName.setText(item.getName());
        holder.price.setText(item.getPrice() + "");
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class MenuItemHolder extends RecyclerView.ViewHolder{
        private TextView itemName;
        private TextView price;
        private Button addButton;
        private Button minusButton;
        private TextView amount;
        public MenuItemHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.addFoodName);
            price = itemView.findViewById(R.id.addFoodPrice);
            addButton = itemView.findViewById(R.id.addAddButton);
            minusButton = itemView.findViewById(R.id.addMinusButton);
            amount = itemView.findViewById(R.id.addAmountText);
        }

        public TextView getItemName() {
            return itemName;
        }

        public TextView getPrice() {
            return price;
        }

        public Button getAddButton() {
            return addButton;
        }

        public Button getMinusButton() {
            return minusButton;
        }

        public TextView getAmount() {
            return amount;
        }
    }
}
