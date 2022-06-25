package com.example.redditclone.support.converterOne;

import com.example.redditclone.models.Banned;
import com.example.redditclone.models.Comment;
import com.example.redditclone.models.Subreddit;
import com.example.redditclone.models.User;
import com.example.redditclone.web.dto.BannedDto;
import com.example.redditclone.web.dto.CommentDto;
import com.example.redditclone.web.dto.SubredditDto;
import com.example.redditclone.web.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class BannedToBannedDto implements Converter<Banned, BannedDto> {

    @Autowired
    private Converter<User, UserDto> toUserDto;
    @Autowired
    private Converter<Subreddit, SubredditDto> toDtoSubreddit;

    public BannedToBannedDto() {}

    public BannedDto convert(Banned source) {
        BannedDto dto = new BannedDto();
        if(source != null) {
            dto.setId(source.getId());
            dto.setTimestamp(source.getTimestamp());
            dto.setByModerator(toUserDto.convert(source.getByModerator()));
        }
        return dto;
    }

    public List<BannedDto> convert(List<Banned> source) {
        List<BannedDto> retVal = new ArrayList<>();
        Iterator var3 = source.iterator();

        while(var3.hasNext()) {
            Banned s = (Banned) var3.next();
            BannedDto dto = this.convert(s);
            retVal.add(dto);
        }

        return retVal;
    }
}
