package com.lab.crmanagement.navigationmenu.tables;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lab.crmanagement.R;
import com.lab.crmanagement.backend.client.ClientSingletonService;
import com.lab.crmanagement.backend.data.client.ClientModel;
import com.lab.crmanagement.backend.data.client.ClientModelSingletonService;
import com.lab.crmanagement.backend.data.menu.MenuItem;
import com.lab.crmanagement.backend.data.table.Table;
import com.lab.crmanagement.navigationmenu.itemmenu.MenuFragment;
import android.view.View.OnClickListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TableFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TableFragment extends Fragment implements TableView{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TABLE_ID = "table_id";
    // TODO: Rename and change types of parameters
    private int tableId;

    private Table table;
    private TablePresenter presenter;

    private TextView addOrder;
    private TextView totalCost;
    private TextView tableNo;
    private TextView tableStatus;
    private TextView orders;

    private Button settleTheBill;





    public TableFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static TableFragment newInstance(int tableIdP) {
        TableFragment fragment = new TableFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TABLE_ID, tableIdP);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tableId = getArguments().getInt(ARG_TABLE_ID);
            table = ClientModelSingletonService.getClientModelInstance().getTable(tableId);
        }
        presenter = new TablePresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceStatte) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_table, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addOrder = view.findViewById(R.id.addorder);
        totalCost = view.findViewById(R.id.totalCostTxt);
        tableNo = view.findViewById(R.id.tableno);
        tableStatus = view.findViewById(R.id.status);
        orders = view.findViewById(R.id.orders);
        settleTheBill = view.findViewById(R.id.settle);


        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OrdersFragment ordersFragment = OrdersFragment.newInstance(getOrderItemNames());

                getActivity().getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.navigatorFragment, ordersFragment, null)
                        .addToBackStack(null)
                        .commit();
            }
        });

        totalCost.setText("Total Cost: " + table.getTotalCost());
        tableNo.setText("Table No: " + table.getId());
        tableStatus.setText("Status: " + table.getStatus());


        addOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MenuFragment menuFragment = MenuFragment.newInstance(tableId);

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.navigatorFragment, menuFragment, null)
                        .commit();
            }
        });

        settleTheBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClientSingletonService.getClientInstance().sendTableSettleData(tableId);

                TablesFragment tablesFragment = TablesFragment.newInstance(
                        ClientModelSingletonService.getClientModelInstance().getTablesAsList());

                getActivity().getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.navigatorFragment, tablesFragment, null)
                        .addToBackStack(null)
                        .commit();
            }
        });

    }

    @Override
    public void updateUi() {

        table = ClientModelSingletonService.getClientModelInstance().getTable(tableId);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                totalCost.setText("Total Cost: " + table.getTotalCost());
                tableStatus.setText("Status: " + table.getStatus());

            }
        });
    }

    public ArrayList<String> getOrderItemNames()
    {
        ArrayList<String> itemNames = new ArrayList<>();
        ArrayList<MenuItem> items = table.getOrders();

        for(MenuItem item: items)
            itemNames.add(item.getName());
        return itemNames;
    }
}