package com.oxilo.cash.job;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.oxilo.cash.Webservices.UpdateProduct;
import com.oxilo.cash.event.ProductResponseFinishedEvent;
import com.oxilo.cash.event.UpdateFinishedEvent;
import com.oxilo.cash.modal.Product;
import com.oxilo.cash.modal.Update;
import com.oxilo.cash.volley.AbstractVolleyJob;
import com.path.android.jobqueue.Params;

import de.greenrobot.event.EventBus;

/**
 * Created by ericbasendra on 04/12/15.
 */
public class UpdateProductJob<T> extends AbstractVolleyJob implements Response.Listener<T>, Response.ErrorListener{
    public Update savingsTypes;
    Context mContext;
    String name,price,tax;
    String id;
    public static final String TAG = "UpdateProductJob";
    public UpdateProductJob(Context mContext,Update savingsTypes,String name,String price,String tax,String id) {
        super(new Params(Priority.HIGH).requireNetwork().addTags(TAG));
        this.mContext = mContext;
        this.savingsTypes = savingsTypes;
        this.name = name;
        this.price = price;
        this.tax = tax;
        this.id = id;
    }

    @Override
    public void onRun() throws Throwable {
        UpdateProduct updateProduct = new UpdateProduct(this,this,name,price,tax,id);
        updateProduct.invoke();
        savingsTypes = updateProduct.savingsTypes;

    }

    @Override
    public void onResponse(T response) {
        Log.e("REEE",response.toString());
        EventBus.getDefault().post(new UpdateFinishedEvent((Product) response));
    }
}
