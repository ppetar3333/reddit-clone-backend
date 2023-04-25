package com.example.redditclone.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

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
    @Field(type = FieldType.Keyword)
    private String title;
    @Field(type = FieldType.Text)
    private String text;
    @Field(type = FieldType.Keyword)
    private String keywords;
    private String filename;
}
