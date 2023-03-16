package com.example.reversiblecomputation.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Paper {
    @Id
    private String fileName;
    //name of the file is PK

    private String description;
    //file description^^

    private String author;
    //name of user who posted

    private String uploadDate;

    public String getUploadDate() {return uploadDate;}
    public void setUploadDate(String uploadDate) {this.uploadDate = uploadDate;}

    public String getFileName() {return fileName;}
    public void setFileName(String fileName) {this.fileName = fileName;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public String getAuthor() {return author;}
    public void setAuthor(String author) {this.author= author;}
}
