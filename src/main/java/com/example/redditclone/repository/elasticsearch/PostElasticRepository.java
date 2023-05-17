package com.example.redditclone.repository.elasticsearch;

import com.example.redditclone.models.PostElastic;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PostElasticRepository extends ElasticsearchRepository<PostElastic, Long> {
    List<PostElastic> findAllByText(String text);
    List<PostElastic> findAllByTitle(String title);
    List<PostElastic> findAllByTextFromPdf(String text);
    List<PostElastic> findAllByVoteCountBetween(Long bottom, Long top);
    List<PostElastic> findAllByVoteCountLessThanEqual(Long top);
    List<PostElastic> findAllByVoteCountGreaterThanEqual(Long bottom);
}
