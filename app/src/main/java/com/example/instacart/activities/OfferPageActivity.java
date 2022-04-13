package com.example.instacart.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.instacart.R;

public class OfferPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_page);

        getSupportActionBar().hide();



    }
}