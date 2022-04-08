package com.example.instacart.models;

import android.widget.Filter;

import com.example.instacart.adapters.AdapterProductSeller;
import com.example.instacart.adapters.AdapterProductUser;

import java.util.ArrayList;

public class FilterProductUser extends Filter {

    private AdapterProductUser adapter;
    private ArrayList<ModelProduct> filterList;

    public FilterProductUser(AdapterProductUser adapter, ArrayList<ModelProduct> filterList) {
        this.adapter = adapter;
        this.filterList = filterList;
    }



    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {

        FilterResults results = new FilterResults();
        if (charSequence !=null && charSequence.length() >0){

            charSequence = charSequence.toString().toUpperCase();
            ArrayList<ModelProduct> filteredModels = new ArrayList<>();
            for(int i=0;i<filterList.size();i++ ){
                if (filterList.get(i).getProductTitle().toUpperCase().contains(charSequence) ||
                        filterList.get(i).getProductCategory().toUpperCase().contains(charSequence)){
                    filteredModels.add(filterList.get(i));
                }
            }
            results.count = filteredModels.size();
            results.values = filteredModels;

        }
        else{
            results.count = filterList.size();
            results.values = filterList;

        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

        adapter.productsList = (ArrayList<ModelProduct>) filterResults.values;

        adapter.notifyDataSetChanged();
    }
}
