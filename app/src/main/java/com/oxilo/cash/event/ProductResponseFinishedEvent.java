package com.oxilo.cash.event;

import com.oxilo.cash.modal.Product;
import com.oxilo.cash.modal.ProductList;

/**
 * Created by ericbasendra on 02/12/15.
 */
public class ProductResponseFinishedEvent {

    private Product mProduct;

    public ProductResponseFinishedEvent(Product mProduct){
        this.mProduct = mProduct;
    }
    public Product getmProduct() {
        return mProduct;
    }
    public void setmProduct(Product mProduct) {
        this.mProduct = mProduct;
    }

}
