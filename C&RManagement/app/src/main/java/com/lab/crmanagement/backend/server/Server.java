package com.lab.crmanagement.backend.server;


import android.content.Context;
import android.util.Log;

import com.lab.crmanagement.backend.data.AppTransferStreamData;
import com.lab.crmanagement.backend.data.DataTypes;
import com.lab.crmanagement.backend.data.Employee.*;
import com.lab.crmanagement.backend.data.TransferStreamData;
import com.lab.crmanagement.backend.data.admin.*;
import com.lab.crmanagement.backend.data.database.*;
import com.lab.crmanagement.backend.data.menu.*;
import com.lab.crmanagement.backend.data.ongoingorders.*;
import com.lab.crmanagement.backend.data.table.*;
import com.lab.crmanagement.localdatabase.AppDatabase;
import com.lab.crmanagement.localdatabase.TableDAO;


import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;


public class Server{

    private ServerSocket server;
    private final Logger logger = Logger.getLogger(Server.class.getName());

    //Object[]{Socket, ObjectInputStream, ObjectOutputStream}
    private final ArrayList<Object[]> clientsConnectionData = new ArrayList<>();
    private int port;
    private DBModel database;
    private final Lock clientDataManipulationLock  = new ReentrantLock();
    private final Lock orderLock = new ReentrantLock();
    private Context context;


    public Server(final int port, final DBModel database, Context context) {
        this.port = port;
        this.database = database;
        this.context = context;
    }

    public void startServer()
    {
        try {
            server = new ServerSocket(port, 20, InetAddress.getByName("localhost"));
            Log.d("SERVER", server.isClosed() + "");
            while (!server.isClosed())
            {
                try {
                    Socket client = server.accept();
                    ObjectOutputStream outputStream = new ObjectOutputStream(client.getOutputStream());
                    ObjectInputStream inputStream = new ObjectInputStream(client.getInputStream());
                    clientsConnectionData.add(new Object[]{client, inputStream, outputStream});
                    new Thread(new ClientHandler(client, inputStream, outputStream)).start();
                    logger.info("new connection is established at: " + ZonedDateTime.now());
                }catch (IOException ioException)
                {
                    logger.warning("Connection process failed with client. ERROR: " + ioException.getMessage());
                }
            }
        }
        catch (Exception e) {
            logger.finest("Server is not created! SERVER CREATION ERROR: " + e.getMessage());
        }finally {
            closeServer();
        }
    }

    public void closeServer()
    {
        for(Object[] clientData : clientsConnectionData)
        {
            closeConnectionWithClient(clientData, clientsConnectionData);
        }
        try {
            server.close();
        } catch (IOException e) {
            logger.warning("Exception when trying to close server! ERROR: " + e.getMessage());
        }
    }
    private void closeConnectionWithClient(Object[] clientData,  ArrayList<Object[]> clientsConnectionDataT){
        clientDataManipulationLock.lock();
        try {
            ((ObjectOutputStream)clientData[2]).close();
            ((ObjectInputStream)clientData[1]).close();
            ((Socket)clientData[0]).close();
            clientsConnectionDataT.remove(clientData);
        } catch (IOException e) {
            logger.finest(e.getMessage() + " Exception when closing socket: at Port" + ((Socket)clientData[0]).getPort());
        }finally {
            clientDataManipulationLock.unlock();
        }
    }
    private void closeConnectionWithClient(Object[] clientData){
        clientDataManipulationLock.lock();
        try {
            ((ObjectOutputStream)clientData[2]).close();
            ((ObjectInputStream)clientData[1]).close();
            ((Socket)clientData[0]).close();
            clientsConnectionData.remove(clientData);
        } catch (IOException e) {
            logger.finest(e.getMessage() + " Exception when closing socket: at Port" + ((Socket)clientData[0]).getPort());
        }finally {
            clientDataManipulationLock.unlock();
        }
    }
    private HashMap<Integer, Table> getTableDataFromDatabase()
    {
        return database.getTables();
    }
    private Employee getEmployee(int id)
    {
        return database.getEmployee(id);
    }

    private HashMap<String, MenuSection> getMenu()
    {
        return database.getMenu();
    }



