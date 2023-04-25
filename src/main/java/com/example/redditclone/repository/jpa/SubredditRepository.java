package com.example.redditclone.repository.jpa;

import com.example.redditclone.models.Flair;
import com.example.redditclone.models.Subreddit;
import com.example.redditclone.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubredditRepository extends JpaRepository<Subreddit, Long> {

    Optional<Subreddit> findSubredditByName(String name);

    @Transactional
    @Modifying
    @Query(value = "insert into subreddit_moderatori(userid,subredditid) values(?1,?2)", nativeQuery = true)
    void saveSubredditModerators(Long userID, Long subredditId);
}
