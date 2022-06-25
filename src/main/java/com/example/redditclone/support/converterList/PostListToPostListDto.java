package com.example.redditclone.support.converterList;

import com.example.redditclone.models.Post;
import com.example.redditclone.support.converterOne.PostToPostDto;
import com.example.redditclone.web.dto.PostDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class PostListToPostListDto implements Converter<List<Post>, List<PostDto>> {

    @Autowired
    private PostToPostDto toDto;

    public PostListToPostListDto() { }

    public List<PostDto> convert(List<Post> source) {
        List<PostDto> target = new ArrayList<>();
        Iterator var3 = source.iterator();

        while(var3.hasNext()) {
            Post u = (Post)var3.next();
            PostDto dto = this.toDto.convert(u);
            target.add(dto);
        }

        return target;
    }
}
