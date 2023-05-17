package com.example.redditclone.models;

import com.example.redditclone.web.dto.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import javax.persistence.Column;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "post")
@Setting(settingPath = "/analyzers/serbianAnalyzer.json")
public class PostElastic {

    @Id
    private String id;
    @Field(type = FieldType.Text)
    private String title;
    @Field(type = FieldType.Text)
    private String text;
    @Field(type = FieldType.Keyword)
    private String keywords;
    @Field(type = FieldType.Text)
    private String filename;
    @Field(type = FieldType.Text)
    private String creationDate;
    @Field(type = FieldType.Integer)
    private int voteCount;
    @Field(type = FieldType.Text)
    private String imagePath;
    @Field(type = FieldType.Object)
    private UserResponseDto user;
    @Field(type = FieldType.Object)
    private FlairResponseDto flair;
    @Field(type = FieldType.Object)
    private SubredditResponseDto subreddit;
    @Field(type = FieldType.Text)
    private String textFromPdf;

}
