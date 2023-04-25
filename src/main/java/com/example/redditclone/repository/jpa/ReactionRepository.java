package com.example.redditclone.repository.jpa;

import com.example.redditclone.enums.ReactionType;
import com.example.redditclone.models.Comment;
import com.example.redditclone.models.Post;
import com.example.redditclone.models.Reaction;
import com.example.redditclone.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {

    @Query(value = "select sum(case when r.type = 0 then 1 else 0 end) + sum(case when r.type = 1 then -1 else 0 end) as vote_count from Reaction as r where r.post.postID = ?1")
    Optional<Integer> getReactionVoteCountByPostID(Long id);

    @Query(value = "select sum(case when r.type = 0 then 1 else 0 end) + sum(case when r.type = 1 then -1 else 0 end) as vote_count from Reaction as r where r.comment.commentID = ?1")
    Optional<Integer> getReactionVoteCountByCommentID(Long id);

    Optional<Reaction> findTopByPostPostIDAndUserUserIDOrderByReactionIDDesc(Long postID, Long userID);

    Optional<Reaction> findTopByCommentCommentIDAndUserUserIDOrderByReactionIDDesc(Long commentID, Long userID);

    List<Reaction> getReactionByUserUserID(Long id);
}
