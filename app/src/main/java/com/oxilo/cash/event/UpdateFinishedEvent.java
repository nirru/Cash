package com.oxilo.cash.event;

import com.oxilo.cash.modal.Product;
import com.oxilo.cash.modal.Update;

/**
 * Created by ericbasendra on 04/12/15.
 */
public class UpdateFinishedEvent {
    private Product update;

    public UpdateFinishedEvent(Product update){
        this.update = update;
    }

    public Product getUpdate() {
        return update;
    }

    public void setUpdate(Product update) {
        this.update = update;
    }
}
