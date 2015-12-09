package com.oxilo.cash.event;

import com.oxilo.cash.modal.Product;

/**
 * Created by ericbasendra on 04/12/15.
 */
public class AddFinishedEvent {
    private Product update;

    public AddFinishedEvent(Product update){
        this.update = update;
    }

    public Product getUpdate() {
        return update;
    }

    public void setUpdate(Product update) {
        this.update = update;
    }
}
