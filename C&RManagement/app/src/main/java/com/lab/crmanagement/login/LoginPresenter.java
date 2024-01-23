package com.lab.crmanagement.login;

import android.util.Log;

import com.lab.crmanagement.backend.client.Client;
import com.lab.crmanagement.backend.client.ClientSingletonService;
import com.lab.crmanagement.backend.data.client.ClientModel;

public class LoginPresenter implements LoginClientListener{

    private LoginView view;
    private ClientModel model;
    private Client client;

    public LoginPresenter(LoginView view) {
        this.view = view;
        client = ClientSingletonService.getClientInstance();
        client.setLoginClientListener(this);
    }


    @Override
    public void sendLoginInfo(int id, String password) {
        loginInfoResult(client.login(id, password));
    }

    @Override
    public void loginInfoResult(boolean result) {
        view.loginResult(result);
        Log.d("LOGIN RESULT", result + "");
    }
}
