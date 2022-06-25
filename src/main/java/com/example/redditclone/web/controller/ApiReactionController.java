package com.example.redditclone.web.controller;

import com.example.redditclone.models.Reaction;
import com.example.redditclone.models.Report;
import com.example.redditclone.models.Subreddit;
import com.example.redditclone.service.ReactionService;
import com.example.redditclone.web.dto.ReactionDto;
import com.example.redditclone.web.dto.ReportDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping({"api/reactions"})
public class ApiReactionController {

    @Autowired
    private ReactionService reactionService;
    @Autowired
    private Converter<List<Reaction>, List<ReactionDto>> toDtoReaction;
    @Autowired
    private Converter<Reaction, ReactionDto> toDto;
    @Autowired
    private Converter<ReactionDto, Reaction> toReaction;

    @CrossOrigin(origins = {"http://localhost:8080"}, maxAge = 3600L)

    @GetMapping()
    public ResponseEntity<List<ReactionDto>> getAll() {
        List<Reaction> reactions = reactionService.all();
        List<ReactionDto> body = toDtoReaction.convert(reactions);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @GetMapping("/findOne/{id}")
    public ResponseEntity<ReactionDto> getById(@PathVariable("id") Long id) {
        Optional<Reaction> reaction = Optional.of(reactionService.one(id).orElse(new Reaction()));
        ReactionDto reactionDto = toDto.convert(reaction.get());
        return new ResponseEntity<>(reactionDto, HttpStatus.OK);
    }

    @GetMapping("/by-user/{id}")
    public ResponseEntity<List<ReactionDto>> getReactionsByUser(@PathVariable("id") Long id) {
        List<Reaction> reactions = reactionService.getReactionByUserUserID(id);
        List<ReactionDto> reactionDto = toDtoReaction.convert(reactions);
        return new ResponseEntity<>(reactionDto, HttpStatus.OK);
    }

    @GetMapping("/karma-post/{id}")
    public ResponseEntity<Integer> getReactionVoteCountByPostID(@PathVariable("id") Long id) {
        Optional<Integer> getResult = reactionService.getReactionVoteCountByPostID(id);
        return getResult.map(integer -> new ResponseEntity<>(integer, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(0, HttpStatus.NOT_FOUND));
    }

    @GetMapping("/karma-comment/{id}")
    public ResponseEntity<Integer> getReactionVoteCountByCommentID(@PathVariable("id") Long id) {
        Optional<Integer> getResult = reactionService.getReactionVoteCountByCommentID(id);
        return getResult.map(integer -> new ResponseEntity<>(integer, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(0, HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        reactionService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ReactionDto> update(@RequestBody ReactionDto reactionDto, @PathVariable("id") Long id) {
        if (!id.equals(reactionDto.getReactionID())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            Reaction persisted = this.reactionService.save(toReaction.convert(reactionDto));
            return new ResponseEntity<>(toDto.convert(persisted), HttpStatus.OK);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<ReactionDto> save(@RequestBody ReactionDto reactionDto) {
        Reaction saved = reactionService.save(toReaction.convert(reactionDto));
        return saved == null ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(toDto.convert(saved), HttpStatus.CREATED);
    }
}
