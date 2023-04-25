package com.example.redditclone.service.impl;

import com.example.redditclone.models.Post;
import com.example.redditclone.repository.jpa.PostRepository;
import com.example.redditclone.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class JpaPostService implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Override
    public Optional<Post> one(Long id) {
        return postRepository.findById(id);
    }

    public List<Post> all() {
        Iterable<Post> posts = postRepository.findAll();
        List<Post> postList = StreamSupport.stream(posts.spliterator(), false)
                .collect(Collectors.toList());
        return postList;
    }

    @Override
    public Post save(Post post) {
        return postRepository.save(post);
    }

    @Override
    public void delete(Long id) { postRepository.deleteById(id); }

    @Override
    public List<Post> findPostBySubredditSubredditID(Long id) {
        return postRepository.findPostBySubredditSubredditID(id);
    }

    @Override
    public List<Post> findPostByUserUserID(Long id) {
        return postRepository.findPostByUserUserID(id);
    }

    @Override
    public List<Post> findAllByOrderByCreationDateDesc() {
        return postRepository.findAllByOrderByCreationDateDesc();
    }

    @Override
    public List<Post> findAllByOrderByVoteCountDesc() {
        return postRepository.findAllByOrderByVoteCountDesc();
    }

    @Override
    public List<Post> findAllByOrderByVoteCountDescCreationDateDesc() {
        return postRepository.findAllByOrderByVoteCountDescCreationDateDesc();
    }

    @Override
    public List<Post> findAllBySubredditSubredditIDOrderByCreationDateDesc(Long id) {
        return postRepository.findAllBySubredditSubredditIDOrderByCreationDateDesc(id);
    }

    @Override
    public List<Post> findAllBySubredditSubredditIDOrderByVoteCountDesc(Long id) {
        return postRepository.findAllBySubredditSubredditIDOrderByVoteCountDesc(id);
    }

    @Override
    public List<Post> findAllBySubredditSubredditIDOrderByVoteCountDescCreationDateDesc(Long id) {
        return postRepository.findAllBySubredditSubredditIDOrderByVoteCountDescCreationDateDesc(id);
    }

}