    /*
    /*@return value might be @null
     */
    private AppTransferStreamData readData(ObjectInputStream inputStream)
    {
        AppTransferStreamData newData = null;
        try {
            newData = (AppTransferStreamData) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            try {
                logger.warning(e.getMessage());
                inputStream.reset();
            } catch (IOException ex) {
                logger.finest("Error in input Stream." + ex.getMessage());
                //todo close connection with client
            }
        }
        return newData;
    }

    private void broadcast(AppTransferStreamData data)
    {
        if(data != null)
        {
            for(Object[] client: clientsConnectionData)
            {
                unicastTransfer(data, client);
            }
        }
    }
    private void unicastTransfer(AppTransferStreamData data, Object[] clientData){
        try {
            clientDataManipulationLock.lock();
            ((ObjectOutputStream)clientData[2]).writeObject(data);
            ((ObjectOutputStream)clientData[2]).flush();
        } catch (IOException e) {
            closeConnectionWithClient(clientData, clientsConnectionData);
            logger.finest("Output stream is broken at: " + ZonedDateTime.now());
        }finally {
            clientDataManipulationLock.unlock();
        }
    }
    private void tableOrderHandler(TableOrderTransferStreamData order)
    {
        if (order != null)
        {
            Log.d("Server; New Order Came", "Table Id: " + order.tableID());
            orderLock.lock();
            database.addOrder(order.tableID(), order.newOrder());
            database.addToOngoingOrders(order.tableID(), order.newOrder());
            orderLock.unlock();
            broadcast(new AppTransferStreamData(DataTypes.TableOrderTransferStreamData, order));
        }
    }
    private void ongoingOrderHandler(OngoingOrderTransferStreamData order)
    {
        if (order != null && order.status() == OrderStatus.FINISHED)
        {
            orderLock.lock();
            database.deleteFromOngoingOrders(order.order());
            orderLock.unlock();
        }
        broadcast(new AppTransferStreamData(DataTypes.OngoingOrderTransferStreamData, order));
    }


    private class ClientHandler implements Runnable{

        private Socket clientSocket;
        private ObjectInputStream inputStream;
        private OutputStream outputStream;
        private Object[] clientData;

        public ClientHandler(Socket clientSocket, ObjectInputStream inputStream, OutputStream outputStream) {
            this.clientSocket = clientSocket;
            this.inputStream = inputStream;
            this.outputStream = outputStream;
            clientData = new Object[]{clientSocket, inputStream, outputStream};
        }

        @Override
        public void run() {
            if (!loginSection())
            {
                closeConnectionWithClient(clientData);
                return;
            }

            while (!clientSocket.isClosed())
            {
                    AppTransferStreamData newDataFromClient = readTransferStreamData();
                    Log.d("Data Arrived", newDataFromClient.getType().name());
                    if (newDataFromClient != null)
                    {
                        if (newDataFromClient.getType() == DataTypes.TableOrderTransferStreamData)
                        {
                            tableOrderHandler((TableOrderTransferStreamData) newDataFromClient.getData());
                        } else if (newDataFromClient.getType() == DataTypes.OngoingOrderTransferStreamData)
                        {
                            ongoingOrderHandler((OngoingOrderTransferStreamData) newDataFromClient.getData());
                        } else if (newDataFromClient.getType() == DataTypes.AdminEmployeeTransferStreamData) {
                            updateEmployeeDatabase((AdminEmployeeTransferStreamData) newDataFromClient.getData());
                        } else if (newDataFromClient.getType() == DataTypes.AdminMenuItemsTransferStreamData)
                        {
                            updateMenuItemsDatabase((AdminMenuItemsTransferStreamData) newDataFromClient.getData());
                        } else if (newDataFromClient.getType() == DataTypes.AdminTableTransferStreamData)
                        {
                            Log.d("Table Insertion", "request in server");
                            adminAddTableHandler((Table)((AdminTableTransferStreamData)newDataFromClient.getData()).table());

                        } else if (newDataFromClient.getType() == DataTypes.EmployeeSessionTransferStreamData &&
                                ((EmployeeSessionTransferStreamData)newDataFromClient.getData()).code() == RequestCode.LOGOUT) {
                            closeSession();
                        } else if (newDataFromClient.getType() == DataTypes.TableSettleTransferStreamData &&
                                ((TableSettleTransferStreamData)newDataFromClient.getData()).requestCode() == TableSettleRequestCode.SETTLE) {
                            Log.d("Incoming Settle request", "table:" +
                                    ((TableSettleTransferStreamData)newDataFromClient.getData()).table());
                            handleSettleRequest(((TableSettleTransferStreamData)newDataFromClient.getData()).table());
                        }
                    }
            }
        }

