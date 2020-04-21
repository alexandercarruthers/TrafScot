package com.example.trafscot;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.RadioGroup;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //data
    List<Event> currentlySelected = new ArrayList<>();
    List<Event> currentIncidents = new ArrayList<>();
    List<Event> currentRoadworks = new ArrayList<>();
    List<Event> futureRoadworks = new ArrayList<>();
    private TextView txtFilterDate;
    private TextView txtRecords;
    private Button btnClear;
    private Button btnFilterDate;
    private Button btnFilterRoad;
    private LinkedHashMap<String, GroupItemsInfo> detailsOfTitlesList;
    private ArrayList<GroupItemsInfo> titlesList;
    private CustomerExpandableListAdapter myExpandableListAdapter;
    private ExpandableListView simpleExpandableListView;
    DatePickerDialog datePickerDialog;
    Date datePicked;
    private Spinner trunkRoadSpinner;
    private RadioGroup radioGroupRoadworks;
    private RadioButton rdbCurrentIncidents;
    private RadioButton rdbCurrentRoadworks;
    private RadioButton rdbFutureRoadworks;
    private Toolbar toolbar;
    private DateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setViews();
        setCustomButtonListeners();
        setCustomRadioButtonListeners();
        setCustomEditTextDatePickerListener();
        setSupportActionBar(toolbar);
        displayNetworkConnection();
        AsyncTaskExample asyncTask = new AsyncTaskExample();
        asyncTask.execute();
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
                displayNetworkConnection();
                if (haveNetworkConnection()) {
                    AsyncTaskExample asyncTask = new AsyncTaskExample();
                    asyncTask.execute();
                }
                return true;
            case R.id.action_help:
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.custom_dialog_layout);
                List<String> delayDescs = Arrays.asList("< 10 ", "> 20 ", "> 60 ");
                List<String> delayColours = Arrays.asList("#ffd460", "#f07b3f", "#ea5455");
                int[] descTextViewIDs = new int[] {R.id.shortDelayDesc, R.id.mediumDelayDesc, R.id.longDelayDesc};
                int[] iconTextViewIDs = new int[] {R.id.shortDelayIcon, R.id.mediumDelayIcon, R.id.longDelayIcon};
                for (int i = 0; i < delayDescs.size(); i++) {
                    TextView tv = dialog.findViewById(descTextViewIDs[i]);
                    tv.setText(delayDescs.get(i));
                    TextView iconTextView = dialog.findViewById(iconTextViewIDs[i]);
                    iconTextView.setText(R.string.trunkRoadPlaceholder);
                    iconTextView.setBackgroundColor(Color.parseColor(delayColours.get(i)));
                }
                dialog.show();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                return super.onOptionsItemSelected(item);// Invoke the superclass to handle it.
        }
    }

    private void displayNetworkConnection() {
        Context applicationContext = getApplicationContext();
        int toastLength = Toast.LENGTH_SHORT;
        String text = "";
        String noNet = "No network connectivity";
        String net = "Network connection established";
        if (!haveNetworkConnection()) {
            text = noNet;
        } else {
            text = net;
        }
        Toast.makeText(applicationContext, text, toastLength).show();
    }

    @SuppressLint("SimpleDateFormat")
    private void setViews() {
        radioGroupRoadworks = findViewById(R.id.radioRoadworks);
        rdbCurrentIncidents = findViewById(R.id.radioCurrentIncidents);
        rdbCurrentRoadworks = findViewById(R.id.radioCurrentRoadworks);
        rdbFutureRoadworks = findViewById(R.id.radioFutureRoadworks);
        txtFilterDate = (EditText) findViewById(R.id.txtFilterDate);
        btnFilterDate = findViewById(R.id.btnFilterDate);
        trunkRoadSpinner = findViewById(R.id.trunkRoadSpinner);
        btnFilterRoad = findViewById(R.id.btnFilterRoad);
        btnClear = findViewById(R.id.btnClear);
        txtRecords = findViewById(R.id.txtRecords);
        simpleExpandableListView = findViewById(R.id.simpleExpandableListView);
        toolbar = findViewById(R.id.my_toolbar);
        dateFormat = new SimpleDateFormat("d-M-y");
        detailsOfTitlesList = new LinkedHashMap<>();
        titlesList = new ArrayList<>();
    }

    private void setCustomButtonListeners() {
        btnFilterDate.setOnClickListener(this);
        btnFilterRoad.setOnClickListener(this);
        btnClear.setOnClickListener(this);
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
                }
            }
        });
        rdbCurrentRoadworks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((RadioButton) v).isChecked();
                if (checked){
                    refreshExpandedListView(currentRoadworks);
                }
            }
        });
        rdbFutureRoadworks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((RadioButton) v).isChecked();
                if (checked){
                    refreshExpandedListView(futureRoadworks);
                }
            }
        });
    }

    @SuppressLint("SimpleDateFormat")
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
                    datePicked = dateFormat.parse(string_date_picked);
                    List<Event>  filteredRoadworks = getFilteredEventList(datePicked,currentlySelected);
                    refreshExpandedListView(filteredRoadworks);
                } catch (ParseException e) {
                    e.printStackTrace();
                    txtFilterDate.setText(R.string.dateError);
                }
            }
        }
        if (view == btnFilterRoad){
            String filterOnTrunkRoad = String.valueOf(trunkRoadSpinner.getSelectedItem());
            List<Event>  filteredRoadworks = getTrunkRoadFilteredEventList(filterOnTrunkRoad,currentlySelected);
            refreshExpandedListView(filteredRoadworks);
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
        return eventDao.getMotorwayEvents(trunkRoad, eventsToFilter);
    }
    private List<Event> getFilteredEventList(Date date, List<Event> eventToFilter){
        EventDao eventDao = new EventDaoImpl();
        return eventDao.getRoadworksOnDate(date, eventToFilter);
    }
    private void setDataExpandingListView(){
        myExpandableListAdapter = new CustomerExpandableListAdapter(this, titlesList);
        // attach the adapter to the expandable list view
        simpleExpandableListView.setAdapter(myExpandableListAdapter);
        // setOnChildClickListener listener for child row click or song name click
        //use this to add event handling for clickig on start date, end date etc
        simpleExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                GroupItemsInfo headerInfo = titlesList.get(groupPosition);
                ChildItemsInfo detailInfo = headerInfo.getEventName().get(childPosition);
                if (detailInfo.getName().startsWith("Location:")){
                    String[] segments = detailInfo.getName().split(":");
                    String[] xy = segments[1].split(",");
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
                GroupItemsInfo headerInfo = titlesList.get(groupPosition);
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
        radioGroupRoadworks.clearCheck();
        currentlySelected.clear();
        txtRecords.setText("0");
        txtFilterDate.setText(R.string.date_placeholder);
        titlesList.clear();
        detailsOfTitlesList.clear();
        clearOnlyListView();
    }
    private String formatDate(Date date){
        return dateFormat.format(date);
    }
    private void loadData(List<Event> data) {
        txtRecords.setText(data.size());
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
            }
        }else
        {
            addProduct("No data", "");
        }
    }

    private void addProduct(String titleName, String roadworkDetail) {
        int groupPosition = 0;
        //check the hashmap if the group already exists
        GroupItemsInfo headerInfo = detailsOfTitlesList.get(titleName);
        //add the group if doesn't exists
        if (headerInfo == null) {
            headerInfo = new GroupItemsInfo();
            headerInfo.setName(titleName);
            detailsOfTitlesList.put(titleName, headerInfo);
            titlesList.add(headerInfo);
        }
        // get the children for the group
        ArrayList<ChildItemsInfo> productList = headerInfo.getEventName();
        // create a new child and add that to the group
        ChildItemsInfo detailInfo = new ChildItemsInfo();
        detailInfo.setName(roadworkDetail);
        productList.add(detailInfo);
        headerInfo.setInnerItemName(productList);
        // find the group position inside the list
        groupPosition = titlesList.indexOf(headerInfo);
    }

    private class AsyncTaskExample extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            while (!haveNetworkConnection()) { //check for network connection every second
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
            displayNetworkConnection();
        }
    }
}