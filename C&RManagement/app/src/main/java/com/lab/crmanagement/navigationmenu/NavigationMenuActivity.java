package com.lab.crmanagement.navigationmenu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lab.crmanagement.R;
import com.lab.crmanagement.backend.data.client.ClientModel;
import com.lab.crmanagement.backend.data.client.ClientModelSingletonService;
import com.lab.crmanagement.navigationmenu.navigator.NavigatorFragment;

public class NavigationMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_menu);

        TextView nameView = findViewById(R.id.name);
        TextView surnameView = findViewById(R.id.surname);

        Button backMenuButton = findViewById(R.id.backMenuButton);
        backMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.navigatorFragment, NavigatorFragment.class, null)
                        .addToBackStack(null)
                        .setReorderingAllowed(true)
                        .commit();
            }
        });

        ClientModel model = ClientModelSingletonService.getClientModelInstance();

        nameView.setText(model.getEmployeeInformation().getFirstName());
        surnameView.setText(model.getEmployeeInformation().getLastName());

        getSupportFragmentManager().beginTransaction().
                setReorderingAllowed(true)
                .add(R.id.navigatorFragment, NavigatorFragment.class, null)
                .commit();
    }
}