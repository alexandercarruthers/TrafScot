package com.example.trafscot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.trafscot.Dao.EventDao;
import com.example.trafscot.Dao.EventDaoImpl;
import com.example.trafscot.Models.Event;
import com.example.trafscot.UI.CurrentIncidents;
import com.example.trafscot.UI.CurrentRoadworks;
import com.example.trafscot.UI.FutureRoadworks;
import com.example.trafscot.UI.RoadworksOnDate;
import com.example.trafscot.UI.Test;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //data
    List<Event> currentIncidents = new ArrayList<>();
    List<Event> currentRoadworks = new ArrayList<>();
    List<Event> futureRoadworks = new ArrayList<>();
    //ui
    private Button btnCurrentIncidents;
    private Button btnCurrentRoadworks;
    private Button btnFutureRoadworks;
    private Button btnRoadworksOnDate;
    private Button btnTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCurrentIncidents = (Button) findViewById(R.id.btnCurrentIncidents);
        btnCurrentIncidents.setOnClickListener(this);

        btnCurrentRoadworks = (Button) findViewById(R.id.btnCurrentRoadworks);
        btnCurrentRoadworks.setOnClickListener(this);

        btnFutureRoadworks = (Button) findViewById(R.id.btnFutureRoadworks);
        btnFutureRoadworks.setOnClickListener(this);

        btnRoadworksOnDate = (Button) findViewById(R.id.btnRoadworksOnDate);
        btnRoadworksOnDate.setOnClickListener(this);

        btnTest = (Button) findViewById(R.id.btnTest);
        btnTest.setOnClickListener(this);

        AsyncTaskExample asyncTask = new AsyncTaskExample();
        asyncTask.execute();
    }

    public void onClick(View view) {
        if (view == btnCurrentIncidents) {
            Intent intent = new Intent(this, CurrentIncidents.class);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("currentIncidents", (ArrayList<? extends Parcelable>) currentIncidents);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        if (view == btnCurrentRoadworks) {
            Intent intent = new Intent(this, CurrentRoadworks.class);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("currentRoadworks", (ArrayList<? extends Parcelable>) currentRoadworks);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        if (view == btnFutureRoadworks){
            Intent intent = new Intent(this, FutureRoadworks.class);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("futureRoadworks", (ArrayList<? extends Parcelable>) futureRoadworks);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        if (view == btnRoadworksOnDate){
            Intent intent = new Intent(this, RoadworksOnDate.class);
            startActivity(intent);
        }
        if (view == btnTest){
            Intent intent = new Intent(this, Test.class);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("currentRoadworks", (ArrayList<? extends Parcelable>) currentRoadworks);
            intent.putExtras(bundle);
            startActivity(intent);
        }
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
            TextView breakingNews = (TextView) findViewById(R.id.salutation);
            breakingNews.setText("");
            for (Event event : currentRoadworks) {
                //breakingNews.append(event.toString());
                //breakingNews.append("\n");
            }
        }

    }
}
