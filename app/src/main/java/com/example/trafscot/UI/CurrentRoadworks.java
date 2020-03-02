package com.example.trafscot.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.trafscot.Models.Event;
import com.example.trafscot.R;
import java.util.ArrayList;
import java.util.List;


public class CurrentRoadworks extends AppCompatActivity {
    //Data
    private List<Event> currentRoadworks;
    //UI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_roadworks);
        //Take data from intent + display
        currentRoadworks = this.getIntent().getExtras().getParcelableArrayList("currentRoadworks");
        ArrayAdapter adapter = new ArrayAdapter<Event>(this, R.layout.listview, currentRoadworks);
        final ListView listView = (ListView) findViewById(R.id.lstCurrentRoadWorks);
        listView.setAdapter(adapter);
        listView.setClickable(true);
    }
}
