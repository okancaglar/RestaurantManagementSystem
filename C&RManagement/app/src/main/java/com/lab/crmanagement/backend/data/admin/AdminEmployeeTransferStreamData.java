package com.lab.crmanagement.backend.data.admin;

import com.lab.crmanagement.backend.data.Employee.Employee;
import com.lab.crmanagement.backend.data.TransferStreamData;

public record AdminEmployeeTransferStreamData(Employee employee, AdminRequestCode requestCode) implements TransferStreamData {
}
