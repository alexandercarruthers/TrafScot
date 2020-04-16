package com.example.trafscot.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.trafscot.MainActivity;
import com.example.trafscot.Models.ChildItemsInfo;
import com.example.trafscot.Models.Event;
import com.example.trafscot.Models.GroupItemsInfo;
import com.example.trafscot.R;
import com.example.trafscot.Service.MyExpandableListAdapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Test extends AppCompatActivity {
    private LinkedHashMap<String, GroupItemsInfo> songsList = new LinkedHashMap<String, GroupItemsInfo>();
    private ArrayList<GroupItemsInfo> deptList = new ArrayList<GroupItemsInfo>();

    private List<Event> currentRoadworks;

    private MyExpandableListAdapter myExpandableListAdapter;
    private ExpandableListView simpleExpandableListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        // add data for displaying in expandable list view
        currentRoadworks = this.getIntent().getExtras().getParcelableArrayList("currentRoadworks");
        loadData();

        //get reference of the ExpandableListView
        simpleExpandableListView = (ExpandableListView) findViewById(R.id.simpleExpandableListView);
        // create the adapter by passing your ArrayList data
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

    private void loadData() {
        for(Event event : currentRoadworks){
            addProduct(event.getTitle(), "Trunk Road: " + event.getTrunkRoad());
            if(!event.getDirection().equals("")){
                addProduct(event.getTitle(), "Direction: " + event.getDirection());
            }
            //if published date and start date
            //if 0 dates are the same display only one
            if(0 == event.getPubDate().compareTo(event.getStartDate())){
                addProduct(event.getTitle(), "Start date: " + event.getStartDate().toString());
            }
            else{ //display both dates as they are different
                addProduct(event.getTitle(), "Published: " + event.getPubDate().toString());
                addProduct(event.getTitle(), "Start date: " + event.getStartDate().toString());
            }

            addProduct(event.getTitle(), "End date: " + event.getEndDate().toString());
            addProduct(event.getTitle(), "Days of works: " + event.getLengthDisruptionDays().toString());
            addProduct(event.getTitle(), event.getDescription());
            //addProduct(event.getTitle(), event.getLink());
            addProduct(event.getTitle(), event.getPoint().toString());
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
}
