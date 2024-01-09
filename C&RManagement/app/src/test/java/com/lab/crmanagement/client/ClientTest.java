package com.lab.crmanagement.client;

import com.lab.crmanagement.backend.client.Client;
import com.lab.crmanagement.backend.data.AppTransferStreamData;
import com.lab.crmanagement.backend.data.TransferStreamData;
import com.lab.crmanagement.backend.data.DataTypes;
import com.lab.crmanagement.backend.data.Employee.*;
import com.lab.crmanagement.backend.data.menu.*;
import com.lab.crmanagement.backend.data.ongoingorders.*;
import com.lab.crmanagement.backend.data.ongoingorders.*;
import com.lab.crmanagement.backend.data.table.TableTransferStreamData;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {


	ServerSocket server;
	Socket clientSocket;
	ObjectOutputStream outputStream;
	ObjectInputStream inputStream;

	@BeforeEach
	void setUpServer()
	{
		try {
			server = new ServerSocket(4444, 20, InetAddress.getByName("localhost"));
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						clientSocket = server.accept();
						 outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
						 inputStream = new ObjectInputStream(clientSocket.getInputStream());
					} catch (IOException e) {

					}
				}
			}).start();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	@AfterEach
	void closeServer()  {
		try {
			server.close();
			outputStream.close();
			inputStream.close();
		}catch (Exception e) {

		}
	}


	@Test
	void loginSection() throws IOException, ClassNotFoundException, InterruptedException {
		Client client = new Client();
		client.startConnection();
		boolean[] result = {false};
		Thread test = new Thread(new Runnable() {
			@Override
			public void run() {
				result[0] = client.loginSection(1, "123");
			}
		});
		test.start();
		AppTransferStreamData input1 = (AppTransferStreamData) inputStream.readObject();
		assertEquals(DataTypes.EmployeeSessionTransferStreamData, input1.getType());

		outputStream.writeObject(new AppTransferStreamData(DataTypes.EmployeeSessionTransferStreamData,
				new EmployeeSessionTransferStreamData(1, "123", RequestCode.SUCCESS_EMPLOYEE)));
		outputStream.flush();

		outputStream.writeObject(new AppTransferStreamData(DataTypes.EmployeeTransferStreamData,
				new EmployeeTransferStreamData(new Employee(0, "", "", "", false))));
		outputStream.flush();
		outputStream.writeObject(new AppTransferStreamData(DataTypes.TableTransferStreamData,
				new TableTransferStreamData(new HashMap<>())));
		outputStream.flush();
		outputStream.writeObject(new AppTransferStreamData(DataTypes.MenuItemsTransferStreamData,
				new MenuItemTransferStreamData(new HashMap<>())));
		outputStream.flush();
		test.join();
		Assertions.assertEquals(true, result[0]);
	}

	@Test
	void sendTableOrderData() throws IOException, ClassNotFoundException {
		Client client = new Client();
		client.startConnection();

		client.sendTableOrderData(1, new ArrayList<>());
		AppTransferStreamData tableInput = (AppTransferStreamData) inputStream.readObject();

		assertEquals(DataTypes.TableOrderTransferStreamData, tableInput.getType());
	}

	@Test
	void sendOngoingOrderData() throws IOException, ClassNotFoundException {
		Client client = new Client();
		client.startConnection();

		client.sendOngoingOrderData(new OngoingOrderPair(1, new ArrayList<>()));
		AppTransferStreamData tableInput = (AppTransferStreamData) inputStream.readObject();

		assertEquals(DataTypes.OngoingOrderTransferStreamData, tableInput.getType());
	}

	@Test
	void databaseEmployeeOperation() {


	}

	@Test
	void databaseMenuItemOperation() {

	}

	@Test
	void databaseTableOperation() {

	}

	@Test
	void logoutHandler() throws IOException, ClassNotFoundException {
		Client client = new Client();
		client.startConnection();

		client.logoutHandler(new EmployeeSessionTransferStreamData(1, "", RequestCode.LOGOUT));
		AppTransferStreamData testInput = (AppTransferStreamData) inputStream.readObject();

		assertEquals(DataTypes.EmployeeSessionTransferStreamData, testInput.getType());
		assertEquals(((EmployeeSessionTransferStreamData)testInput.getData()).code(), RequestCode.LOGOUT);
	}

}