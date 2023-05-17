package com.example.redditclone.web.dto.mapper;

import com.example.redditclone.models.PostElastic;
import com.example.redditclone.web.dto.PostDto;
import com.example.redditclone.web.dto.PostResponseDto;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.List;

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
                .textFromPdf(post.getTextFromPdf())
                .build();
    }

    public static List<PostResponseDto> mapDtos(SearchHits<PostElastic> searchHits) {
        return searchHits
                .map(subreddit -> mapResponseDto(subreddit.getContent()))
                .toList();
    }
}
