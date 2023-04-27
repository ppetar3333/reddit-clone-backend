package com.example.redditclone.web.dto.mapper;

import com.example.redditclone.models.PostElastic;
import com.example.redditclone.web.dto.PostDto;
import com.example.redditclone.web.dto.PostResponseDto;

public class PostMapper {

    public static PostResponseDto mapResponseDto(PostElastic post){
        return PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .text(post.getText())
                .creationDate(post.getCreationDate())
                .keywords(post.getKeywords())
                .filename(post.getFilename())
                .voteCount(post.getVoteCount())
                .imagePath(post.getImagePath())
                .flair(post.getFlair())
                .user(post.getUser())
                .subreddit(post.getSubreddit())
                .build();
    }
}
