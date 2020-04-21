package com.example.trafscot.Models;
import java.util.ArrayList;

/**
 * Alexander Carruthers - S1828301
 */
public class GroupItemsInfo {

    private String listName;
    private ArrayList<ChildItemsInfo> innerItemList = new ArrayList<ChildItemsInfo>();

    public String getName() {
        return listName;
    }

    public void setName(String eventListName) {
        this.listName = eventListName;
    }

    public ArrayList<ChildItemsInfo> getEventName() {
        return innerItemList;
    }

    public void setInnerItemName(ArrayList<ChildItemsInfo> eventName) {
        this.innerItemList = eventName;
    }

}