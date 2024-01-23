package com.lab.crmanagement.navigationmenu.tables;

import com.lab.crmanagement.backend.client.ClientSingletonService;

public class TablePresenter implements TableListener{

    private TableView view;

    public TablePresenter(TableView view) {
        this.view = view;
        ClientSingletonService.getClientInstance().setTableListener(this);
    }

    @Override
    public void updateData() {
        view.updateUi();
    }
}
