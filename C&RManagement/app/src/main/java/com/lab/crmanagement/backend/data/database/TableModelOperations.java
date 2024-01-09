package com.lab.crmanagement.backend.data.database;

import com.lab.crmanagement.backend.data.menu.*;
import com.lab.crmanagement.backend.data.table.*;

import java.util.ArrayList;
import java.util.HashMap;

public interface TableModelOperations {
    void addOrder(int tableID, ArrayList<MenuItem> items);
    HashMap<Integer, Table> getTables();
}
