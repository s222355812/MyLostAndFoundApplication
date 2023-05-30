package com.example.mylostandfoundapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.io.IOException;

import android.location.Geocoder;
import android.location.Address;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private MapView mapView;
    private GoogleMap googleMap;
    private List<LostFoundItem> lostFoundItemList;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Retrieve all lost and found items from the database
        lostFoundItemList = databaseHelper.getAllLostFoundItems();

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        // Get the current location coordinates (latitude and longitude) passed from the previous activity
        double currentLatitude = getIntent().getDoubleExtra("latitude", 0.0);
        double currentLongitude = getIntent().getDoubleExtra("longitude", 0.0);

        // Create a LatLng object with the current location coordinates
        LatLng currentLocation = new LatLng(currentLatitude, currentLongitude);

        showLostFoundItemsOnMap(currentLocation);
    }


    private void showLostFoundItemsOnMap(LatLng currentLocation) {
        if (googleMap == null || lostFoundItemList == null) {
            return;
        }

        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses;

        for (LostFoundItem item : lostFoundItemList) {
            String itemLocation = item.getLocation();

            try {
                addresses = geocoder.getFromLocationName(itemLocation, 1);

                if (!addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    double latitude = address.getLatitude();
                    double longitude = address.getLongitude();

                    LatLng itemLatLng = new LatLng(latitude, longitude);
                    String itemName = item.getName();

                    // Customize the marker icon
                    BitmapDescriptor markerIcon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);

                    // Add marker to the map
                    googleMap.addMarker(new MarkerOptions()
                            .position(itemLatLng)
                            .title(itemName)
                            .icon(markerIcon));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Move the camera to the first lost and found item's location
        if (!lostFoundItemList.isEmpty()) {
            LostFoundItem firstItem = lostFoundItemList.get(0);
            String firstItemLocation = firstItem.getLocation();

            try {
                addresses = geocoder.getFromLocationName(firstItemLocation, 1);

                if (!addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    double latitude = address.getLatitude();
                    double longitude = address.getLongitude();

                    LatLng firstItemLatLng = new LatLng(latitude, longitude);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstItemLatLng, 12));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}

