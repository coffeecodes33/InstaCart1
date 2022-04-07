package com.example.instacart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
       getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        firebaseAuth = FirebaseAuth.getInstance();

        Thread thread = new Thread(){

            public void run(){

                try{
                    sleep( 4000);

                }
                catch(Exception e){
                    e.printStackTrace();
                }
                finally{
//                    Intent intent = new Intent( SplashActivity.this, LoginActivity.class);
//                    startActivity(intent);
//                    finish();

                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user==null){
                        //user not logged in
                        startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                        finish();

                    }else{
                        //user logged in

                        checkUserType();
                    }
                }

            }

        }; thread.start();


    }

    private void checkUserType() {

        DatabaseReference ref = FirebaseDatabase.getInstance("https://instacart-fab15-default-rtdb.firebaseio.com/").getReference("Users");
        ref.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot ds: dataSnapshot.getChildren()){
                            String accountType = ""+ds.child("accountType").getValue();
                            if(accountType.equals("Seller")){


                                startActivity(new Intent(SplashActivity.this,MainSellerActivity.class));
                                finish();

                            }else{



                                startActivity(new Intent(SplashActivity.this,MainUserActivity.class));
                                finish();

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
