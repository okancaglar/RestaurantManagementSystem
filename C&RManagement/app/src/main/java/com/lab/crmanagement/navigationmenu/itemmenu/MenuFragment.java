package com.lab.crmanagement.navigationmenu.itemmenu;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.lab.crmanagement.R;
import com.lab.crmanagement.backend.client.ClientSingletonService;
import com.lab.crmanagement.backend.data.client.ClientModel;
import com.lab.crmanagement.backend.data.client.ClientModelSingletonService;
import com.lab.crmanagement.backend.data.menu.MenuItem;
import com.lab.crmanagement.backend.data.menu.MenuSection;
import com.lab.crmanagement.navigationmenu.tables.TableFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuFragment extends Fragment implements SectionAdapter.SectionListener, MenuItemsAdapter.MenuItemsListener{

    private static final String TABLE_ID_PARAM = "table_id";
    private List<MenuSection> menuSections = new ArrayList<>();
    private RecyclerView itemsAdapterView;
    private ClientModel model = ClientModelSingletonService.getClientModelInstance();

    private int tableId;
    private  NewOrder newOrder;



    public MenuFragment() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment MenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MenuFragment newInstance(int tableId) {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putInt(TABLE_ID_PARAM, tableId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tableId = getArguments().getInt(TABLE_ID_PARAM);
            newOrder = new NewOrder(tableId, new ArrayList<>());
        }
        menuSections = model.getMenuSectionsAsList();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.sectionRecyclerView);
        this.itemsAdapterView = view.findViewById(R.id.itemsRecyclerView);

        Button confirmButton = view.findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo make presenter for client
/*
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ClientSingletonService.getClientInstance().sendTableOrderData(tableId,
                                newOrder.getItems());
                    }
                }).start();*/

                ClientSingletonService.getClientInstance().sendTableOrderData(tableId,
                        newOrder.getItems());

                if (newOrder.getItems().size() == 0)
                {
                    Toast.makeText(getContext(), "There is no item in the basket", Toast.LENGTH_LONG)
                            .show();
                    return;
                }else
                {
                    Toast.makeText(getContext(), "Operation is successful", Toast.LENGTH_LONG)
                            .show();
                }
                TableFragment tableFragment = TableFragment.newInstance(tableId);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.navigatorFragment, tableFragment, null)
                        .addToBackStack(null)
                        .commit();

            }

        });



        Context context = getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,
                false));
        recyclerView.setAdapter(new SectionAdapter(menuSectionsToStringArray(), this));


        return view;
    }


    private List<String> menuSectionsToStringArray()
    {
        ArrayList<String> sectionNames = new ArrayList<>();
        for (MenuSection section: menuSections)
        {
            sectionNames.add(section.getSectionName());
        }
        return sectionNames;
    }

    private List<MenuItem> menuSectionsToItems(String sectionName)
    {
        ArrayList<MenuItem> menuItems = new ArrayList<>();
        MenuSection section = getMenuSection(sectionName);
        if (section != null)
        {
            menuItems.addAll((section.getItems()).values());
        }
        return menuItems;
    }

    private MenuSection getMenuSection(String sectionName)
    {
        for (MenuSection section:menuSections)
        {
            if (section.getSectionName().equals(sectionName))
                return section;
        }
        return null;
    }

    @Override
    public void getMenuItems(String sectionName) {
        Context context = getContext();
        MenuItemsAdapter menuItemsAdapter = new MenuItemsAdapter(menuSectionsToItems(sectionName), this);
        itemsAdapterView.setLayoutManager(new GridLayoutManager(context, 2));
        itemsAdapterView.setAdapter(menuItemsAdapter);
    }

    @Override
    public void addItemToTheCart(MenuItem item)
    {
        newOrder.getItems().add(item);
    }

    @Override
    public void deleteItemFromCart(MenuItem item) {
        newOrder.getItems().remove(item);
    }
}