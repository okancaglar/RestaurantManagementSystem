package com.lab.crmanagement.navigationmenu.itemmenu;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lab.crmanagement.R;
import com.lab.crmanagement.backend.data.menu.MenuItem;
import com.lab.crmanagement.backend.data.menu.MenuSection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuFragment extends Fragment implements SectionAdapter.SectionListener {

    private List<MenuSection> menuSections = new ArrayList<>();
    private RecyclerView itemsAdapterView;


    public MenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MenuFragment newInstance(String param1, String param2) {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        HashMap<Integer, MenuItem> items = new HashMap<>();
        items.put(1, new MenuItem("test", 1, "yemek1", 10, "karabiber, yoğurt"));
        menuSections.add(new MenuSection("test", items));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.sectionRecyclerView);
        this.itemsAdapterView = view.findViewById(R.id.itemsRecyclerView);


        Context context = getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
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
        MenuItemsAdapter menuItemsAdapter = new MenuItemsAdapter(menuSectionsToItems(sectionName));
        itemsAdapterView.setLayoutManager(new GridLayoutManager(context, 2));
        itemsAdapterView.setAdapter(menuItemsAdapter);
    }
}