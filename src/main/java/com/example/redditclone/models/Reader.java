package com.example.redditclone.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Builder
@AllArgsConstructor
@Document(indexName = "readers")
public class Reader {
    @Id
    private String id;
    private String name;
    private String lastName;
    private String address;
}
