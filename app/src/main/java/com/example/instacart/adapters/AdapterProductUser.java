package com.example.instacart.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instacart.R;
import com.example.instacart.activities.PaymentActivity;
import com.example.instacart.activities.ShopDetailsActivity;
import com.example.instacart.models.FilterProductUser;
import com.example.instacart.models.ModelProduct;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import p32929.androideasysql_library.Column;
import p32929.androideasysql_library.EasyDB;

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

       final ModelProduct modelProduct = productsList.get(position);
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

        holder.addToCartTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               showQuantityDialog(modelProduct);
            }
        });



        holder.buyNowTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, PaymentActivity.class);
                intent.putExtra("productPrice",productPrice);
                context.startActivity(intent);
            }
        });




    }

    private  double cost = 0;
    private  double finalCost = 0;
    private int quantity = 0;
    private void showQuantityDialog(ModelProduct modelProduct) {

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_quantity,null);

        ImageView productIv = view.findViewById(R.id.productIv);
        TextView titleTv = view.findViewById(R.id.titleTv);
        TextView pQuantityTv = view.findViewById(R.id.pQuantityTv);
        TextView descriptionTv = view.findViewById(R.id.descriptionTv);
        TextView priceTv = view.findViewById(R.id.priceTv);
        TextView quantityTv = view.findViewById(R.id.quantityTv);
        ImageButton decrementBtn = view.findViewById(R.id.decrementBtn);
        Button continueBtn = view.findViewById(R.id.continueBtn);
        ImageButton incrementBtn = view.findViewById(R.id.incrementBtn);


        //get data from model
        String productId = modelProduct.getProductId();
        String title = modelProduct.getProductTitle();
        String productQuantity = modelProduct.getProductQuantity();
        String description = modelProduct.getProductDescription();
        String image = modelProduct.getProductIcon();

        String price = modelProduct.getProductPrice();

        cost = Double.parseDouble(price.replaceAll("₹ ",""));
        finalCost = Double.parseDouble(price.replaceAll("₹ ",""));
        quantity=1;


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);

        try {

            Picasso.get().load(image).placeholder(R.drawable.ic_baseline_add_shopping_cart_24).into(productIv);

        }catch (Exception e){

            productIv.setImageResource(R.drawable.ic_baseline_add_shopping_cart_24);

        }

        titleTv.setText(""+title);
        pQuantityTv.setText(""+productQuantity);
        descriptionTv.setText(""+description);
        quantityTv.setText(""+quantity);
        priceTv.setText("₹ "+price);

        AlertDialog dialog = builder.create();
        dialog.show();

        incrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finalCost = finalCost+cost;
                quantity++;

                priceTv.setText("₹ "+finalCost);
                quantityTv.setText(""+quantity);

            }
        });


        decrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (quantity>1){
                    finalCost = finalCost-cost;
                    quantity--;
                    priceTv.setText("₹ "+finalCost);
                    quantityTv.setText(""+quantity);

                }
            }
        });


        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = titleTv.getText().toString().trim();
                String priceEach = priceTv.getText().toString().trim().replace("₹ ","");
                String quantity = quantityTv.getText().toString().trim();


                addtoCart(productId,title,priceEach,quantity);
                dialog.dismiss();

            }
        });


    }

    private int itemId = 1;
    private void addtoCart(String productId, String title, String priceEach, String quantity) {

        itemId++;
        EasyDB easyDB = EasyDB.init(context,"ITEMS_DB")
                .setTableName("ITEMS_TABLE")
                .addColumn(new Column("Item_Id",new String[]{"text","unique"}))
                .addColumn(new Column("Item_PID",new String[]{"text","not null"}))
                .addColumn(new Column("Item_Name",new String[]{"text","not null"}))
                .addColumn(new Column("Item_Price_Each",new String[]{"text","not null"}))
                .addColumn(new Column("Item_Quantity",new String[]{"text","not null"}))
                .doneTableColumn();

        Boolean b = easyDB.addData("Item_Id",itemId)
                .addData("Item_PID",productId)
                .addData("Item_Name",title)
                .addData("Item_Price_Each",priceEach)
                .addData("Item_Quantity",quantity)
                .doneDataAdding();


        Toast.makeText(context, "Added to Cart....", Toast.LENGTH_SHORT).show();

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
        private TextView titleTv,descriptionTv,quantityTv,priceTv,addToCartTv,buyNowTv;

        public HolderProductUser(@NonNull View itemView) {
            super(itemView);

            productIconIv = itemView.findViewById(R.id.productIconIv);
            titleTv = itemView.findViewById(R.id.titleTv);
            descriptionTv = itemView.findViewById(R.id.descriptionTv);
            quantityTv = itemView.findViewById(R.id.quantityTv);
            priceTv = itemView.findViewById(R.id.priceTv);
            addToCartTv = itemView.findViewById(R.id.addToCartTv);
            buyNowTv = itemView.findViewById(R.id.buyNowTv);

        }


    }

}
