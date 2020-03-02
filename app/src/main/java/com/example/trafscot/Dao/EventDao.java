package com.example.trafscot.Dao;

import com.example.trafscot.Models.Event;
import java.util.Date;
import java.util.List;

public interface EventDao {
    public List<Event> getAllCurrentIncidents();
    public List<Event> getAllCurrentRoadworks();
    public List<Event> getAllFutureRoadworks();
    public List<Event> getMotorwayEvents(String searchForMotorway, List<Event> events);
    public List<Event> getRoadworksOnDate(Date date, List<Event> roadworks);

}
