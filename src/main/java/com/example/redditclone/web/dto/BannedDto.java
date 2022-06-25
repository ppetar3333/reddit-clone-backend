package com.example.redditclone.web.dto;

import com.example.redditclone.models.Banned;
import com.example.redditclone.models.Subreddit;
import com.example.redditclone.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class BannedDto {

    @NotNull
    private Long id;
    @NotNull
    private LocalDateTime timestamp;
    @NotNull
    private UserDto byModerator;
    @NotNull
    private SubredditDto subreddit;
    @JsonIgnore
    @Autowired
    private Converter<User, UserDto> toDtoUser;
    @JsonIgnore
    @Autowired
    private Converter<Subreddit, SubredditDto> toDtoSubreddit;

    public BannedDto () {}

    public BannedDto (Banned banned) {
        this.id = banned.getId();
        this.timestamp = banned.getTimestamp();
        this.byModerator = toDtoUser.convert(banned.getByModerator());
    }

}
