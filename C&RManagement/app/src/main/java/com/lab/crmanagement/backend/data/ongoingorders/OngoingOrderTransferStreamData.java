package com.lab.crmanagement.backend.data.ongoingorders;

import com.lab.crmanagement.backend.data.TransferStreamData;

public record OngoingOrderTransferStreamData(OngoingOrderPair order, OrderStatus status) implements TransferStreamData {
}
