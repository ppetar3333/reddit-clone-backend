package com.example.redditclone.support.converterOneReverse;

import com.example.redditclone.models.Subreddit;
import com.example.redditclone.service.SubredditService;
import com.example.redditclone.web.dto.SubredditDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class SubredditDtoToSubreddot implements Converter<SubredditDto, Subreddit> {

    @Autowired
    private SubredditService subredditService;

    public SubredditDtoToSubreddot() { }

    public Subreddit convert(SubredditDto source) {
        Subreddit target = null;

        if (source.getSubredditID() != null) target = subredditService.one(source.getSubredditID()).get();

        if (target == null) target = new Subreddit();

        target.setSubredditID(source.getSubredditID());
        target.setName(source.getName());
        target.setCreationDate(source.getCreationDate());
        target.setDescription(source.getDescription());
        target.setSuspended(source.isSuspended());
        target.setRules(source.getRules());
        target.setSuspendedReason(source.getSuspendedReason());
        target.setModerator(source.getModerators());

        return target;
    }
}
