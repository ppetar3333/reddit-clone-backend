package com.example.redditclone.support.converterOneReverse;

import com.example.redditclone.models.Flair;
import com.example.redditclone.service.FlairService;
import com.example.redditclone.web.dto.FlairDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FlairDtoToFlair implements Converter<FlairDto, Flair> {

    @Autowired
    private FlairService flairService;

    public FlairDtoToFlair() { }

    public Flair convert(FlairDto source) {
        Flair target = null;

        if (source.getFlairID() != null) target = flairService.one(source.getFlairID()).get();

        if (target == null) target = new Flair();

        target.setFlairID(source.getFlairID());
        target.setName(source.getName());

        return target;
    }
}
