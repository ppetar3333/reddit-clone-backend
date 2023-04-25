package com.example.redditclone.repository.elasticsearch;

import com.example.redditclone.models.Reader;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReaderRepository extends ElasticsearchRepository<Reader, String> {
}
