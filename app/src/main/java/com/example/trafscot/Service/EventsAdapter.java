package com.example.trafscot.Service;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.trafscot.Models.Event;
import com.example.trafscot.R;

import java.util.ArrayList;
import java.util.List;

public class EventsAdapter extends ArrayAdapter<Event> {

    public EventsAdapter(Context context, List<Event> events) {
        super(context, 0, events);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Event event = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_event, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvHome = (TextView) convertView.findViewById(R.id.tvHome);
        // Populate the data into the template view using the data object
        tvName.setText(event.getTitle());
        tvHome.append(event.getLengthDisruptionDays().toString());
        Long days = event.getLengthDisruptionDays();
        Helpers helpers = new Helpers();
        int colour = helpers.getRoadworksImpact(days.intValue());
        tvHome.setBackgroundColor(colour);
        // Return the completed view to render on screen
        return convertView;
    }

}
