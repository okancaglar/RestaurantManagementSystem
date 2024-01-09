package com.lab.crmanagement.navigationmenu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.lab.crmanagement.R;
import com.lab.crmanagement.navigationmenu.navigator.NavigatorFragment;

public class NavigationMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_menu);

        getSupportFragmentManager().beginTransaction().
                setReorderingAllowed(true)
                .add(R.id.navigatorFragment, NavigatorFragment.class, null)
                .commit();
    }
}