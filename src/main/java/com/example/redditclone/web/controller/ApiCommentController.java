package com.example.redditclone.web.controller;

import com.example.redditclone.models.Comment;
import com.example.redditclone.models.Flair;
import com.example.redditclone.models.Post;
import com.example.redditclone.models.User;
import com.example.redditclone.service.CommentService;
import com.example.redditclone.web.dto.CommentDto;
import com.example.redditclone.web.dto.FlairDto;
import com.example.redditclone.web.dto.PostDto;
import com.example.redditclone.web.dto.UserDto;
import com.mysql.jdbc.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping({"api/comments"})
public class ApiCommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private Converter<List<Comment>, List<CommentDto>> toDtoComment;
    @Autowired
    private Converter<Comment, CommentDto> toDto;
    @Autowired
    private Converter<CommentDto, Comment> toComment;
    @Autowired
    private Converter<UserDto, User> toUser;
    @Autowired
    private Converter<PostDto, Post> toPost;

    @CrossOrigin(origins = {"http://localhost:8080"}, maxAge = 3600L)

    @GetMapping()
    public ResponseEntity<List<CommentDto>> getAll() {
        List<Comment> comments = commentService.all();
        List<CommentDto> body = toDtoComment.convert(comments);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @GetMapping("/findOne/{id}")
    public ResponseEntity<CommentDto> getById(@PathVariable("id") Long id) {
        Optional<Comment> comment = Optional.of(commentService.one(id).orElse(new Comment()));
        CommentDto commentDto = toDto.convert(comment.get());
        return new ResponseEntity<>(commentDto, HttpStatus.OK);
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<List<CommentDto>> getCommentsByPostID(@PathVariable("id") Long id) {
        List<Comment> comments = commentService.findCommentByPostPostID(id);
        List<CommentDto> commentDtos = toDtoComment.convert(comments);
        return new ResponseEntity<>(commentDtos, HttpStatus.OK);
    }

    @GetMapping("/children/{id}")
    public ResponseEntity<List<CommentDto>> getCommentsByParentID(@PathVariable("id") Long id) {
        List<Comment> comments = commentService.findCommentsByParentCommentID(id);
        List<CommentDto> commentDtos = toDtoComment.convert(comments);
        return new ResponseEntity<>(commentDtos, HttpStatus.OK);
    }

    @GetMapping("/children-by-text/{text}")
    public ResponseEntity<List<CommentDto>> getCommentsByText(@PathVariable("text") String text) {
        List<Comment> comments = commentService.findCommentsByText(text);
        List<CommentDto> commentDtos = toDtoComment.convert(comments);
        return new ResponseEntity<>(commentDtos, HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<CommentDto>> getCommentsByUserID(@PathVariable("id") Long id) {
        List<Comment> comments = commentService.findCommentByUserUserID(id);
        List<CommentDto> commentDtos = toDtoComment.convert(comments);
        return new ResponseEntity<>(commentDtos, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        commentService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/update/{id}")  
    public ResponseEntity<CommentDto> update(@RequestBody CommentDto commentDto, @PathVariable("id") Long id) {
        if (!id.equals(commentDto.getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            Comment persisted = this.commentService.save(toComment.convert(commentDto));
            return new ResponseEntity<>(toDto.convert(persisted), HttpStatus.OK);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<Long> save(@RequestBody CommentDto commentDto) {
        Comment comment = new Comment();

        comment.setText(commentDto.getText());
        if (commentDto.getParentComment() != null) {
            Optional<Comment> parentComment = commentService.one(commentDto.getParentComment());
            comment.setParent(parentComment.get());
        }
        comment.setUser(toUser.convert(commentDto.getUser()));
        comment.setPost(toPost.convert(commentDto.getPost()));
        comment.setTimestamp(LocalDateTime.now());
        comment.setDeleted(false);
        comment.setVoteCount(commentDto.getVoteCount());

        commentService.save(comment);

        return new ResponseEntity<>(comment.getCommentID(), HttpStatus.CREATED);
    }

    @GetMapping("/sort-comments/by-post/{option}/{id}")
    public ResponseEntity<List<CommentDto>> sortPosts(@PathVariable("option") String option, @PathVariable("id") Long id) {
        List<Comment> comments;

        if(option.equals("New")) {
            comments = commentService.findCommentByPostPostIDOrderByTimestampDesc(id);
        } else if(option.equals("Top")) {
            comments = commentService.findCommentByPostPostIDOrderByVoteCountDesc(id);
        } else {
            comments = commentService.findCommentByPostPostIDOrderByTimestampAsc(id);
        }

        List<CommentDto> body = toDtoComment.convert(comments);

        return new ResponseEntity<>(body, HttpStatus.OK);
    }
}
