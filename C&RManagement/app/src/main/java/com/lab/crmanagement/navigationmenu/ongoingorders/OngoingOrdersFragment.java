package com.lab.crmanagement.navigationmenu.ongoingorders;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lab.crmanagement.R;
import com.lab.crmanagement.backend.client.ClientSingletonService;
import com.lab.crmanagement.backend.data.client.ClientModelSingletonService;
import com.lab.crmanagement.backend.data.ongoingorders.OngoingOrderPair;
import com.lab.crmanagement.navigationmenu.tables.TablesAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OngoingOrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OngoingOrdersFragment extends Fragment implements OngoingOrdersView{

    private OngoingOrderPresenter presenter;
    private RecyclerView recyclerView;

    public OngoingOrdersFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static OngoingOrdersFragment newInstance() {
        OngoingOrdersFragment fragment = new OngoingOrdersFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new OngoingOrderPresenter(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ongoing_orders, container, false);
        View rView = view.findViewById(R.id.recyclerviewongoingorders);
        if (rView instanceof RecyclerView)
        {
            Context context = getContext();
            recyclerView = (RecyclerView) rView;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new OngoingOrdersAdapter(ClientModelSingletonService.getClientModelInstance().getOngoingOrders()));
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    @Override
    public void updateScreen() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((OngoingOrdersAdapter)recyclerView.getAdapter()).updateData(
                        ClientModelSingletonService.getClientModelInstance().getOngoingOrders()
                );
            }
        });
    }
}