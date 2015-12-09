package com.oxilo.cash.event;

import com.oxilo.cash.modal.Product;

/**
 * Created by ericbasendra on 05/12/15.
 */
public class DeleteFinishedEvent {
    private Product delete;
    public DeleteFinishedEvent(Product delete){
        this.delete = delete;
    }

    public Product getDelete() {
        return delete;
    }

    public void setDelete(Product delete) {
        this.delete = delete;
    }


}
