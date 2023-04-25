package com.example.redditclone.web.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReaderDTO {
    private String name;
    private String lastName;
    private String address;
}
