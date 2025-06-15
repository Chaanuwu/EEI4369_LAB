package com.s23010916.chanuka;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapIntegration extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap myMap;
    private EditText searchInput;
    private Button searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_map_integration);

        Button sensorBtn = findViewById(R.id.sensorBtn);
        sensorBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MapIntegration.this, SensorIntegration.class);
            startActivity(intent);
        });

        searchInput = findViewById(R.id.search);
        searchBtn = findViewById(R.id.searchBtn);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        searchBtn.setOnClickListener(v -> {
            String location = searchInput.getText().toString().trim();
            if (!location.isEmpty()) {
                Geocoder geocoder = new Geocoder(MapIntegration.this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocationName(location, 1);
                    if (!addresses.isEmpty()) {
                        Address address = addresses.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        myMap.clear();
                        myMap.addMarker(new MarkerOptions().position(latLng).title(location));
                        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                    } else {
                        Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Geocoder error", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please enter a location", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;
        LatLng loc = new LatLng(7.3378920474134315, 80.30029707102045);
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15.0f));
        myMap.getUiSettings().setZoomControlsEnabled(true);
    }
}
