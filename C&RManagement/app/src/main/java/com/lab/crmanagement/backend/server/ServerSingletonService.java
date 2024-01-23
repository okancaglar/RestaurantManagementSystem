package com.lab.crmanagement.backend.server;

import com.lab.crmanagement.backend.data.database.DBModel;

public class ServerSingletonService {
    private static Server server;

    public static Server getServerInstance(int port, DBModel model)
    {
        if (server == null)
        {
            server = new Server(port, model);
        }
        return server;
    }
}
