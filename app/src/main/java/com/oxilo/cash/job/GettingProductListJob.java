package com.oxilo.cash.job;

import android.content.Context;

import com.android.volley.Response;
import com.oxilo.cash.Webservices.GettingProductList;
import com.oxilo.cash.event.ProductResponseFinishedEvent;
import com.oxilo.cash.modal.Product;
import com.oxilo.cash.volley.AbstractVolleyJob;
import com.path.android.jobqueue.Params;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by ericbasendra on 02/12/15.
 */
public class GettingProductListJob <T> extends AbstractVolleyJob implements Response.Listener<T>, Response.ErrorListener {
    public Product savingsTypes;
    Context mContext;
    int count;
    public static final String TAG = "GettingProductListJob";
    public GettingProductListJob(Context mContext,Product savingsTypes,int count) {
        super(new Params(Priority.HIGH).requireNetwork().addTags(TAG));
        this.mContext = mContext;
        this.savingsTypes = savingsTypes;
        this.count = count;
    }

    @Override
    public void onRun() throws Throwable {
        GettingProductList gettingProductList = new GettingProductList(this,this,count);
        gettingProductList.invoke();
        savingsTypes = gettingProductList.savingsTypes;
    }


    @Override
    public void onResponse(T response) {
        EventBus.getDefault().post(new ProductResponseFinishedEvent((Product)response));
    }
}
