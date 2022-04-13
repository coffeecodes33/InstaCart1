package com.example.instacart.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.instacart.R;
import com.example.instacart.adapters.AdapterProductUser;
import com.example.instacart.models.Constraints;
import com.example.instacart.models.ModelProduct;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShopDetailsActivity extends AppCompatActivity {


    private ImageView shopIv;
    private TextView shopNameTv,phoneTv,emailTv,filteredProductsTv,openCloseTv,addressTv,deliveryFeeTv;
    private ImageButton callBtn,addToCartBtn,filterProductBtn;
    private EditText searchProductEt;
    private RecyclerView  productsRv;

    private String shopUid;
    private String shopName,shopEmail,shopAddress,shopPhone;

    private FirebaseAuth firebaseAuth;
    private ArrayList<ModelProduct> productsList;
    private AdapterProductUser adapterProductUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_details);


        getSupportActionBar().hide();
        shopIv = findViewById(R.id.shopIv);
        shopNameTv = findViewById(R.id.shopNameTv);
        phoneTv = findViewById(R.id.phoneTv);
        emailTv = findViewById(R.id.emailTv);
        filteredProductsTv = findViewById(R.id.filteredProductsTv);
        openCloseTv = findViewById(R.id.openCloseTv);
        addressTv = findViewById(R.id.addressTv);
        deliveryFeeTv = findViewById(R.id.deliveryFeeTv);
        addToCartBtn = findViewById(R.id.addToCartBtn);
        callBtn = findViewById(R.id.callBtn);
        filterProductBtn = findViewById(R.id.filterProductBtn);
        searchProductEt = findViewById(R.id.searchProductEt);
        productsRv = findViewById(R.id.productsRv);

        shopUid = getIntent().getStringExtra("shopUid");


        firebaseAuth = FirebaseAuth.getInstance();
        loadMyInfo();
        loadShopDetails();
        loadShopProducts();

        searchProductEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                try {

                    adapterProductUser.getFilter().filter(charSequence);
                }catch(Exception e){

                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                startActivity(new Intent(ShopDetailsActivity.this, OfferPageActivity.class));
            }
        });

        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialPhone();
            }
        });

        filterProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ShopDetailsActivity.this);
                builder.setTitle("Choose Category:")
                        .setItems(Constraints.categories1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                String selected = Constraints.categories1[i];
                                filteredProductsTv.setText(selected);
                                if (selected.equals("All")){

                                  //  loadShopDetails();
                                    loadShopProducts();
                                }else{
                                   adapterProductUser.getFilter().filter(selected);
                                }
                            }
                        })
                        .show();
            }
        });

    }

    private void dialPhone() {

        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+Uri.encode(shopPhone))));
        Toast.makeText(this, ""+shopPhone, Toast.LENGTH_SHORT).show();
    }

    private void loadMyInfo() {

        DatabaseReference ref = FirebaseDatabase.getInstance("https://instacart-fab15-default-rtdb.firebaseio.com/").getReference("Users");

        ref.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot ds: dataSnapshot.getChildren()){
                            String name = ""+ds.child("name").getValue();
                            String email = ""+ds.child("email").getValue();
                            String phone = ""+ds.child("phone").getValue();
                            String city = ""+ds.child("city").getValue();
                            String accountType = ""+ds.child("accountType").getValue();



                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }



    private void loadShopDetails() {

        DatabaseReference ref = FirebaseDatabase.getInstance("https://instacart-fab15-default-rtdb.firebaseio.com/").getReference("Users");

        ref.child(shopUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name = ""+dataSnapshot.child("name").getValue();
                shopName = ""+dataSnapshot.child("shopName").getValue();
                shopEmail = ""+dataSnapshot.child("email").getValue();
                shopPhone = ""+dataSnapshot.child("phone").getValue();
                shopAddress = ""+dataSnapshot.child("address").getValue();

                String deliveryFee = ""+dataSnapshot.child("deliveryFee").getValue();
                String shopOpen = ""+dataSnapshot.child("shopOpen").getValue();

                shopNameTv.setText(shopName);
                emailTv.setText(shopEmail);
                deliveryFeeTv.setText("Delivery Fee: ₹"+deliveryFee);
                addressTv.setText(shopAddress);
                phoneTv.setText(shopPhone);
                if (shopOpen.equals("true")){

                    openCloseTv.setText("Open");
                }else{
                    openCloseTv.setText("Closed");
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void loadShopProducts() {
        productsList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance("https://instacart-fab15-default-rtdb.firebaseio.com/").getReference("Users");

        reference.child(shopUid).child("Products")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        productsList.clear();
                        for (DataSnapshot ds: dataSnapshot.getChildren()){
                            ModelProduct modelProduct = ds.getValue(ModelProduct.class);
                            productsList.add(modelProduct);

                        }

                        adapterProductUser = new AdapterProductUser(ShopDetailsActivity.this,productsList);
                        productsRv.setAdapter(adapterProductUser);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


}