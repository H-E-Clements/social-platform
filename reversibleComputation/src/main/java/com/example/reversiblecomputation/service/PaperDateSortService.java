package com.example.reversiblecomputation.service;

import com.example.reversiblecomputation.domain.Feed;
import com.example.reversiblecomputation.domain.Paper;

import java.util.Comparator;

public class PaperDateSortService implements Comparator<Paper> {
    //sorts paper (files) by upload date
    @Override
    public int compare(Paper paper1, Paper paper2) {
        return paper1.getUploadDate().compareTo(paper2.getUploadDate());}
}
