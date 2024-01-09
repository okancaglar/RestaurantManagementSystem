package com.lab.crmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import android.content.Intent;
import android.os.Bundle;

import com.lab.crmanagement.login.LoginActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //todo setup model, server, client


        /* navigate login screen */
        startActivity(new Intent(this, LoginActivity.class));
    }
}