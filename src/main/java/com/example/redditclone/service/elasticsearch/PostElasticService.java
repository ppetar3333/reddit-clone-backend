package com.example.redditclone.service.elasticsearch;

import com.example.redditclone.models.Post;
import com.example.redditclone.models.PostElastic;
import com.example.redditclone.repository.elasticsearch.PostElasticRepository;
import com.example.redditclone.web.dto.PostDto;
import com.example.redditclone.web.dto.PostResponseDto;
import com.example.redditclone.web.dto.mapper.PostMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostElasticService {

    private final PostElasticRepository postElasticRepository;
    private final ElasticsearchRestTemplate elasticsearchRestTemplate;

    public PostElasticService( PostElasticRepository postElasticRepository, ElasticsearchRestTemplate elasticsearchRestTemplate) {
        this.postElasticRepository = postElasticRepository;
        this.elasticsearchRestTemplate = elasticsearchRestTemplate;
    }

    public List<PostResponseDto> findPostsByText(String text){
        List<PostElastic> posts = postElasticRepository.findAllByText(text);
        return mapPostsToPostDTO(posts);
    }

    public List<PostResponseDto> findPostsByTitle(String title){
        List<PostElastic> posts = postElasticRepository.findAllByTitle(title);
        return mapPostsToPostDTO(posts);
    }

    private List<PostResponseDto> mapPostsToPostDTO(List<PostElastic> posts){
        List<PostResponseDto> postsDto = new ArrayList<>();
        for(PostElastic postElastic : posts){
            postsDto.add(PostMapper.mapResponseDto(postElastic));
        }
        return postsDto;
    }

    public void index(List<PostElastic> posts) {
        postElasticRepository.saveAll(posts);
    }
}
