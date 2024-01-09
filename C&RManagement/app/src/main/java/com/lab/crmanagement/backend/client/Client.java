package com.lab.crmanagement.backend.client;

import com.lab.crmanagement.backend.data.AppTransferStreamData;
import com.lab.crmanagement.backend.data.DataTypes;
import com.lab.crmanagement.backend.data.Employee.*;
import com.lab.crmanagement.backend.data.menu.*;
import com.lab.crmanagement.backend.data.ongoingorders.*;
import com.lab.crmanagement.backend.data.table.*;



import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public class Client {
    private Employee employeeInformation;
    private HashMap<Integer, Table> tableData;
    private HashMap<String, MenuSection> menu;
    /* Structure of Object[] is {tableId, MenuItem} */
    private ArrayList<Object[]> ongoingOrders;
    private Socket clientSocket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private final Logger logger = Logger.getLogger(Client.class.getName());

    private final Lock employeeInformationLock = new ReentrantLock();
    private final Lock tableDataLock = new ReentrantLock();
    private final Lock menuLock = new ReentrantLock();
    private final Lock ongoingOrderLock = new ReentrantLock();

    private boolean setUpConnection()
    {
        try {
            clientSocket = new Socket(InetAddress.getByName("localhost"), 4444);
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            inputStream = new ObjectInputStream(clientSocket.getInputStream());
            return true;
        }catch (IOException e)
        {
            closeConnection();
            logger.finest("client couldnt connect to server");
        }
        return false;
    }


    /* after this method to build ui use employee information to determine is employee or admin then invoke the proper user interface*/
    public boolean loginSection(int id, String password)
    {
        boolean loginStatus = false;


        AppTransferStreamData employeeLoginData = new AppTransferStreamData(DataTypes.EmployeeSessionTransferStreamData,
                new EmployeeSessionTransferStreamData(id, password, RequestCode.LOGIN));
        sendDataToServer(employeeLoginData);
        AppTransferStreamData loginResponse = readDataFromServer();
        if (loginResponse != null && loginResponse.getType() == DataTypes.EmployeeSessionTransferStreamData)
        {
            if (((EmployeeSessionTransferStreamData)loginResponse.getData()).code() == RequestCode.SUCCESS_EMPLOYEE)
            {
                getInitialData();
                //todo load employee interface
                loginStatus = true;
            } else if (((EmployeeSessionTransferStreamData)loginResponse.getData()).code() == RequestCode.SUCCESS_ADMIN)
            {
                getAdminInitialData();
                //todo load admin interface
                loginStatus = true;
            } else if (((EmployeeSessionTransferStreamData)loginResponse.getData()).code() == RequestCode.STREAM_DATA_TYPE_ERROR) {
                logger.finest("logical error in client login section");
            } else if (((EmployeeSessionTransferStreamData)loginResponse.getData()).code() == RequestCode.FAILED_LOGIN_ATTEMPT) {
                //todo invoke ui notification
            }
        }else
        {
            //todo invoke ui notification for too many logging attempt
            logger.warning("too many logging attempt");
        }

        //if (loginStatus)
            //startClientHandler();

        return loginStatus;
    }

    private void startClientHandler() {
        new Thread(new ClientHandler()).start();
    }

    public void startConnection()
    {
        if (!setUpConnection())
        {
            //todo invoke ui notification
            logger.finest("cannot connect to server. Critical program error at: " + ZonedDateTime.now());
            return;
        }
        //loginSection();
    }

    private void getAdminInitialData() {
        //todo build admin initial data logic
    }


    private void sendDataToServer(AppTransferStreamData data)
    {
        try {
            outputStream.writeObject(data);
            outputStream.flush();
        } catch (IOException e) {
            closeConnection();
            logger.finest("IO exception when sending the data to server" + ZonedDateTime.now() + " Error: " + e.getMessage());
        }
    }
    private AppTransferStreamData readDataFromServer()
    {
        AppTransferStreamData input = null;
        try {
             input = (AppTransferStreamData) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            closeConnection();
            logger.finest("ıo exception when getting the data from server at:" + ZonedDateTime.now() + " error: " +
                    e.getMessage());
        }
        return input;
    }
    private void getInitialData()
    {
        int count = 3;
        while (count-- > 0) {
            AppTransferStreamData input = readDataFromServer();
            if (input.getType() == DataTypes.EmployeeTransferStreamData) {
                employeeInformation = (Employee) ((EmployeeTransferStreamData)input.getData()).employee();
            } else if (input.getType() == DataTypes.TableTransferStreamData) {
                tableData = (HashMap<Integer, Table>) ((TableTransferStreamData)input.getData()).tables();
            } else if (input.getType() == DataTypes.MenuItemsTransferStreamData) {
                menu = (HashMap<String, MenuSection>) ((MenuItemTransferStreamData)input.getData()).menu();
            } else {
                closeConnection();
                logger.finest("initial data transfer is not working! critical logic error at: " + ZonedDateTime.now());
            }
        }
    }
    /* DATA MANIPULATION METHODS */
    private void addOrderToTable(TableOrderTransferStreamData data)
    {
        //tableDataLock.lock();
        tableData.get(data.tableID()).addItems(data.newOrder());
        //tableDataLock.unlock();
        ongoingOrderLock.lock();
        for (int i = 0; i < data.newOrder().size(); i++) {
            ongoingOrders.add(new Object[]{data.tableID(), data.newOrder().get(i)});
        }
        ongoingOrderLock.unlock();
    }
    private void deleteFromOngoingOrder(int tableID, ArrayList<MenuItem> items)
    {
        ongoingOrderLock.lock();
        for (int i = 0; i < items.size(); i++) {
            for (int j = 0; j < ongoingOrders.size(); j++) {
                if ((int)(ongoingOrders.get(j)[0]) == tableID &&
                        ((MenuItem)(ongoingOrders.get(j)[1])).getId() == items.get(i).getId())
                {
                    ongoingOrders.remove(j);
                    break;
                }
            }
        }
        ongoingOrderLock.unlock();
    }
    /* END */

    /* SENDER METHODS */

    public void sendTableOrderData(int tableId, ArrayList<MenuItem> order)
    {
        AppTransferStreamData data = new AppTransferStreamData(DataTypes.TableOrderTransferStreamData,
                new TableOrderTransferStreamData(tableId, order));
        sendDataToServer(data);
    }

    public void sendOngoingOrderData(OngoingOrderPair dataPair)
    {
        AppTransferStreamData data = new AppTransferStreamData(DataTypes.OngoingOrderTransferStreamData,
                new OngoingOrderTransferStreamData(dataPair, OrderStatus.FINISHED));
        sendDataToServer(data);
    }

    /* END */

    /* ADMIN METHODS THAT MANIPULATES DATABASE */
    public void databaseEmployeeOperation(){/*todo*/}
    public void databaseMenuItemOperation(){/*todo*/}
    public void databaseTableOperation(){/*todo*/}
    /* END */

    private void closeConnection()
    {
        try {
            inputStream.close();
            outputStream.close();
            clientSocket.close();
        } catch (IOException e) {
            logger.finest("error in closeConnection at : " + ZonedDateTime.now());
        }
    }
    public void logoutHandler(EmployeeSessionTransferStreamData data)
    {
        sendDataToServer(new AppTransferStreamData(DataTypes.EmployeeSessionTransferStreamData, data));
        //todo invoke close the android app
    }

     private class ClientHandler implements Runnable{
        @Override
        public void run()
        {
           while (!clientSocket.isClosed())
           {
               AppTransferStreamData newData = readData();
               if (newData == null)
               {
                   closeConnection();
                   return;
               }
               if (newData.getType() == DataTypes.TableOrderTransferStreamData)
               {
                   tableOrderHandler((TableOrderTransferStreamData) newData.getData());
               } else if (newData.getType() == DataTypes.OngoingOrderTransferStreamData) {
                   ongoingOrderHandler((OngoingOrderTransferStreamData) newData.getData());
               }
           }
        }
         private AppTransferStreamData readData()
         {
             AppTransferStreamData input = null;
             try {
                 input = (AppTransferStreamData) inputStream.readObject();
             } catch (IOException | ClassNotFoundException e) {
                 closeConnection();
                 logger.finest("ıo exception when getting the data from server at:" + ZonedDateTime.now() + " error: " +
                         e.getMessage());
             }
             return input;
         }
         private void tableOrderHandler(TableOrderTransferStreamData data)
         {
            if (data == null)
                return;
            addOrderToTable(data);
            //update ui
         }
         private void ongoingOrderHandler(OngoingOrderTransferStreamData data)
         {
            if (data != null && data.status() == OrderStatus.FINISHED)
            {
                deleteFromOngoingOrder(data.order().tableID(), data.order().items());
            }
         }
    }
}
