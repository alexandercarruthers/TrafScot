package com.example.trafscot.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.trafscot.Models.Event;
import com.example.trafscot.R;
import java.util.List;


public class CurrentIncidents extends AppCompatActivity {
    //Data
    private List<Event> currentIncidents;
    //UI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_incidents);
        //Take data from intent + display
        currentIncidents = this.getIntent().getExtras().getParcelableArrayList("currentIncidents");
        ArrayAdapter adapter = new ArrayAdapter<Event>(this, R.layout.listview, currentIncidents);
        final ListView listView = (ListView) findViewById(R.id.lstCurrentIncidents);
        listView.setAdapter(adapter);
        listView.setClickable(true);
    }
}
