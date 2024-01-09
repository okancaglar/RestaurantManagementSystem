package com.lab.crmanagement.backend.data.menu;

import com.lab.crmanagement.backend.data.database.MenuModelOperations;

import java.util.HashMap;

public class MenuModel implements MenuModelOperations {
    private HashMap<String, MenuSection> menuSections;

    public MenuModel() {
    }
    public MenuModel(HashMap<String, MenuSection> menuSections) {
        this.menuSections = menuSections;
    }

    //todo implement add-delete-update-getter methods


    @Override
    public HashMap<String, MenuSection> getMenu() {
        return menuSections;
    }
}
