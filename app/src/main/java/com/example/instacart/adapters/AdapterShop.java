package com.example.instacart.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instacart.R;
import com.example.instacart.activities.ShopDetailsActivity;
import com.example.instacart.models.ModelShop;

import java.util.ArrayList;

public class AdapterShop extends RecyclerView.Adapter<AdapterShop.HolderShop>{


    private Context context;
    public ArrayList<ModelShop> shopsList;

    public AdapterShop(Context context, ArrayList<ModelShop> shopsList) {
        this.context = context;
        this.shopsList = shopsList;
    }

    @NonNull
    @Override
    public HolderShop onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

       View view = LayoutInflater.from(context).inflate(R.layout.row_shop,parent,false);

        return new HolderShop(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderShop holder, int position) {

        ModelShop modelShop = shopsList.get(position);
        String accountType = modelShop.getAccountType();
        String address = modelShop.getAddress();
        String city = modelShop.getCity();
        String state = modelShop.getState();
        final String uid = modelShop.getUid();
        String deliveryFee = modelShop.getDeliveryFee();
        String email = modelShop.getEmail();
        String shopName = modelShop.getShopName();
        String name = modelShop.getName();
        String online = modelShop.getOnline();
        String phone = modelShop.getPhone();
        String shopOpen = modelShop.getShopOpen();
        String timestamp = modelShop.getTimestamp();

        holder.shopNameTv.setText(shopName);
        holder.phoneTv.setText(phone);
        holder.addressTv.setText(address);
        if (online.equals("true")){
            holder.onlineIv.setVisibility(View.VISIBLE);
        }else{
            holder.onlineIv.setVisibility(View.GONE);
        }

        if (shopOpen.equals("true")){
            holder.shopClosedTv.setVisibility(View.GONE);
        }else{

            holder.shopClosedTv.setVisibility(View.VISIBLE);
        }



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ShopDetailsActivity.class);
                intent.putExtra("shopUid",uid);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return shopsList.size();
    }

    class HolderShop extends RecyclerView.ViewHolder{

        private ImageView onlineIv,nextIv;
        private RatingBar ratingBar;
        private TextView shopClosedTv,shopNameTv,phoneTv,addressTv;

        public HolderShop(@NonNull View itemView) {
            super(itemView);

            onlineIv = itemView.findViewById(R.id.onlineIv);
            nextIv = itemView.findViewById(R.id.nextIv);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            shopClosedTv = itemView.findViewById(R.id.shopClosedTv);
            shopNameTv = itemView.findViewById(R.id.shopNameTv);
            phoneTv = itemView.findViewById(R.id.phoneTv);
            addressTv = itemView.findViewById(R.id.addressTv);

        }
    }

}
