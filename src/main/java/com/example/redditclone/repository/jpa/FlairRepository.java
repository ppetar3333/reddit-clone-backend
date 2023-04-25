package com.example.redditclone.repository.jpa;

import com.example.redditclone.models.Flair;
import com.example.redditclone.web.dto.SubredditFlairsDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlairRepository extends JpaRepository<Flair, Long> {
    Optional<Flair> findFlairByName(String name);

    @Transactional
    @Modifying
    @Query(value = "select sf.flairid, sf.subredditid from subreddit_flairs as sf where sf.subredditid = ?1", nativeQuery = true)
    List<SubredditFlairsDto> getFlairsBySubredditID(Long subredditID);

    @Transactional
    @Modifying
    @Query(value = "delete from subreddit_flairs where subreddit_flairs.flairid =?1 and subreddit_flairs.subredditid =?2", nativeQuery = true)
    void deleteFlairInSubreddit(Long flairID, Long subredditID);

    @Transactional
    @Modifying
    @Query(value = "insert into subreddit_flairs values(?1,?2)", nativeQuery = true)
    void saveIntoSubreddit(Long flairid, Long subredditid);
}
