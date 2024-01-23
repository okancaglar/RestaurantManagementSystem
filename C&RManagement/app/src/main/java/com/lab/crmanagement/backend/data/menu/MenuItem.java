package com.lab.crmanagement.backend.data.menu;

import java.io.Serializable;

public class MenuItem implements Serializable {
    private  String menuSection;
    private int id;
    private String name;
    private double price;
    private String ingredients;


    public MenuItem(String menuSection, int id, String name, double price, String ingredients) {
        this.menuSection = menuSection;
        this.id = id;
        this.name = name;
        this.price = price;
        this.ingredients = ingredients;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}
