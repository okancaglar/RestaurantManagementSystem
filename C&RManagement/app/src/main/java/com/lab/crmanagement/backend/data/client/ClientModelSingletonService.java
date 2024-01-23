package com.lab.crmanagement.backend.data.client;

public class ClientModelSingletonService {

    private static ClientModel model;

    public static ClientModel getClientModelInstance()
    {
           if (model == null)
           {
               model = new ClientModel();
           }
           return model;
    }


}
