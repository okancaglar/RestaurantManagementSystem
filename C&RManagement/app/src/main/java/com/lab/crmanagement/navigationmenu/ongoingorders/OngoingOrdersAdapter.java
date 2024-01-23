package com.lab.crmanagement.navigationmenu.ongoingorders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lab.crmanagement.R;
import com.lab.crmanagement.backend.client.ClientSingletonService;
import com.lab.crmanagement.backend.data.menu.MenuItem;
import com.lab.crmanagement.backend.data.ongoingorders.OngoingOrderPair;

import java.util.ArrayList;

public class OngoingOrdersAdapter extends RecyclerView.Adapter<OngoingOrdersAdapter.OngoingOrderHolder> {

    /* object : {tableId, MenuItem}*/
    private ArrayList<Object[]> ongoingOrders;

    public OngoingOrdersAdapter(ArrayList<Object[]> ongoingOrders) {
        this.ongoingOrders = ongoingOrders;
    }

    @NonNull
    @Override
    public OngoingOrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View holder = LayoutInflater.from(parent.getContext()).inflate(R.layout.ongoingorder_item,
                parent, false);
        return new OngoingOrderHolder(holder);
    }

    @Override
    public void onBindViewHolder(@NonNull OngoingOrderHolder holder, int position) {
        Object[] order = ongoingOrders.get(position);
        holder.setItem(order);
        holder.getOngoingOrderName().setText(((MenuItem)order[1]).getName());
        holder.getTableId().setText(order[0] + "");
        holder.getDoneButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ClientSingletonService.getClientInstance().sendOngoingOrderData(
                                new OngoingOrderPair((Integer) holder.getItem()[0],
                                        (MenuItem) holder.getItem()[1])
                        );
                    }
                }).start();
            }
        });
    }

    @Override
    public int getItemCount() {
        return ongoingOrders.size();
    }


    public void updateData(ArrayList<Object[]> data)
    {
        notifyDataSetChanged();
    }

    public class OngoingOrderHolder extends RecyclerView.ViewHolder
    {
        private TextView ongoingOrderName;
        private TextView tableId;
        private Button doneButton;
        private Object[] item;

        public void setItem(Object[] item) {
            this.item = item;
        }

        public OngoingOrderHolder(@NonNull View itemView) {
            super(itemView);
            ongoingOrderName = itemView.findViewById(R.id.ongoingOrderName);
            tableId = itemView.findViewById(R.id.tableId);
            doneButton = itemView.findViewById(R.id.done);

        }

        public Object[] getItem() {
            return item;
        }

        public TextView getOngoingOrderName() {
            return ongoingOrderName;
        }

        public TextView getTableId() {
            return tableId;
        }

        public Button getDoneButton() {
            return doneButton;
        }
    }

}
