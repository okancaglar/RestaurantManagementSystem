package com.lab.crmanagement.backend.data.admin;

import com.lab.crmanagement.backend.data.TransferStreamData;
import com.lab.crmanagement.backend.data.table.Table;

public record AdminTableTransferStreamData(int tableId, Table table, AdminRequestCode requestCode) implements TransferStreamData {
}
