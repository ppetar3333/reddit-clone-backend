package com.example.redditclone.service.impl;

import com.example.redditclone.models.Flair;
import com.example.redditclone.repository.jpa.FlairRepository;
import com.example.redditclone.service.FlairService;
import com.example.redditclone.web.dto.SubredditFlairsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JpaFlairService implements FlairService {

    @Autowired
    private FlairRepository flairRepository;

    public Optional<Flair> one(Long id) {
        return flairRepository.findById(id);
    }

    public List<Flair> all() {
        return flairRepository.findAll();
    }

    public Flair save(Flair flair) {
        return flairRepository.save(flair);
    }

    public void delete(Long id) { flairRepository.deleteById(id); }

    @Override
    public Optional<Flair> findFlairByName(String name) {
        return flairRepository.findFlairByName(name);
    }

    @Override
    public List<SubredditFlairsDto> getFlairsBySubredditID(Long subredditID) {
        return flairRepository.getFlairsBySubredditID(subredditID);
    }

    @Override
    public void deleteFlairInSubreddit(Long flairID, Long subredditID) {
        flairRepository.deleteFlairInSubreddit(flairID, subredditID);
    }

    @Override
    public void saveIntoSubreddit(Long flairid, Long subredditid) {
        flairRepository.saveIntoSubreddit(flairid, subredditid);
    }
}
