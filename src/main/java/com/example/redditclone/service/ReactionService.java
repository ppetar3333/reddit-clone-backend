package com.example.redditclone.service;

import com.example.redditclone.enums.ReactionType;
import com.example.redditclone.models.Comment;
import com.example.redditclone.models.Post;
import com.example.redditclone.models.Reaction;
import com.example.redditclone.models.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ReactionService {
    Optional<Reaction> one(Long id);

    List<Reaction> all();

    Reaction save(Reaction reaction);

    void delete(Long id);

    Optional<Integer> getReactionVoteCountByPostID(Long id);

    Optional<Integer> getReactionVoteCountByCommentID(Long id);

    Optional<Reaction> findTopByPostPostIDAndUserUserIDOrderByReactionIDDesc(Long postID, Long userID);

    Optional<Reaction> findTopByCommentCommentIDAndUserUserIDOrderByReactionIDDesc(Long commentID, Long userID);

    List<Reaction> getReactionByUserUserID(Long id);
}
