package com.example.finalproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Search extends AppCompatActivity implements OnSuccessListener<Location>{
    private final int REQ_CODE = 100;
    private FusedLocationProviderClient fusedLocationClient;

    private Button searchButton;
    private ImageButton locationButton;
    private EditText locationEditText;
    private EditText nameEditText;
    //Volley request queue;
    private RequestQueue queue;
    private Context context;

    //LOCATION REQUEST CODE KEY, not important since only asking for a single permission and don't need to distinguish
    public static final int LOCATION_REQUEST_CODE = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchButton = findViewById(R.id.searchButton);
        locationButton = findViewById(R.id.locButton);
        locationEditText = findViewById(R.id.locationEditText);
        nameEditText = findViewById(R.id.nameEditText);
        context = getApplicationContext();

        //Instantiate the request queue
        queue = Volley.newRequestQueue(this);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_login:
                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.action_search:
                    Toast.makeText(Search.this, "Search", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.action_favorites:
                    Intent intent2 = new Intent(context, Favorites.class);
                    startActivity(intent2);
                    break;
            }
            return true;
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE: {
                if (resultCode == RESULT_OK && null != data){
                    ArrayList result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    locationEditText.setText((CharSequence) result.get(0));
                }
                break;
            }
        }
    }

    public void onSearchClick(View v){
        System.out.println("Go Button Clicked");
        String nameText = nameEditText.getText().toString();
        String locText = locationEditText.getText().toString();
        if(nameText.length() == 0 || locText.length() == 0){
            Toast.makeText(Search.this,"Please enter a name or location", Toast.LENGTH_SHORT).show();
        }else{
            getBusinessByName(nameText,locText);
        }

    }

    @SuppressLint("NewApi")
    public void onLocClick(View v){
        System.out.println("Location Button Clicked");

        //Ask for location permission
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            getLocation();
            System.out.println("PERMISSION ALREADY GRANTED FOR LOCATION, DOING ACTION");
        }else {
            //directly ask for the permission.
            requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION },LOCATION_REQUEST_CODE);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        System.out.println("onRequestPermissionsResult Callback Entered");

        //Check that permission was granted
        if (grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED){
            getLocation();
        }
    }

    @SuppressLint("MissingPermission")
    private void getLocation(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, this);
    }

    @Override
    public void onSuccess(Location location) {
        //Get Weather by Location
        String lat = String.valueOf(location.getLatitude());
        String lon = String.valueOf(location.getLongitude());
        String nameText = nameEditText.getText().toString();
        System.out.println("Lattitude = " + lat);
        System.out.println("Longitude = " + lon);
        getBusinessByLocation(lat,lon,nameText);

    }

    private void getBusinessByLocation(String lat, String lon, String name) {
        String url = getString(R.string.YAHOO_API_URL_TERM) + name + "&latitude=" + lat + "&longitude=" + lon;
    }

    private void getBusinessByName(String name, String loc) {
        String url = getString(R.string.YAHOO_API_URL_BASE) + name;
        System.out.println(url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject main = response.getJSONObject("main");
                            JSONObject coords = response.getJSONObject("coord");
                            JSONObject wind = response.getJSONObject("wind");

                        } catch (JSONException e) {
                            System.out.println("JSON EXPLOSION");
                        }


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("ERROR WITH VOLLEY REQUEST");

                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headerMap = new HashMap<String, String>();
                        headerMap.put("Authorization", "Bearer " + getString(R.string.YAHOO_API_KEY));
                        return headerMap;
                    }
        };

        queue.add(jsonObjectRequest);
    }
}