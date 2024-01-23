package com.lab.crmanagement.backend.data.ongoingorders;

import com.lab.crmanagement.backend.data.menu.MenuItem;
import com.lab.crmanagement.backend.data.database.OngoingOrdersModelOperations;

import java.util.ArrayList;

public class OngoingOrdersModel implements OngoingOrdersModelOperations {
    //Object[] -> {table_id, MenuItem}
    private ArrayList<Object[]> orders = new ArrayList<>();

    @Override
    public void addToOngoingOrders(int tableId, ArrayList<MenuItem> items) {
        if (items != null)
        {
            for(MenuItem item: items)
            {
                orders.add(new Object[]{tableId, item});
            }
        }
    }

    @Override
    public void deleteFromOngoingOrders(OngoingOrderPair item) {
        for (int i = 0; i < orders.size(); i++) {
            if (item.tableID() == (int)(orders.get(i)[0]))
            {
                orders.remove(i);
            }
        }
    }
}
