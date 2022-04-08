package com.example.instacart.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.instacart.adapters.AdapterProductSeller;
import com.example.instacart.models.Constraints;
import com.example.instacart.models.ModelProduct;
import com.example.instacart.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MainSellerActivity extends AppCompatActivity {

    private TextView nameTv,emailTv,shopNameTv,tabProductsTv,filteredProductsTv;
    private ImageButton logoutBtn,addProductBtn,filterProductBtn;

    private RecyclerView productsRv;
    private EditText searchProductEt;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private RelativeLayout productsRl;

    private ArrayList<ModelProduct> productList;
    private AdapterProductSeller adapterProductSeller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_seller);

        getSupportActionBar().hide();

        nameTv = findViewById(R.id.nameTv);
        searchProductEt = findViewById(R.id.searchProductEt);
        shopNameTv = findViewById(R.id.shopNameTv);
        tabProductsTv = findViewById(R.id.tabProductsTv);
        filteredProductsTv = findViewById(R.id.filteredProductsTv);


        emailTv= findViewById(R.id.emailTv);
        logoutBtn = findViewById(R.id.logoutBtn);
        addProductBtn = findViewById(R.id.addProductBtn);
        filterProductBtn = findViewById(R.id.filterProductBtn);
        productsRl = findViewById(R.id.productsRl);
        productsRv = findViewById(R.id.productsRv);


        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait..");
        progressDialog.setCanceledOnTouchOutside(false);




        checkUser();
        loadAllProducts();

        searchProductEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                try {

                    adapterProductSeller.getFilter().filter(charSequence);
                }catch(Exception e){

                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeMeOffline();
            }
        });

        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                startActivity(new Intent(MainSellerActivity.this, AddProductActivity.class));
            }
        });

        tabProductsTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //load products
                showProductsUI();

            }
        });

        filterProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainSellerActivity.this);
                builder.setTitle("Choose Category:")
                        .setItems(Constraints.categories1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                String selected = Constraints.categories1[i];
                                filteredProductsTv.setText(selected);
                                if (selected.equals("All")){

                                    loadAllProducts();
                                }else{
                                    loadFilteredProducts(selected);
                                }
                            }
                        })
                        .show();
            }
        });


    }

    private void loadFilteredProducts(String selected) {

        productList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance("https://instacart-fab15-default-rtdb.firebaseio.com/").getReference("Users");
        reference.child(firebaseAuth.getUid()).child("Products")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        productList.clear();
                        for (DataSnapshot ds: dataSnapshot.getChildren()){

                            String productCategory = ""+ds.child("productCategory").getValue();
                            if (selected.equals(productCategory)){
                                ModelProduct modelProduct = ds.getValue(ModelProduct.class);
                                productList.add(modelProduct);
                            }

                        }


                        adapterProductSeller = new AdapterProductSeller(MainSellerActivity.this,productList);

                        productsRv.setAdapter(adapterProductSeller);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {


                    }
                });

    }

    private void loadAllProducts() {

        productList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance("https://instacart-fab15-default-rtdb.firebaseio.com/").getReference("Users");
        reference.child(firebaseAuth.getUid()).child("Products")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        productList.clear();
                        for (DataSnapshot ds: dataSnapshot.getChildren()){

                            ModelProduct modelProduct = ds.getValue(ModelProduct.class);
                            productList.add(modelProduct);
                        }


                        adapterProductSeller = new AdapterProductSeller(MainSellerActivity.this,productList);

                        productsRv.setAdapter(adapterProductSeller);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void showProductsUI() {


    }

    private void makeMeOffline() {

        progressDialog.setMessage("Logging out User...");

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("online","false");

        //update value to db
        DatabaseReference ref = FirebaseDatabase.getInstance("https://instacart-fab15-default-rtdb.firebaseio.com/").getReference("Users");
        ref.child(firebaseAuth.getUid()).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        firebaseAuth.signOut();
                        checkUser();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {


                        progressDialog.dismiss();
                        Toast.makeText(MainSellerActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });




    }

    private void checkUser() {

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user==null){
            startActivity(new Intent(MainSellerActivity.this, LoginActivity.class));
            finish();
        }
        else {
            loadMyInfo();
        }

    }

    private void loadMyInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance("https://instacart-fab15-default-rtdb.firebaseio.com/").getReference("Users");

        ref.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot ds: dataSnapshot.getChildren()){
                            String name = ""+ds.child("name").getValue();
                            String accountType = ""+ds.child("accountType").getValue();
                            String shopName = ""+ds.child("shopName").getValue();
                            String email = ""+ds.child("email").getValue();


                            nameTv.setText(name + " ("+accountType+")");
                            shopNameTv.setText(shopName);
                            emailTv.setText(email);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}