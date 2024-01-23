package com.lab.crmanagement.backend.data.database;

import com.lab.crmanagement.backend.data.Employee.*;
import com.lab.crmanagement.backend.data.menu.*;
import com.lab.crmanagement.backend.data.ongoingorders.*;
import com.lab.crmanagement.backend.data.table.*;

import java.util.ArrayList;
import java.util.HashMap;

public class DBModel implements TableModelOperations, OngoingOrdersModelOperations, EmployeeModelOperations,
MenuModelOperations{
    private EmployeeModel employees;
    private MenuModel menu;
    private TableModel tables;
    private OngoingOrdersModel ongoingOrders;

    public DBModel(EmployeeModel employees, MenuModel menu, TableModel tables) {
        this.employees = employees;
        this.menu = menu;
        this.tables = tables;
        ongoingOrders = new OngoingOrdersModel();
    }

    @Override
    public HashMap<Integer, Table> getTables()
    {
        return tables.getTables();
    }



    @Override
    public EmployeeLoginPair login(int id, String password)
    {
        return employees.login(id, password);
    }

    @Override
    public void addOrder(int tableID, ArrayList<MenuItem> items) {
        tables.addOrder(tableID, items);
    }


    @Override
    public void addToOngoingOrders(int tableId, ArrayList<MenuItem> items) {
        ongoingOrders.addToOngoingOrders(tableId, items);
    }

    @Override
    public void deleteFromOngoingOrders(OngoingOrderPair items) {
        ongoingOrders.deleteFromOngoingOrders(items);
    }

    @Override
    public Employee getEmployee(int id)
    {
        return employees.getEmployee(id);
    }

    @Override
    public HashMap<Integer, Employee> getEmployees() {
        return employees.getEmployees();
    }

    @Override
    public HashMap<String, MenuSection> getMenu()
    {
        return menu.getMenu();
    }
}
