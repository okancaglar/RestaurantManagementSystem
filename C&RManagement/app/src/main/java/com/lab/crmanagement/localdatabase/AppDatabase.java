package com.lab.crmanagement.localdatabase;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.Executors;

@Database(entities = {Employee.class, Menu.class, Table.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract EmployeeDAO employeeDAO();
    public abstract MenuDAO menuDAO();
    public abstract TableDAO tableDAO();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabaseInstance(final Context context)
    {
        if (INSTANCE == null)
        {
            RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);

                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            EmployeeDAO employeeDao = INSTANCE.employeeDAO();
                            MenuDAO menuDao = INSTANCE.menuDAO();
                            TableDAO tableDAo = INSTANCE.tableDAO();
                            employeeDao.insert(new Employee(1, "testpassword", "okan", "caglar",
                                    true));
                            employeeDao.insert(new Employee(10, "employee10password",
                                    "mert", "sahin", false));
                            tableDAo.insert(new Table(1));
                            tableDAo.insert(new Table(2));
                            tableDAo.insert(new Table(3));
                            tableDAo.insert(new Table(4));
                            tableDAo.insert(new Table(5));
                            tableDAo.insert(new Table(6));

                            menuDao.insert(new Menu("pizzas", 1,
                                    "mix pizza", 25,
                                    "cheese, salami, olive, pepper"));

                            menuDao.insert(new Menu("drinks", 10,
                                    "espresso", 5,
                                    "espresso"));
                            menuDao.insert(new Menu("deserts", 50,
                                    "lemon cheesecake", 15,
                                    "cheese, lemon, egg, sugar"));
                            menuDao.insert(new Menu("steaks", 70,
                                    "korean steak", 70,
                                    "%100 korean steak"));
                            menuDao.insert(new Menu("soup", 120,
                                    "chicken soup", 7,
                                    "chicken, "));

                        }
                    });
                }
            };
            synchronized (AppDatabase.class){
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class,
                        "app_database")
                        .addCallback(roomCallback)
                        .build();
            }
        }
        return INSTANCE;
    }


}
