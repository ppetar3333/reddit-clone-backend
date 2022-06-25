package com.example.redditclone.support.converterList;

import com.example.redditclone.models.Banned;
import com.example.redditclone.models.Comment;
import com.example.redditclone.support.converterOne.BannedToBannedDto;
import com.example.redditclone.web.dto.BannedDto;
import com.example.redditclone.web.dto.CommentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class BannedListToBannedListDto implements Converter<List<Banned>, List<BannedDto>> {

    @Autowired
    private BannedToBannedDto toDto;

    public BannedListToBannedListDto () {}

    public List<BannedDto> convert(List<Banned> source) {
        List<BannedDto> target = new ArrayList<>();
        Iterator var3 = source.iterator();

        while(var3.hasNext()) {
            Banned u = (Banned) var3.next();
            BannedDto dto = this.toDto.convert(u);
            target.add(dto);
        }

        return target;
    }
}
