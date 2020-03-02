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
import com.example.trafscot.UI.CurrentRoadworks;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //data
    List<Event> currentRoadworks = new ArrayList<>();
    //ui
    private Button btnCurrentRoadworks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCurrentRoadworks = (Button) findViewById(R.id.btnCurrentRoadworks);
        btnCurrentRoadworks.setOnClickListener(this);
        AsyncTaskExample asyncTask = new AsyncTaskExample();
        asyncTask.execute();
    }

    public void onClick(View view) {
        if (view == btnCurrentRoadworks) {
            Intent intent = new Intent(this, CurrentRoadworks.class);
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
            currentRoadworks = eventDao.getAllCurrentRoadworks();
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            TextView breakingNews = (TextView) findViewById(R.id.salutation);
            for (Event event : currentRoadworks) {
                breakingNews.append(event.toString());
                breakingNews.append("\n");
            }
        }

    }
}
