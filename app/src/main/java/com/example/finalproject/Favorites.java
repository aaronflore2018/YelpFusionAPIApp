package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

public class Favorites extends AppCompatActivity {
    private String TAG;
    private Context context;
    private ListView listView;
    public static Business currentBusiness = new Business();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        fillListView();
        context = getApplicationContext();
        listView = findViewById(R.id.favorites_list);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

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
                    Toast.makeText(Favorites.this, "Search", Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        });
    }

    public void fillListView() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        ArrayList<String> list = new ArrayList<>();
        db.collection("users").document(user.getUid()).collection("favorites")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getData().get("name") != null){
                                    list.add(document.getData().get("name").toString());
                                }
                                ArrayAdapter listViewAdapter = new ArrayAdapter(context,android.R.layout.simple_list_item_1,list);
                                listView.setAdapter(listViewAdapter);
                                /*
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        System.out.println(document.getData().get("id"));
                                        if(document.getId().equals(document.getData().get("id"))){
                                            currentBusiness.setName(document.getData().get("name").toString());
                                            System.out.println(document.getData().get("name").toString());
                                            currentBusiness.setId(document.getId());
                                            System.out.println(document.getData().get("name").toString());
                                            currentBusiness.setPhone(document.getData().get("phone").toString());
                                            currentBusiness.setAddress(document.getData().get("address").toString());
                                            currentBusiness.setRating(document.getData().get("rating").toString());
                                            currentBusiness.setPrice(document.getData().get("price").toString());
                                            currentBusiness.setLat(document.getData().get("lat").toString());
                                            currentBusiness.setLon(document.getData().get("lon").toString());
                                            Intent intent = new Intent(context, Result.class);
                                            startActivity(intent);
                                        }
                                    }
                                });*/
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}