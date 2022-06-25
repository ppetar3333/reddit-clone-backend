package com.example.redditclone.support.converterOneReverse;

import com.example.redditclone.models.Banned;
import com.example.redditclone.models.Reaction;
import com.example.redditclone.models.Subreddit;
import com.example.redditclone.models.User;
import com.example.redditclone.service.BannedService;
import com.example.redditclone.web.dto.BannedDto;
import com.example.redditclone.web.dto.SubredditDto;
import com.example.redditclone.web.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class BannedDtoToBanned implements Converter<BannedDto, Banned> {

    @Autowired
    private BannedService bannedService;
    @Autowired
    private Converter<UserDto, User> toDtoUser;
    @Autowired
    private Converter<SubredditDto, Subreddit> toDtoSubreddit;

    public BannedDtoToBanned() {}

    public Banned convert(BannedDto source) {
        Banned target = null;

        if (source.getId() != null) target = bannedService.one(source.getId()).get();

        if (target == null) target = new Banned();

        target.setId(source.getId());
        target.setTimestamp(source.getTimestamp());
        target.setByModerator(toDtoUser.convert(source.getByModerator()));

        return target;
    }
}
