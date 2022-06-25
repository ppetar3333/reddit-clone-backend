package com.example.redditclone.web.controller;

import com.example.redditclone.enums.ReactionType;
import com.example.redditclone.models.Comment;
import com.example.redditclone.models.Post;
import com.example.redditclone.models.Reaction;
import com.example.redditclone.models.User;
import com.example.redditclone.repository.CommentRepository;
import com.example.redditclone.repository.PostRepository;
import com.example.redditclone.repository.ReactionRepository;
import com.example.redditclone.web.dto.CommentDto;
import com.example.redditclone.web.dto.PostDto;
import com.example.redditclone.web.dto.ReactionDto;
import com.example.redditclone.web.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Convert;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping({"api/votes"})
public class ApiVoteController {


    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ReactionRepository reactionRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private Converter<PostDto, Post> toPost;
    @Autowired
    private Converter<UserDto, User> toUser;
    @Autowired
    private Converter<CommentDto, Comment> toComment;

    @CrossOrigin(origins = {"http://localhost:8080"}, maxAge = 3600L)

    @PostMapping("/posts")
    public ResponseEntity<String> votePost(@RequestBody ReactionDto reactionDto) {
        Optional<Post> post = postRepository.findById(reactionDto.getPost().getPostID());
        Optional<Reaction> voteByPostAndUser = reactionRepository.findTopByPostPostIDAndUserUserIDOrderByReactionIDDesc(reactionDto.getPost().getPostID(), reactionDto.getUser().getUserID());


        if(voteByPostAndUser.isPresent() && voteByPostAndUser.get().getType() == reactionDto.getType()) {
            return new ResponseEntity<>("You have already " + reactionDto.getType() + " for this post", HttpStatus.BAD_REQUEST);
        }

        if (reactionDto.getType() == ReactionType.upvote) {
            if(reactionDto.getPost().getVoteCount() == -1) {
                reactionDto.getPost().setVoteCount(reactionDto.getPost().getVoteCount() + 2);
            } else {
                reactionDto.getPost().setVoteCount(reactionDto.getPost().getVoteCount() + 1);
            }
        } else {
            if(reactionDto.getPost().getVoteCount() == 1) {
                reactionDto.getPost().setVoteCount(reactionDto.getPost().getVoteCount() - 2);
            } else {
                reactionDto.getPost().setVoteCount(reactionDto.getPost().getVoteCount() - 1);
            }
        }


        Reaction reaction = new Reaction();

        reaction.setPost(toPost.convert(reactionDto.getPost()));
        reaction.setUser(toUser.convert(reactionDto.getUser()));
        reaction.setComment(null);
        reaction.setTimestamp(LocalDateTime.now());
        reaction.setType(reactionDto.getType());

        reactionRepository.save(reaction);
        postRepository.save(reaction.getPost());

        return new ResponseEntity<>("Voted", HttpStatus.OK);
    }

    @PostMapping("/comments")
    public ResponseEntity<String> voteComment(@RequestBody ReactionDto reactionDto) {
        Optional<Comment> comment = commentRepository.findById(reactionDto.getComment().getId());
        Optional<Reaction> voteByCommentAndUser = reactionRepository.findTopByCommentCommentIDAndUserUserIDOrderByReactionIDDesc(reactionDto.getComment().getId(), reactionDto.getUser().getUserID());

        if(voteByCommentAndUser.isPresent() && voteByCommentAndUser.get().getType() == reactionDto.getType()) {
            return new ResponseEntity<>("You have already " + reactionDto.getType() + " for this post", HttpStatus.BAD_REQUEST);
        }

        if (reactionDto.getType() == ReactionType.upvote) {
            if(reactionDto.getComment().getVoteCount() == -1) {
                reactionDto.getComment().setVoteCount(reactionDto.getComment().getVoteCount() + 2);
            } else {
                reactionDto.getComment().setVoteCount(reactionDto.getComment().getVoteCount() + 1);
            }
        } else {
            if(comment.get().getVoteCount() == 1) {
                reactionDto.getComment().setVoteCount(reactionDto.getComment().getVoteCount() - 2);
            } else {
                reactionDto.getComment().setVoteCount(reactionDto.getComment().getVoteCount() - 1);
            }
        }


        Reaction reaction = new Reaction();

        reaction.setPost(null);
        reaction.setUser(toUser.convert(reactionDto.getUser()));
        reaction.setComment(toComment.convert(reactionDto.getComment()));
        reaction.setTimestamp(LocalDateTime.now());
        reaction.setType(reactionDto.getType());

        reactionRepository.save(reaction);
        commentRepository.save(reaction.getComment());

        return new ResponseEntity<>("Voted", HttpStatus.OK);
    }
}
