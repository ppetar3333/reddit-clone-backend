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
@Document(indexName = "flair")
@Setting(settingPath = "/analyzers/serbianAnalyzer.json")
public class FlairElastic {

    @Id
    private Long id;
    @Field(type = FieldType.Text)
    private String name;
}
