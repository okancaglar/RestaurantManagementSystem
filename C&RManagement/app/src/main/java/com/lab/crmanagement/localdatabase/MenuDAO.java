package com.lab.crmanagement.localdatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MenuDAO {
    @Query("select * from menu")
    List<Menu> getAllItems();

    @Insert
    void insert(Menu item);

    @Delete
    void delete(Menu item);



}
