package com.lab.crmanagement.localdatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface EmployeeDAO {
    @Query("SELECT * FROM employee")
    List<Employee> getAll();

    @Update
    void update(Employee employee);

    @Delete
    void delete(Employee employee);

    @Insert
    void insert(Employee employee);

    @Query("SELECT * FROM employee WHERE id = :id")
    Employee findById(int id);
}
