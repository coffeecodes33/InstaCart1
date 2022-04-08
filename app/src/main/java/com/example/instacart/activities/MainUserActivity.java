package com.example.instacart.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.instacart.R;
import com.example.instacart.adapters.AdapterShop;
import com.example.instacart.models.ModelShop;
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

public class MainUserActivity extends AppCompatActivity {

    private TextView nameTv,emailTv,tabShopsTv,phoneTv;
    private ImageButton logoutBtn;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private RelativeLayout shopsRl;
    private RecyclerView shopsRv;

    private ArrayList<ModelShop> shopsList;
    private AdapterShop adapterShop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);
        getSupportActionBar().hide();

        nameTv = findViewById(R.id.nameTv);
        emailTv = findViewById(R.id.emailTv);
        phoneTv = findViewById(R.id.phoneTv);
        tabShopsTv = findViewById(R.id.tabShopsTv);
        logoutBtn = findViewById(R.id.logoutBtn);
        shopsRl = findViewById(R.id.shopsRl);
        shopsRv = findViewById(R.id.shopsRv);


        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait..");
        progressDialog.setCanceledOnTouchOutside(false);



        checkUser();
        showShopsUI();

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeMeOffline();

            }
        });
    }

    private void showShopsUI() {


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
                        Toast.makeText(MainUserActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void checkUser() {

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user==null){
            startActivity(new Intent(MainUserActivity.this, LoginActivity.class));
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
                            String email = ""+ds.child("email").getValue();
                            String phone = ""+ds.child("phone").getValue();
                            String city = ""+ds.child("city").getValue();
                            String accountType = ""+ds.child("accountType").getValue();


                            nameTv.setText(name + " ("+accountType+")");
                            emailTv.setText(email);
                            phoneTv.setText(phone);

                            loadShops(city);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void loadShops(String myCity) {

        shopsList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance("https://instacart-fab15-default-rtdb.firebaseio.com/").getReference("Users");
        ref.orderByChild("accountType").equalTo("Seller")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        shopsList.clear();
                        for (DataSnapshot ds: dataSnapshot.getChildren()){
                            ModelShop modelShop = ds.getValue(ModelShop.class);

                            String  shopCity = ""+ds.child("city").getValue();

                            if (shopCity.equals(myCity)){
                                shopsList.add(modelShop);
                            }

                            //shopsList.add(modelShop);
                        }

                        adapterShop = new AdapterShop(MainUserActivity.this,shopsList);

                        shopsRv.setAdapter(adapterShop);




                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}