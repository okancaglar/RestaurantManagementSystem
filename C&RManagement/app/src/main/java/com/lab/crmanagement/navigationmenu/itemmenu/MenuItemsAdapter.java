package com.lab.crmanagement.navigationmenu.itemmenu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lab.crmanagement.R;
import com.lab.crmanagement.backend.data.client.ClientModelSingletonService;
import com.lab.crmanagement.backend.data.menu.MenuItem;


import java.util.List;

public class MenuItemsAdapter extends RecyclerView.Adapter<MenuItemsAdapter.MenuItemHolder> {
    private List<MenuItem> items;
    private MenuItemsListener listener;

    public MenuItemsAdapter(List<MenuItem> items, MenuItemsListener listener)
    {
        this.items = items;
        this.listener = listener;
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
        holder.setItem(item);
        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.amount.setText(Integer.parseInt(holder.amount.getText().toString()) + 1 + "");
                listener.addItemToTheCart(item);
            }
        });
        holder.minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentAmount = Integer.parseInt(holder.amount.getText().toString());
                if (currentAmount > 0)
                {
                    holder.amount.setText((currentAmount - 1) + "");
                    listener.deleteItemFromCart(holder.item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class MenuItemHolder extends RecyclerView.ViewHolder{
        private MenuItem item;
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

        public MenuItem getItem() {
            return item;
        }

        public void setItem(MenuItem item) {
            this.item = item;
        }
    }


    public interface MenuItemsListener
    {
        void addItemToTheCart(MenuItem item);
        void deleteItemFromCart(MenuItem item);
    }
}
