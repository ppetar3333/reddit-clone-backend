package com.example.redditclone.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubredditResponseDto {
    private String id;
    private String name;
    private String description;
    private String creationDate;
    private boolean isSuspended;
    private String suspendedReason;
    private List<String> rules;
    private String textFromPdf;
    private String filename;
    private String keywords;
}
