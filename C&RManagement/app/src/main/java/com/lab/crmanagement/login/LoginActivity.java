package com.lab.crmanagement.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lab.crmanagement.R;
import com.lab.crmanagement.backend.data.client.ClientModelSingletonService;
import com.lab.crmanagement.backend.data.menu.MenuItem;
import com.lab.crmanagement.backend.data.menu.MenuSection;
import com.lab.crmanagement.navigationmenu.NavigationMenuActivity;

import java.util.Map;

public class LoginActivity extends AppCompatActivity implements LoginView{
    private LoginPresenter loginPresenter;
    private Button loginButton;
    private EditText id;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.loginPresenter = new LoginPresenter(this);

        loginButton = findViewById(R.id.login);
        id = findViewById(R.id.username);
        password = findViewById(R.id.password);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(LoginActivity.this, NavigationMenuActivity.class));
                if (isIdValid(id.getText().toString()) && password.getText().toString() != "")
                {
                    loginPresenter.sendLoginInfo(Integer.parseInt(id.getText().toString()), password.getText().toString());
                }else
                {
                    Toast.makeText(getApplicationContext(), "Invalid id or password type! Id must contains digit, password cannot be empty",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public boolean isIdValid(String id)
    {
        for (char c : id.toCharArray())
        {
            if (!Character.isDigit(c))
                return false;
        }
        return true;
    }


    @Override
    public void loginResult(boolean result) {
        if (result)
        {
            Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_LONG).show();
            startActivity(new Intent(LoginActivity.this, NavigationMenuActivity.class));
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (Map.Entry<String, MenuSection> itemEntry: ClientModelSingletonService.getClientModelInstance().getMenu().entrySet())
                    {
                        for (Map.Entry<Integer, MenuItem> item: itemEntry.getValue().getItems().entrySet())
                        {
                            Log.d("Menu Item", item.getValue().getName());
                        }
                    }
                }
            }).start();
        }else{
            Toast.makeText(getApplicationContext(), "Login failed!! Wrong id or password", Toast.LENGTH_LONG).show();
        }
    }
}