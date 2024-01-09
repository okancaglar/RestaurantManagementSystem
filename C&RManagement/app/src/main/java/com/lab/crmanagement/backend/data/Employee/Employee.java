package com.lab.crmanagement.backend.data.Employee;

import java.io.Serializable;

public class Employee implements Serializable {
    private int id;
    private String firstName;
    private String lastName;
    private int workingTime = 0;
    private String password;

    private boolean isAdmin;

    //other necessary attributes can be implemented later


    public Employee(int id, String firstName, String lastName, String password, boolean isAdmin) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getWorkingTime() {
        return workingTime;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAdmin() {return this.isAdmin;}
}
