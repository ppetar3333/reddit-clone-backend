package com.example.redditclone.web.controller;

import com.example.redditclone.lucene.indexing.filters.CyrillicLatinConverter;
import com.example.redditclone.models.*;
import com.example.redditclone.service.EmailService;
import com.example.redditclone.service.SubredditService;
import com.example.redditclone.service.elasticsearch.PostElasticService;
import com.example.redditclone.service.elasticsearch.SubredditElasticService;
import com.example.redditclone.web.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
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

    @PostMapping("/save")
    public ResponseEntity<SubredditDto> save(@RequestBody SubredditDto subredditDto) throws MessagingException {
        Subreddit data = new Subreddit();

        data.setName(subredditDto.getName());
        data.setDescription(subredditDto.getDescription());
        data.setCreationDate(LocalDateTime.now());
        data.setSuspended(subredditDto.isSuspended());
        data.setRules(subredditDto.getRules());
        data.setSuspendedReason(subredditDto.getSuspendedReason());
        data.setModerator(subredditDto.getModerators());

        subredditService.save(data);

        for(User moderator : subredditDto.getModerators()) {
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
                        subreddit.getKeywords()))
                .collect(Collectors.toList());

        subredditElasticService.index(subredditElastics);

        return ResponseEntity.ok("All subreddits indexed successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<List<SubredditResponseDto>> getAllElastic() {
        List<SubredditResponseDto> subredditResponseDtos = subredditElasticService.all();
        return new ResponseEntity<>(subredditResponseDtos, HttpStatus.OK);
    }

    @GetMapping("/name")
    public List<SubredditResponseDto> findSubredditsByName(@RequestBody ObjectNode objectNode){
        String rawName = String.valueOf(objectNode.get("name"));
        String name = normalizeTitle(rawName).toLowerCase();
        return subredditElasticService.findSubredditsByName(name);
    }

    @GetMapping("/description")
    public List<SubredditResponseDto> findSubredditsByDescription(@RequestBody ObjectNode objectNode){
        String rawDescription = String.valueOf(objectNode.get("description"));
        String description = normalizeTitle(rawDescription).toLowerCase();
        return subredditElasticService.findSubredditsByDescription(description);
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
