package com.example.trafscot.Service;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;
import com.example.trafscot.Models.ChildItemsInfo;
import com.example.trafscot.Models.GroupItemsInfo;
import com.example.trafscot.R;
import java.util.ArrayList;

/**
 * Alexander Carruthers - S1828301
 */
public class CustomerExpandableListAdapter implements ExpandableListAdapter {

    private Context context;
    private ArrayList<GroupItemsInfo> teamName;

    public CustomerExpandableListAdapter(Context context, ArrayList<GroupItemsInfo> deptList) {
        this.context = context;
        this.teamName = deptList;
    }

    public CustomerExpandableListAdapter() {

    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {    }

    @Override
    public int getGroupCount() {
        return teamName.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<ChildItemsInfo> productList = teamName.get(groupPosition).getEventName();
        return productList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return teamName.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<ChildItemsInfo> productList = teamName.get(groupPosition).getEventName();
        return productList.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupItemsInfo headerInfo = (GroupItemsInfo) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inf.inflate(R.layout.group_items, null);
        }
        TextView heading = convertView.findViewById(R.id.heading);
        heading.setText(headerInfo.getName().trim());
        //trunk road
        int iend = headerInfo.getName().indexOf(" ");
        String motorway = headerInfo.getName();
        if (iend != -1) {
            motorway= headerInfo.getName().substring(0 , iend);
        }
        TextView icon = convertView.findViewById(R.id.icon);
        icon.setText(motorway);
        ArrayList<ChildItemsInfo> values = headerInfo.getEventName();
        for(ChildItemsInfo items : values){
            if (items.getName().startsWith("Days of works")){
                String[] segments = items.getName().split(": ");
                String days_value = segments[1];
                Integer days_of_works = Integer.parseInt(days_value);
                icon.setBackgroundColor(Color.parseColor(getRoadworksImpact(days_of_works)));
            }
        }
        return convertView;
    }

    public String getRoadworksImpact(int days) {
        if (days <= 20) {
            return "#ffd460";  // yellow
        } else if (days > 20 && days < 60 ){
            return "#f07b3f"; // orange
        } else if (days >= 60) {
            return "#ea5455"; // red
        }
        return "#222831"; //off black
    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildItemsInfo detailInfo = (ChildItemsInfo) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.child_items, null);
        }
        TextView childItem = convertView.findViewById(R.id.childItem);
        String display = detailInfo.getName();
        childItem.setText(display.trim());
        if(display.startsWith("Loc")) {
            childItem.setTextColor(Color.WHITE);
        }
        else{

            int color = Color.parseColor("#00a8cc");
            childItem.setTextColor(color);
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {   }

    @Override
    public void onGroupCollapsed(int groupPosition) {    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }
}