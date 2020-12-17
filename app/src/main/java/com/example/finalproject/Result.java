package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Result extends AppCompatActivity {

    private TextView nameText;
    private String id;
    private TextView phoneText;
    private TextView addressText;
    private TextView ratingText;
    private TextView priceText;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Business b = Search.getBusinessInstance();
        context = getApplicationContext();
        nameText = findViewById(R.id.business_name_edit);
        phoneText = findViewById(R.id.business_phone_edit);
        addressText = findViewById(R.id.business_address_edit);
        ratingText = findViewById(R.id.business_rating_edit);
        priceText = findViewById(R.id.business_price_edit);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        nameText.setText(b.getName());
        phoneText.setText(b.getPhone());
        addressText.setText(b.getAddress());
        ratingText.setText(b.getRating());
        priceText.setText(b.getPrice());

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_login:
                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.action_search:
                    Intent intent2 = new Intent(context, Search.class);
                    startActivity(intent2);
                    break;
                case R.id.action_favorites:
                    Intent intent3 = new Intent(context, Favorites.class);
                    startActivity(intent3);
                    break;
            }
            return true;
        });
    }


}