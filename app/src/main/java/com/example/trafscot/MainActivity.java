package com.example.trafscot;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;

import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.trafscot.Dao.EventDao;
import com.example.trafscot.Dao.EventDaoImpl;
import com.example.trafscot.Models.ChildItemsInfo;
import com.example.trafscot.Models.Event;
import com.example.trafscot.Models.GroupItemsInfo;
import com.example.trafscot.Service.CustomerExpandableListAdapter;
import com.example.trafscot.UI.Map;
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
    private TextView txtFilterDate;
    private TextView txtRecords;
    private Button btnClear;
    private Button btnFilterDate;
    private Button btnFilterRoad;
    private LinkedHashMap<String, GroupItemsInfo> songsList = new LinkedHashMap<String, GroupItemsInfo>();
    private ArrayList<GroupItemsInfo> deptList = new ArrayList<GroupItemsInfo>();
    private CustomerExpandableListAdapter myExpandableListAdapter;
    private ExpandableListView simpleExpandableListView;


    DatePickerDialog datePickerDialog;
    Date datePicked;

    private Spinner trunkRoadSpinner;

    private RadioButton rdbCurrentIncidents;
    private RadioButton rdbCurrentRoadworks;
    private RadioButton rdbFutureRoadworks;

    private Toolbar toolbar;                              // Declaring the Toolbar Object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        rdbCurrentIncidents = (RadioButton) findViewById(R.id.radioCurrentIncidents);
        rdbCurrentRoadworks = (RadioButton) findViewById(R.id.radioCurrentRoadworks);
        rdbFutureRoadworks = (RadioButton) findViewById(R.id.radioFutureRoadworks);
        txtFilterDate = (EditText)findViewById(R.id.txtFilterDate);
        btnFilterDate = (Button) findViewById(R.id.btnFilterDate);
        btnFilterDate.setOnClickListener(this);
        trunkRoadSpinner = (Spinner) findViewById(R.id.trunkRoadSpinner);
        btnFilterRoad = (Button) findViewById(R.id.btnFilterRoad);
        btnFilterRoad.setOnClickListener(this);
        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);
        txtRecords = (TextView)findViewById(R.id.txtRecords);
        simpleExpandableListView = (ExpandableListView) findViewById(R.id.simpleExpandableListView);
        setCustomRadioButtonListeners();
        setCustomEditTextDatePickerListener();
        Boolean network_connected = haveNetworkConnection();
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        if(network_connected == false) {
            Toast.makeText(context, "No network connectivity", duration).show();
        }
        else{

//            Toast.makeText(context, "Data sources loaded", duration).show();
        }
        AsyncTaskExample asyncTask = new AsyncTaskExample();
        asyncTask.execute();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                Boolean network_connected = haveNetworkConnection();
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                if(network_connected == false) {
                    Toast.makeText(context, "No network connectivity", duration).show();
                }
                else{
                    AsyncTaskExample asyncTask = new AsyncTaskExample();
                    asyncTask.execute();
                    Toast.makeText(context, "Data sources refreshed", duration).show();
                }
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_help:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    private void setCustomEditTextDatePickerListener() {
        txtFilterDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
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
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

    }

    private void refreshExpandedListView(List<Event> currentData){
        currentlySelected.clear();
        clearExpandingListView();
        currentlySelected.addAll(currentData);
        populateTrunkRoadSpinner(currentlySelected);
        loadData(currentlySelected);
        setDataExpandingListView();
    }


    public void setCustomRadioButtonListeners(){
        rdbCurrentIncidents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((RadioButton) v).isChecked();
                if (checked){
                    refreshExpandedListView(currentIncidents);
//                    currentlySelected.clear();
//                    clearExpandingListView();
//                    currentlySelected.addAll(currentIncidents);
//                    populateTrunkRoadSpinner(currentlySelected);
//                    loadData(currentlySelected);
//                    setDataExpandingListView();
                }
            }
        });

        rdbCurrentRoadworks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((RadioButton) v).isChecked();
                if (checked){
                    refreshExpandedListView(currentRoadworks);
//                    currentlySelected.clear();
//                    clearExpandingListView();
//                    currentlySelected.addAll(currentRoadworks);
//                    populateTrunkRoadSpinner(currentlySelected);
//                    loadData(currentlySelected);
//                    setDataExpandingListView();
                }
            }
        });

        rdbFutureRoadworks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((RadioButton) v).isChecked();
                if (checked){
                    refreshExpandedListView(futureRoadworks);
//                    currentlySelected.clear();
//                    clearExpandingListView();
//                    currentlySelected.addAll(futureRoadworks);
//                    populateTrunkRoadSpinner(currentlySelected);
//                    loadData(currentlySelected);
//                    setDataExpandingListView();
                }
            }
        });
    }
    public void onClick(View view) {
        if (view == btnClear){
            currentlySelected.clear();
            populateTrunkRoadSpinner(currentlySelected);
            clearExpandingListView();
        }
        if (view == btnFilterDate){
            String string_date_picked = txtFilterDate.getText().toString();
            if(!string_date_picked.equals("")){
                Date datePicked = null;
                try {
                    datePicked = new SimpleDateFormat("d-M-y").parse(string_date_picked);
                    List<Event>  filteredRoadworks = getFilteredEventList(datePicked,currentlySelected);
                    refreshExpandedListView(filteredRoadworks);
//                    currentlySelected.clear();
//                    clearExpandingListView();
//                    currentlySelected.addAll(filteredRoadworks);
//                    populateTrunkRoadSpinner(currentlySelected);
//                    loadData(currentlySelected);
//                    setDataExpandingListView();
                } catch (ParseException e) {
                    e.printStackTrace();
                    txtFilterDate.setText("DATE ERROR");
                }

            }
        }
        if (view == btnFilterRoad){
            String filterOnTrunkRoad = String.valueOf(trunkRoadSpinner.getSelectedItem());
            List<Event>  filteredRoadworks = getTrunkRoadFilteredEventList(filterOnTrunkRoad,currentlySelected);
            refreshExpandedListView(filteredRoadworks);
//            currentlySelected.clear();
//            clearExpandingListView();
//            currentlySelected.addAll(filteredRoadworks);
//            populateTrunkRoadSpinner(currentlySelected);
//            loadData(currentlySelected);
//            setDataExpandingListView();
        }
    }

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
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, setOfTrunkRoads);
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
        myExpandableListAdapter = new CustomerExpandableListAdapter(this, deptList);
        // attach the adapter to the expandable list view
        simpleExpandableListView.setAdapter(myExpandableListAdapter);
        // setOnChildClickListener listener for child row click or song name click
        //use this to add event handling for clickig on start date, end date etc
        simpleExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                GroupItemsInfo headerInfo = deptList.get(groupPosition);
                ChildItemsInfo detailInfo = headerInfo.getEventName().get(childPosition);
                if (detailInfo.getName().startsWith("Location:")){
                    String segments[] = detailInfo.getName().split(":");
                    String xy[] =  segments[1].split(",");
                    String x = xy[0];
                    String y = xy[1];
                    String title = headerInfo.getName();
                    String start_date = "";
                    String end_date = "";
                    String days_of_works = "";
                    ArrayList<ChildItemsInfo> innerList = headerInfo.getEventName();
                    for(ChildItemsInfo item : innerList){
                        String inner_list_start_date = item.getName();
                        if(inner_list_start_date.startsWith("Start date:")){
                            start_date = inner_list_start_date;
                        }
                        if(inner_list_start_date.startsWith("End date:")){
                            end_date = inner_list_start_date;
                        }
                        if(inner_list_start_date.startsWith("Days of works:")){
                            days_of_works = inner_list_start_date;
                        }
                    }
                    Intent myIntent = new Intent(MainActivity.this, Map.class);
                    myIntent.putExtra("marker_title",title);
                    myIntent.putExtra("start_date", start_date);
                    myIntent.putExtra("end_date", end_date);
                    myIntent.putExtra("days_of_works", days_of_works);
                    myIntent.putExtra("x", x);
                    myIntent.putExtra("y", y);
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
        ArrayList<GroupItemsInfo> emptyList = new ArrayList<GroupItemsInfo>();
        myExpandableListAdapter = new CustomerExpandableListAdapter(this, emptyList);
        simpleExpandableListView.setAdapter(myExpandableListAdapter);
    }
    private void clearExpandingListView(){
        currentlySelected.clear();
        txtRecords.setText("0");
        txtFilterDate.setText(R.string.date_placeholder);
        deptList.clear();
        songsList.clear();
        clearOnlyListView();
    }
    private String formatDate(Date date){
        DateFormat dateFormat = new SimpleDateFormat("d-M-y");
        String strDate = dateFormat.format(date);
        return strDate;
    }
    private void loadData(List<Event> data) {
        txtRecords.setText(" " + data.size());
        if(data.size() > 0){
            for(Event event : data){
                addProduct(event.getTitle(), "Trunk Road: " + event.getTrunkRoad());
                if(!event.getDirection().equals("")){
                    addProduct(event.getTitle(), "Direction: " + event.getDirection());
                }
                //if published date and start date = 0; dates are the same display only one
                if(0 == event.getPubDate().compareTo(event.getStartDate())){
                    addProduct(event.getTitle(), "Start date: " + formatDate(event.getStartDate()));
                }
                else{ //display both dates as they are different
                    addProduct(event.getTitle(), "Published: " + formatDate(event.getPubDate()));
                    addProduct(event.getTitle(), "Start date: " + formatDate(event.getStartDate()));
                }
                addProduct(event.getTitle(), "End date: " + formatDate(event.getEndDate()) );
                addProduct(event.getTitle(), "Days of works: " + event.getLengthDisruptionDays().toString());
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
                //addProduct(event.getTitle(), event.getDescription());
            }
        }else
        {
            //addProduct("no data", "");
        }
    }
    private void addProduct(String songsListName, String roadworkDetail) {
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
        ArrayList<ChildItemsInfo> productList = headerInfo.getEventName();
        // size of the children list
        int listSize = productList.size();
        // add to the counter
        listSize++;
        // create a new child and add that to the group
        ChildItemsInfo detailInfo = new ChildItemsInfo();
        detailInfo.setName(roadworkDetail);
        productList.add(detailInfo);
        headerInfo.setInnerItemName(productList);
        // find the group position inside the list
        groupPosition = deptList.indexOf(headerInfo);
    }



    private class AsyncTaskExample extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            while(!haveNetworkConnection()){
                try {
                    Thread.sleep(1000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            EventDao eventDao = new EventDaoImpl();
            currentIncidents = eventDao.getAllCurrentIncidents();
            currentRoadworks = eventDao.getAllCurrentRoadworks();
            futureRoadworks = eventDao.getAllFutureRoadworks();

            return null;
        }
        @Override
        protected void onPostExecute(Void v) {
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(context, "connection established", duration).show();
        }
    }
}
