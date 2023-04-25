package com.example.redditclone.service.elasticsearch;

import com.example.redditclone.models.Reader;
import com.example.redditclone.repository.elasticsearch.ReaderRepository;
import com.example.redditclone.service.SearchQueryGenerator;
import com.example.redditclone.web.dto.ReaderDTO;
import com.example.redditclone.web.dto.SimpleQueryEs;
import com.example.redditclone.web.dto.mapper.ReaderMapper;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReaderService {
    private final ReaderRepository readerRepository;
    private final ElasticsearchRestTemplate elasticsearchRestTemplate;

    public ReaderService(ReaderRepository readerRepository, ElasticsearchRestTemplate elasticsearchRestTemplate) {
        this.readerRepository = readerRepository;
        this.elasticsearchRestTemplate = elasticsearchRestTemplate;
    }
    public void index(ReaderDTO readerDTO){
        readerRepository.save(new Reader("123", readerDTO.getName(), readerDTO.getLastName(), readerDTO.getAddress()));
    }

    public List<ReaderDTO> findByFirstNameAndLastName(String firstName, String lastName){
        QueryBuilder firstNameQuery = SearchQueryGenerator.createMatchQueryBuilder(new SimpleQueryEs("name", firstName));
        QueryBuilder lastNameQuery = SearchQueryGenerator.createMatchQueryBuilder(new SimpleQueryEs("lastName", lastName));

        BoolQueryBuilder boolQueryFirstNameAndLastName = QueryBuilders
                .boolQuery()
                .must(firstNameQuery)
                .must(lastNameQuery);

        return ReaderMapper.mapDtos(searchByBoolQuery(boolQueryFirstNameAndLastName));
    }

    private SearchHits<Reader> searchByBoolQuery(BoolQueryBuilder boolQueryBuilder) {
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .build();

        return elasticsearchRestTemplate.search(searchQuery, Reader.class,  IndexCoordinates.of("readers"));
    }
}
