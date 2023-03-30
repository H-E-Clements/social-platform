package com.example.reversiblecomputation.service;

import com.example.reversiblecomputation.domain.Event;
import com.example.reversiblecomputation.domain.Paper;

import java.util.Comparator;

public class EventDateSortService implements Comparator<Event> {
    //sorts event objects by date
    @Override
    public int compare(Event event1, Event event2) {
        return event1.getDate().compareTo(event2.getDate());}
}
