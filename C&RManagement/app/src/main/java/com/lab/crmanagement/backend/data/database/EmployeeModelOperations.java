package com.lab.crmanagement.backend.data.database;

import com.lab.crmanagement.backend.data.Employee.*;

import java.util.HashMap;

public interface EmployeeModelOperations {
    EmployeeLoginPair login(int id, String password);
    Employee getEmployee(int id);
    HashMap<Integer, Employee> getEmployees();

}
