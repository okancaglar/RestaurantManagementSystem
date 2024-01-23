package com.lab.crmanagement.navigationmenu.itemmenu;

import com.lab.crmanagement.backend.data.menu.MenuItem;

import java.util.ArrayList;

public class NewOrder {
    private int tableID;
    private ArrayList<MenuItem> items;

    public NewOrder(int tableID, ArrayList<MenuItem> items) {
        this.tableID = tableID;
        this.items = items;
    }

    public int getTableID() {
        return tableID;
    }

    public ArrayList<MenuItem> getItems() {
        return items;
    }

}
