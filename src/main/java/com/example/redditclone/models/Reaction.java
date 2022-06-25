package com.example.redditclone.models;

import com.example.redditclone.enums.ReactionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@RequiredArgsConstructor
@ToString
@Getter
@Setter
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reactionID;

    @Column(nullable = false)
    private ReactionType type;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(referencedColumnName = "userID")
    private User user;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(referencedColumnName = "commentID")
    private Comment comment;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(referencedColumnName = "postID")
    private Post post;
}
