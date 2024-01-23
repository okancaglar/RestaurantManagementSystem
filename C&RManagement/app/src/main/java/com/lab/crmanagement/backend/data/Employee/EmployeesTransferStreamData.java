package com.lab.crmanagement.backend.data.Employee;

import com.lab.crmanagement.backend.data.TransferStreamData;

import java.util.HashMap;

public record EmployeesTransferStreamData(HashMap<Integer, Employee> employees) implements TransferStreamData {
}
