package com.example.redditclone.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SimpleQueryEs {
    private String field;
    private String value;
}
