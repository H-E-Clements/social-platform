package com.example.reversiblecomputation.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="comments")
public class Comment {
    @GeneratedValue
    @Id
    private Long Id;
    //comment id
    private String content;
    //content of comment
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user", nullable = false)
    private User user;
    //user who made the comment

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "target", nullable = false)
    private User target;
    //user who was commented on

    private String date;
    //date comment submitted
}
