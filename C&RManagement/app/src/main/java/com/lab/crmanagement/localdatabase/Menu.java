package com.lab.crmanagement.localdatabase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(primaryKeys = {"sectionName", "itemId"})
public class Menu {
    public String sectionName;
    public int itemId;

    @ColumnInfo()
    public String name;
    @ColumnInfo()
    public double price;
    @ColumnInfo()
    public String ingredients;
}
