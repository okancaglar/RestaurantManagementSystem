package com.lab.crmanagement.backend.data.Employee;

import com.lab.crmanagement.backend.data.TransferStreamData;

public record EmployeeTransferStreamData(Employee employee) implements TransferStreamData {
}
