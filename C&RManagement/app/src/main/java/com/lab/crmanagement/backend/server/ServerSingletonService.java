package com.lab.crmanagement.backend.server;

import android.content.Context;

import com.lab.crmanagement.backend.data.database.DBModel;

public class ServerSingletonService {
    private static Server server;

    public static Server getServerInstance(int port, DBModel model, Context context)
    {
        if (server == null)
        {
            server = new Server(port, model, context);
        }
        return server;
    }
}
