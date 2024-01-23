package com.lab.crmanagement.navigationmenu.ongoingorders;

import com.lab.crmanagement.backend.client.ClientSingletonService;

public class OngoingOrderPresenter implements OngoingOrdersListener{

    private OngoingOrdersView view;

    public OngoingOrderPresenter(OngoingOrdersView view) {
        this.view = view;
        ClientSingletonService.getClientInstance().setOngoingOrdersListener(this);
    }

    @Override
    public void updateData() {
        view.updateScreen();
    }


}