        private void adminAddTableHandler(Table table)
        {
            AppDatabase db = AppDatabase.getDatabaseInstance(context);

            TableDAO tableDAO = db.tableDAO();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    com.lab.crmanagement.localdatabase.Table tableEntity =
                            new com.lab.crmanagement.localdatabase.Table(table.getId());
                    tableDAO.insert(tableEntity);

                    Log.d("Table Insertion","Table insertion is succesfull");
                }
            }).start();
        }

        private void closeSession() {
            closeConnectionWithClient(clientData);
        }

        private void updateTableDatabase(TransferStreamData data) {
            //todo
        }

        private void updateMenuItemsDatabase(AdminMenuItemsTransferStreamData item) {
            //todo
        }

        private boolean loginSection(){
            int loginRequestLimit = 3;
            while (!clientSocket.isClosed() && loginRequestLimit-- > 0)
            {
                AppTransferStreamData dataFromClient = readTransferStreamData();
                if (dataFromClient != null && dataFromClient.getType() == DataTypes.EmployeeSessionTransferStreamData)
                {
                    EmployeeSessionTransferStreamData employeeData = (EmployeeSessionTransferStreamData) dataFromClient.getData();
                    EmployeeLoginPair loginResult = login(employeeData.id(), employeeData.password());
                    if (loginResult.isSuccessful())
                    {
                        if (loginResult.isAdmin())
                        {
                            AppTransferStreamData result = new AppTransferStreamData(DataTypes.EmployeeSessionTransferStreamData,
                                    new EmployeeSessionTransferStreamData(employeeData.id(), employeeData.password(), RequestCode.SUCCESS_ADMIN));
                            unicastTransfer(result, clientData);

                            //send initial data to admin
                            sendAdminInitialData(employeeData.id());
                        }else
                        {
                            AppTransferStreamData result = new AppTransferStreamData(DataTypes.EmployeeSessionTransferStreamData,
                                    new EmployeeSessionTransferStreamData(employeeData.id(), employeeData.password(), RequestCode.SUCCESS_EMPLOYEE));
                            unicastTransfer(result, clientData);

                            //send initial data to employee
                            sendEmployeeInitialData(employeeData.id());
                        }
                        return true;
                    }else
                    {
                        AppTransferStreamData result = new AppTransferStreamData(DataTypes.EmployeeTransferStreamData,
                                new EmployeeSessionTransferStreamData(employeeData.id(), employeeData.password(), RequestCode.FAILED_LOGIN_ATTEMPT));
                        unicastTransfer(result, clientData);
                    }
                }else {
                    AppTransferStreamData result = new AppTransferStreamData(DataTypes.EmployeeTransferStreamData,
                            new EmployeeSessionTransferStreamData(-1, null, RequestCode.STREAM_DATA_TYPE_ERROR));
                    unicastTransfer(result, clientData);
                    logger.finest("Wrong Type of data in login section!!");
                }
            }
            return false;
        }


        /*@return value can be null*/
        private AppTransferStreamData readTransferStreamData()
        {
            AppTransferStreamData newData = null;
            try {
                newData = (AppTransferStreamData) inputStream.readObject();
            } catch (IOException e) {
                closeConnection();
                logger.finest("IOException at input stream in ClientHandler at: " + ZonedDateTime.now());
            } catch (ClassNotFoundException e) {
                closeConnection();
                logger.warning("Data Transfer Class not found at: " + ZonedDateTime.now());
            }finally {
                return newData;
            }
        }

        private EmployeeLoginPair login(int id, String password){return database.login(id, password);}


        /*
        * First order is added to table's order.
        * Then order is added to current order section
        * Order is broadcast to all clients
        * */
        private void tableOrderHandler(TableOrderTransferStreamData order)
        {
            Server.this.tableOrderHandler(order);
        }

        /*
        * ongoingorderhandler will only be used for finished orders.
        * */
        private void ongoingOrderHandler(OngoingOrderTransferStreamData order) {Server.this.ongoingOrderHandler(order);}
        private void closeConnection(){Server.this.closeConnectionWithClient(new Object[]{clientSocket, inputStream, outputStream});}


        /* order of the data to send:
        * 1-employee data
        * 2-tables data
        * 3-menu data
        * */
        private void sendEmployeeInitialData(int employeeID)
        {
            //todo do send initial data for admin that it can have all the database data

            AppTransferStreamData employee = new AppTransferStreamData(DataTypes.EmployeeTransferStreamData,
                    new EmployeeTransferStreamData(database.getEmployee(employeeID)));
            unicastTransfer(employee, clientData);


            AppTransferStreamData tables = new AppTransferStreamData(DataTypes.TableTransferStreamData,
                    new TableTransferStreamData(getTableDataFromDatabase()));
            unicastTransfer(tables, clientData);

            AppTransferStreamData menu = new AppTransferStreamData(DataTypes.MenuItemsTransferStreamData,
                    new MenuItemTransferStreamData(getMenu()));
            unicastTransfer(menu, clientData);
        }


        /* Order of the data to send
        * 1-Employee data
        * 2-Employees data
        * 3-Tables data
        * 4-Menu data
        * */
        private void sendAdminInitialData(int employeeID)
        {
            AppTransferStreamData employee = new AppTransferStreamData(DataTypes.EmployeeTransferStreamData,
                    new EmployeeTransferStreamData(database.getEmployee(employeeID)));
            unicastTransfer(employee, clientData);

            AppTransferStreamData employees = new AppTransferStreamData(DataTypes.EmployeesTransferStreamData,
                    new EmployeesTransferStreamData(database.getEmployees()));
            unicastTransfer(employees, clientData);

            AppTransferStreamData tables = new AppTransferStreamData(DataTypes.TableTransferStreamData,
                    new TableTransferStreamData(getTableDataFromDatabase()));
            unicastTransfer(tables, clientData);

            AppTransferStreamData menu = new AppTransferStreamData(DataTypes.MenuItemsTransferStreamData,
                    new MenuItemTransferStreamData(getMenu()));
            unicastTransfer(menu, clientData);
        }


        private void updateEmployeeDatabase(AdminEmployeeTransferStreamData employee)
        {
            //todo
        }

        private void handleSettleRequest(int tableId)
        {
            database.resetTable(tableId);
            Log.d("Settle request success sending to server", "table:" + tableId);
            unicastTransfer(new AppTransferStreamData(DataTypes.TableSettleTransferStreamData,
                    new TableSettleTransferStreamData(tableId, TableSettleRequestCode.SUCCESS)), clientData);
        }
    }









