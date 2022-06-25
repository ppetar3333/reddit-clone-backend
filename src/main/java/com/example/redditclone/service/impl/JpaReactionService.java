package com.example.redditclone.service.impl;

import com.example.redditclone.enums.ReactionType;
import com.example.redditclone.models.Comment;
import com.example.redditclone.models.Post;
import com.example.redditclone.models.Reaction;
import com.example.redditclone.models.User;
import com.example.redditclone.repository.ReactionRepository;
import com.example.redditclone.service.ReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JpaReactionService implements ReactionService {

    @Autowired
    private ReactionRepository reactionRepository;

    public Optional<Reaction> one(Long id) {
        return reactionRepository.findById(id);
    }

    public List<Reaction> all() {
        return reactionRepository.findAll();
    }

    public Reaction save(Reaction reaction) {
        return reactionRepository.save(reaction);
    }

    public void delete(Long id) { reactionRepository.deleteById(id); }

    @Override
    public Optional<Integer> getReactionVoteCountByPostID(Long id) { return reactionRepository.getReactionVoteCountByPostID(id); }

    @Override
    public Optional<Integer> getReactionVoteCountByCommentID(Long id) { return reactionRepository.getReactionVoteCountByCommentID(id); }

    @Override
    public Optional<Reaction> findTopByPostPostIDAndUserUserIDOrderByReactionIDDesc(Long postID, Long userID) {
        return reactionRepository.findTopByPostPostIDAndUserUserIDOrderByReactionIDDesc(postID, userID);
    }

    @Override
    public Optional<Reaction> findTopByCommentCommentIDAndUserUserIDOrderByReactionIDDesc(Long commentID, Long userID) {
        return reactionRepository.findTopByCommentCommentIDAndUserUserIDOrderByReactionIDDesc(commentID, userID);
    }

    @Override
    public List<Reaction> getReactionByUserUserID(Long id) {
        return reactionRepository.getReactionByUserUserID(id);
    }
}
