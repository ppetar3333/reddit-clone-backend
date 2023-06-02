package com.example.redditclone.support.converterOne;

import com.example.redditclone.models.Subreddit;
import com.example.redditclone.web.dto.SubredditDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class SubredditToSubredditDto implements Converter<Subreddit, SubredditDto> {

    public SubredditToSubredditDto() {}

    public SubredditDto convert(Subreddit source) {
        SubredditDto dto = new SubredditDto();
        if (source != null) {
            dto.setSubredditID(source.getSubredditID());
            dto.setName(source.getName());
            dto.setDescription(source.getDescription());
            dto.setCreationDate(source.getCreationDate());
            dto.setSuspended(source.isSuspended());
            dto.setRules(source.getRules());
            dto.setSuspendedReason(source.getSuspendedReason());
            dto.setModerators(source.getModerator());
            dto.setTextFromPdf(source.getTextFromPdf());
            dto.setPostsCount(source.getPostsCount());
        }
        return dto;
    }

    public List<SubredditDto> convert(List<Subreddit> source) {
        List<SubredditDto> retVal = new ArrayList<>();
        Iterator var3 = source.iterator();

        while(var3.hasNext()) {
            Subreddit s = (Subreddit) var3.next();
            SubredditDto dto = this.convert(s);
            retVal.add(dto);
        }

        return retVal;
    }
}
