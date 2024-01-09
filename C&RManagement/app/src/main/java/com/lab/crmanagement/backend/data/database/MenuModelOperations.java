package com.lab.crmanagement.backend.data.database;

import com.lab.crmanagement.backend.data.menu.*;

import java.util.HashMap;

public interface MenuModelOperations {
    HashMap<String, MenuSection> getMenu();
}
