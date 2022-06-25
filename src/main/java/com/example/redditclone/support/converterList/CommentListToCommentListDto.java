package com.example.redditclone.support.converterList;

import com.example.redditclone.models.Comment;
import com.example.redditclone.support.converterOne.CommentToCommentDto;
import com.example.redditclone.web.dto.CommentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class CommentListToCommentListDto implements Converter<List<Comment>, List<CommentDto>> {

    @Autowired
    private CommentToCommentDto toDto;

    public CommentListToCommentListDto() { }

    public List<CommentDto> convert(List<Comment> source) {
        List<CommentDto> target = new ArrayList<>();
        Iterator var3 = source.iterator();

        while(var3.hasNext()) {
            Comment u = (Comment) var3.next();
            CommentDto dto = this.toDto.convert(u);
            target.add(dto);
        }

        return target;
    }
}
