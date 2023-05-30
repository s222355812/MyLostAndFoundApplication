package com.example.mylostandfoundapplication;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CreateAdvertActivity extends AppCompatActivity implements LocationListener {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final long MIN_TIME = 10000; // Minimum time interval for location updates (in milliseconds)
    private static final float MIN_DISTANCE = 10; // Minimum distance for location updates (in meters)

    private EditText etName;
    private EditText etPhone;
    private EditText etDescription;
    private EditText etDate;
    private EditText etLocation;
    private Button btnSubmit;
    private Button btnGetCurrentLocation;

    private DatabaseHelper databaseHelper;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advert);

        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etDescription = findViewById(R.id.etDescription);
        etDate = findViewById(R.id.etDate);
        etLocation = findViewById(R.id.etLocation);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnGetCurrentLocation = findViewById(R.id.btnGetCurrentLocation);

        databaseHelper = new DatabaseHelper(this);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String phone = etPhone.getText().toString();
                String description = etDescription.getText().toString();
                String date = etDate.getText().toString();
                String location = etLocation.getText().toString();

                if (name.isEmpty() || phone.isEmpty() || description.isEmpty() || date.isEmpty() || location.isEmpty()) {
                    Toast.makeText(CreateAdvertActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    insertAdvert(name, phone, description, date, location);
                }
            }
        });

        btnGetCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation();
            }
        });
    }

    private void insertAdvert(String name, String phone, String description, String date, String location) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("phone", phone);
        values.put("description", description);
        values.put("date", date);
        values.put("location", location);

        long newRowId = db.insert("lost_found_items", null, values);

        if (newRowId != -1) {
            Toast.makeText(this, "Advert created successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to create advert", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    private void getCurrentLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Check if the app has permission to access fine location
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Request location updates
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
        } else {
            // Request permission to access fine location
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, get current location
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Permission denied. Unable to get current location.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        // Get the latitude and longitude of the current location
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        // Convert latitude and longitude to a readable address
        String address = getAddressFromLocation(latitude, longitude);

        // Update the location EditText with the address
        etLocation.setText(address);

        // Stop receiving location updates
        locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // Unused method
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        // Unused method
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        // Unused method
    }

    @Nullable
    private String getAddressFromLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String address = null;

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (!addresses.isEmpty()) {
                Address fetchedAddress = addresses.get(0);
                StringBuilder addressBuilder = new StringBuilder();

                for (int i = 0; i <= fetchedAddress.getMaxAddressLineIndex(); i++) {
                    addressBuilder.append(fetchedAddress.getAddressLine(i));
                    if (i < fetchedAddress.getMaxAddressLineIndex()) {
                        addressBuilder.append(", ");
                    }
                }

                address = addressBuilder.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return address;
    }
}
