package com.lab.crmanagement.navigationmenu.editdatabase;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.lab.crmanagement.R;
import com.lab.crmanagement.backend.client.ClientSingletonService;
import com.lab.crmanagement.backend.data.table.Table;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddTableFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddTableFragment extends Fragment {



    public AddTableFragment() {
        // Required empty public constructor
    }


    public static AddTableFragment newInstance(String param1, String param2) {
        AddTableFragment fragment = new AddTableFragment();
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
        return inflater.inflate(R.layout.fragment_add_table, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText tableId = view.findViewById(R.id.addTableId);
        Button send = view.findViewById(R.id.addTableButton);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ClientSingletonService.getClientInstance().sendAdminTableData
                                (new Table(Integer.parseInt(tableId.getText().toString())));
                    }
                }).start();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.navigatorFragment, EditTableFragment.class, null)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}