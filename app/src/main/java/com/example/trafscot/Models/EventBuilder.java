package com.example.trafscot.Models;

import java.util.Date;

/**
 * Alexander Carruthers - S1828301
 */
public class EventBuilder {
    private String title;
    private String description;
    private String link;
    private GeoPoint point;
    private String author;
    private String comments;
    private Date pubDate;
    private String trunkRoad;
    private Date startDate;
    private Date endDate;
    private String delayInformation;
    private String direction;
    private String disruption;
    private Long lengthDisruptionDays;

    public EventBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public EventBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public EventBuilder setLink(String link) {
        this.link = link;
        return this;
    }

    public EventBuilder setPoint(GeoPoint point) {
        this.point = point;
        return this;
    }

    public EventBuilder setAuthor(String author) {
        this.author = author;
        return this;
    }

    public EventBuilder setComments(String comments) {
        this.comments = comments;
        return this;
    }

    public EventBuilder setPubDate(Date pubDate) {
        this.pubDate = pubDate;
        return this;
    }

    public EventBuilder setTrunkRoad(String trunkRoad) {
        this.trunkRoad = trunkRoad;
        return this;
    }

    public EventBuilder setStartDate(Date startDate) {
        this.startDate = startDate;
        return this;
    }

    public EventBuilder setEndDate(Date endDate) {
        this.endDate = endDate;
        return this;
    }

    public EventBuilder setDelayInformation(String delayInformation) {
        this.delayInformation = delayInformation;
        return this;
    }

    public EventBuilder setDirection(String direction) {
        this.direction = direction;
        return this;
    }

    public EventBuilder setDisruption(String disruption) {
        this.disruption = disruption;
        return this;
    }

    public EventBuilder setLengthDisruptionDays(Long lengthDisruptionDays) {
        this.lengthDisruptionDays = lengthDisruptionDays;
        return this;
    }

    public Event createEvent() {
        return new Event(title, description, link, point, author, comments, pubDate, trunkRoad, startDate, endDate, delayInformation, direction, disruption, lengthDisruptionDays);
    }
}