/*
    @Nested
    public class ServerTest {

        ServerTest(){
            Server.this.port = 0;
            Server.this.database = null;
        }

        @Test
        void testReadInput() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException {
            Server server1 = new Server(0, null);
            AppTransferStreamData data = new AppTransferStreamData(DataTypes.EmployeeTransferStreamData,
                    new EmployeeTransferStreamData(1, "123", RequestCode.LOGIN));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(data);
            }

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream inputStream = new ObjectInputStream(bais);

            AppTransferStreamData result = (AppTransferStreamData) readData(inputStream);

            assertEquals(data.getType(), result.getType());

            bais.close();
            baos.close();
        }

        @Test
        void closeClientConnectionTest() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {

            Server server = new Server(0, null);
            Method method = Server.class.getMethod("closeConnectionWithClient");
            method.setAccessible(true);

            ServerSocket serverSocket = new ServerSocket(4444, 20, InetAddress.getByName("localhost"));
            new Thread(new Runnable() {
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
            }).start();
            Socket testSocket = new Socket(InetAddress.getByName("localhost"), 4444);
            ArrayList<Object[]> testClients = new ArrayList<>();
            ObjectOutputStream outputStream = new ObjectOutputStream(testSocket.getOutputStream());
            outputStream.flush();
            ObjectInputStream inputStream = new ObjectInputStream(testSocket.getInputStream());
            Object[] testClientData = new Object[]{testSocket,
                    new ObjectOutputStream(testSocket.getOutputStream()), inputStream, outputStream};
            testClients.add(testClientData);

            method.invoke(server, testSocket, testClients);

            Assertions.assertEquals(0, testClients.size());

        }
    }*/
}
























