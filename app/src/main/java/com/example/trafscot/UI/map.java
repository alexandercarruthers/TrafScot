package com.example.trafscot.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.trafscot.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class map extends AppCompatActivity  implements OnMapReadyCallback {

    private double x;
    private double y;
    private String marker_title;
    private String start_date;
    private String end_date;

    private TextView MapDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        marker_title = intent.getStringExtra("marker_title"); //if it's a string you stored.
        start_date = intent.getStringExtra("start_date"); //if it's a string you stored.
        end_date = intent.getStringExtra("end_date"); //if it's a string you stored.
        String string_x = intent.getStringExtra("x"); //if it's a string you stored.
        String string_y = intent.getStringExtra("y"); //if it's a string you stored.
        x = Double.parseDouble(string_x);
        y = Double.parseDouble(string_y);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);
        MapDetails = (TextView)findViewById(R.id.MapDetails);
        MapDetails.append(marker_title);
        MapDetails.append(" Start date: " + start_date);
        MapDetails.append(" End date: " + end_date);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        LatLng sydney = new LatLng(x, y);
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title(marker_title));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        googleMap.animateCamera( CameraUpdateFactory.zoomTo( 14.0f ) );
    }
}
