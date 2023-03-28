package com.example.reversiblecomputation.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
public class Event {
    @Id
    private String title;
    private String description;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private String date;
    private String time;
    private String duration;
    private String location;
    private String author;

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public String getDate() {return date;}
    public void setDate(String date) {this.date = date;}

    public String getTime() {return time;}
    public void setTime(String time) {this.time = time;}

    public String getDuration() {return duration;}
    public void setDuration(String duration) {this.duration = duration;}

    public String getLocation() {return location;}
    public void setLocation(String location) {this.location = location;}

    public String getAuthor() {return author;}
    public void setAuthor(String author) {this.author = author;}
}
