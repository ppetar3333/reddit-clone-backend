package com.example.redditclone.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.security.cert.PolicyNode;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentID;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private int voteCount;

    @Column(nullable = false)
    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(referencedColumnName = "commentID")
    private Comment parent;

    @ManyToOne
    @JoinColumn(referencedColumnName = "userID")
    private User user;

    @ManyToOne
    @JoinColumn(referencedColumnName = "postID")
    private Post post;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "parent")
    private Set<Comment> children = new HashSet<>();

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "reactionID")
    private Set<Reaction> reactions = new HashSet<>();

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "reportID")
    private Set<Report> report = new HashSet<>();

    public void removeComment(Comment c) {
        c.setParent(null);
        getChildren().remove(c);
    }

    public void addComment(Comment c) {
        if(c.getParent() != null)
            c.getParent().getChildren().remove(c);
        c.setParent(this);
        getChildren().add(c);
    }

    public void addReaction(Reaction r) {
        if(r.getComment() != null)
            r.getComment().getReactions().remove(r);
        r.setComment(this);
        getReactions().add(r);
    }

    public void removeReaction(Reaction r) {
        r.setComment(null);
        getReactions().remove(r);
    }

    public void addReport(Report r) {
        if(r.getComment() != null)
            r.getComment().getReport().remove(r);
        r.setComment(this);
        getReport().add(r);
    }

    public void removeReport(Report r) {
        r.setComment(null);
        getReport().remove(r);
    }
}
