package com.lab.crmanagement.backend.data.ongoingorders;

import com.lab.crmanagement.backend.data.menu.MenuItem;
import com.lab.crmanagement.backend.data.database.OngoingOrdersModelOperations;

import java.util.ArrayList;

public class OngoingOrdersModel implements OngoingOrdersModelOperations {
    //Object[] -> {table_id, MenuItem}
    private ArrayList<Object[]> orders;

    @Override
    public void addToOngoingOrders(final OngoingOrderPair order) {
        if (order.items() != null)
        {
            for (MenuItem item: order.items())
            {
                orders.add(new Object[]{order.tableID(), item});
            }
        }
    }

    @Override
    public void deleteFromOngoingOrders(OngoingOrderPair items) {
        for (int i = 0; i < orders.size(); i++) {
            if (items.tableID() == (int)(orders.get(i)[0]))
            {
                orders.remove(i);
            }
        }
    }
}
