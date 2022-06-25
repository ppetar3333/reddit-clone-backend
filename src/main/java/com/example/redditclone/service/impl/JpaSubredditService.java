package com.example.redditclone.service.impl;

import com.example.redditclone.models.Flair;
import com.example.redditclone.models.Subreddit;
import com.example.redditclone.repository.SubredditRepository;
import com.example.redditclone.service.SubredditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JpaSubredditService implements SubredditService {

    @Autowired
    private SubredditRepository subredditRepository;

    public Optional<Subreddit> one(Long id) {
        return subredditRepository.findById(id);
    }

    public List<Subreddit> all() {
        return subredditRepository.findAll();
    }

    public Subreddit save(Subreddit subreddit) {
        return subredditRepository.save(subreddit);
    }

    public void delete(Long id) { subredditRepository.deleteById(id); }

    @Override
    public Optional<Subreddit> findSubredditByName(String name) {
        return subredditRepository.findSubredditByName(name);
    }

    @Override
    public void saveSubredditModerators(Long userID, Long subredditId) {
        subredditRepository.saveSubredditModerators(userID, subredditId);
    }
}
