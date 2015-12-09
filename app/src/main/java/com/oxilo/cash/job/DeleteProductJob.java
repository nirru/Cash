package com.oxilo.cash.job;

import android.content.Context;

import com.android.volley.Response;
import com.oxilo.cash.Webservices.DeleteProduct;
import com.oxilo.cash.event.DeleteFinishedEvent;
import com.oxilo.cash.modal.Product;
import com.oxilo.cash.modal.Update;
import com.oxilo.cash.volley.AbstractVolleyJob;
import com.path.android.jobqueue.Params;

import de.greenrobot.event.EventBus;

/**
 * Created by ericbasendra on 05/12/15.
 */
public class DeleteProductJob<T> extends AbstractVolleyJob implements Response.Listener<T>, Response.ErrorListener{

    public Update savingsTypes;
    Context mContext;
    String id;
    public static final String TAG = "DeleteProductTag";

    public DeleteProductJob(Context mContext,Update savingsTypes,String id) {
        super(new Params(Priority.HIGH).requireNetwork().addTags(TAG));
        this.mContext = mContext;
        this.savingsTypes = savingsTypes;
        this.id = id;
    }

    @Override
    public void onRun() throws Throwable {
        DeleteProduct deleteProduct = new DeleteProduct(this,this,id);
        deleteProduct.invoke();
        savingsTypes = deleteProduct.savingsTypes;
    }

    @Override
    public void onResponse(T response) {
        EventBus.getDefault().post(new DeleteFinishedEvent((Product)response));
    }
}
