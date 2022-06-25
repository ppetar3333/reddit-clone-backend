package com.example.redditclone.support.converterOne;

import com.example.redditclone.models.Comment;
import com.example.redditclone.models.Post;
import com.example.redditclone.models.User;
import com.example.redditclone.web.dto.CommentDto;
import com.example.redditclone.web.dto.PostDto;
import com.example.redditclone.web.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class CommentToCommentDto implements Converter<Comment, CommentDto> {

    @Autowired
    private Converter<User, UserDto> toUserDto;
    @Autowired
    private Converter<Post, PostDto> toPostDto;

    public CommentToCommentDto() {}

    public CommentDto convert(Comment source) {
        CommentDto dto = new CommentDto();
        if (source != null) {
            dto.setId(source.getCommentID());
            dto.setText(source.getText());
            dto.setTimestamp(source.getTimestamp());
            dto.setVoteCount(source.getVoteCount());
            dto.setDeleted(source.isDeleted());
            if (source.getParent() != null) dto.setParentComment(source.getParent().getCommentID());
            dto.setUser(toUserDto.convert(source.getUser()));
            dto.setPost(toPostDto.convert(source.getPost()));
        }
        return dto;
    }

    public List<CommentDto> convert(List<Comment> source) {
        List<CommentDto> retVal = new ArrayList<>();
        Iterator var3 = source.iterator();

        while(var3.hasNext()) {
            Comment s = (Comment) var3.next();
            CommentDto dto = this.convert(s);
            retVal.add(dto);
        }

        return retVal;
    }
}
