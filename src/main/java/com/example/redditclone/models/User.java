package com.example.redditclone.models;

import com.example.redditclone.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@RequiredArgsConstructor
@ToString
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = true)
    private String profileDescription;

    @Column(nullable = true)
    private String displayName;

    @Column(nullable = true)
    private String avatar;

    @Column(nullable = false)
    private LocalDateTime registrationDate;

    @Column(nullable = false)
    private boolean isBanned;

    @Column(nullable = false)
    private UserRole role;

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name="subreddit_moderatori", joinColumns = {@JoinColumn(name = "userid")},
            inverseJoinColumns = {@JoinColumn(name = "subredditid")})
    private Set<Subreddit> subreddits = new HashSet<>();

    @JsonIgnore
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "commentID")
    private Set<Comment> userComments = new HashSet<>();

    @JsonIgnore
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "postID")
    private Set<Post> userPosts = new HashSet<>();

    @JsonIgnore
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "id")
    private Set<Banned> banneds = new HashSet<>();

    @JsonIgnore
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "reactionID")
    private Set<Reaction> userReactions = new HashSet<>();

    public void removePost(Post p) {
        p.setUser(null);
        getUserPosts().remove(p);
    }
}
