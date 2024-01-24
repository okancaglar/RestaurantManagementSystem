package com.lab.crmanagement.backend.data.client;

import com.lab.crmanagement.backend.client.Client;
import com.lab.crmanagement.backend.data.Employee.Employee;
import com.lab.crmanagement.backend.data.menu.MenuItem;
import com.lab.crmanagement.backend.data.menu.MenuSection;
import com.lab.crmanagement.backend.data.table.Table;
import com.lab.crmanagement.backend.data.table.TableOrderTransferStreamData;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ClientModel {
    private Employee employeeInformation;
    private HashMap<Integer, Table> tableData;
    private HashMap<String, MenuSection> menu;
    /* Structure of Object[] is {tableId, MenuItem} */
    private ArrayList<Object[]> ongoingOrders = new ArrayList<>();
    private HashMap<Integer, Employee> employees;


    /*public ClientModel(Employee employeeInformation, HashMap<Integer, Table> tableData, HashMap<String,
            MenuSection> menu, ArrayList<Object[]> ongoingOrders, HashMap<Integer, Employee> employees) {
        this.employeeInformation = employeeInformation;
        this.tableData = tableData;
        this.menu = menu;
        this.ongoingOrders = ongoingOrders;
        this.employees = employees;
    }*/

    public void setEmployees(HashMap<Integer, Employee> employees) {
        this.employees = employees;
    }

    public Employee getEmployeeInformation() {
        return employeeInformation;
    }

    public HashMap<Integer, Table> getTableData() {
        return tableData;
    }

    public HashMap<String, MenuSection> getMenu() {
        return menu;
    }

    public ArrayList<Object[]> getOngoingOrders() {
        return ongoingOrders;
    }

    public void setEmployeeInformation(Employee employeeInformation) {
        this.employeeInformation = employeeInformation;
    }

    public void setTableData(HashMap<Integer, Table> tableData) {
        this.tableData = tableData;
    }

    public void setMenu(HashMap<String, MenuSection> menu) {
        this.menu = menu;
    }

    public void setOngoingOrders(ArrayList<Object[]> ongoingOrders) {
        this.ongoingOrders = ongoingOrders;
    }

    public void addOrderToTable(TableOrderTransferStreamData data) {
        tableData.get(data.tableID()).addItems(data.newOrder());
    }

    public void addOrderToOngoingOrders(TableOrderTransferStreamData data)
    {
        for (int i = 0; i < data.newOrder().size(); i++) {
            ongoingOrders.add(new Object[]{data.tableID(), data.newOrder().get(i)});
        }
    }

    public void deleteFromOngoingOrder(int tableID, MenuItem item)
    {

        for (int j = 0; j < ongoingOrders.size(); j++) {
            if ((int)(ongoingOrders.get(j)[0]) == tableID &&
                    ((MenuItem)(ongoingOrders.get(j)[1])).getId() == item.getId())
            {
                ongoingOrders.remove(j);
                break;
            }
        }

    }

    public List<Table> getTablesAsList()
    {
        List<Table> tableList = new ArrayList<>();
        for (Map.Entry<Integer, Table> table: tableData.entrySet())
        {
            tableList.add(table.getValue());
        }
        return tableList;
    }
    public Table getTable(int tableId)
    {
        return tableData.get(tableId);
    }

    public List<MenuSection> getMenuSectionsAsList()
    {
        List<MenuSection> menuSectionsList = new ArrayList<>();
        for(Map.Entry<String, MenuSection> menuSection: menu.entrySet())
        {
            menuSectionsList.add(menuSection.getValue());
        }
        return menuSectionsList;
    }

    public void updateTableStatus(int tableId)
    {
        boolean flag=true;
        for(Object[] order: ongoingOrders)
        {
            if ((int)order[0] == tableId)
            {
                flag = false;
            }
        }
        if (flag)
        {
            Table table = getTable(tableId);
            if (table.getOrders().size() != 0)
            {
                table.setStatus(Table.OCCUPIED);
            }
        }
    }

    public void resetTable(int tableId)
    {
        tableData.get(tableId).reset();
    }
}
