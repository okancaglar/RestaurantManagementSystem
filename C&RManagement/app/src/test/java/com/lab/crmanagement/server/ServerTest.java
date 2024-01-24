package com.lab.crmanagement.server;

import com.lab.crmanagement.backend.data.AppTransferStreamData;
import com.lab.crmanagement.backend.data.TransferStreamData;
import com.lab.crmanagement.backend.data.DataTypes;
import com.lab.crmanagement.backend.data.Employee.*;
import com.lab.crmanagement.backend.data.database.*;
import com.lab.crmanagement.backend.data.menu.*;
import com.lab.crmanagement.backend.data.table.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

import com.lab.crmanagement.backend.server.Server;

class ServerTest {


    Server server;
    Thread serverThread;

    @BeforeEach
    void startServer()
    {
        HashMap<Integer, Employee> employeeHashMap = new HashMap<>();
        employeeHashMap.putIfAbsent(1, new Employee(1, "okan", "caglar", "123", false));
        EmployeeModel employeeModel = new EmployeeModel(employeeHashMap);

        //create menu
        MenuItem cheeseburger = new MenuItem("hamburgers", 1, "cheeseburger", 5,
                "beef patty, cheeseburger, pickle, tomato, red onion");
        HashMap<Integer, MenuItem> hamburgerItems = new HashMap<>();
        hamburgerItems.put(cheeseburger.getId(), cheeseburger);
        MenuSection hamburgers = new MenuSection("hamburgers", hamburgerItems);

        HashMap<String, MenuSection> sections = new HashMap<>();
        sections.put("hamburgers", hamburgers);
        MenuModel menuModel = new MenuModel(sections);

        Table t1 = new Table(1);
        Table t2 = new Table(2);

        HashMap<Integer, Table> tableHashMap = new HashMap<>();
        tableHashMap.put(1, t1);
        tableHashMap.put(2, t2);

        TableModel tbModel = new TableModel(tableHashMap);

        DBModel database = new DBModel(new EmployeeModel(employeeHashMap), menuModel, tbModel);
        server = new Server(8089, database, null);
        serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    server.startServer();
                }catch (Exception e)
                {
                    System.out.println(e.getMessage() + "Exception at server thread");
                }
            }
        });
        serverThread.start();
    }

    @Test
    void loginTest() throws IOException, ClassNotFoundException {
        Socket client = new Socket(InetAddress.getByName("localhost"), 8089);
        ObjectOutputStream outputStream = new ObjectOutputStream(client.getOutputStream());
        ObjectInputStream inputStream = new ObjectInputStream(client.getInputStream());

        AppTransferStreamData loginData = new AppTransferStreamData(DataTypes.EmployeeSessionTransferStreamData,
                new EmployeeSessionTransferStreamData(1, "123", RequestCode.LOGIN));

        outputStream.writeObject(loginData);
        outputStream.flush();

        AppTransferStreamData loginResponse = (AppTransferStreamData) inputStream.readObject();

        Assertions.assertEquals(RequestCode.SUCCESS_EMPLOYEE, ((EmployeeSessionTransferStreamData)loginResponse.getData()).code());
    }


    @Test
    void initialDataTest() throws IOException, ClassNotFoundException {
        Socket client = new Socket(InetAddress.getByName("localhost"), 8089);
        ObjectOutputStream outputStream = new ObjectOutputStream(client.getOutputStream());
        ObjectInputStream inputStream = new ObjectInputStream(client.getInputStream());

        AppTransferStreamData loginData = new AppTransferStreamData(DataTypes.EmployeeSessionTransferStreamData,
                new EmployeeSessionTransferStreamData(1, "123", RequestCode.LOGIN));

        outputStream.writeObject(loginData);
        outputStream.flush();

        AppTransferStreamData loginResponse = (AppTransferStreamData) inputStream.readObject();

        Assertions.assertEquals(RequestCode.SUCCESS_EMPLOYEE, ((EmployeeSessionTransferStreamData)loginResponse.getData()).code());

        AppTransferStreamData employeeInfo = (AppTransferStreamData) inputStream.readObject();

        Assertions.assertEquals(DataTypes.EmployeeTransferStreamData, employeeInfo.getType());

        AppTransferStreamData tableData = (AppTransferStreamData) inputStream.readObject();

        Assertions.assertEquals(DataTypes.TableTransferStreamData, tableData.getType());

        AppTransferStreamData menuData = (AppTransferStreamData) inputStream.readObject();

        Assertions.assertEquals(DataTypes.MenuItemsTransferStreamData, menuData.getType());


    }









    @Test
    void testReadInput() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException {
        Server server = new Server(0, null, null);
        Method method = server.getClass().getDeclaredMethod("readData", ObjectInputStream.class);
        method.setAccessible(true);

        AppTransferStreamData data = new AppTransferStreamData(DataTypes.EmployeeTransferStreamData,
                new EmployeeSessionTransferStreamData(1, "123", RequestCode.LOGIN));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)){
            oos.writeObject(data);
        }

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream inputStream = new ObjectInputStream(bais);

        AppTransferStreamData result = (AppTransferStreamData) method.invoke(server, inputStream);

        assertEquals(data.getType(), result.getType());

        bais.close();
        baos.close();
    }

    @Test
    void closeClientConnectionTest() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InterruptedException {

        Server server = new Server(22222, null, null);
        Method method = server.getClass().getDeclaredMethod("closeConnectionWithClient", Object[].class, ArrayList.class);
        method.setAccessible(true);

        ServerSocket serverSocket = new ServerSocket(5034, 20, InetAddress.getByName("localhost"));
        Thread serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = serverSocket.accept();
                    ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        serverThread.start();
        Socket testSocket = new Socket(InetAddress.getByName("localhost"), 5034);
        ArrayList<Object[]> testClients = new ArrayList<>();
        ObjectOutputStream outputStream = new ObjectOutputStream(testSocket.getOutputStream());
        outputStream.flush();
        ObjectInputStream inputStream = new ObjectInputStream(testSocket.getInputStream());
        Object[] testClientData = new Object[]{testSocket, inputStream, outputStream};
        testClients.add(testClientData);

        method.invoke(server, testClientData, testClients);

        Assertions.assertEquals(0, testClients.size());

        serverThread.interrupt();
        serverThread.join();


    }

    @AfterEach
    void closeServer()
    {
        //server.closeServer();
        serverThread.interrupt();
    }





















   /* Server server;

    @BeforeEach
    void startServer()
    {
        HashMap<Integer, Employee> employeeHashMap = new HashMap<>();
        employeeHashMap.putIfAbsent(1, new Employee(1, "okan", "caglar", "123"));
        EmployeeModel employeeModel = new EmployeeModel(employeeHashMap);

        //create menu
        MenuItem cheeseburger = new MenuItem("hamburgers", 1, "cheeseburger", 5,
                "beef patty, cheeseburger, pickle, tomato, red onion");
        HashMap<Integer, MenuItem> hamburgerItems = new HashMap<>();
        hamburgerItems.put(cheeseburger.getId(), cheeseburger);
        MenuSection hamburgers = new MenuSection("hamburgers", hamburgerItems);

        HashMap<String, MenuSection> sections = new HashMap<>();
        sections.put("hamburgers", hamburgers);
        MenuModel menuModel = new MenuModel(sections);

        TableModel tbModel = new TableModel();

        DBModel database = new DBModel(new EmployeeModel(employeeHashMap), menuModel, null);
        server = new Server(8000, 10, database);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    server.startServer();
                }catch (Exception e)
                {
                    System.out.println(e.getMessage() + "Exception at server thread");
                }
            }
        }).start();
    }

    @Test
    void loginTest() throws IOException, ClassNotFoundException {
        Socket client = new Socket(InetAddress.getByName("localhost"), 8000);
        ObjectOutputStream outputStream = new ObjectOutputStream(client.getOutputStream());
        ObjectInputStream inputStream = new ObjectInputStream(client.getInputStream());

        outputStream.writeObject(new AppTransferStreamData(DataTypes.EmployeeTransferStreamData, new EmployeeTransferStreamData(1, "123", RequestCode.LOGIN)));
        AppTransferStreamData input = (AppTransferStreamData) inputStream.readObject();
        DataTypes type = input.getType();
        Assertions.assertEquals(DataTypes.EmployeeTransferStreamData, type);

        EmployeeTransferStreamData employeeTransferStreamData = (EmployeeTransferStreamData) input.getData();

        Assertions.assertSame(employeeTransferStreamData.code(), RequestCode.SUCCESS);
        *//*inputStream.close();
        outputStream.close();
        client.close();*//*
    }

    @AfterEach
    void close()
    {
        server.closeServer();
    }
*/
}