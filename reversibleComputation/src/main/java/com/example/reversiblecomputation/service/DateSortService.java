package com.example.reversiblecomputation.service;

import com.example.reversiblecomputation.domain.Feed;

import java.util.Comparator;

public class DateSortService implements Comparator<Feed> {
    @Override
    public int compare(Feed feed1, Feed feed2) {
        return feed1.getPostDate().compareTo(feed2.getPostDate());}
}
