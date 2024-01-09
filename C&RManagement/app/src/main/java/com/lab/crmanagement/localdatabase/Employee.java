package com.lab.crmanagement.localdatabase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Employee {
    @PrimaryKey
    public int id;

    @ColumnInfo()
    public String password;

    @ColumnInfo()
    public String name;
    @ColumnInfo()
    public String surname;

    @ColumnInfo
    public boolean admin;
}
