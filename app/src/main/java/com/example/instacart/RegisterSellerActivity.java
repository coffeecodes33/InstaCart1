package com.example.instacart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterSellerActivity extends AppCompatActivity {

    private EditText nameEt,shopnameEt,emailEt,phoneEt,passwordEt,cpasswordEt,deliveryFeeEt
            ,countryEt,cityEt,stateEt,addressEt;

    private Button registerBtn;


    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_seller);

        getSupportActionBar().hide();

        nameEt = findViewById(R.id.nameEt);
        shopnameEt = findViewById(R.id.shopnameEt);
        emailEt = findViewById(R.id.emailEt);
        phoneEt = findViewById(R.id.phoneEt);
        deliveryFeeEt = findViewById(R.id.deliveryFeeEt);
        passwordEt = findViewById(R.id.passwordEt);
        cpasswordEt = findViewById(R.id.cpasswordEt);
        countryEt = findViewById(R.id.countryEt);
        cityEt = findViewById(R.id.cityEt);
        stateEt = findViewById(R.id.stateEt);
        addressEt = findViewById(R.id.addressEt);
        registerBtn = findViewById(R.id.registerBtn);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 //register seller
                inputData();

            }
        });

    }

    private  String fullName,shopName,phoneNumber,deliveryFee,
    country,state,city,address,email,password,confirmPassword;

    private void inputData() {

        fullName = nameEt.getText().toString().trim();
        shopName = shopnameEt.getText().toString().trim();
        phoneNumber = phoneEt.getText().toString().trim();
        deliveryFee = deliveryFeeEt.getText().toString().trim();
        country = countryEt.getText().toString().trim();
        state = stateEt.getText().toString().trim();
        city = cityEt.getText().toString().trim();
        address = addressEt.getText().toString().trim();
        email = emailEt.getText().toString().trim();
        password = passwordEt.getText().toString().trim();
        confirmPassword = cpasswordEt.getText().toString().trim();

        //validate data
        if(TextUtils.isEmpty(fullName)){
            Toast.makeText(this,"Enter Name...",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(shopName)){
            Toast.makeText(this,"Enter ShopName...",Toast.LENGTH_SHORT).show();
            return;
        }


        if(TextUtils.isEmpty(phoneNumber)){
            Toast.makeText(this,"Enter PhoneNumber...",Toast.LENGTH_SHORT).show();
            return;
        }


        if(TextUtils.isEmpty(deliveryFee)){
            Toast.makeText(this,"Enter DeliveryFee...",Toast.LENGTH_SHORT).show();
            return;
        }


        if(TextUtils.isEmpty(city)){
            Toast.makeText(this,"Enter city...",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(country)){
            Toast.makeText(this,"Enter country...",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(state)){
            Toast.makeText(this,"Enter state...",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(address)){
            Toast.makeText(this,"Enter Address...",Toast.LENGTH_SHORT).show();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this,"Invalid email pattern...",Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.length()<6){
            Toast.makeText(this,"Password must be atleast 6 characters...",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!password.equals(confirmPassword)){
            Toast.makeText(this,"Password and Confirm Password must be same...",Toast.LENGTH_SHORT).show();
            return;
        }


        createAccount();

    }

    private void createAccount() {

        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        //create account

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        //account created successfully

                        saveFirebaseData();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        //failed creating account

                        progressDialog.dismiss();
                        Toast.makeText(RegisterSellerActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private void saveFirebaseData() {

        progressDialog.setMessage("Saving Account Info...");

        final String timestamp = ""+System.currentTimeMillis();
        HashMap<String,Object> hashMap = new HashMap<>();

        hashMap.put("uid",""+firebaseAuth.getUid());
        hashMap.put("email",""+email);
        hashMap.put("password",""+password);
        hashMap.put("name",""+fullName);
        hashMap.put("shopName",""+shopName);
        hashMap.put("phone",""+phoneNumber);
        hashMap.put("deliveryFee",""+deliveryFee);
        hashMap.put("country",""+country);
        hashMap.put("state",""+state);
        hashMap.put("city",""+city);
        hashMap.put("address",""+address);
        hashMap.put("timestamp",""+timestamp);
        hashMap.put("accountType","Seller");
        hashMap.put("online","true");
        hashMap.put("shopOpen","true");

        DatabaseReference ref = FirebaseDatabase.getInstance("https://instacart-fab15-default-rtdb.firebaseio.com/").getReference("Users");
        ref.child(firebaseAuth.getUid()).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        progressDialog.dismiss();
                        startActivity(new Intent(RegisterSellerActivity.this,MainSellerActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                progressDialog.dismiss();
                startActivity(new Intent(RegisterSellerActivity.this,MainSellerActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                progressDialog.dismiss();
                Toast.makeText(RegisterSellerActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });



    }
}