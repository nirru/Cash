package com.oxilo.cash.custom;

import android.widget.Filter;

import com.oxilo.cash.adapter.ProductListAdapter;
import com.oxilo.cash.modal.Product;
import com.oxilo.cash.modal.ProductList;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ericbasendra on 05/12/15.
 */
public class ProductFilter<T> extends Filter {

    private final ProductListAdapter adapter;

    private final List<T> originalList;

    private final List<T> filteredList;

    public ProductFilter(ProductListAdapter adapter, List<T> originalList){
        super();
        this.adapter = adapter;
        this.originalList = new LinkedList<>(originalList);
        this.filteredList = new ArrayList<>();
        filteredList.addAll(originalList);

    }
    @Override
    protected FilterResults performFiltering(CharSequence  constraint) {
        filteredList.clear();
        final FilterResults results = new FilterResults();

        if (constraint.length() == 0) {
            filteredList.addAll(originalList);
        } else {
            final String filterPattern = constraint.toString().toLowerCase().trim();

            for (final T product : originalList) {
                if (((ProductList)product).getName().contains(constraint)) {
                    filteredList.add(product);
                }
            }
        }
        results.values = filteredList;
        results.count = filteredList.size();
        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        adapter.filteredProductList.clear();
        adapter.filteredProductList.addAll((ArrayList<T>) filterResults.values);
        adapter.notifyDataSetChanged();
    }
}
