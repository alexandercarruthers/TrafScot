package com.example.trafscot.Dao;

import com.example.trafscot.Models.Event;
import java.util.Date;
import java.util.List;

/**
 * Alexander Carruthers - S1828301
 */
public interface EventDao {
    List<Event> getAllCurrentIncidents();
    List<Event> getAllCurrentRoadworks();
    List<Event> getAllFutureRoadworks();
    List<Event> getMotorwayEvents(String searchForMotorway, List<Event> events);
    List<Event> getRoadworksOnDate(Date date, List<Event> roadworks);

}
