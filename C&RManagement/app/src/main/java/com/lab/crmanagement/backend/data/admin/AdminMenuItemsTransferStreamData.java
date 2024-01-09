package com.lab.crmanagement.backend.data.admin;

import com.lab.crmanagement.backend.data.TransferStreamData;
import com.lab.crmanagement.backend.data.menu.MenuItem;

import java.util.ArrayList;

public record AdminMenuItemsTransferStreamData(MenuItem itme, AdminRequestCode requesCode) implements TransferStreamData {
}
