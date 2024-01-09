package com.lab.crmanagement.backend.data;

public class AppTransferStreamData implements TransferStreamData{
    private final DataTypes type;
    private TransferStreamData data;

    public AppTransferStreamData(DataTypes type, TransferStreamData data) {
        this.type = type;
        this.data = data;
    }

    public DataTypes getType() {
        return type;
    }

    public TransferStreamData getData() {
        return data;
    }
}
