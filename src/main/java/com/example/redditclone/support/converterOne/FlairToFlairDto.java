package com.example.redditclone.support.converterOne;

import com.example.redditclone.models.Flair;
import com.example.redditclone.web.dto.FlairDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class FlairToFlairDto implements Converter<Flair, FlairDto> {

    public FlairToFlairDto() {}

    public FlairDto convert(Flair source) {
        FlairDto dto = new FlairDto();
        if (source != null) {
            dto.setFlairID(source.getFlairID());
            dto.setName(source.getName());
        }
        return dto;
    }

    public List<FlairDto> convert(List<Flair> source) {
        List<FlairDto> retVal = new ArrayList<>();
        Iterator var3 = source.iterator();

        while(var3.hasNext()) {
            Flair s = (Flair) var3.next();
            FlairDto dto = this.convert(s);
            retVal.add(dto);
        }

        return retVal;
    }
}
