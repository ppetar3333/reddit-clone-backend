package com.example.redditclone.web.controller;

import com.example.redditclone.lucene.indexing.filters.CyrillicLatinConverter;
import com.example.redditclone.models.*;
import com.example.redditclone.service.FlairService;
import com.example.redditclone.service.PostService;
import com.example.redditclone.service.SubredditService;
import com.example.redditclone.service.UserService;
import com.example.redditclone.service.elasticsearch.PostElasticService;
import com.example.redditclone.web.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.elasticsearch.index.query.QueryBuilders;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
    private FlairService flairService;
    @Autowired
    private UserService userService;
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

    @GetMapping("/reindex")
    public void reindex(){
        postElasticService.reindex();
    }

    @PostMapping(path = "/save/{userid}/{flairid}/{subreditid}", consumes = {"multipart/form-data"})
    public ResponseEntity<Long> save(@ModelAttribute PostDto postDto, @PathVariable("userid") Long userid, @PathVariable("flairid") Long flairid, @PathVariable("subreditid") Long subreditid) throws IOException {

        Optional<Flair> flair = flairService.one(flairid);
        Optional<User> user = userService.one(userid);
        Optional<Subreddit> subreddit = subredditService.one(subreditid);

        Post post = new Post();

        post.setTitle(postDto.getTitle());
        post.setText(postDto.getText());
        post.setVoteCount(1);
        post.setImagePath(postDto.getImagePath());
        post.setCreationDate(LocalDateTime.now());
        post.setUser(user.get());
        post.setSubreddit(subreddit.get());
        if(flair.get().getFlairID() != null) post.setFlair(flair.get());

        String text = "";

        if (postDto.getFiles()[0].getOriginalFilename() != null) {
            InputStream is = postDto.getFiles()[0].getInputStream();

            PDDocument document = PDDocument.load(is);
            PDFTextStripper pdfStripper = new PDFTextStripper();
            text = pdfStripper.getText(document);
            post.setTextFromPdf(text);

            is.close();
            document.close();
        }

        postService.save(post);

        postElasticService.indexUploadedFile(postDto, post.getKeywords(), post.getFilename(), post, text);

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
                .map(post -> new PostElastic(post.getPostID().toString(), post.getTitle(), post.getText(), post.getKeywords(),
                        post.getFilename(), post.getCreationDate().format(formatter), post.getVoteCount(), post.getImagePath(),
                        new UserResponseDto(post.getUser().getUserID(), post.getUser().getUsername(), post.getUser().getDisplayName(), post.getUser().getRole().toString(), post.getUser().getAvatar()),
                        post.getFlair() == null ? null : new FlairResponseDto(post.getFlair().getFlairID(), post.getFlair().getName()),
                        post.getSubreddit() == null ? null : new SubredditResponseDto(post.getSubreddit().getSubredditID().toString(), post.getSubreddit().getName(), post.getSubreddit().getDescription(), post.getSubreddit().getCreationDate().format(formatter), post.getSubreddit().isSuspended(), post.getSubreddit().getSuspendedReason(), post.getSubreddit().getRules(), post.getSubreddit().getTextFromPdf(), post.getSubreddit().getFilename(), post.getSubreddit().getKeywords()), post.getTextFromPdf()))
                .collect(Collectors.toList());

        postElasticService.index(postElastics);

        return ResponseEntity.ok("All posts indexed successfully");
    }

    @GetMapping("/text")
    public List<PostResponseDto> findPostsByText(@RequestBody ObjectNode objectNode){
        String textRaw = String.valueOf(objectNode.get("text")).toLowerCase();
        String text = normalizeTitle(textRaw).toLowerCase();
        return postElasticService.findPostsByText(text);
    }

    @GetMapping("/text-pdf")
    public List<PostResponseDto> findPostsByTextPdf(@RequestBody ObjectNode objectNode){
        String textPdfRaw = String.valueOf(objectNode.get("textPdf"));
        String textPdf = normalizeTitle(textPdfRaw).toLowerCase();
        return postElasticService.findPostsByTextPdf(textPdf);
    }

    @GetMapping("/title")
    public List<PostResponseDto> findPostsByTitle(@RequestBody ObjectNode objectNode){
        String rawTitle = String.valueOf(objectNode.get("title"));
        String title = normalizeTitle(rawTitle).toLowerCase();
        return postElasticService.findPostsByTitle(title);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PostResponseDto>> getAllElastic() {
        List<PostResponseDto> posts = postElasticService.all();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }
    
    @GetMapping("/karma")
    public ResponseEntity<List<PostResponseDto>> getByKarma(@RequestParam(value = "bottom", required = false) Long bottom, @RequestParam(value = "top", required = false) Long top) {
        List<PostResponseDto> posts;

        if (top != null && bottom != null) {
            posts = postElasticService.findAllByVoteCountBetween(bottom, top);
        } else if (top != null) {
            posts = postElasticService.findAllByVoteCountLessThanEqual(top);
        } else if (bottom != null) {
            posts = postElasticService.findAllByVoteCountGreaterThanEqual(bottom);
        } else {
            posts = new ArrayList<>();
        }

        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/text-title")
    public List<PostResponseDto> searchPosts(@RequestBody ObjectNode objectNode) {
        String title = normalizeTitle(objectNode.get("title").asText().toLowerCase());
        String text = normalizeTitle(objectNode.get("text").asText().toLowerCase());
        String operator = objectNode.get("operator").asText();

        return postElasticService.findByTextOrTitle(title, text, operator);
    }

    private String normalizeTitle(String title) {
        boolean containsCyrillic = false;
        boolean containsLatinic = false;

        // check if title contains Cyrillic or Latinic characters
        for (char c : title.toCharArray()) {
            if (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CYRILLIC) {
                containsCyrillic = true;
            } else if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
                containsLatinic = true;
            }
        }

        // if title contains Cyrillic characters, convert to Latinic
        if (containsCyrillic) {
            return CyrillicLatinConverter.cir2lat(title);
        } else if (containsLatinic) {
            return title;
        } else {
            // if title contains neither Cyrillic nor Latinic characters, return empty string
            return "";
        }
    }
}
