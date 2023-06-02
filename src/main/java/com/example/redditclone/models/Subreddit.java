package com.example.redditclone.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Where;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@RequiredArgsConstructor
@ToString
@Getter
@Setter
public class Subreddit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subredditID;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime creationDate;

    @Column(nullable = false)
    @ElementCollection
    private List<String> rules;

    @Column(nullable = false)
    private boolean isSuspended;

    @Column(nullable = false)
    private String suspendedReason;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "postID")
    private Set<Post> posts = new HashSet<>();

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "id")
    private Set<Banned> banneds = new HashSet<>();

    @ManyToMany(mappedBy = "subreddits")
//    @Where(clause = "role = '1'")
    private Set<User> moderator = new HashSet<>();

    @ManyToMany(mappedBy = "subredditSet")
    private Set<Flair> flairs = new HashSet<>();

    @Column(nullable = true, columnDefinition = "LONGTEXT")
    private String textFromPdf;

    private String filename;

    @Field(type = FieldType.Keyword)
    private String keywords;

    @Column(nullable = false)
    private int postsCount;
}
