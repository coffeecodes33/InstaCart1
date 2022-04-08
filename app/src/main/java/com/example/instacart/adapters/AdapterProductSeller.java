package com.example.instacart.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.instacart.R;
import com.example.instacart.models.FilterProduct;
import com.example.instacart.models.ModelProduct;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterProductSeller extends RecyclerView.Adapter<AdapterProductSeller.HolderProductSeller> implements Filterable {

    private Context context;
    public ArrayList<ModelProduct> productList,filterList;
    private FilterProduct filter;


    public AdapterProductSeller(Context context, ArrayList<ModelProduct> productList) {
        this.context = context;
        this.productList = productList;
        this.filterList = productList;
    }




    @NonNull
    @Override
    public HolderProductSeller onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       //inflate layout
        View view = LayoutInflater.from(context).inflate(R.layout.row_product_seller,parent,false);

        return new HolderProductSeller(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderProductSeller holder, int position) {

        ModelProduct modelProduct = productList.get(position);
        //get data
        String id = modelProduct.getProductId();
        String uid = modelProduct.getUid();
        String title = modelProduct.getProductTitle();
        String category = modelProduct.getProductCategory();
        String price = modelProduct.getProductPrice();
        String description = modelProduct.getProductDescription();
        String icon = modelProduct.getProductIcon();
        String quantity = modelProduct.getProductQuantity();
        String timeStamp = modelProduct.getTimestamp();


        holder.titleTv.setText(title);
        holder.quantityTv.setText(quantity);
        holder.priceTv.setText("₹"+price);

        try{
            Picasso.get().load(icon).placeholder(R.drawable.ic_baseline_add_shopping_cart_24).into(holder.productIconIv);

        }catch (Exception e){
            holder.productIconIv.setImageResource(R.drawable.ic_baseline_add_shopping_cart_24);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }




    @Override
    public int getItemCount() {
        return productList.size();
    }

    @Override
    public Filter getFilter() {
        if(filter==null){
            filter = new FilterProduct(this,filterList);
        }
        return filter;
    }

    class HolderProductSeller extends RecyclerView.ViewHolder{


        private ImageView productIconIv,nextIv;
        private TextView titleTv,quantityTv,priceTv;

        public HolderProductSeller(@NonNull View itemView){
            super(itemView);

            productIconIv = itemView.findViewById(R.id.productIconIv);
            nextIv = itemView.findViewById(R.id.nextIv);
            priceTv = itemView.findViewById(R.id.priceTv);
            quantityTv = itemView.findViewById(R.id.quantityTv);
            titleTv = itemView.findViewById(R.id.titleTv);


        }
    }
}
