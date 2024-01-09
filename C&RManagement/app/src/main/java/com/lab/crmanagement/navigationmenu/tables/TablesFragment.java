package com.lab.crmanagement.navigationmenu.tables;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lab.crmanagement.R;
import com.lab.crmanagement.backend.data.table.Table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TablesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TablesFragment extends Fragment implements TablesAdapter.TablesFragmentListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private static final String ARG_PARAM1 = "table_list";

    // TODO: Rename and change types of parameters
    private List<Table> tables = new ArrayList<>();


    public TablesFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static TablesFragment newInstance(List<Table> tablesParam) {
        TablesFragment fragment = new TablesFragment();
        Bundle args = new Bundle();
        //args.putSerializable(ARG_PARAM1, (Serializable) tablesParam);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/

        tables.add(new Table(1));
        tables.add(new Table(2));
        tables.add(new Table(3));
        tables.add(new Table(4));

        //todo get Data From model
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_tables, container, false);

        if (view instanceof RecyclerView)
        {
            Context context = getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
            recyclerView.setAdapter(new TablesAdapter(tables, this));
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }

    @Override
    public void onButtonClicked(int position, int tableId) {
        TableFragment tableFragment = TableFragment.newInstance(tableId);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.navigatorFragment, tableFragment, null)
                .addToBackStack(null)
                .commit();
    }
}