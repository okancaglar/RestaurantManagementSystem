package com.lab.crmanagement.navigationmenu.navigator;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lab.crmanagement.R;
import com.lab.crmanagement.backend.data.client.ClientModel;
import com.lab.crmanagement.backend.data.client.ClientModelSingletonService;
import com.lab.crmanagement.navigationmenu.editdatabase.EditDatabaseFragment;
import com.lab.crmanagement.navigationmenu.ongoingorders.OngoingOrdersFragment;
import com.lab.crmanagement.navigationmenu.tables.TablesFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NavigatorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NavigatorFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private ClientModel model = ClientModelSingletonService.getClientModelInstance();

    public NavigatorFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static NavigatorFragment newInstance() {
        NavigatorFragment fragment = new NavigatorFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_navigator, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button tablesButton = view.findViewById(R.id.tablesButton);
        Button ongoingOrders = view.findViewById(R.id.ongoingOrdersButton);
        Button editDatabase = view.findViewById(R.id.editButton);

        tablesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TablesFragment tablesFragment = TablesFragment.newInstance(model.getTablesAsList());

                getActivity().getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.navigatorFragment, tablesFragment, null)
                        .addToBackStack(null)
                        .commit();
            }
        });


        ongoingOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.navigatorFragment, OngoingOrdersFragment.class, null)
                        .addToBackStack(null)
                        .commit();
            }
        });

        editDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.navigatorFragment, EditDatabaseFragment.class, null)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}