package com.example.redditclone.support.converterOneReverse;

import com.example.redditclone.models.Post;
import com.example.redditclone.models.Subreddit;
import com.example.redditclone.models.User;
import com.example.redditclone.service.PostService;
import com.example.redditclone.web.dto.PostDto;
import com.example.redditclone.web.dto.SubredditDto;
import com.example.redditclone.web.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PostDtoToPost implements Converter<PostDto, Post> {

    @Autowired
    private PostService postService;
    @Autowired
    private Converter<UserDto, User> toDtoUser;
    @Autowired
    private Converter<SubredditDto, Subreddit> toDtoSubreddit;

    public PostDtoToPost() { }

    public Post convert(PostDto source) {
        Post target = null;

        if (source.getPostID() != null) target = postService.one(source.getPostID()).get();

        if (target == null) target = new Post();

        target.setPostID(source.getPostID());
        target.setTitle(source.getTitle());
        target.setText(source.getText());
        target.setVoteCount(source.getVoteCount());
        target.setCreationDate(source.getCreationDate());
        target.setImagePath(source.getImagePath());
        target.setSubreddit(toDtoSubreddit.convert(source.getSubreddit()));
        target.setUser(toDtoUser.convert(source.getUser()));
        target.setTextFromPdf(source.getTextFromPdf());

        return target;
    }
}
