package com.example.redditclone.service;

import com.example.redditclone.models.Comment;
import com.example.redditclone.models.Reaction;
import com.example.redditclone.models.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CommentService {
    Optional<Comment> one(Long id);

    List<Comment> all();

    Comment save(Comment comment);

    void delete(Long id);

    List<Comment> findCommentByPostPostID(Long id);

    List<Comment> findCommentByUserUserID(Long id);

    List<Comment> findCommentsByParentCommentID(Long id);

    List<Comment> findCommentByPostPostIDOrderByTimestampDesc(Long id);

    List<Comment> findCommentByPostPostIDOrderByVoteCountDesc(Long id);

    List<Comment> findCommentByPostPostIDOrderByTimestampAsc(Long id);

    List<Comment> findCommentsByText(String text);
}
