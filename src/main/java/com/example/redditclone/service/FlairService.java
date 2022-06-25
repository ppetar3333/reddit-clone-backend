package com.example.redditclone.service;

import com.example.redditclone.models.Flair;
import com.example.redditclone.web.dto.SubredditFlairsDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface FlairService {
    Optional<Flair> one(Long id);

    List<Flair> all();

    Flair save(Flair flair);

    void delete(Long id);

    Optional<Flair> findFlairByName(String name);

    List<SubredditFlairsDto> getFlairsBySubredditID(Long subredditID);

    void deleteFlairInSubreddit(Long flairID, Long subredditID);

    void saveIntoSubreddit(Long flairid, Long subredditid);
}
