package com.example.redditclone.support.converterList;

import com.example.redditclone.models.Subreddit;
import com.example.redditclone.support.converterOne.SubredditToSubredditDto;
import com.example.redditclone.web.dto.SubredditDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class SubredditListToSubredditListDto implements Converter<List<Subreddit>, List<SubredditDto>> {

    @Autowired
    private SubredditToSubredditDto toDto;

    public SubredditListToSubredditListDto() { }

    public List<SubredditDto> convert(List<Subreddit> source) {
        List<SubredditDto> target = new ArrayList<>();
        Iterator var3 = source.iterator();

        while(var3.hasNext()) {
            Subreddit u = (Subreddit)var3.next();
            SubredditDto dto = this.toDto.convert(u);
            target.add(dto);
        }

        return target;
    }

}
