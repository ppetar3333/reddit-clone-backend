package com.example.redditclone.service;

import com.example.redditclone.models.Flair;
import com.example.redditclone.models.Subreddit;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface SubredditService {
    Optional<Subreddit> one(Long id);

    List<Subreddit> all();

    Subreddit save(Subreddit subreddit);

    void delete(Long id);

    Optional<Subreddit> findSubredditByName(String name);

    void saveSubredditModerators(Long userID, Long subredditId);
}
