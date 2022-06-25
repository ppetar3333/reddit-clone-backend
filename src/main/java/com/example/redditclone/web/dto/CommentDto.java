package com.example.redditclone.web.dto;

import com.example.redditclone.models.Comment;
import com.example.redditclone.models.Post;
import com.example.redditclone.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class CommentDto implements Serializable {

    @NotNull
    private Long id;
    @Size(min = 5, max = 1000, message = "Text needs to be between 5 and 1000")
    private String text;
    @NotNull
    private LocalDateTime timestamp;
    private boolean isDeleted;
    @NotNull
    private int voteCount;
    private Long parentComment;
    @JsonIgnore
    @Autowired
    private Converter<User, UserDto> toDtoUser;
    @JsonIgnore
    @Autowired
    private Converter<Post, PostDto> toDtoPost;
    private UserDto user;
    private PostDto post;

    public CommentDto() { super(); }

    public CommentDto(Comment comment) {
        this.id = comment.getCommentID();
        this.text = comment.getText();
        this.timestamp = comment.getTimestamp();
        this.voteCount = comment.getVoteCount();
        this.isDeleted = comment.isDeleted();
        if (comment.getParent() != null) this.parentComment = comment.getParent().getCommentID();
        if (comment.getUser() != null) this.user = toDtoUser.convert(comment.getUser());
        if (comment.getPost() != null) this.post = toDtoPost.convert(comment.getPost());
    }
}
