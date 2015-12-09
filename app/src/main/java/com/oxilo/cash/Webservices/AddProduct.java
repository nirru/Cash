package com.oxilo.cash.Webservices;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.reflect.TypeToken;
import com.oxilo.cash.AppConstant;
import com.oxilo.cash.modal.Product;
import com.oxilo.cash.modal.Update;
import com.oxilo.cash.volley.GsonRequest;
import com.oxilo.cash.volley.MyVolley;

/**
 * Created by ericbasendra on 04/12/15.
 */
public class AddProduct {

    private final Response.Listener success;
    private final Response.ErrorListener error;
    public Update savingsTypes;
    String name, price,tax;
    String id;
    public AddProduct(Response.Listener success, Response.ErrorListener error, String name, String price, String tax){
        this.success = success;
        this.error = error;
        this.name = name;
        this.price = price;
        this.tax = tax;
        this.id = id;
    }

    //GetRecordsRequestInvoke
    public void invoke() {
        String URL = AppConstant.CASH_ADD_PRODUCT_URL+"&name="+name+"&price="+price+"&taxclass="+tax;
        Log.e("URL","" + URL);
        //TODO: serialization is failing, need to fix, for now use other Get Request as example.
        GsonRequest request = new GsonRequest<Update>(Request.Method.POST,
                URL,
                new TypeToken<Product>() {}.getType(),
                success,
                error);
        request.setSoftTTLDefault(24 * 60 * 60 * 1000); //pull from cache if not invalidated for 1 week
        request.setRetryPolicy(new DefaultRetryPolicy(30000, 1, 1));
        MyVolley.getRequestQueue().add(request);
    }

}
