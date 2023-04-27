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
@Document(indexName = "user")
@Setting(settingPath = "/analyzers/serbianAnalyzer.json")
public class UserElastic {

    @Id
    private Long id;
    @Field(type = FieldType.Text)
    private String username;
    @Field(type = FieldType.Text)
    private String displayName;
    @Field(type = FieldType.Text)
    private String role;
    @Field(type = FieldType.Text)
    private String avatar;
}
