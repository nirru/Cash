package com.oxilo.cash.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.oxilo.cash.R;
import com.oxilo.cash.custom.ProductFilter;
import com.oxilo.cash.modal.Product;
import com.oxilo.cash.modal.ProductList;
import com.oxilo.cash.util.AnimationUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * Created by ericbasendra on 02/12/15.
 */
public class ProductListAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private Context mContext;
    public List<T> dataSet;
    public  List<T> filteredProductList;
    private static MyClickListener myClickListener;
    private int mLastPosition = 5;

    public ProductListAdapter(List<T> productLists,Context mContext) {
        this.mContext = mContext;
        this.dataSet = productLists;
        this.filteredProductList = new ArrayList<>();
    }
    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }
    public void addItems(@NonNull List<T> newDataSetItems) {
        filteredProductList.addAll(newDataSetItems);
    }

    public void animateTo(List<T> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }


    private void applyAndAnimateRemovals(List<T> newModels) {
        for (int i = dataSet.size() - 1; i >= 0; i--) {
            final T model = dataSet.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }


    private void applyAndAnimateAdditions(List<T> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final T model = newModels.get(i);
            if (!dataSet.contains(model)) {
                addItem(i, model);
            }
        }
    }


    private void applyAndAnimateMovedItems(List<T> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final T model = newModels.get(toPosition);
            final int fromPosition = dataSet.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public void addItem(T item) {
        if (!dataSet.contains(item)) {
            dataSet.add(item);
            notifyItemInserted(dataSet.size() - 1);
        }
    }

    public void addItem(int position, T model) {
        dataSet.add(position, model);
        notifyItemInserted(position);
    }

    public void removeItem(T item) {
        int indexOfItem = dataSet.indexOf(item);
        if (indexOfItem != -1) {
            this.dataSet.remove(indexOfItem);
            notifyItemRemoved(indexOfItem);
        }
    }

    public T removeItem(int position) {
        final T model = dataSet.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void moveItem(int fromPosition, int toPosition) {
        final T model = dataSet.remove(fromPosition);
        dataSet.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public T getItem(int index) {
        if (dataSet != null && dataSet.get(index) != null) {
            return dataSet.get(index);
        } else {
            throw new IllegalArgumentException("Item with index " + index + " doesn't exist, dataSet is " + dataSet);
        }
    }


    @Override
    public int getItemViewType(int position) {
        return dataSet.get(position)!=null? VIEW_ITEM: VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh = null;
        if (viewType == VIEW_ITEM){
            View itemView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.custom_card, parent, false);
            vh = new ProductViewHolder(itemView);
        }
        else if(viewType == VIEW_PROG){
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.progress_item, parent, false);
            vh = new ProgressViewHolder(v);
        }else {
            throw new IllegalStateException("Invalid type, this type ot items " + viewType + " can't be handled");
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ProductViewHolder){
            T dataItem = dataSet.get(position);
            setAnimation(holder,position);
            ((ProductViewHolder) holder).nameView.setText(((ProductList)dataItem).getName());
            ((ProductViewHolder) holder).priceView.setText(((ProductList)dataItem).getPrice());
            ((ProductViewHolder) holder).taxView.setText(((ProductList)dataItem).getTaxclass());
        }else{
            ((ProgressViewHolder)holder).progressBar.setIndeterminate(true);
        }

    }

    @Override
    public int getItemCount() {
        if (dataSet!=null)
            return dataSet.size();
        else
            return 0;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        dataSet.clear();
        if (charText.length() == 0) {
            dataSet.addAll(filteredProductList);
        }
        else
        {
            for (T cI : filteredProductList)
            {
                if (((ProductList)cI).getName().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    dataSet.add(cI);

                }
            }
        }
        notifyDataSetChanged();
    }
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView nameView,priceView,taxView;
        TextView updateView,deleteView;
        public ProductViewHolder(View itemView) {
            super(itemView);

            nameView = (TextView)itemView.findViewById(R.id.action_name);
            priceView = (TextView)itemView.findViewById(R.id.action_price);
            taxView = (TextView)itemView.findViewById(R.id.action_tax);
            updateView = (TextView)itemView.findViewById(R.id.update);
            deleteView = (TextView)itemView.findViewById(R.id.delete);
            updateView.setOnClickListener(this);
            deleteView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            myClickListener.onItemClick(getLayoutPosition(), view);
        }
    }


    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public MaterialProgressBar progressBar;
        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (MaterialProgressBar)v.findViewById(R.id.progress_bar);
        }
    }
    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(RecyclerView.ViewHolder viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > mLastPosition)
        {
            AnimationUtils.animate(viewToAnimate, true);
            mLastPosition = position;
        }
    }

    /**
     * y Custom Item Listener
     */

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}
