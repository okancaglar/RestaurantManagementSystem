package com.lab.crmanagement.backend.data.database;

import com.lab.crmanagement.backend.data.ongoingorders.*;

public interface OngoingOrdersModelOperations {
     void addToOngoingOrders(OngoingOrderPair items);
     void deleteFromOngoingOrders(OngoingOrderPair items);
}
