package com.lab.crmanagement.backend.client;

public class ClientSingletonService {
    private static Client client;

    public static Client getClientInstance()
    {
        if (client == null)
        {
            client = new Client();
        }
        return client;
    }
}
