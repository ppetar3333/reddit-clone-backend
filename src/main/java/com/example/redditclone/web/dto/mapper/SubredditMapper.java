package com.example.redditclone.web.dto.mapper;

import com.example.redditclone.models.PostElastic;
import com.example.redditclone.models.SubredditElastic;
import com.example.redditclone.web.dto.PostResponseDto;
import com.example.redditclone.web.dto.SubredditResponseDto;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.List;

public class SubredditMapper {

    public static SubredditResponseDto mapResponseDto(SubredditElastic subreddit){
        return SubredditResponseDto.builder()
                .id(subreddit.getId())
                .name(subreddit.getName())
                .description(subreddit.getDescription())
                .creationDate(subreddit.getCreationDate())
                .isSuspended(subreddit.isSuspended())
                .suspendedReason(subreddit.getSuspendedReason())
                .rules(subreddit.getRules())
                .textFromPdf(subreddit.getTextFromPdf())
                .keywords(subreddit.getKeywords())
                .postsCount(subreddit.getPostsCount())
                .build();
    }

    public static List<SubredditResponseDto> mapDtos(SearchHits<SubredditElastic> searchHits) {
        return searchHits
                .map(subreddit -> mapResponseDto(subreddit.getContent()))
                .toList();
    }
}
