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
import com.example.instacart.models.FilterProductUser;
import com.example.instacart.models.ModelProduct;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterProductUser extends  RecyclerView.Adapter<AdapterProductUser.HolderProductUser> implements Filterable {


    private Context context;
    public ArrayList<ModelProduct> productsList,filterList;
    private FilterProductUser filter;

    public AdapterProductUser(Context context, ArrayList<ModelProduct> productsList) {
        this.context = context;
        this.productsList = productsList;
        this.filterList = productsList;
    }

    @NonNull
    @Override
    public HolderProductUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_product_user,parent,false);

        return new HolderProductUser(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderProductUser holder, int position) {

        ModelProduct modelProduct = productsList.get(position);
        String productPrice = modelProduct.getProductPrice();
        String productCategory = modelProduct.getProductCategory();
        String productDescription = modelProduct.getProductDescription();
        String productTitle = modelProduct.getProductTitle();
        String  productQuantity= modelProduct.getProductQuantity();
        String  productId= modelProduct.getProductId();
        String  timestamp= modelProduct.getTimestamp();
        String  productIcon= modelProduct.getProductIcon();

        holder.titleTv.setText(productTitle);
        holder.priceTv.setText("₹ "+productPrice);
        holder.descriptionTv.setText(productDescription);
        holder.quantityTv.setText(productQuantity);

        try{
            Picasso.get().load(productIcon).placeholder(R.drawable.ic_baseline_add_shopping_cart_24).into(holder.productIconIv);
        }catch (Exception e){
            holder.productIconIv.setImageResource(R.drawable.ic_baseline_add_shopping_cart_24);
        }




    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    @Override
    public Filter getFilter() {

        if (filter==null){
            filter = new FilterProductUser(this,filterList);

        }
        return filter;
    }

    class HolderProductUser extends RecyclerView.ViewHolder{

        private ImageView productIconIv;
        private TextView titleTv,descriptionTv,quantityTv,priceTv;

        public HolderProductUser(@NonNull View itemView) {
            super(itemView);

            productIconIv = itemView.findViewById(R.id.productIconIv);
            titleTv = itemView.findViewById(R.id.titleTv);
            descriptionTv = itemView.findViewById(R.id.descriptionTv);
            quantityTv = itemView.findViewById(R.id.quantityTv);
            priceTv = itemView.findViewById(R.id.priceTv);

        }


    }

}
