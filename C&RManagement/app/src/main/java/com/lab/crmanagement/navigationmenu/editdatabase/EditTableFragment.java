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
 * Use the {@link EditTableFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditTableFragment extends Fragment {


    // TODO: Rename and change types and number of parameters
    public static EditTableFragment newInstance() {
        EditTableFragment fragment = new EditTableFragment();
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
        return inflater.inflate(R.layout.fragment_edit_table, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button addTableNavigator = view.findViewById(R.id.addTableNavigationButton);
        addTableNavigator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.navigatorFragment, AddTableFragment.class, null)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}