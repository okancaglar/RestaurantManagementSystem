package com.lab.crmanagement.backend.data.menu;

import java.io.Serializable;
import java.util.HashMap;

public class MenuSection implements Serializable {
    private String sectionName;
    private HashMap<Integer, MenuItem> items;

    public MenuSection(String sectionName, HashMap<Integer, MenuItem> items) {
        this.sectionName = sectionName;
        this.items = items;
    }

    public String getSectionName() {
        return sectionName;
    }

    public HashMap<Integer, MenuItem> getItems() {
        return items;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public void setItems(HashMap<Integer, MenuItem> items) {
        this.items = items;
    }
}
