package com.example.redditclone.service;

import com.example.redditclone.models.Post;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface PostService {
    Optional<Post> one(Long id);

    List<Post> all();

    Post save(Post post);

    void delete(Long id);

    List<Post> findPostBySubredditSubredditID(Long id);

    List<Post> findPostByUserUserID(Long id);

    List<Post> findAllByOrderByCreationDateDesc();

    List<Post> findAllByOrderByVoteCountDesc();

    List<Post> findAllByOrderByVoteCountDescCreationDateDesc();

    List<Post> findAllBySubredditSubredditIDOrderByCreationDateDesc(Long id);
    List<Post> findAllBySubredditSubredditIDOrderByVoteCountDesc(Long id);
    List<Post> findAllBySubredditSubredditIDOrderByVoteCountDescCreationDateDesc(Long id);}