/*
import data.Employee.Employee;
import data.Employee.EmployeeModel;
import data.menu.MenuModel;
import data.menu.MenuSection;
import org.junit.jupiter.api.*;

import java.net.InetAddress;

import data.AppTransferStreamData;
import data.DataTypes;
import data.Employee.EmployeeTransferStreamData;
import data.Employee.RequestCode;
import data.menu.MenuItem;
import data.ongoingorders.OngoingOrderPair;
import data.ongoingorders.OngoingOrderTransferStreamData;
import data.ongoingorders.OrderStatus;
import data.table.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;
import java.time.LocalDate;


public class Server {

    private static final Logger logger = Logger.getLogger("SingleFileHTTPServer");
    private int port;
    private ServerSocket server;
    private int threadNumberLimit;
    private static ArrayList<Object[]> broadcastList;

    private DBModel database;

    private final Lock broadcastLock = new ReentrantLock();

    public Server(int port, int threadNumberSafety, DBModel database) {
        this.port = port;
        threadNumberLimit = threadNumberSafety;
        this.database = database;
        broadcastList = new ArrayList<>();
    }

    public void startServer()
    {
        try {
            InetAddress localhost = InetAddress.getByName("localhost");
            server = new ServerSocket(this.port, 20, localhost);
            logger.info("Server started at port: " + this.port + "| ip adress:" + localhost.getHostAddress());
            while (!server.isClosed())
            {
                if (threadNumberLimit > 0)
                {
                        Socket client = server.accept();
                        threadNumberLimit--;
                        broadcastList.add(new Object[]{client, new ObjectOutputStream(client.getOutputStream())});
                        new Thread(new ClientHandler(client)).start();
                        logger.info("new client at time: " +
                                DateTimeFormatter.ofPattern("yyyy/MM/dd").format(LocalDate.now()));
                }else
                {
                    Thread.sleep(10000);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage() + "| error in startServer() Method");
        }finally {
            closeServer();
        }
    }
    public void broadcast(AppTransferStreamData data)
    {
        broadcastLock.lock();
        for(Object[] clientDataTuple: broadcastList)
        {
            try {
                ((ObjectOutputStream) clientDataTuple[1]).writeObject(data);
            } catch (IOException e) {
                System.out.println("error at broadcast method");
                closeConnection((Socket) clientDataTuple[0]);
            }
        }
        broadcastLock.unlock();
    }

    public void removeClientFromBroadcastList(Socket socket)
    {
        broadcastLock.lock();
        for (Object[] client: broadcastList)
        {
            if (client[0] == socket)
                broadcastList.remove(client);
        }
        broadcastLock.unlock();
    }
    private void removeClientFromBroadcastListAsync(Socket socket)
    {
        for (Object[] client: broadcastList)
        {
            if (client[0] == socket)
                broadcastList.remove(client);
        }
    }

    private void closeConnection(Socket socket)
    {
        try {
            removeClientFromBroadcastListAsync(socket);
            socket.getInputStream().close();
            socket.getOutputStream().close();
            socket.close();
        } catch (IOException e) {
            System.out.println("Exception at close connection");
        }
    }

    public void closeServer()
    {
        for(Object[] client: broadcastList)
        {
            try {
                ((ObjectOutputStream)client[1]).close();
                ((Socket)client[0]).getInputStream().close();
                ((Socket)client[0]).close();
            } catch (IOException e) {
                System.out.println("Socket is already closed || closerServer() Method");
            }
        }
        try {
            server.close();
        } catch (IOException e) {
            System.out.println("Exception in close server method");
        }

    }




    private class ClientHandler implements Runnable{
        private Socket socket;
        ObjectInputStream inputStream;
        ObjectOutputStream outputStream;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                inputStream = new ObjectInputStream(socket.getInputStream());
                outputStream = new ObjectOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                //closeConnection();
                System.out.println("Anomaly at connection between server-client");
            }
        }

        private boolean login(int id, String password)
        {
            return database.login(id, password);
        }
        @Override
        public void run()
        {
            */
