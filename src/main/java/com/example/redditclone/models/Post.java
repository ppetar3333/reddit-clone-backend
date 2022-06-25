package com.example.redditclone.models;

import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@RequiredArgsConstructor
@ToString
@Getter
@Setter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postID;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private LocalDateTime creationDate;

    @Column(nullable = false)
    private int voteCount;

    private String imagePath;

    @ManyToOne
    @JoinColumn(referencedColumnName = "subredditID", nullable = false)
    private Subreddit subreddit;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(referencedColumnName = "userID")
    private User user;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "commentID")
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "reactionID")
    private Set<Reaction> reactions = new HashSet<>();

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "reportID")
    private Set<Report> reports = new HashSet<>();

    @OneToOne
    private Flair flair;

    public void addReaction(Reaction r) {
        if(r.getPost() != null)
            r.getPost().getReactions().remove(r);
        r.setPost(this);
        getReactions().add(r);
    }

    public void removeReaction(Reaction r) {
        r.setComment(null);
        getReactions().remove(r);
    }

    public void addReport(Report r) {
        if(r.getPost() != null)
            r.getPost().getReports().remove(r);
        r.setPost(this);
        getReports().add(r);
    }

    public void removeReport(Report r) {
        r.setComment(null);
        getReports().remove(r);
    }
}
