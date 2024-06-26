package com.example.redditclone.repository.jpa;

import com.example.redditclone.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findPostBySubredditSubredditID(Long id);
    List<Post> findPostByUserUserID(Long id);
    List<Post> findAllByOrderByCreationDateDesc();
    List<Post> findAllByOrderByVoteCountDesc();
    List<Post> findAllByOrderByVoteCountDescCreationDateDesc();
    List<Post> findAllBySubredditSubredditIDOrderByCreationDateDesc(Long id);
    List<Post> findAllBySubredditSubredditIDOrderByVoteCountDesc(Long id);
    List<Post> findAllBySubredditSubredditIDOrderByVoteCountDescCreationDateDesc(Long id);
}
