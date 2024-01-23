package com.lab.crmanagement.localdatabase;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Employee {
    @NonNull
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

    public Employee(int id, String password, String name, String surname, boolean admin) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.admin = admin;
    }
}
