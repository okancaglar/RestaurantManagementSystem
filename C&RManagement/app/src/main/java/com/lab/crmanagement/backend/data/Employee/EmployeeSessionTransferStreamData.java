package com.lab.crmanagement.backend.data.Employee;

import com.lab.crmanagement.backend.data.TransferStreamData;

public record EmployeeSessionTransferStreamData(int id, String password, RequestCode code)
        implements TransferStreamData {
}
