package com.oxilo.cash.modal;

/**
 * Created by ericbasendra on 02/12/15.
 */
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("ProductList")
    @Expose
    private List<com.oxilo.cash.modal.ProductList> ProductList = new ArrayList<com.oxilo.cash.modal.ProductList>();

    /**
     *
     * @return
     * The status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     *
     * @param status
     * The status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     *
     * @return
     * The message
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @param message
     * The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     *
     * @return
     * The ProductList
     */
    public List<com.oxilo.cash.modal.ProductList> getProductList() {
        return ProductList;
    }

    /**
     *
     * @param ProductList
     * The ProductList
     */
    public void setProductList(List<com.oxilo.cash.modal.ProductList> ProductList) {
        this.ProductList = ProductList;
    }

}
