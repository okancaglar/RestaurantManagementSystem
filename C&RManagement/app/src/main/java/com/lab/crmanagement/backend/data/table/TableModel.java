package com.lab.crmanagement.backend.data.table;

import com.lab.crmanagement.backend.data.menu.MenuItem;
import com.lab.crmanagement.backend.data.database.TableModelOperations;

import java.util.ArrayList;
import java.util.HashMap;

public class TableModel implements TableModelOperations {
    private HashMap<Integer, Table> tables;

    public TableModel(HashMap<Integer, Table> tables) {
        this.tables = tables;
    }

    @Override
    public void addOrder(int tableID, ArrayList<MenuItem> items) {
        Table table;
        if ((table = tables.get(tableID)) != null)
        {
            table.addItems(items);
        }
    }

    public HashMap<Integer, Table> getTables() {
        return tables;
    }

    public void resetTable(int tableId)
    {
        tables.get(tableId).reset();
    }
}
