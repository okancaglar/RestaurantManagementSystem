package com.lab.crmanagement.backend.data.table;

import com.lab.crmanagement.backend.data.TransferStreamData;
import com.lab.crmanagement.backend.data.menu.MenuItem;

import java.util.ArrayList;

public record TableOrderTransferStreamData(int tableID, ArrayList<MenuItem> newOrder) implements TransferStreamData {
}
