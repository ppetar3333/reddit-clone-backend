package com.example.redditclone.repository.elasticsearch;

import com.example.redditclone.models.PostElastic;
import com.example.redditclone.models.SubredditElastic;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubredditElasticRepository extends ElasticsearchRepository<SubredditElastic, Long> {
    List<SubredditElastic> findAllByDescription(String description);
    List<SubredditElastic> findAllByName(String name);
    List<SubredditElastic> findAllByTextFromPdf(String text);
}
