package com.example.redditclone.repository.elasticsearch;

import com.example.redditclone.models.Post;
import com.example.redditclone.models.PostElastic;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostElasticRepository extends ElasticsearchRepository<PostElastic, String> {

}
