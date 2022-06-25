package com.example.redditclone.support.converterOneReverse;

import com.example.redditclone.models.Comment;
import com.example.redditclone.models.Post;
import com.example.redditclone.models.Reaction;
import com.example.redditclone.models.User;
import com.example.redditclone.service.ReactionService;
import com.example.redditclone.web.dto.CommentDto;
import com.example.redditclone.web.dto.PostDto;
import com.example.redditclone.web.dto.ReactionDto;
import com.example.redditclone.web.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ReactionDtoToReaction implements Converter<ReactionDto, Reaction> {

    @Autowired
    private ReactionService reactionService;
    @Autowired
    private Converter<UserDto, User> toDtoUser;
    @Autowired
    private Converter<CommentDto, Comment> toDtoComment;
    @Autowired
    private Converter<PostDto, Post> toDtoPost;

    public ReactionDtoToReaction() { }

    public Reaction convert(ReactionDto source) {
        Reaction target = null;

        if (source.getReactionID() != null) target = reactionService.one(source.getReactionID()).get();

        if (target == null) target = new Reaction();

        target.setReactionID(source.getReactionID());
        target.setType(source.getType());
        target.setTimestamp(source.getTimestamp());
        target.setUser(toDtoUser.convert(source.getUser()));
        target.setComment(toDtoComment.convert(source.getComment()));
        target.setPost(toDtoPost.convert(source.getPost()));

        return target;
    }
}
