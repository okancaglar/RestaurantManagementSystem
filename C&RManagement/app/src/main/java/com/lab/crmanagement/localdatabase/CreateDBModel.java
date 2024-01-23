package com.lab.crmanagement.localdatabase;

import android.util.Log;

import com.lab.crmanagement.backend.data.Employee.EmployeeModel;
import com.lab.crmanagement.backend.data.database.DBModel;
import com.lab.crmanagement.backend.data.menu.MenuItem;
import com.lab.crmanagement.backend.data.menu.MenuModel;
import com.lab.crmanagement.backend.data.menu.MenuSection;
import com.lab.crmanagement.backend.data.table.TableModel;

import java.util.HashMap;
import java.util.List;

public class CreateDBModel {

    public static DBModel createDBModelFromDatabase(AppDatabase database)
    {
        List<Employee> employeeList = database.employeeDAO().getAll();
        HashMap<Integer, com.lab.crmanagement.backend.data.Employee.Employee> employeesBackend = new HashMap<>();
        for (Employee employee: employeeList)
        {
            employeesBackend.put(employee.id,
                    new com.lab.crmanagement.backend.data.Employee.Employee(employee.id, employee.name,
                            employee.surname, employee.password, employee.admin));
        }
        EmployeeModel employeeModel = new EmployeeModel(employeesBackend);

        List<Table> dbTableList = database.tableDAO().getAllTable();
        HashMap<Integer, com.lab.crmanagement.backend.data.table.Table> tablesFromBackend = new HashMap<>();
        for (Table table: dbTableList)
        {
            tablesFromBackend.put(table.id, new com.lab.crmanagement.backend.data.table.Table(table.id));
        }
        TableModel tableModel = new TableModel(tablesFromBackend);

        List<Menu> menuList = database.menuDAO().getAllItems();
        menuList.forEach(item -> Log.d("Menu item", item.name));
        HashMap<String, MenuSection> menuSectionHashMap = new HashMap<>();

        for (Menu menu: menuList)
        {
            MenuSection menuSection;
            if ((menuSection = menuSectionHashMap.get(menu.sectionName)) != null)
            {
                menuSection.getItems().put(menu.itemId,
                        new MenuItem(menu.sectionName, menu.itemId, menu.name, menu.price, menu.ingredients));
            }else
            {
                menuSectionHashMap.put(menu.sectionName, new MenuSection(menu.sectionName,
                        new HashMap<>()));
                menuSectionHashMap.get(menu.sectionName).getItems().put(menu.itemId,
                        new MenuItem(menu.sectionName, menu.itemId, menu.name, menu.price, menu.ingredients));
            }
        }
        MenuModel menuModel = new MenuModel(menuSectionHashMap);

        return new DBModel(employeeModel, menuModel, tableModel);
    }
}
