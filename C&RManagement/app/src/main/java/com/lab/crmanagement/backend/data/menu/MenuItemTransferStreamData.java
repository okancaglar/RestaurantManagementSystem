package com.lab.crmanagement.backend.data.menu;

import com.lab.crmanagement.backend.data.TransferStreamData;

import java.util.HashMap;

public record MenuItemTransferStreamData(HashMap<String, MenuSection> menu) implements TransferStreamData {
}
