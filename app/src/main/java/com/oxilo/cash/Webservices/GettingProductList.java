package com.oxilo.cash.Webservices;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.reflect.TypeToken;
import com.oxilo.cash.AppConstant;
import com.oxilo.cash.modal.Product;
import com.oxilo.cash.volley.AbstractVolleyJob;
import com.oxilo.cash.volley.GsonRequest;
import com.oxilo.cash.volley.MyVolley;
import com.path.android.jobqueue.Params;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ericbasendra on 02/12/15.
 */
public class GettingProductList{
    private final Response.Listener success;
    private final Response.ErrorListener error;
    public Product savingsTypes;
    int count;
    public GettingProductList(Response.Listener success, Response.ErrorListener error,int count) {
        this.success = success;
        this.error = error;
        this.count = count;
    }

    //GetRecordsRequestInvoke
    public void invoke() {
//        String URL = AppConstant.CASH_PRODUCT_LISTING_URL+"page="+count+"limit="+5;
        String URL = AppConstant.CASH_PRODUCT_LISTING_URL;
        Log.e("URL","" + URL);
        //TODO: serialization is failing, need to fix, for now use other Get Request as example.
        GsonRequest request = new GsonRequest<Product>(Request.Method.POST,
                URL,
                new TypeToken<Product>() {}.getType(),
                prepareHasMap(),
                success,
                error);
        request.setSoftTTLDefault(24 * 60 * 60 * 1000); //pull from cache if not invalidated for 1 week
        request.setRetryPolicy(new DefaultRetryPolicy(30000, 1, 1));
        MyVolley.getRequestQueue().add(request);
    }

    private HashMap prepareHasMap(){
        HashMap<String,String> hm = new HashMap<String,String>();
        hm.put("firstName", "");
        return hm;
    }

}
