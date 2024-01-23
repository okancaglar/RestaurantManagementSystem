package com.lab.crmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;

import com.lab.crmanagement.backend.client.Client;
import com.lab.crmanagement.backend.client.ClientSingletonService;
import com.lab.crmanagement.backend.data.database.DBModel;
import com.lab.crmanagement.backend.server.Server;
import com.lab.crmanagement.backend.server.ServerSingletonService;
import com.lab.crmanagement.localdatabase.AppDatabase;
import com.lab.crmanagement.localdatabase.CreateDBModel;
import com.lab.crmanagement.login.LoginActivity;

import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    /*
    * Main activity logic: First create app database if not exist with initial values.Afterwards,
    * create DBModel for server. DBModel is created by CreateDBModel class.
    * Later on server instance is created with specified port and and database model.
    * Finally client server is started. No constructor parameter is needed.
    * */




    private Server server;
    private Client client;
    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);


        //todo setup model, server, client
        database = AppDatabase.getDatabaseInstance(getApplicationContext());


        new Thread(new Runnable() {
            @Override
            public void run() {
                DBModel dbModel = CreateDBModel.createDBModelFromDatabase(database);
                server = ServerSingletonService.getServerInstance(4444, dbModel);
                Logger.getLogger(MainActivity.class.getName()).finest(server + "");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        server.startServer();
                    }
                }).start();

                client = ClientSingletonService.getClientInstance();
                client.startConnection();
            }
        }).start();
        startActivity(new Intent(this, LoginActivity.class));
    }
}