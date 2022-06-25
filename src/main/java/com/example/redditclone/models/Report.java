package com.example.redditclone.models;

import com.example.redditclone.enums.ReportReason;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@RequiredArgsConstructor
@ToString
@Getter
@Setter
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportID;

    @Column(nullable = false)
    private ReportReason reportReason;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private boolean accepted;

    @ManyToOne
    @JoinColumn(referencedColumnName = "userID")
    private User user;

    @ManyToOne
    @JoinColumn(referencedColumnName = "commentID")
    private Comment comment;

    @ManyToOne
    @JoinColumn(referencedColumnName = "postID")
    private Post post;
}
