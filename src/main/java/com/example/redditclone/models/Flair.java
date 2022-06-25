package com.example.redditclone.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@RequiredArgsConstructor
@ToString
@Getter
@Setter
public class Flair {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flairID;

    @Column(nullable = false)
    private String name;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "postID")
    private Set<Post> posts = new HashSet<>();

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name="subreddit_flairs", joinColumns = {@JoinColumn(name = "flairid")},
            inverseJoinColumns = {@JoinColumn(name = "subredditid")})
    private Set<Subreddit> subredditSet = new HashSet<>();
}
