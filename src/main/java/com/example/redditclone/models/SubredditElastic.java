package com.example.redditclone.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "subreddit")
@Setting(settingPath = "/analyzers/serbianAnalyzer.json")
public class SubredditElastic {

    @Id
    private String id;
    @Field(type = FieldType.Text)
    private String name;
    @Field(type = FieldType.Text)
    private String description;
    @Field(type = FieldType.Text)
    private String creationDate;
    @Field(type = FieldType.Boolean)
    private boolean isSuspended;
    @Field(type = FieldType.Text)
    private String suspendedReason;
    @Field(type = FieldType.Keyword)
    private List<String> rules;
    @Field(type = FieldType.Text)
    private String textFromPdf;
    @Field(type = FieldType.Text)
    private String filename;
    @Field(type = FieldType.Keyword)
    private String keywords;
    @Field(type = FieldType.Integer)
    private int postsCount;
}
