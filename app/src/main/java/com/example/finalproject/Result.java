package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Result extends AppCompatActivity {

    private TextView nameText;
    private String id;
    private String lat;
    private String lon;
    private TextView phoneText;
    private TextView addressText;
    private TextView ratingText;
    private TextView priceText;
    private Context context;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Business b = Search.getBusinessInstance();
        context = getApplicationContext();
        getSupportActionBar().hide();
        nameText = findViewById(R.id.business_name_edit);
        phoneText = findViewById(R.id.business_phone_edit);
        addressText = findViewById(R.id.business_address_edit);
        ratingText = findViewById(R.id.business_rating_edit);
        priceText = findViewById(R.id.business_price_edit);
        button = findViewById(R.id.favorite_button);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        nameText.setText(b.getName());
        id = b.getId();
        phoneText.setText(b.getPhone());
        addressText.setText(b.getAddress());
        ratingText.setText(b.getRating());
        priceText.setText(b.getPrice());
        lat = b.getLat();
        lon = b.getLon();


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

    public void onFavoriteClick(View v){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (user != null) {
            Map<String, Object> favorites = new HashMap<>();
            favorites.put("name",nameText.getText());
            favorites.put("phone",phoneText.getText());
            favorites.put("address",addressText.getText());
            favorites.put("rating",ratingText.getText());
            favorites.put("price",priceText.getText());
            favorites.put("lat",lat);
            favorites.put("lon",lon);
            db.collection("users").document(user.getUid()).collection("favorites").document(id).set(favorites);
            Toast.makeText(Result.this,"Added to Favorites!", Toast.LENGTH_SHORT).show();
        }
    }

}