package com.example.redditclone.service.impl;

import com.example.redditclone.models.Comment;
import com.example.redditclone.repository.jpa.CommentRepository;
import com.example.redditclone.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JpaCommnetService implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public Optional<Comment> one(Long id) {
        return commentRepository.findById(id);
    }

    public List<Comment> all() {
        return commentRepository.findAll();
    }

    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    public void delete(Long id) { commentRepository.deleteById(id); }

    @Override
    public List<Comment> findCommentByPostPostID(Long id) {
        return commentRepository.findCommentByPostPostID(id);
    }

    @Override
    public List<Comment> findCommentByUserUserID(Long id) {
        return commentRepository.findCommentByUserUserID(id);
    }

    @Override
    public List<Comment> findCommentsByParentCommentID(Long id) {
        return commentRepository.findCommentsByParentCommentID(id);
    }

    @Override
    public List<Comment> findCommentByPostPostIDOrderByTimestampDesc(Long id) {
        return commentRepository.findCommentByPostPostIDOrderByTimestampDesc(id);
    }

    @Override
    public List<Comment> findCommentByPostPostIDOrderByVoteCountDesc(Long id) {
        return commentRepository.findCommentByPostPostIDOrderByVoteCountDesc(id);
    }

    @Override
    public List<Comment> findCommentByPostPostIDOrderByTimestampAsc(Long id) {
        return commentRepository.findCommentByPostPostIDOrderByTimestampAsc(id);
    }

    @Override
    public List<Comment> findCommentsByText(String text) {
        return commentRepository.findCommentsByText(text);
    }
}
