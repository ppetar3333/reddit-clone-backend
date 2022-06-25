package com.example.redditclone.support.converterOneReverse;

import com.example.redditclone.models.Comment;
import com.example.redditclone.models.Post;
import com.example.redditclone.models.User;
import com.example.redditclone.service.CommentService;
import com.example.redditclone.service.PostService;
import com.example.redditclone.service.UserService;
import com.example.redditclone.web.dto.CommentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CommentDtoToComment implements Converter<CommentDto, Comment> {

    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;

    public CommentDtoToComment() { }

    public Comment convert(CommentDto source) {
        Comment target = null;

        if (source.getId() != null) target = commentService.one(source.getId()).get();

        if (target == null) target = new Comment();

        target.setCommentID(source.getId());
        target.setText(source.getText());
        target.setTimestamp(source.getTimestamp());
        target.setDeleted(source.isDeleted());
        target.setVoteCount(source.getVoteCount());
        if (source.getParentComment() != null) {
            Optional<Comment> parent = Optional.of(commentService.one(source.getParentComment()).orElse(new Comment()));
            target.setParent(parent.get());
        }
        Optional<User> user = Optional.of(userService.one(source.getUser().getUserID()).orElse(new User()));
        target.setUser(user.get());
        Optional<Post> post = Optional.of(postService.one(source.getPost().getPostID()).orElse(new Post()));
        target.setPost(post.get());

        return target;
    }

}
