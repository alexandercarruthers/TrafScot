package com.example.trafscot.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.trafscot.Dao.EventDao;
import com.example.trafscot.Dao.EventDaoImpl;
import com.example.trafscot.MainActivity;
import com.example.trafscot.Models.Event;
import com.example.trafscot.R;
import com.example.trafscot.Service.EventsAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public class RoadworksOnDate extends AppCompatActivity {
    DatePicker picker;
    Button btnGet;

    //data
    private List<Event> currentIncidents;
    private List<Event> currentRoadworks;
    private List<Event> futureRoadworks;

    private List<Event> allRoadworks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roadworks_on_date);

        AsyncTaskExample asyncTask = new AsyncTaskExample();
        asyncTask.execute();


        picker=(DatePicker)findViewById(R.id.datePicker1);
        btnGet=(Button)findViewById(R.id.button1);

//        currentIncidents = this.getIntent().getExtras().getParcelableArrayList("currentIncidents");
//        currentRoadworks = this.getIntent().getExtras().getParcelableArrayList("currentRoadworks");
//        futureRoadworks = this.getIntent().getExtras().getParcelableArrayList("futureRoadworks");


        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strDate = picker.getDayOfMonth()+"/"+ (picker.getMonth() + 1)+"/"+picker.getYear();
                Date chosenDate = getDate(strDate);
                List<Event> filteredEvents = getFilteredEventList(chosenDate, allRoadworks);
                displayFilteredEventList(filteredEvents);
            }
        });
    }
    private Date getDate(String strDate){
        SimpleDateFormat format = new SimpleDateFormat("d/M/yy");
        Date date = null;
        try {
            date = format.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    private List<Event> getFilteredEventList(Date date, List<Event> allEvents){
        EventDao eventDao = new EventDaoImpl();
        List<Event> filteredEvents = eventDao.getRoadworksOnDate(date,allEvents);
        return filteredEvents;
    }
    private void displayFilteredEventList(List<Event> filteredEvents){
        EventsAdapter adapter1 = new EventsAdapter(this, filteredEvents);
        ListView listView = (ListView) findViewById(R.id.lstFilteredEvents);
        listView.setAdapter(adapter1);
        picker.setVisibility(View.GONE);
    }

    private class AsyncTaskExample extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            EventDao eventDao = new EventDaoImpl();
            currentIncidents = eventDao.getAllCurrentIncidents();
            currentRoadworks = eventDao.getAllCurrentRoadworks();
            futureRoadworks = eventDao.getAllFutureRoadworks();
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            allRoadworks = new ArrayList<>();

            for(Event e : currentIncidents){
                allRoadworks.add(e);
            }
            for(Event e : currentRoadworks){
                allRoadworks.add(e);
            }
        }

    }
}
