package com.example.trafscot.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public  class Event implements Parcelable {
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

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(link);
        parcel.writeParcelable(point,i);
        parcel.writeString(author);
        parcel.writeString(comments);
        parcel.writeLong(pubDate.getTime());
        parcel.writeString(trunkRoad);
        parcel.writeLong(startDate.getTime());
        parcel.writeLong(endDate.getTime());
        parcel.writeString(delayInformation);
        parcel.writeString(direction);
        parcel.writeString(disruption);
        parcel.writeLong(lengthDisruptionDays);

    }
    public Event(Parcel in){
        this.title = in.readString();
        this.description = in.readString();
        this.link = in.readString();
        this.point = in.readParcelable(GeoPoint.class.getClassLoader());
        this.author = in.readString();
        this.comments = in.readString();
        this.pubDate = new Date(in.readLong());
        this.trunkRoad = in.readString();
        this.startDate = new Date(in.readLong());
        this.endDate = new Date(in.readLong());
        this.delayInformation = in.readString();
        this.direction = in.readString();
        this.disruption = in.readString();
        this.lengthDisruptionDays = in.readLong();
    }


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

    public GeoPoint getPoint() {
        return point;
    }

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
