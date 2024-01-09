package com.lab.crmanagement.backend.data.Employee;

import com.lab.crmanagement.backend.data.database.EmployeeModelOperations;

import java.util.HashMap;

public class EmployeeModel implements EmployeeModelOperations {
    private HashMap<Integer, Employee> employees;

    public EmployeeModel(HashMap<Integer, Employee> employees) {
        this.employees = employees;
    }
    public void addEmployee(Employee employee)
    {employees.putIfAbsent(employee.getId(), employee);}
    @Override
    public Employee getEmployee(int id)
    {return employees.get(id);}

    @Override
    public EmployeeLoginPair login(int id, String password)
    {
        if (password == null) return null;
        String truePassword = employees.get(id).getPassword();
        return truePassword != null && truePassword.equals(password) ? new EmployeeLoginPair(true,
                employees.get(id).isAdmin()) : new EmployeeLoginPair(false, false);
    }
}
