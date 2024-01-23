package com.lab.crmanagement.backend.data.table;

import com.lab.crmanagement.backend.data.menu.MenuItem;

import java.io.Serializable;
import java.util.ArrayList;

public class Table implements Serializable {

    //table status
    public static final int EMPTY = 0;
    public static final int WAITING_ORDER = 1;
    public static final int OCCUPIED = 2;


    //table attributes
    private int id;
    private int status;
    private double totalCost;
    private ArrayList<MenuItem> orders;

    public Table(int id) {
        this.id = id;
        this.status = EMPTY;
        this.totalCost = 0;
        this.orders = new ArrayList<>();
    }

    public void addItems(ArrayList<MenuItem> items)
    {
        status = WAITING_ORDER;
        orders.addAll(items);
        for (int i = 0; i < items.size(); i++) {
            totalCost += items.get(i).getPrice();
        }
    }

    public int getId() {
        return id;
    }

    public String getStatus() {
        switch (status)
        {
            case WAITING_ORDER ->
            {
                return "Waiting Order";
            }
            case OCCUPIED ->
            {
                return "Occupied";
            }
            default ->
            {
                return "Empty";
            }
        }

    }

    public double getTotalCost() {
        return totalCost;
    }

    public ArrayList<MenuItem> getOrders() {
        return orders;
    }

    //todo getters, setters implementation
}
