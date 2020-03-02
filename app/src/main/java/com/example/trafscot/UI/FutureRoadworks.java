package com.example.trafscot.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.trafscot.Models.Event;
import com.example.trafscot.R;
import java.util.List;


public class FutureRoadworks extends AppCompatActivity {
    //Data
    private List<Event> futureRoadworks;
    //UI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_roadworks);
        //Take data from intent + display
        futureRoadworks = this.getIntent().getExtras().getParcelableArrayList("futureRoadworks");
        ArrayAdapter adapter = new ArrayAdapter<Event>(this, R.layout.listview, futureRoadworks);
        final ListView listView = (ListView) findViewById(R.id.lstCurrentRoadWorks);
        listView.setAdapter(adapter);
        listView.setClickable(true);
    }
}
