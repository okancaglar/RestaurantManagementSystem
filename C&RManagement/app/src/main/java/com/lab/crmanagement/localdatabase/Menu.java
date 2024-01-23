package com.lab.crmanagement.localdatabase;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(primaryKeys = {"sectionName", "itemId"})
public class Menu {
    @NonNull
    public String sectionName;
    @NonNull
    public int itemId;

    @ColumnInfo()
    public String name;
    @ColumnInfo()
    public double price;
    @ColumnInfo()
    public String ingredients;

    public Menu(String sectionName, int itemId, String name, double price, String ingredients) {
        this.sectionName = sectionName;
        this.itemId = itemId;
        this.name = name;
        this.price = price;
        this.ingredients = ingredients;
    }
}
