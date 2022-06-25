package com.example.redditclone.support.converterOne;

import com.example.redditclone.models.Comment;
import com.example.redditclone.models.Post;
import com.example.redditclone.models.Reaction;
import com.example.redditclone.models.User;
import com.example.redditclone.web.dto.CommentDto;
import com.example.redditclone.web.dto.PostDto;
import com.example.redditclone.web.dto.ReactionDto;
import com.example.redditclone.web.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class ReactionToReactionDto implements Converter<Reaction, ReactionDto> {

    @Autowired
    private Converter<User, UserDto> toUserDto;
    @Autowired
    private Converter<Comment, CommentDto> toCommentDto;
    @Autowired
    private Converter<Post, PostDto> toPostDto;

    public ReactionToReactionDto() {}

    public ReactionDto convert(Reaction source) {
        ReactionDto dto = new ReactionDto();
        dto.setReactionID(source.getReactionID());
        dto.setType(source.getType());
        dto.setTimestamp(source.getTimestamp());
        dto.setUser(toUserDto.convert(source.getUser()));
        dto.setComment(toCommentDto.convert(source.getComment()));
        dto.setPost(toPostDto.convert(source.getPost()));
        return dto;
    }

    public List<ReactionDto> convert(List<Reaction> source) {
        List<ReactionDto> retVal = new ArrayList<>();
        Iterator var3 = source.iterator();

        while(var3.hasNext()) {
            Reaction s = (Reaction) var3.next();
            ReactionDto dto = this.convert(s);
            retVal.add(dto);
        }

        return retVal;
    }
}
