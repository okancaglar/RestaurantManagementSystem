package com.lab.crmanagement.backend.data.ongoingorders;

import com.lab.crmanagement.backend.data.menu.MenuItem;

import java.io.Serializable;
import java.util.ArrayList;

public record OngoingOrderPair(int tableID, ArrayList<MenuItem> items) implements Serializable {
}
