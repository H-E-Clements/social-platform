package com.example.reversiblecomputation.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Post {
    @Id
    private String title;
    private String text;
    private String postDate;
    private String author;

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public String getText() {return text;}
    public void setText(String text) {this.text = text;}

    public String getPostDate() {return postDate;}
    public void setPostDate(String postDate) {this.postDate = postDate;}

    public String getAuthor() {return author;}
    public void setAuthor(String author) {this.author = author;}
}