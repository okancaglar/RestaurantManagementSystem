package com.lab.crmanagement.login;

public interface LoginClientListener {


    public void sendLoginInfo(int id, String password);
    public void loginInfoResult(boolean result);

}
