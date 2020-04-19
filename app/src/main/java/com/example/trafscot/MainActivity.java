package com.example.trafscot;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trafscot.Dao.EventDao;
import com.example.trafscot.Dao.EventDaoImpl;
import com.example.trafscot.Models.ChildItemsInfo;
import com.example.trafscot.Models.Event;
import com.example.trafscot.Models.GroupItemsInfo;
import com.example.trafscot.Service.MyExpandableListAdapter;
import com.example.trafscot.UI.map;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //current selected
    List<Event> currentlySelected = new ArrayList<>();
    //data
    List<Event> currentIncidents = new ArrayList<>();
    List<Event> currentRoadworks = new ArrayList<>();
    List<Event> futureRoadworks = new ArrayList<>();
    //ui
    private Button btnClear;
    private Button btnGetDate;
    private Button btnFilterRoad;
    private Button btnViewMap;
    private LinkedHashMap<String, GroupItemsInfo> songsList = new LinkedHashMap<String, GroupItemsInfo>();
    private ArrayList<GroupItemsInfo> deptList = new ArrayList<GroupItemsInfo>();

    private MyExpandableListAdapter myExpandableListAdapter;
    private ExpandableListView simpleExpandableListView;

    private ArrayList<GroupItemsInfo> emptyTest = new ArrayList<GroupItemsInfo>();

    DatePickerDialog datePickerDialog;
    Date datePicked;

    private TextView txtFilterDate;
    private TextView filterDate;
    private TextView records;

    private Spinner trunkRoadSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);

        btnGetDate = (Button) findViewById(R.id.btnGetDate);
        btnGetDate.setOnClickListener(this);

        btnFilterRoad = (Button) findViewById(R.id.btnFilterRoad);
        btnFilterRoad.setOnClickListener(this);

        txtFilterDate = (EditText)findViewById(R.id.txtFilterDate);
        filterDate = (TextView)findViewById(R.id.filterDate);

        records = (TextView)findViewById(R.id.records);
        txtFilterDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, monthOfYear, dayOfMonth);
                                datePicked = calendar.getTime();
                                DateFormat dateFormat = new SimpleDateFormat("d-M-y");
                                String strDate = dateFormat.format(datePicked);
                                txtFilterDate.setText(strDate);
//                                List<Event>  filteredRoadworks = getFilteredEventList(datePicked,currentlySelected);
//                                currentlySelected.clear();
//                                currentlySelected.addAll(filteredRoadworks);
//                                txtFilterDate.setText("Date: " + strDate);
//                                deptList.clear();
//                                songsList.clear();
//                                clearOnlyListView();
//                                loadData(filteredRoadworks);
//                                setDataExpandingListView();
//                                populateTrunkRoadSpinner(currentlySelected);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();


            }
        });
        RadioButton rdb1 = (RadioButton) findViewById(R.id.radioCurrentIncidents);
        rdb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((RadioButton) v).isChecked();
                // Check which radiobutton was pressed
                if (checked){
                    currentlySelected.addAll(currentIncidents);
                    populateTrunkRoadSpinner(currentlySelected);
                    clearExpandingListView();
                    loadData(currentIncidents);
                    setDataExpandingListView();
                }
                else{
                    // Do your coding
                }
            }
        });

        RadioButton rdb2 = (RadioButton) findViewById(R.id.radioCurrentRoadworks);
        rdb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((RadioButton) v).isChecked();
                // Check which radiobutton was pressed
                if (checked){
                    //getAllTrunkRoad(currentRoadworks);
                    clearExpandingListView();
                    currentlySelected.addAll(currentRoadworks);
                    populateTrunkRoadSpinner(currentlySelected);
                    loadData(currentlySelected);
                    setDataExpandingListView();
                }
                else{
                    // Do your coding
                }
            }
        });

        RadioButton rdb3 = (RadioButton) findViewById(R.id.radioFutureRoadworks);
        rdb3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((RadioButton) v).isChecked();
                // Check which radiobutton was pressed
                if (checked){
                    clearExpandingListView();
                    currentlySelected.addAll(futureRoadworks);
                    populateTrunkRoadSpinner(currentlySelected);
                    loadData(currentlySelected);
                    setDataExpandingListView();
                }
                else{
                    // Do your coding
                }
            }
        });

        trunkRoadSpinner = (Spinner) findViewById(R.id.trunkRoadSpinner);

