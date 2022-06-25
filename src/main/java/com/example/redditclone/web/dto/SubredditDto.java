package com.example.redditclone.web.dto;

import com.example.redditclone.models.Flair;
import com.example.redditclone.models.Subreddit;
import com.example.redditclone.models.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@ToString
public class SubredditDto implements Serializable {

    @NotNull
    private Long subredditID;
    @NotEmpty
    @Size(min = 5, max = 100, message = "Subreddit name needs to be between 5 and 100")
    private String name;
    @Size(min = 5, max = 500, message = "Subreddit name needs to be between 5 and 500")
    private String description;
    @NotNull
    private LocalDateTime creationDate;
    @NotNull
    private boolean isSuspended;
    private List<String> rules;
    private String suspendedReason;
    private Set<User> moderators;

    public SubredditDto() {}

    public SubredditDto(Subreddit subreddit) {
        this.subredditID = subreddit.getSubredditID();
        this.name = subreddit.getName();
        this.description = subreddit.getDescription();
        this.creationDate = subreddit.getCreationDate();
        this.rules = subreddit.getRules();
        this.moderators = subreddit.getModerator();
        this.isSuspended = subreddit.isSuspended();
        this.suspendedReason = subreddit.getSuspendedReason();
    }
}
