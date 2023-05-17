package com.example.redditclone.service.elasticsearch;

import com.example.redditclone.lucene.indexing.handlers.*;
import com.example.redditclone.models.Post;
import com.example.redditclone.models.PostElastic;
import com.example.redditclone.repository.elasticsearch.PostElasticRepository;
import com.example.redditclone.service.SearchQueryGenerator;
import com.example.redditclone.web.dto.*;
import com.example.redditclone.web.dto.mapper.PostMapper;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PostElasticService {

    private final PostElasticRepository postElasticRepository;
    private final ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Value("${files.path}")
    private String filesPath;

    public PostElasticService( PostElasticRepository postElasticRepository, ElasticsearchRestTemplate elasticsearchRestTemplate) {
        this.postElasticRepository = postElasticRepository;
        this.elasticsearchRestTemplate = elasticsearchRestTemplate;
    }

    public List<PostResponseDto> findPostsByText(String text){
        List<PostElastic> posts = postElasticRepository.findAllByText(text);
        return mapPostsToPostDTO(posts);
    }

    public List<PostResponseDto> findAllByVoteCountBetween(Long bottom, Long top) {
        List<PostElastic> posts = postElasticRepository.findAllByVoteCountBetween(bottom, top);
        return mapPostsToPostDTO(posts);
    }

    public List<PostResponseDto> findAllByVoteCountLessThanEqual(Long top) {
        List<PostElastic> posts = postElasticRepository.findAllByVoteCountLessThanEqual(top);
        return mapPostsToPostDTO(posts);
    }

    public List<PostResponseDto> findAllByVoteCountGreaterThanEqual(Long bottom) {
        List<PostElastic> posts = postElasticRepository.findAllByVoteCountGreaterThanEqual(bottom);
        return mapPostsToPostDTO(posts);
    }

    public List<PostResponseDto> findPostsByTextPdf(String text){
        List<PostElastic> posts = postElasticRepository.findAllByTextFromPdf(text);
        return mapPostsToPostDTO(posts);
    }

    public List<PostResponseDto> findPostsByTitle(String title){
        List<PostElastic> posts = postElasticRepository.findAllByTitle(title);
        return mapPostsToPostDTO(posts);
    }

    public List<PostResponseDto> all(){
        Iterable<PostElastic> posts = postElasticRepository.findAll();
        List<PostElastic> postList = StreamSupport.stream(posts.spliterator(), false)
                .collect(Collectors.toList());
        return mapPostsToPostDTO(postList);
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

    public void save(PostElastic postElastic) {
        postElasticRepository.save(postElastic);
    }

    public void indexUploadedFile(PostDto post, String keywords, String filename, Post post1, String text) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (MultipartFile file : post.getFiles()) {
            if (file.isEmpty()) {
                continue;
            }

            String fileName = saveUploadedFileInFolder(file);
            if(fileName != null){
                PostElastic postElastic = getHandler(fileName).getIndexUnit(new File(fileName));

                postElastic.setId(post1.getPostID().toString());
                postElastic.setTitle(post.getTitle());
                postElastic.setText(post.getText());
                postElastic.setFilename(filename);
                postElastic.setKeywords(keywords);
                postElastic.setCreationDate(LocalDateTime.now().format(formatter));
                postElastic.setVoteCount(post.getVoteCount());
                postElastic.setImagePath(post.getImagePath());
                postElastic.setUser(new UserResponseDto(post1.getUser().getUserID(), post1.getUser().getUsername(), post1.getUser().getDisplayName(), post1.getUser().getRole().toString(), post1.getUser().getAvatar()));
                postElastic.setFlair(post1.getFlair() == null ? null : new FlairResponseDto(post1.getFlair().getFlairID(), post1.getFlair().getName()));
                postElastic.setSubreddit(post1.getSubreddit() == null ? null : new SubredditResponseDto(post1.getSubreddit().getSubredditID().toString(), post1.getSubreddit().getName(), post1.getSubreddit().getDescription(), post1.getSubreddit().getCreationDate().format(formatter), post1.getSubreddit().isSuspended(), post1.getSubreddit().getSuspendedReason(), post1.getSubreddit().getRules(), post1.getSubreddit().getTextFromPdf(), post1.getSubreddit().getFilename(), post1.getSubreddit().getKeywords()));
                postElastic.setTextFromPdf(text);

                save(postElastic);
            }
        }
    }

    private String saveUploadedFileInFolder(MultipartFile file) throws IOException {
        String retVal = null;
        if (!file.isEmpty()) {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(new File(filesPath).getAbsolutePath() + File.separator + file.getOriginalFilename());
            Files.write(path, bytes);
            retVal = path.toString();
        }
        return retVal;
    }

    public void reindex() {
        File dataDir = new File(filesPath);
        indexUnitFromFile(dataDir);
    }

    public int indexUnitFromFile(File file) {
        DocumentHandler handler;
        String fileName;
        int retVal = 0;
        try {
            File[] files;
            if(file.isDirectory()){
                files = file.listFiles();
            }else{
                files = new File[1];
                files[0] = file;
            }
            assert files != null;
            for(File newFile : files){
                if(newFile.isFile()){
                    fileName = newFile.getName();
                    handler = getHandler(fileName);
                    if(handler == null){
                        System.out.println("Nije moguce indeksirati dokument sa nazivom: " + fileName);
                        continue;
                    }
                    save(handler.getIndexUnit(newFile));
                    retVal++;
                } else if (newFile.isDirectory()){
                    retVal += indexUnitFromFile(newFile);
                }
            }
            System.out.println("indexing done");
        } catch (Exception e) {
            System.out.println("indexing NOT done");
        }
        return retVal;
    }

    public DocumentHandler getHandler(String fileName){
        return getDocumentHandler(fileName);
    }

    public static DocumentHandler getDocumentHandler(String fileName) {
        if(fileName.endsWith(".txt")){
            return new TextDocHandler();
        }else if(fileName.endsWith(".pdf")){
            return new PDFHandler();
        }else if(fileName.endsWith(".doc")){
            return new WordHandler();
        }else if(fileName.endsWith(".docx")){
            return new Word2007Handler();
        }else{
            return null;
        }
    }

    public List<PostResponseDto> findByTextOrTitle(String title, String text, String operator) {
        QueryBuilder titleQuery = SearchQueryGenerator.createMatchQueryBuilder(new SimpleQueryEs("title", title));
        QueryBuilder textQuery = SearchQueryGenerator.createMatchQueryBuilder(new SimpleQueryEs("text", text));

        BoolQueryBuilder boolQuery = new BoolQueryBuilder();

        if ("and".equals(operator)) {
            boolQuery.must(titleQuery);
            boolQuery.must(textQuery);
        } else if ("or".equals(operator)) {
            boolQuery.should(titleQuery);
            boolQuery.should(textQuery);
        }

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQuery)
                .build();

        return PostMapper.mapDtos(searchByBoolQuery(searchQuery));
    }

    private SearchHits<PostElastic> searchByBoolQuery(NativeSearchQuery searchQuery) {
        return elasticsearchRestTemplate.search(searchQuery, PostElastic.class, IndexCoordinates.of("post"));
    }

}
