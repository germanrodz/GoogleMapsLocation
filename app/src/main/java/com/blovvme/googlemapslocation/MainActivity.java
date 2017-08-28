package com.blovvme.googlemapslocation;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blovvme.googlemapslocation.model.AddressResponse;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "Location";
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 9;
    FusedLocationProviderClient fusedLocationProviderClient;

    public static final String GEO_KEY = "https://maps.googleapis.com/maps/api/geocode/json?latlng=40.714224,-73.961452&key=AIzaSyCNZY9k_guP19amz0OUMhvBWDZF-WL1diM";
//          +  "AIzaSyCNZY9k_guP19amz0OUMhvBWDZF-WL1diM";
//public static final String GEO_KEY ="AIzaSyCNZY9k_guP19amz0OUMhvBWDZF-WL1diM";

    public static final String BASE_URL = "https://maps.googleapis.com/" +
            "maps/api/geocode/json" +
            "?latlng=40.714224,-73.961452&key=YOUR_API_KEY";

    Button btnLocation;
    TextView tvLatitude, tvLongitude;
    Location currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPermission();

        btnLocation = (Button) findViewById(R.id.btnLocation);
        tvLatitude = (TextView) findViewById(R.id.tvLatitude);
        tvLongitude = (TextView) findViewById(R.id.tvLongitude);

        // getGeocodeAddress();

    }


    private void getPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // getLocation();
        }
    }

    public void getLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        //Log.d(TAG, "onSuccess: " + location.toString());
                        currentLocation = location;
                        tvLatitude.setText(location.getLatitude() + "");
                        tvLongitude.setText(location.getLongitude() + "");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: ");
                    }
                });

    }

    //    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    //getLocation();
//                    // permission was granted, yay! Do the
//                    // contacts-related task you need to do.
//
//                } else {
//
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//                }
//                return;
//            }
//
//            // other 'case' lines to check for other
//            // permissions this app might request
//        }
//    }
//
    public void onLocation(View view) {
        getLocation();
    }


    public void onPassToOtherActivity(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("location", currentLocation);
        startActivity(intent);
    }

//    public void getGeocodeAddress() {
//        String currentLatLng = currentLocation.getLatitude() + "," + currentLocation.getLongitude();
//        OkHttpClient client = new OkHttpClient();
//        HttpUrl url = new HttpUrl.Builder()
//                .scheme("https")
//                .host("maps.googleapis.com")
//                .addPathSegment("maps")
//                .addPathSegment("api")
//                .addPathSegment("geocode")
//                .addPathSegment("json")
//                .addQueryParameter("latlng", currentLatLng)
//                .addQueryParameter("key", GEO_KEY)
//                .build();
//        Request request = new Request.Builder()
//                .url(url)
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.d(TAG, "onFailure: " + e.toString());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                Gson gson = new Gson();
//                AddressResponse addressResponse = gson.fromJson(response.body().string(), AddressResponse.class);
//                Log.d(TAG, "onResponse: " + addressResponse.getResults().get(0).getFormattedAddress());
//            }
//        });
//
//    }

    public void onGeolocation(View view) {
        //getGeocodeAddress();
        String currentLatLng = currentLocation.getLatitude() + "," + currentLocation.getLongitude();
        OkHttpClient client = new OkHttpClient();
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                //.host("maps.googleapis.com")
                .host("https://maps.googleapis.com")
                .addPathSegment("maps")
                .addPathSegment("api")
                .addPathSegment("geocode")
                .addPathSegment("json")
                .addQueryParameter("latlng", currentLatLng)
                .addQueryParameter("key", GEO_KEY)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: " + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                AddressResponse addressResponse = gson.fromJson(response.body().string(), AddressResponse.class);
                Log.d(TAG, "onResponse: " + addressResponse.getResults().get(0).getFormattedAddress());
            }
        });
    }
}
