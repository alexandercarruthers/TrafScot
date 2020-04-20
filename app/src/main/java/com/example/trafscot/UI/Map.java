package com.example.trafscot.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.trafscot.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Map extends AppCompatActivity  implements OnMapReadyCallback {
    private LatLng coordinates;
    private String marker_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Intent intent = getIntent();
        marker_title = intent.getStringExtra("marker_title");
        String start_date = intent.getStringExtra("start_date");
        String end_date = intent.getStringExtra("end_date");
        String days_of_works = intent.getStringExtra("days_of_works");
        String string_x = intent.getStringExtra("x");
        String string_y = intent.getStringExtra("y");
        double x = Double.parseDouble(string_x);
        double y = Double.parseDouble(string_y);
        coordinates = new LatLng(x, y);
        populateMapDetailsTextView(start_date,days_of_works,end_date);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void populateMapDetailsTextView(String start_date, String days_of_works, String end_date){
        TextView MapDetails = (TextView)findViewById(R.id.MapDetails);
        MapDetails.append(marker_title);
        MapDetails.append(System.getProperty("line.separator"));
        MapDetails.append(start_date);
        MapDetails.append(System.getProperty("line.separator"));
        MapDetails.append(days_of_works);
        MapDetails.append(System.getProperty("line.separator"));
        MapDetails.append(end_date);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinates, 11);
        googleMap.addMarker(new MarkerOptions().position(coordinates).title(marker_title));
        googleMap.animateCamera(yourLocation);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
    }
}
