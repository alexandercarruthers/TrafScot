package com.example.trafscot.Service;

import org.junit.Test;

import static org.junit.Assert.*;

public class CustomerExpandableListAdapterTest {
    @Test
    public void testAddition() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testShortRoadworksImpact() {
        CustomerExpandableListAdapter customAdpt = new CustomerExpandableListAdapter();
        String yellow = "#ffd460";
        String result = customAdpt.getRoadworksImpact(20);
        assertSame(yellow, result);
    }

    @Test
    public void testMediumRoadworksImpact() {
        CustomerExpandableListAdapter customAdpt = new CustomerExpandableListAdapter();
        String orange = "#f07b3f";
        String result = customAdpt.getRoadworksImpact(21);
        assertSame(orange, result);
    }

    @Test
    public void testLongRoadworksImpact() {
        CustomerExpandableListAdapter customAdpt = new CustomerExpandableListAdapter();
        String red = "#ea5455";
        String result = customAdpt.getRoadworksImpact(60);
        assertSame(red, result);
    }
}