package com.lab.crmanagement.backend.data.table;

import com.lab.crmanagement.backend.data.TransferStreamData;

import java.util.HashMap;

public record TableTransferStreamData(HashMap<Integer, Table> tables) implements TransferStreamData {

}
