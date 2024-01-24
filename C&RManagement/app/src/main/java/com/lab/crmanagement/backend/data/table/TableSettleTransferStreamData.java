package com.lab.crmanagement.backend.data.table;

import com.lab.crmanagement.backend.data.TransferStreamData;

public record TableSettleTransferStreamData(int table, TableSettleRequestCode requestCode) implements TransferStreamData {
}
