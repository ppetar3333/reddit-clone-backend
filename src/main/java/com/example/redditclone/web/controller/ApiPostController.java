package com.example.redditclone.web.controller;

import com.example.redditclone.models.*;
import com.example.redditclone.service.PostService;
import com.example.redditclone.service.SubredditService;
import com.example.redditclone.service.elasticsearch.PostElasticService;
import com.example.redditclone.web.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping({"api/posts"})
public class ApiPostController {

    @Autowired
    private PostService postService;
    @Autowired
    private SubredditService subredditService;
    @Autowired
    private Converter<List<Post>, List<PostDto>> toDtoPost;
    @Autowired
    private Converter<Post, PostDto> toDto;
    @Autowired
    private Converter<PostDto, Post> toPost;
    @Autowired
    private Converter<UserDto, User> toUserDto;
    @Autowired
    private Converter<SubredditDto, Subreddit> toSubredditDto;
    @Autowired
    private Converter<FlairDto, Flair> toFlairDto;

    private final PostElasticService postElasticService;

    public ApiPostController(PostElasticService postElasticService) {
        this.postElasticService = postElasticService;
    }

    @CrossOrigin(origins = {"http://localhost:8080"}, maxAge = 3600L)

    @GetMapping()
    public ResponseEntity<List<PostDto>> getAll() {
        List<Post> posts = postService.all();
        List<PostDto> body = toDtoPost.convert(posts);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @GetMapping("/subreddit/{id}")
    public ResponseEntity<List<PostDto>> getPostsBySubreddit(@PathVariable("id") Long id) {
        List<Post> posts = postService.findPostBySubredditSubredditID(id);
        List<PostDto> postDtos = toDtoPost.convert(posts);
        return new ResponseEntity<>(postDtos, HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<PostDto>> getPostsByUser(@PathVariable("id") Long id) {
        List<Post> posts = postService.findPostByUserUserID(id);
        List<PostDto> postDtos = toDtoPost.convert(posts);
        return new ResponseEntity<>(postDtos, HttpStatus.OK);
    }

    @GetMapping("/findOne/{id}")
    public ResponseEntity<PostDto> getById(@PathVariable("id") Long id) {
        Optional<Post> post = Optional.of(postService.one(id).orElse(new Post()));
        PostDto postDto = toDto.convert(post.get());
        return new ResponseEntity<>(postDto, HttpStatus.OK);
    }

    @DeleteMapping( "/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        postService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping( "/deleteBySubreddit/{id}")
    public ResponseEntity<Void> deleteBySubreddit(@PathVariable("id") Long id) {
        List<Post> posts = postService.findPostBySubredditSubredditID(id);
        for (Post post : posts) {
            postService.delete(post.getPostID());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<PostDto> update(@RequestBody PostDto postDto, @PathVariable("id") Long id) {
        if (!id.equals(postDto.getPostID())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            Post persisted = this.postService.save(toPost.convert(postDto));
            return new ResponseEntity<>(toDto.convert(persisted), HttpStatus.OK);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<Long> save(@RequestBody PostDto postDto) {

        Post post = new Post();

        post.setTitle(postDto.getTitle());
        post.setText(postDto.getText());
        post.setVoteCount(1);
        post.setImagePath(postDto.getImagePath());
        post.setCreationDate(LocalDateTime.now());
        post.setUser(toUserDto.convert(postDto.getUser()));
        post.setSubreddit(toSubredditDto.convert(postDto.getSubreddit()));
        if(postDto.getFlair() != null) post.setFlair(toFlairDto.convert(postDto.getFlair()));

        postService.save(post);

        return new ResponseEntity<>(post.getPostID(), HttpStatus.CREATED);
    }

    @GetMapping("/sort-posts/{option}")
    public ResponseEntity<List<PostDto>> sortPosts(@PathVariable("option") String option) {
        List<Post> posts;

        if(option.equals("New")) {
            posts = postService.findAllByOrderByCreationDateDesc();
        } else if(option.equals("Top")) {
            posts = postService.findAllByOrderByVoteCountDesc();
        } else {
            posts = postService.findAllByOrderByVoteCountDescCreationDateDesc();
        }

        List<PostDto> body = toDtoPost.convert(posts);

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @GetMapping("/sort-posts/by-subreddit/{id}/{option}")
    public ResponseEntity<List<PostDto>> sortPostsBySubreddit(@PathVariable("id") Long id, @PathVariable("option") String option) {
        List<Post> posts;

        if(option.equals("New")) {
            posts = postService.findAllBySubredditSubredditIDOrderByCreationDateDesc(id);
        } else if(option.equals("Top")) {
            posts = postService.findAllBySubredditSubredditIDOrderByVoteCountDesc(id);
        } else {
            posts = postService.findAllBySubredditSubredditIDOrderByVoteCountDescCreationDateDesc(id);
        }

        List<PostDto> body = toDtoPost.convert(posts);

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    // ELASTICSEARCH
    @PostMapping("/indexAll")
    public ResponseEntity<String> indexAll() throws JsonProcessingException {
        List<Post> posts = postService.all();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        List<PostElastic> postElastics = posts.stream()
                .map(post -> new PostElastic(post.getPostID(), post.getTitle(), post.getText(), post.getKeywords(),
                        post.getFilename(), post.getCreationDate().format(formatter), post.getVoteCount(), post.getImagePath(),
                        new UserResponseDto(post.getUser().getUserID(), post.getUser().getUsername(), post.getUser().getDisplayName(), post.getUser().getRole().toString(), post.getUser().getAvatar()),
                        post.getFlair() == null ? null : new FlairResponseDto(post.getFlair().getFlairID(), post.getFlair().getName()),
                        post.getSubreddit() == null ? null : new SubredditResponseDto(post.getSubreddit().getSubredditID(), post.getSubreddit().getName(), post.getSubreddit().getDescription())))
                .collect(Collectors.toList());

        postElasticService.index(postElastics);

        return ResponseEntity.ok("All posts indexed successfully");
    }

    @GetMapping("/text")
    public List<PostResponseDto> findPostsByText(@RequestBody ObjectNode objectNode){
        return postElasticService.findPostsByText(String.valueOf(objectNode.get("text")));
    }

    @GetMapping("/title")
    public List<PostResponseDto> findPostsByTitle(@RequestBody ObjectNode objectNode){
        return postElasticService.findPostsByTitle(String.valueOf(objectNode.get("title")));
    }

}
