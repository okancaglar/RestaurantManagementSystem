package com.lab.crmanagement.backend.data.client;

import com.lab.crmanagement.backend.client.Client;
import com.lab.crmanagement.backend.data.Employee.Employee;
import com.lab.crmanagement.backend.data.menu.MenuSection;
import com.lab.crmanagement.backend.data.table.Table;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

public class ClientModel {
    private Employee employeeInformation;
    private HashMap<Integer, Table> tableData;
    private HashMap<String, MenuSection> menu;
    /* Structure of Object[] is {tableId, MenuItem} */
    private ArrayList<Object[]> ongoingOrders;



}
