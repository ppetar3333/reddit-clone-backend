package com.example.redditclone.repository.jpa;

import com.example.redditclone.models.Comment;
import com.example.redditclone.models.Post;
import com.example.redditclone.models.Reaction;
import com.example.redditclone.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findCommentByPostPostID(Long id);
    List<Comment> findCommentByUserUserID(Long id);
    List<Comment> findCommentsByParentCommentID(Long id);
    List<Comment> findCommentsByText(String text);
    List<Comment> findCommentByPostPostIDOrderByTimestampDesc(Long id);
    List<Comment> findCommentByPostPostIDOrderByVoteCountDesc(Long id);
    List<Comment> findCommentByPostPostIDOrderByTimestampAsc(Long id);
}
