package com.lab.crmanagement.localdatabase;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Table {
    @NonNull
    @PrimaryKey
    public int id;

    public Table(int id) {
        this.id = id;
    }
}