/* client must log in to continue to get a service from server *//*

            try {
                boolean loginResult = loginSection();
                if (!loginResult) return;
            } catch (Exception e) {
                //closeConnection();
                System.out.println(e.getMessage() + "| login section exception at run method");
            }
            while (!socket.isClosed())
            {
                try {
                    AppTransferStreamData rawData = (AppTransferStreamData) inputStream.readObject();
                    if (rawData.getType() == DataTypes.TableOrderTransferStreamData)
                    {
                        TableOrderTransferStreamData newOrders = (TableOrderTransferStreamData) rawData.getData();
                        tableOrderHandler(newOrders.tableID(), newOrders.newOrder());
                    } else if (rawData.getType() == DataTypes.EmployeeTransferStreamData) {
                        EmployeeTransferStreamData employee = (EmployeeTransferStreamData) rawData.getData();
                        if (employee.code() == RequestCode.LOGOUT)
                        {
                            // todo logout();
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println(e.getMessage() + "| Exception in while loop in run method!");
                } finally {
                    //closeConnection();
                }
            }
        }

        */
/*
        * when new order comes to server, first new order is added to the order array of the specified table.
        * then new order is added to the ongoing order section
        * finally new order is been broadcast to clients with using OngoingOderTransferStreamData object.
        * client must handle new order with filtering OngoingOrderTransferStreamData object *//*

        private void tableOrderHandler(int tableID, ArrayList<MenuItem> items)
        {
            if(items != null)
            {
                database.addOrder(tableID, items);
                ArrayList<String> newOngoingOrders = new ArrayList<>();
                for(MenuItem item : items)
                {
                    newOngoingOrders.add(item.getName());
                }
                database.addToOngoingOrders(new OngoingOrderPair(tableID, newOngoingOrders));
                Server.this.broadcast(new AppTransferStreamData(DataTypes.OngoingOrderTransferStreamData,
                        new OngoingOrderTransferStreamData(new OngoingOrderPair(tableID, newOngoingOrders), OrderStatus.NEW_ORDER)));
            }
        }
        */
/*
        *login step of application
        *@return true if id and password true otherwise false
        *//*

        private boolean loginSection() throws Exception
        {
            int loginLimit = 3;
            while (!socket.isClosed() && loginLimit-- > 0)
            {
                AppTransferStreamData rawData = (AppTransferStreamData) inputStream.readObject();
                if (rawData.getType() == DataTypes.EmployeeTransferStreamData)
                {
                    EmployeeTransferStreamData employeeData = (EmployeeTransferStreamData) rawData.getData();
                    if (login(employeeData.id(), employeeData.password()))
                    {
                        EmployeeTransferStreamData returnData = new EmployeeTransferStreamData(employeeData.id(),
                                employeeData.password(), RequestCode.SUCCESS);
                        outputStream.writeObject(returnData);
                        outputStream.flush();
                        return true;
                    }else
                    {
                        EmployeeTransferStreamData returnData = new EmployeeTransferStreamData(employeeData.id(),
                                employeeData.password(), RequestCode.FAILED_LOGIN_ATTEMPT);
                        outputStream.writeObject(returnData);
                        outputStream.flush();
                        return false;
                    }
                }else
                {
                    EmployeeTransferStreamData returnData = new EmployeeTransferStreamData(-1, null, RequestCode.STREAM_DATA_TYPE_ERROR);
                    outputStream.writeObject(returnData);
                    outputStream.flush();
                }
            }
            EmployeeTransferStreamData returnData = new EmployeeTransferStreamData(-1, null, RequestCode.LIMIT);
            outputStream.writeObject(returnData);
            outputStream.flush();

            return false;
        }

        private void closeConnection()
        {
            try
            {
                inputStream.close();
                outputStream.close();
                socket.close();
            }catch (IOException ioException)
            {
                System.out.println(ioException.getMessage() + "| Exception at client handler closeConnection Method");
            }
        }
    }

}*/
