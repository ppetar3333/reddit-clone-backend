package com.example.redditclone.web.controller;

import com.example.redditclone.lucene.indexing.filters.CyrillicLatinConverter;
import com.example.redditclone.models.*;
import com.example.redditclone.service.EmailService;
import com.example.redditclone.service.PostService;
import com.example.redditclone.service.SubredditService;
import com.example.redditclone.service.UserService;
import com.example.redditclone.service.elasticsearch.PostElasticService;
import com.example.redditclone.service.elasticsearch.SubredditElasticService;
import com.example.redditclone.web.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping({"api/subreddits"})
public class ApiSubredditController {

    @Autowired
    private SubredditService subredditService;
    @Autowired
    private Converter<List<Subreddit>, List<SubredditDto>> toDtoSubreddit;
    @Autowired
    private Converter<Subreddit, SubredditDto> toDto;
    @Autowired
    private Converter<SubredditDto, Subreddit> toSubreddit;
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;
    @Autowired
    private EmailService emailService;
    private final SubredditElasticService subredditElasticService;

    public ApiSubredditController(SubredditElasticService subredditElasticService) {
        this.subredditElasticService = subredditElasticService;
    }

    @CrossOrigin(origins = {"http://localhost:8080"}, maxAge = 3600L)

    @GetMapping()
    public ResponseEntity<List<SubredditDto>> getAll() {
        List<Subreddit> subreddits = subredditService.all();
        List<SubredditDto> body = toDtoSubreddit.convert(subreddits);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @GetMapping("/findOne/{id}")
    public ResponseEntity<SubredditDto> getById(@PathVariable("id") Long id) {
        Optional<Subreddit> subreddit = Optional.of(subredditService.one(id).orElse(new Subreddit()));
        SubredditDto subredditDto = toDto.convert(subreddit.get());
        return new ResponseEntity<>(subredditDto, HttpStatus.OK);
    }

    @GetMapping("/byName/{name}")
    public ResponseEntity<SubredditDto> getByName(@PathVariable("name") String name) {
        Optional<Subreddit> subreddit = Optional.of(subredditService.findSubredditByName(name).orElse(new Subreddit()));
        SubredditDto subredditDto = toDto.convert(subreddit.get());
        return new ResponseEntity<>(subredditDto, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        subredditService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<SubredditDto> update(@RequestBody SubredditDto subredditDto,@PathVariable("id") Long id) {
        if (!id.equals(subredditDto.getSubredditID())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            Subreddit persisted = this.subredditService.save(toSubreddit.convert(subredditDto));
            return new ResponseEntity<>(toDto.convert(persisted), HttpStatus.OK);
        }
    }

    @PostMapping(path = "/save/{userid}", consumes = {"multipart/form-data"})
    public ResponseEntity<SubredditDto> save(@ModelAttribute SubredditDto subredditDto, @PathVariable("userid") Long userid, @RequestParam(value ="files", required=false) MultipartFile[] files) throws MessagingException, IOException {
        Optional<User> user = userService.one(userid);
        Set<User> moderators = new HashSet<>();
        moderators.add(user.get());

        List<String> rules = new ArrayList<>();
        rules.add("1");
        rules.add("2");
        rules.add("3");

        Subreddit data = new Subreddit();

        data.setName(subredditDto.getName());
        data.setDescription(subredditDto.getDescription());
        data.setCreationDate(LocalDateTime.now());
        data.setSuspended(false);
        data.setRules(rules);
        data.setSuspendedReason("");
        data.setModerator(moderators);
        data.setPostsCount(0);

        String text = "";
        String filename = "";

        if (files != null) {
            MultipartFile file = files[0];

            if (file.getContentType().equalsIgnoreCase("application/pdf")) {
                InputStream is = file.getInputStream();

                PDDocument document = PDDocument.load(is);
                PDFTextStripper pdfStripper = new PDFTextStripper();
                text = pdfStripper.getText(document);
                data.setTextFromPdf(text);
                filename = file.getOriginalFilename();

                is.close();
                document.close();
            }
        }

        Subreddit saved = subredditService.save(data);

        subredditElasticService.indexUploadedFile(subredditDto, data.getKeywords(), filename, text, saved.getSubredditID(), saved.getRules(), files);

        for(User moderator : data.getModerator()) {
            subredditService.saveSubredditModerators(moderator.getUserID(), data.getSubredditID());
            emailService.sendMail(moderator.getEmail(), "New Subreddit", "You are now moderator of your own subreddit, enjoy!");
        }

        return new ResponseEntity<>(toDto.convert(data), HttpStatus.CREATED);
    }

    // ELASTICSEARCH

    @PostMapping("/indexAll")
    public ResponseEntity<String> indexAll() throws JsonProcessingException {
        List<Subreddit> subreddits = subredditService.all();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        List<SubredditElastic> subredditElastics = subreddits.stream()
                .map(subreddit -> new SubredditElastic(
                        subreddit.getSubredditID().toString(),
                        subreddit.getName(),
                        subreddit.getDescription(),
                        subreddit.getCreationDate().format(formatter),
                        subreddit.isSuspended(),
                        subreddit.getSuspendedReason(),
                        subreddit.getRules(),
                        subreddit.getTextFromPdf(),
                        subreddit.getFilename(),
                        subreddit.getKeywords(),
                        subreddit.getPostsCount()))
                .collect(Collectors.toList());

        subredditElasticService.index(subredditElastics);

        return ResponseEntity.ok("All subreddits indexed successfully");
    }

    @GetMapping("/reindex")
    public void reindex(){
        subredditElasticService.reindex();
    }

    @GetMapping("/all")
    public ResponseEntity<List<SubredditResponseDto>> getAllElastic() {
        List<SubredditResponseDto> subredditResponseDtos = subredditElasticService.all();
        return new ResponseEntity<>(subredditResponseDtos, HttpStatus.OK);
    }

    @PostMapping("/name")
    public List<SubredditResponseDto> findSubredditsByName(@RequestBody ObjectNode objectNode){
        String rawName = String.valueOf(objectNode.get("name"));
        String name = normalizeTitle(rawName).toLowerCase();
        return subredditElasticService.findSubredditsByName(name);
    }

    @PostMapping("/description")
    public List<SubredditResponseDto> findSubredditsByDescription(@RequestBody ObjectNode objectNode){
        String rawDescription = String.valueOf(objectNode.get("description"));
        String description = normalizeTitle(rawDescription).toLowerCase();
        return subredditElasticService.findSubredditsByDescription(description);
    }

    @PostMapping("/text-pdf")
    public List<SubredditResponseDto> findPostsByTextPdf(@RequestBody ObjectNode objectNode){
        String textPdfRaw = String.valueOf(objectNode.get("textPdf"));
        String textPdf = normalizeTitle(textPdfRaw).toLowerCase();
        if (containsCyrillicCharacters(textPdfRaw)) {

            List<SubredditResponseDto> result = subredditElasticService.findSubredditsByTextPdf(textPdfRaw);

            if (!result.isEmpty()) {
                return result;
            } else {
                List<SubredditResponseDto> result2 = subredditElasticService.findSubredditsByTextPdf(textPdf);
                return result2;
            }

        } else {
            return subredditElasticService.findSubredditsByTextPdf(textPdf);
        }
    }

    @GetMapping("/posts-count")
    public ResponseEntity<List<SubredditResponseDto>> getByPostCount(@RequestParam(value = "bottom", required = false) Long bottom, @RequestParam(value = "top", required = false) Long top) {
        List<SubredditResponseDto> subreddits;

        if (top != null && bottom != null) {
            subreddits = subredditElasticService.findAllByPostsCountBetween(bottom, top);
        } else if (top != null) {
            subreddits = subredditElasticService.findAllByPostsCountLessThanEqual(top);
        } else if (bottom != null) {
            subreddits = subredditElasticService.findAllByPostsCountGreaterThanEqual(bottom);
        } else {
            subreddits = new ArrayList<>();
        }

        return new ResponseEntity<>(subreddits, HttpStatus.OK);
    }


    private boolean containsCyrillicCharacters(String text) {
        for (char c : text.toCharArray()) {
            if (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CYRILLIC) {
                return true;
            }
        }
        return false;
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
