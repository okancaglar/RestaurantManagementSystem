package com.lab.crmanagement.backend.data.database;

import com.lab.crmanagement.backend.data.menu.MenuItem;
import com.lab.crmanagement.backend.data.ongoingorders.*;

import java.util.ArrayList;

public interface OngoingOrdersModelOperations {
     void addToOngoingOrders(int tableId, ArrayList<MenuItem> items);
     void deleteFromOngoingOrders(OngoingOrderPair items);
}
