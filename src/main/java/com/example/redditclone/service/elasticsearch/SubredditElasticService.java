package com.example.redditclone.service.elasticsearch;

import com.example.redditclone.lucene.indexing.handlers.*;
import com.example.redditclone.models.Post;
import com.example.redditclone.models.PostElastic;
import com.example.redditclone.models.SubredditElastic;
import com.example.redditclone.repository.elasticsearch.PostElasticRepository;
import com.example.redditclone.repository.elasticsearch.SubredditElasticRepository;
import com.example.redditclone.web.dto.*;
import com.example.redditclone.web.dto.mapper.PostMapper;
import com.example.redditclone.web.dto.mapper.SubredditMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
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
public class SubredditElasticService {

    private final SubredditElasticRepository subredditElasticRepository;
    private final ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Value("${files.path}")
    private String filesPath;

    public SubredditElasticService(SubredditElasticRepository subredditElasticRepository, ElasticsearchRestTemplate elasticsearchRestTemplate) {
        this.subredditElasticRepository = subredditElasticRepository;
        this.elasticsearchRestTemplate = elasticsearchRestTemplate;
    }

    public List<SubredditResponseDto> findSubredditsByDescription(String description){
        List<SubredditElastic> subreddits = subredditElasticRepository.findAllByDescription(description);
        return mapSubredditToSubredditDTO(subreddits);
    }

    public List<SubredditResponseDto> findSubredditsByName(String name){
        List<SubredditElastic> subreddits = subredditElasticRepository.findAllByName(name);
        return mapSubredditToSubredditDTO(subreddits);
    }

    public List<SubredditResponseDto> findSubredditsByTextPdf(String text){
        List<SubredditElastic> subreddits = subredditElasticRepository.findAllByTextFromPdf(text);
        return mapSubredditToSubredditDTO(subreddits);
    }

    public List<SubredditResponseDto> all(){
        Iterable<SubredditElastic> subreddits = subredditElasticRepository.findAll();
        List<SubredditElastic> subredditElastics = StreamSupport.stream(subreddits.spliterator(), false)
                .collect(Collectors.toList());
        return mapSubredditToSubredditDTO(subredditElastics);
    }

    public List<SubredditResponseDto> findAllByPostsCountBetween(Long bottom, Long top) {
        List<SubredditElastic> subreddits = subredditElasticRepository.findAllByPostsCountBetween(bottom, top);
        return mapSubredditToSubredditDTO(subreddits);
    }

    public List<SubredditResponseDto> findAllByPostsCountLessThanEqual(Long top) {
        List<SubredditElastic> subreddits = subredditElasticRepository.findAllByPostsCountLessThanEqual(top);
        return mapSubredditToSubredditDTO(subreddits);
    }

    public List<SubredditResponseDto> findAllByPostsCountGreaterThanEqual(Long bottom) {
        List<SubredditElastic> subreddits = subredditElasticRepository.findAllByPostsCountGreaterThanEqual(bottom);
        return mapSubredditToSubredditDTO(subreddits);
    }

    public void index(List<SubredditElastic> subreddits) {
        subredditElasticRepository.saveAll(subreddits);
    }

    public void save(SubredditElastic subredditElastic) {
        subredditElasticRepository.save(subredditElastic);
    }

    public void reindex() {
        File dataDir = new File(filesPath);
        indexUnitFromFile(dataDir);
    }

    public int indexUnitFromFile(File file) {
        DocumentHandlerSubreddit handler;
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

    public DocumentHandlerSubreddit getHandler(String fileName){
        return getDocumentHandler(fileName);
    }

    public static DocumentHandlerSubreddit getDocumentHandler(String fileName) {
        if(fileName.endsWith(".txt")){
            return new TextDocHandlerSubreddit();
        }else if(fileName.endsWith(".pdf")){
            return new TextDocHandlerSubreddit();
        }else if(fileName.endsWith(".doc")){
            return new TextDocHandlerSubreddit();
        }else if(fileName.endsWith(".docx")){
            return new TextDocHandlerSubreddit();
        }else{
            return null;
        }
    }

    public void indexUploadedFile(SubredditDto subreddit, String keywords, String filename, String text, Long id, List<String> rules, MultipartFile[] files) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if (files != null) {
            for (MultipartFile file : files) {
                if (file.isEmpty()) {
                    continue;
                }

                String fileName = saveUploadedFileInFolder(file);
                if (fileName != null) {
                    SubredditElastic subredditElastic = getHandler(fileName).getIndexUnit(new File(fileName));
                    setSubredditElasticFields(subredditElastic, subreddit, id.toString(), filename, text, rules, keywords);
                    save(subredditElastic);
                }
            }
        } else {
            SubredditElastic subredditElastic = new SubredditElastic();
            setSubredditElasticFields(subredditElastic, subreddit, id.toString(), filename, text, rules, keywords);
            save(subredditElastic);
        }
    }

    private void setSubredditElasticFields(SubredditElastic subredditElastic, SubredditDto subreddit, String id, String filename, String text, List<String> rules, String keywords) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        subredditElastic.setId(id);
        subredditElastic.setName(subreddit.getName());
        subredditElastic.setDescription(subreddit.getDescription());
        subredditElastic.setCreationDate(LocalDateTime.now().format(formatter));
        subredditElastic.setSuspended(subreddit.isSuspended());
        subredditElastic.setSuspendedReason("");
        subredditElastic.setRules(rules);
        subredditElastic.setTextFromPdf(text);
        subredditElastic.setFilename(filename);
        subredditElastic.setKeywords(keywords);
        subredditElastic.setPostsCount(0);
    }

    public void incrementPostsCount(Long id, int postsCount) {
        SubredditElastic existingSubreddit = subredditElasticRepository.findById(id).orElse(null);

        if (existingSubreddit != null) {
            existingSubreddit.setPostsCount(postsCount);

            subredditElasticRepository.save(existingSubreddit);
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

    private List<SubredditResponseDto> mapSubredditToSubredditDTO(List<SubredditElastic> subreddits){
        List<SubredditResponseDto> subredditsDto = new ArrayList<>();
        for(SubredditElastic subredditElastic : subreddits){
            subredditsDto.add(SubredditMapper.mapResponseDto(subredditElastic));
        }
        return subredditsDto;
    }
}
