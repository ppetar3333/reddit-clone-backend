package com.example.redditclone.web.controller;

import com.example.redditclone.models.Flair;
import com.example.redditclone.models.Post;
import com.example.redditclone.service.FlairService;
import com.example.redditclone.web.dto.FlairDto;
import com.example.redditclone.web.dto.PostDto;
import com.example.redditclone.web.dto.SubredditDto;
import com.example.redditclone.web.dto.SubredditFlairsDto;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping({"api/flairs"})
public class ApiFlairController {

    @Autowired
    private FlairService flairService;
    @Autowired
    private Converter<List<Flair>, List<FlairDto>> toDtoFlair;
    @Autowired
    private Converter<Flair, FlairDto> toDto;
    @Autowired
    private Converter<FlairDto, Flair> toFlair;

    @CrossOrigin(origins = {"http://localhost:8080"}, maxAge = 3600L)

    @GetMapping()
    public ResponseEntity<List<FlairDto>> getAll() {
        List<Flair> flairs = flairService.all();
        List<FlairDto> body = toDtoFlair.convert(flairs);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @GetMapping("/findOne/{id}")
    public ResponseEntity<FlairDto> getById(@PathVariable("id") Long id) {
        Optional<Flair> flair = Optional.of(flairService.one(id).orElse(new Flair()));
        FlairDto flairDto = toDto.convert(flair.get());
        return new ResponseEntity<>(flairDto, HttpStatus.OK);
    }

    @GetMapping("/byName/{name}")
    public ResponseEntity<FlairDto> getByName(@PathVariable("name") String name) {
        Optional<Flair> flair = Optional.of(flairService.findFlairByName(name).orElse(new Flair()));
        FlairDto flairDto = toDto.convert(flair.get());
        return new ResponseEntity<>(flairDto, HttpStatus.OK);
    }

    @GetMapping("/bySubreddit/{id}")
    public ResponseEntity<List<SubredditFlairsDto>> getByCommunityName(@PathVariable("id") Long id) {
        List<SubredditFlairsDto> flair = flairService.getFlairsBySubredditID(id);
        return new ResponseEntity<>(flair, HttpStatus.OK);
    }

    @DeleteMapping("/delete/by-subreddit/{subredditid}/{flairName}")
    public ResponseEntity<Void> getFlairInsideSubreddit(@PathVariable("subredditid") Long subredditid, @PathVariable("flairName") String flairName) {
        Optional<Flair> flair = flairService.findFlairByName(flairName);
        flairService.deleteFlairInSubreddit(flair.get().getFlairID(), subredditid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        flairService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<FlairDto> update(@RequestBody FlairDto flairDto, @PathVariable("id") Long id) {
        if (!id.equals(flairDto.getFlairID())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            Flair persisted = this.flairService.save(toFlair.convert(flairDto));
            return new ResponseEntity<>(toDto.convert(persisted), HttpStatus.OK);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<FlairDto> save(@RequestBody FlairDto flairDto) {
        Flair saved = flairService.save(toFlair.convert(flairDto));
        return saved == null ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(toDto.convert(saved), HttpStatus.CREATED);
    }

    @PostMapping("/save/into-subreddit")
    public ResponseEntity<Void> saveIntoSubreddit(@RequestBody ObjectNode objectNode) {
        flairService.saveIntoSubreddit(objectNode.get("flairid").asLong(), objectNode.get("subredditid").asLong());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
