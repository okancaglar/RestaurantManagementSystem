package com.lab.crmanagement.navigationmenu.tables;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lab.crmanagement.R;
import com.lab.crmanagement.backend.data.table.Table;

import java.util.List;

public class TablesAdapter extends RecyclerView.Adapter<TablesAdapter.TableHolder> {

    private List<Table> tables;
    private TablesFragmentListener listener;


    public TablesAdapter(List<Table> tables, TablesFragmentListener listener) {
        this.tables = tables;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TableHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View holder = LayoutInflater.from(parent.getContext()).inflate(R.layout.table, parent, false);
        return new TableHolder(holder);
    }

    @Override
    public void onBindViewHolder(@NonNull TableHolder holder, int position) {
        holder.tableButton.setText(Integer.toString(tables.get(position).getId()));
        holder.tableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                {
                    int position = holder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION)
                    {
                        listener.onButtonClicked(position, tables.get(position).getId());
                    }
                }
            }
        });
        //todo color choose based on ongoingorder
    }

    @Override
    public int getItemCount() {
        return tables.size();
    }

    public static class TableHolder extends RecyclerView.ViewHolder{

        Button tableButton;
        public TableHolder(@NonNull View itemView) {
            super(itemView);

            tableButton = (Button) itemView.findViewById(R.id.tableButton);
        }
    }


    public interface TablesFragmentListener{
        void onButtonClicked(int position, int tableId);
    }

}
