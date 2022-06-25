package com.example.redditclone.support.converterList;

import com.example.redditclone.models.Reaction;
import com.example.redditclone.support.converterOne.ReactionToReactionDto;
import com.example.redditclone.web.dto.ReactionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class ReactionListToReactionListDto implements Converter<List<Reaction>, List<ReactionDto>> {

    @Autowired
    private ReactionToReactionDto toDto;

    public ReactionListToReactionListDto() { }

    public List<ReactionDto> convert(List<Reaction> source) {
        List<ReactionDto> target = new ArrayList<>();
        Iterator var3 = source.iterator();

        while(var3.hasNext()) {
            Reaction u = (Reaction)var3.next();
            ReactionDto dto = this.toDto.convert(u);
            target.add(dto);
        }

        return target;
    }

}
