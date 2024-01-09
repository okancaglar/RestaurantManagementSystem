package com.lab.crmanagement.localdatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TableDAO {

    @Query("select * from `Table`")
    List<Table> getAllTable();

    @Delete
    void delete(Table table);

    @Insert
    void insert(Table insert);
}
