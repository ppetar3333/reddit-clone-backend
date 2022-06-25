package com.example.redditclone.web.dto;

import com.example.redditclone.enums.ReactionType;
import com.example.redditclone.models.Comment;
import com.example.redditclone.models.Post;
import com.example.redditclone.models.Reaction;
import com.example.redditclone.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ReactionDto implements Serializable {

    @NotNull
    private Long reactionID;
    @NotNull
    private ReactionType type;
    @NotNull
    private LocalDateTime timestamp;
    @NotNull
    private UserDto user;
    @NotNull
    private CommentDto comment;
    @NotNull
    private PostDto post;
    @JsonIgnore
    @Autowired
    private Converter<User, UserDto> toDtoUser;
    @JsonIgnore
    @Autowired
    private Converter<Comment, CommentDto> toDtoComment;
    @JsonIgnore
    @Autowired
    private Converter<Post, PostDto> toDtoPost;

    public ReactionDto() {}

    public ReactionDto(Reaction reaction) {
        this.reactionID = reaction.getReactionID();
        this.type = reaction.getType();
        this.timestamp = reaction.getTimestamp();
        if(reaction.getUser() != null) this.user = toDtoUser.convert(reaction.getUser());
        if(reaction.getComment() != null) this.comment = toDtoComment.convert(reaction.getComment());
        if(reaction.getPost() != null) this.post = toDtoPost.convert(reaction.getPost());
    }
}
