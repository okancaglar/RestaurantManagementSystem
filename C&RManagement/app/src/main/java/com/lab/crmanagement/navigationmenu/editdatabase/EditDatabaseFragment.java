package com.lab.crmanagement.navigationmenu.editdatabase;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lab.crmanagement.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditDatabaseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditDatabaseFragment extends Fragment {


    public EditDatabaseFragment() {
        // Required empty public constructor
    }



    // TODO: Rename and change types and number of parameters
    public static EditDatabaseFragment newInstance() {
        EditDatabaseFragment fragment = new EditDatabaseFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_database, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Button editMenuButton = view.findViewById(R.id.editMenuButton);
        Button editEmployeeButton = view.findViewById(R.id.editEmployeeButton);
        Button editTableButton = view.findViewById(R.id.editTableButton);

        editMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.navigatorFragment, EditMenuFragment.class, null)
                        .addToBackStack(null)
                        .commit();
            }
        });

        editEmployeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.navigatorFragment, EditEmployeeFragment.class, null)
                        .addToBackStack(null)
                        .commit();
            }
        });

        editTableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.navigatorFragment, EditTableFragment.class, null)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}