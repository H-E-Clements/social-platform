package com.example.reversiblecomputation.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Feed {
    @Id
    private String title;
    //title of feed object
    private String text;
    //content of feed object
    private String author;
    //who made the post
    private Long authorId;
    //id of person who made post
    private String postDate;
    //date post made

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public String getText() {return text;}
    public void setText(String text) {this.text = text;}

    public String getAuthor() {return author;}
    public void setAuthor(String author) {this.author = author;}

    public String getPostDate() {return postDate;}
    public void setPostDate(String postDate) {this.postDate = postDate;}

    public Long getAuthorId() {return authorId;}
    public void setAuthorId(Long authorId) {this.authorId = authorId;}
}
