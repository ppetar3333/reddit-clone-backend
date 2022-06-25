package com.example.redditclone.support.converterOne;

import com.example.redditclone.models.*;
import com.example.redditclone.web.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class PostToPostDto implements Converter<Post, PostDto> {

    @Autowired
    private Converter<User, UserDto> toUserDto;
    @Autowired
    private Converter<Flair, FlairDto> toFlairDto;
    @Autowired
    private Converter<Subreddit, SubredditDto> toSubredditDto;

    public PostToPostDto() {}

    public PostDto convert(Post source) {
        PostDto dto = new PostDto();
        if(source != null) {
            dto.setPostID(source.getPostID());
            dto.setTitle(source.getTitle());
            dto.setText(source.getText());
            dto.setVoteCount(source.getVoteCount());
            dto.setCreationDate(source.getCreationDate());
            dto.setImagePath(source.getImagePath());
            dto.setUser(toUserDto.convert(source.getUser()));
            if (source.getFlair() != null) dto.setFlair(toFlairDto.convert(source.getFlair()));
            dto.setSubreddit(toSubredditDto.convert(source.getSubreddit()));
        }
        return dto;
    }

    public List<PostDto> convert(List<Post> source) {
        List<PostDto> retVal = new ArrayList<>();
        Iterator var3 = source.iterator();

        while(var3.hasNext()) {
            Post s = (Post) var3.next();
            PostDto dto = this.convert(s);
            retVal.add(dto);
        }

        return retVal;
    }
}
