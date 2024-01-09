package com.lab.crmanagement.backend.client;

import com.lab.crmanagement.backend.data.Employee.Employee;
import com.lab.crmanagement.backend.data.menu.MenuSection;
import com.lab.crmanagement.backend.data.table.Table;

import java.util.ArrayList;
import java.util.HashMap;

public class ClientModel {
    private Employee employeeInformation;
    private HashMap<Integer, Table> tableData;
    private HashMap<String, MenuSection> menu;
    /* Structure of Object[] is {tableId, MenuItem} */
    private ArrayList<Object[]> ongoingOrders;



}