//        trunkRoadSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
//        {
//            @Override
//            public void onItemSelected(AdapterView adapter, View v, int i, long lng) {
//
////                //String filterOnTruckRoad =  adapter.getItemAtPosition(i).toString();
////                //or this can be also right: selecteditem = level[i];
//                String filterOnTruckRoad = String.valueOf(trunkRoadSpinner.getSelectedItem());
//                List<Event>  filteredRoadworks = getTrunkRoadFilteredEventList(filterOnTruckRoad,currentlySelected);
//                currentlySelected.clear();
//                currentlySelected.addAll(filteredRoadworks);
//                deptList.clear();
//                songsList.clear();
//                clearOnlyListView();
//                loadData(filteredRoadworks);
//                setDataExpandingListView();
//
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView)
//            {
//
//            }
//        });

        AsyncTaskExample asyncTask = new AsyncTaskExample();
        asyncTask.execute();
    }


    public void onClick(View view) {
        if (view == btnClear){
            currentlySelected.clear();
            populateTrunkRoadSpinner(currentlySelected);
            clearExpandingListView();
        }
        if (view == btnGetDate){
            String string_date_picked = txtFilterDate.getText().toString();
            Date datePicked = null;
            try {
                datePicked = new SimpleDateFormat("d-M-y").parse(string_date_picked);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            List<Event>  filteredRoadworks = getFilteredEventList(datePicked,currentlySelected);
            currentlySelected.clear();
            currentlySelected.addAll(filteredRoadworks);
            deptList.clear();
            songsList.clear();
            clearOnlyListView();
            loadData(filteredRoadworks);
            setDataExpandingListView();
            populateTrunkRoadSpinner(currentlySelected);
//            final Calendar c = Calendar.getInstance();
//            int mYear = c.get(Calendar.YEAR); // current year
//            int mMonth = c.get(Calendar.MONTH); // current month
//            int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
//            // date picker dialog
//            datePickerDialog = new DatePickerDialog(MainActivity.this,
//                    new DatePickerDialog.OnDateSetListener() {
//                        @Override
//                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                            Calendar calendar = Calendar.getInstance();
//                            calendar.set(year, monthOfYear, dayOfMonth);
//                            datePicked = calendar.getTime();
//                            DateFormat dateFormat = new SimpleDateFormat("d-M-y");
//                            String strDate = dateFormat.format(datePicked);
//                            List<Event>  filteredRoadworks = getFilteredEventList(datePicked,currentlySelected);
//                            currentlySelected.clear();
//                            currentlySelected.addAll(filteredRoadworks);
//                            txtFilterDate.setText("Date: " + strDate);
//                            deptList.clear();
//                            songsList.clear();
//                            clearOnlyListView();
//                            loadData(filteredRoadworks);
//                            setDataExpandingListView();
//                            populateTrunkRoadSpinner(currentlySelected);
//                        }
//                    }, mYear, mMonth, mDay);
//            datePickerDialog.show();
        }
        if (view == btnFilterRoad){
            String filterOnTruckRoad = String.valueOf(trunkRoadSpinner.getSelectedItem());
            List<Event>  filteredRoadworks = getTrunkRoadFilteredEventList(String.valueOf(trunkRoadSpinner.getSelectedItem()),currentlySelected);
            currentlySelected.clear();
            currentlySelected.addAll(filteredRoadworks);
            deptList.clear();
            songsList.clear();
            clearOnlyListView();
            loadData(filteredRoadworks);
            setDataExpandingListView();
            populateTrunkRoadSpinner(currentlySelected);

        }
        if (view == btnViewMap){
            Intent myIntent = new Intent(MainActivity.this, map.class);
            myIntent.putExtra("x", 2); //Optional parameters
            myIntent.putExtra("y", 2); //Optional parameters
            MainActivity.this.startActivity(myIntent);
        }
    }

    // Function to remove duplicates from an ArrayList
    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list)
    {
        ArrayList<T> newList = new ArrayList<T>(); // Create a new ArrayList
        for (T element : list) {// Traverse through the first list
            if (!newList.contains(element)) {// If this element is not present in newList then add it
                newList.add(element);
            }
        }
        return newList;// return the new list
    }

    private void populateTrunkRoadSpinner(List<Event> events){
        ArrayList<String> trunkRoads = new ArrayList<>();
        for(Event event : events){
            trunkRoads.add(event.getTrunkRoad());
        }
        List<String> setOfTrunkRoads = removeDuplicates(trunkRoads);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, setOfTrunkRoads);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        trunkRoadSpinner.setAdapter(dataAdapter);
    }

    private List<Event> getTrunkRoadFilteredEventList(String trunkRoad, List<Event> eventsToFilter){
        EventDao eventDao = new EventDaoImpl();
        List<Event> filteredEvents = eventDao.getMotorwayEvents(trunkRoad,eventsToFilter);
        return filteredEvents;
    }
    private List<Event> getFilteredEventList(Date date, List<Event> eventToFilter){
        EventDao eventDao = new EventDaoImpl();
        List<Event> filteredEvents = eventDao.getRoadworksOnDate(date,eventToFilter);
        return filteredEvents;
    }

    private void setDataExpandingListView(){
        // create the adapter by passing your ArrayList data
        myExpandableListAdapter = new MyExpandableListAdapter(this, emptyTest);
        simpleExpandableListView.setAdapter(myExpandableListAdapter);
        myExpandableListAdapter = new MyExpandableListAdapter(this, deptList);
        // attach the adapter to the expandable list view
        simpleExpandableListView.setAdapter(myExpandableListAdapter);
        // setOnChildClickListener listener for child row click or song name click
        //use this to add event handling for clickig on start date, end date etc
        simpleExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                GroupItemsInfo headerInfo = deptList.get(groupPosition);
                ChildItemsInfo detailInfo = headerInfo.getSongName().get(childPosition);
                if (detailInfo.getName().startsWith("Location:")){
                    String segments[] = detailInfo.getName().split(":");
                    String xy[] =  segments[1].split(",");
                    String x = xy[0];
                    String y = xy[1];
                    String title = headerInfo.getName();
                    String start_date = "";
                    String end_date = "";
                    ArrayList<ChildItemsInfo> innerList = headerInfo.getSongName();
                    for(ChildItemsInfo item : innerList){
                        String inner_list_start_date = item.getName();
                        if(inner_list_start_date.startsWith("Start Date:")){
                            start_date = inner_list_start_date;
                        }
                        if(inner_list_start_date.startsWith("End Date:")){
                            end_date = inner_list_start_date;
                        }
                    }
                    Intent myIntent = new Intent(MainActivity.this, map.class);
                    myIntent.putExtra("marker_title",title);
                    myIntent.putExtra("start_date", start_date); //Optional parameters
                    myIntent.putExtra("end_date", end_date); //Optional parameters
                    myIntent.putExtra("x", x); //Optional parameters
                    myIntent.putExtra("y", y); //Optional parameters
                    MainActivity.this.startActivity(myIntent);
                }
                return false;
            }
        });
        simpleExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                GroupItemsInfo headerInfo = deptList.get(groupPosition);
                return false;
            }
        });
    }

    private void clearOnlyListView(){
        simpleExpandableListView = (ExpandableListView) findViewById(R.id.simpleExpandableListView);
        myExpandableListAdapter = new MyExpandableListAdapter(this, emptyTest);
        simpleExpandableListView.setAdapter(myExpandableListAdapter);
    }
    private void clearExpandingListView(){
        currentlySelected.clear();
        records.setText("Records: ");
        txtFilterDate.setText("");
        deptList.clear();
        songsList.clear();
        simpleExpandableListView = (ExpandableListView) findViewById(R.id.simpleExpandableListView);
        myExpandableListAdapter = new MyExpandableListAdapter(this, emptyTest);
        simpleExpandableListView.setAdapter(myExpandableListAdapter);
    }
    private String formatDate(Date date){
        DateFormat dateFormat = new SimpleDateFormat("d-M-y");
        String strDate = dateFormat.format(date);
        return strDate;
    }
    private void loadData(List<Event> data) {
        records.setText("Records: " +  data.size());
        if(data.size() > 0){
            for(Event event : data){
                addProduct(event.getTitle(), "Trunk Road: " + event.getTrunkRoad());
                if(!event.getDirection().equals("")){
                    addProduct(event.getTitle(), "Direction: " + event.getDirection());
                }
                //if published date and start date
                //if 0 dates are the same display only one
                if(0 == event.getPubDate().compareTo(event.getStartDate())){
                    addProduct(event.getTitle(), "Start date: " + formatDate(event.getStartDate()));
                }
                else{ //display both dates as they are different
                    addProduct(event.getTitle(), "Published: " + formatDate(event.getPubDate()));
                    addProduct(event.getTitle(), "Start date: " + formatDate(event.getStartDate()));
                }

                addProduct(event.getTitle(), "End date: " + formatDate(event.getEndDate()) );
                addProduct(event.getTitle(), "Days of works: " + event.getLengthDisruptionDays().toString());
                //addProduct(event.getTitle(), event.getLink());
                if(!event.getAuthor().equals("")){
                    addProduct(event.getTitle(), event.getAuthor());
                }
                if(!event.getComments().equals("")){
                    addProduct(event.getTitle(), event.getComments());
                }
                if(!event.getDelayInformation().equals("")){
                    addProduct(event.getTitle(), event.getDelayInformation());
                }
                if(!event.getDisruption().equals("")){
                    addProduct(event.getTitle(), event.getDisruption());
                }
                addProduct(event.getTitle(), event.getPoint().toString());
                addProduct(event.getTitle(), event.getDescription());
            }
        }else
        {
            addProduct("no data", "nothing");
        }
    }
    // here we maintain songsList and songs names
    private int addProduct(String songsListName, String songName) {
        int groupPosition = 0;
        //check the hashmap if the group already exists
        GroupItemsInfo headerInfo = songsList.get(songsListName);
        //add the group if doesn't exists
        if (headerInfo == null) {
            headerInfo = new GroupItemsInfo();
            headerInfo.setName(songsListName);
            songsList.put(songsListName, headerInfo);
            deptList.add(headerInfo);
        }
        // get the children for the group
        ArrayList<ChildItemsInfo> productList = headerInfo.getSongName();
        // size of the children list
        int listSize = productList.size();
        // add to the counter
        listSize++;
        // create a new child and add that to the group
        ChildItemsInfo detailInfo = new ChildItemsInfo();
        detailInfo.setName(songName);
        productList.add(detailInfo);
        headerInfo.setPlayerName(productList);
        // find the group position inside the list
        groupPosition = deptList.indexOf(headerInfo);
        return groupPosition;
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
        }

    }
}
