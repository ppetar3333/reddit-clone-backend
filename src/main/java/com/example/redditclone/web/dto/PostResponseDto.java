package com.example.redditclone.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {
    private String id;
    private String title;
    private String text;
    private String keywords;
    private String filename;
    private String creationDate;
    private int voteCount;
    private String imagePath;
    private UserResponseDto user;
    private FlairResponseDto flair;
    private SubredditResponseDto subreddit;
    private String textFromPdf;
}
