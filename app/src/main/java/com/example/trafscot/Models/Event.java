package com.example.trafscot.Models;
import java.util.Date;

public  class Event{
    //Declare variables as private final
    private final String title;
    private final String description;
    private final String link;
    private final GeoPoint point;
    private final String author;
    private final String comments;
    private final Date pubDate;
    private final String trunkRoad;
    private final Date startDate;
    private final Date endDate;
    private final String delayInformation;
    private final String direction; //NB, SB, EB, WS
    private final String disruption; //Types:
    private final Long lengthDisruptionDays;

    @Override
    public String toString() { return title ; }
    //Generate normal constructor with all variables set constructor to private
    Event(String title, String description, String link, GeoPoint point, String author, String comments, Date pubDate, String trunkRoad, Date startDate, Date endDate, String delayInformation, String direction, String disruption, Long lengthDisruptionDays) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.point = point;
        this.author = author;
        this.comments = comments;
        this.pubDate = pubDate;
        this.trunkRoad = trunkRoad;
        this.startDate = startDate;
        this.endDate = endDate;
        this.delayInformation = delayInformation;
        this.direction = direction;
        this.disruption = disruption;
        this.lengthDisruptionDays = lengthDisruptionDays;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    public GeoPoint getPoint() { return point; }

    public String getAuthor() {
        return author;
    }

    public String getComments() {
        return comments;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public String getTrunkRoad() {
        return trunkRoad;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getDelayInformation() {
        return delayInformation;
    }

    public String getDirection() {
        return direction;
    }

    public String getDisruption() {
        return disruption;
    }

    public Long getLengthDisruptionDays() {
        return lengthDisruptionDays;
    }
}
